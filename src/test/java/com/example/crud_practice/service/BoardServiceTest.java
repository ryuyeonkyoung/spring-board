package com.example.crud_practice.service;

import com.example.crud_practice.dto.BoardRequestDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import com.example.crud_practice.repository.BoardFileRepository;
import com.example.crud_practice.repository.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; // Junit 5
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks; // Mockito
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardFileRepository boardFileRepository;

    @InjectMocks
    private BoardService boardService;

    @DisplayName("파일 없이 게시글만 저장됨")
    @Test
    void save_본문만_저장되는_경우() throws Exception {
        // given - 조건 준비
        // Mockito 3.x부터는 기본 strict 모드 적용- 사용하지 않는 when()이 있으면 테스트 실패
        // 테스트 흐름에 영향 없는 when()은 lenient().when()으로 UnnecessaryStubbingException 방지 가능
        BoardRequestDTO request = mock(BoardRequestDTO.class); // Mockito로 Mock 객체 생성
        when(request.getBoardWriter()).thenReturn("작성자");
        when(request.getBoardPass()).thenReturn("비밀번호");
        when(request.getBoardTitle()).thenReturn("제목");
        when(request.getBoardContents()).thenReturn("내용");
        when(request.getBoardFile()).thenReturn(List.of()); // 첨부파일 없음

        BoardEntity entity = BoardEntity.toSaveEntity(request);
        // BoardRepository.sqve()가 호출되면 내가 만든 entity를 반환하도록 설정
        when(boardRepository.save(any(BoardEntity.class))).thenReturn(entity);

        // when - 실행
        boardService.save(request);

        // then - 검증
        verify(boardRepository, times(1)).save(any(BoardEntity.class)); // boardRepository.save()가 1회 호출되었는지 확인
        verify(boardFileRepository, never()).save(any()); // boardFileRepository.save()가 호출되지 않았는지 확인
    }

    @DisplayName("게시글, 파일 둘 다 있고 저장 성공함")
    @Test
    void save_파일_첨부시_DB에_게시글과_파일_정보가_저장된다() throws Exception {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.png");

        BoardRequestDTO request = mock(BoardRequestDTO.class);
        when(request.getBoardFile()).thenReturn(List.of(file));

        BoardEntity board = BoardEntity.toSaveFileEntity(request);
        when(boardRepository.save(any(BoardEntity.class))).thenReturn(board);
        when(boardRepository.findById(any())).thenReturn(Optional.of(board));

        // when
        boardService.save(request);

        // then
        verify(boardRepository, times(1)).save(any(BoardEntity.class));
        verify(boardFileRepository, times(1)).save(any(BoardFileEntity.class));
    }

    @DisplayName("게시글, 파일 둘 다 있고 저장 실패함")
    @Test
    void save_파일_저장_실패시_IOException_발생() throws Exception {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("error.png");
        doThrow(new IOException("파일 저장 실패")).when(file).transferTo((File) any());

        BoardRequestDTO request = mock(BoardRequestDTO.class);
        when(request.getBoardFile()).thenReturn(List.of(file));

        BoardEntity board = BoardEntity.toSaveFileEntity(request);
        when(boardRepository.save(any(BoardEntity.class))).thenReturn(board);
        when(boardRepository.findById(any())).thenReturn(Optional.of(board));

        // when & then
        IOException exception = assertThrows(IOException.class, () -> boardService.save(request));
        verify(boardRepository, times(1)).save(any(BoardEntity.class));
        verify(boardFileRepository, never()).save(any());
        assert exception.getMessage().contains("파일 저장 실패");
    }
}