## DTO의 불변성을 보장하기 위한 방법 - Builder, final

### Builder
- Builder는 setter 없이 가독성 좋게 객체를 생성할 수 있게 해주는 패턴

- @Builder 어노테이션을 사용하면 .builder(), .build() 메서드로 필요한 필드를 선택적으로 설정하여 객체를 생성할 수 있음
- 유효성 검사는 Builder 내부가 아닌 DTO와 컨트롤러 계층에서 수행하는 것이 일반적
    - **DTO에서 유효성 검사를 수행하는 것이 일반적**이고, 필요한 경우 컨트롤러에서 추가적인 로직을 처리

builder 사용 시 주의점

- Lombok의 @Builder는 내부적으로 **모든 필드를 포함한 생성자** 하나를 만들어 해당 생성자를 기반으로 빌더 객체를 생성한다.
    - 빌더 메서드를 통해 설정하지 않은 필드는 기본값(null 또는 초기화된 값)이 적용된다.
    - 다양한 생성자를 만드는 @AllArgsConstructor, @NoArgsConstructor, @RequiredArgsConstructor과 관련되어 있다.
- **Entity 계층에서 JPA와의 충돌 주의** : @AllArgsConstructor
- **@Builder.Default 없이는 초기값이 무시될 수 있음**
- Builder는 객체 생성이라는 하나의 역할만 담당하게 하기. (유효성 검사는 DTO에서 진행)

---

### final
- final은 해당 필드나 계층의 불변성을 보장하기 위해 사용

계층별 final 사용 여부와 이유

1. DTO(권장) :  불변성 보장, 클라이언트의 값 변경 방지, 명확한 설계
2. Service(권장) : DI 받은 의존성(예: Repository)의 변경을 방지하고, 의도한 불변성을 명확히 하기 위해
3. Config(권장) : 변경되면 안 되는 설정값에 이용
4. Entity(지양) : JPA가 값 변경을 감지해야 함 (Dirty Checking)
5. Controller(없음) : 상태를 갖지 않기 때문에 유효성 검증을 할 필요가 없음, 매핑된 요청을 처리할 때 값이 변경될 필요가 없음.

final 사용 시 주의점
- final 필드는 반드시 생성자에서 초기화해야 함

---

### DTO의 불변성과 final, builder 조합

#### 1. final 사용, builder 패턴 미사용
- **목적**: DTO의 불변성 보장
- **설명**: `final` 필드를 사용하여 객체의 상태를 변경 불가능하게 만들고, `builder` 패턴은 사용하지 않음. 생성자는 `final` 필드를 초기화하는 생성자를 만들어 불변성을 보장.
- **필요한 생성자**:
    - `@RequiredArgsConstructor` (`@AllArgsConstructor`도 가능하지만, 의도를 명확히 하려면 `@RequiredArgsConstructor`를 사용하는 것이 좋음)
- **사용하는 롬복 어노테이션**:
    - `@Getter`
    - `@RequiredArgsConstructor` (혹은 `@AllArgsConstructor`)

**예시**:
```java
@Getter
@RequiredArgsConstructor  // final 필드만 초기화하는 생성자 제공
public class BoardRequestDTO {
    private final String boardTitle;
    private final String boardContents;
}
```

#### 2. final 사용, builder 패턴 사용
   
- **목적**: 불변 객체 생성 및 유연한 객체 생성
- **설명**: final 필드를 사용하여 불변 객체를 만들고, builder 패턴을 사용하여 객체 생성을 유연하게 함. 이때 생성자는 @AllArgsConstructor를 사용하여 모든 필드를 초기화하고, @Builder를 사용하여 객체를 유연하게 생성하도록 함.
- **필요한 생성자**:
  - `@AllArgsConstructor` (모든 필드를 초기화하는 생성자 제공)
- **사용하는 롬복 어노테이션**:
  - `@Getter` 
  - `@AllArgsConstructor`
  - `@Builder`

**예시**:
```java
@Getter
@AllArgsConstructor  // 모든 필드를 초기화하는 생성자 제공
@Builder
public class BoardResponseDTO {
    private final String boardTitle;
    private final String boardContents;
}
```

#### 3. final 미사용, builder 패턴 사용

- **목적**: 불변성 보장 필요 없음 / 유연한 객체 생성
- **설명**: `final` 필드를 사용하지 않으며, `builder` 패턴을 사용하여 객체를 유연하게 생성. `final`을 사용하지 않기 때문에 객체의 필드를 동적으로 설정할 수 있으며, 객체의 값이 변경될 수 있는 경우에 유용함.
- **필요한 생성자**:
    - 생성자는 필요없고 `@Builder`로 객체를 생성합니다.
- **사용하는 롬복 어노테이션**:
    - `@Getter`
    - `@Builder`

**예시**:
```java
@Getter
@Builder
public class BoardRequestDTO {
    private String boardTitle;
    private String boardContents;
}
```