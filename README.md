# Store API Server
## 진영수

- 📨 catsriding@gmail.com
- 🌩 [Swagger API Docs](https://store-api.catsriding.com/swagger)
- 📝 [Spring REST Docs](https://store-api.catsriding.com/docs/index.html)

## 1. 프로젝트 개요

### 1-1. 주요 기술 스택
- Java 23
- Gradle 8
- MySQL 8
- Spring Boot 3.4.3
- JPA
- QueryDSL
- Swagger
- Spring REST Docs
- Git & GitHub
- AWS
- Docker


### 1-2. 프로젝트 구조
프로젝트는 명확한 책임 분리를 위해 모듈화하여 설계되었으며, 각 계층 간의 접근을 강제함으로써 유지보수성과 확장성을 높였습니다. 주요 계층 및 기능은 다음과 같습니다.

```text
📁 store-server
├── 📁 api
├── 📁 application
├── 📁 domain
└── 📁 infra
     ├── 📁 database
     ├── 📁 logging
     ├── 📁 monitoring
     ├── 📁 security
     └── 📁 time
```

- **api**: 클라이언트와의 인터페이스를 담당하는 계층
-	**application**: 비즈니스 로직 조합 및 애플리케이션 흐름 제어 계층
-	**domain**: 핵심 도메인 모델과 비즈니스 규칙 및 정책을 정의하는 계층
- **infra**: 시스템 전반의 기술적 기능을 제공하는 계층
  - **database**: 데이터베이스 연결 및 접근 관리
  - **logging**: 애플리케이션 로깅 및 로그 저장 관리
  - **monitoring**: 서비스 상태 및 성능 모니터링
  - **security**: 암호화 처리
  - **time**: 시간 변환 및 포맷 관리

### 1-3. API 응답 표준화
일관된 API 응답 형식을 유지하기 위해 `ApiSuccessResponse<T>`와 `ApiErrorResponse<T>`를 사용하여 성공 및 오류 응답을 표준화하였습니다. 이를 통해 클라이언트는 일관된 형식으로 응답을 받을 수 있습니다.

```java
/**
 * API 요청이 정상적으로 처리되었을 때 반환하는 성공 응답 형식
 *
 * @param status  응답 상태: `SUCCESS`
 * @param data    요청 처리 결과 데이터: 제네릭 타입, 선택적 사용
 * @param message 응답 메시지 (예: "요청이 성공적으로 처리되었습니다.")
 */
public record ApiSuccessResponse<T>(
        ResponseType status,
        T data,
        String message) {...}

/**
 * API 요청 처리 중 오류가 발생했을 때 반환하는 예외 응답 형식
 *
 * @param status  응답 상태: `FAILURE`, `UNAUTHORIZED`, `FORBIDDEN`
 * @param data    오류 관련 추가 데이터: 제네릭 타입, 선택적 사용
 * @param message 오류 메시지 (예: "잘못된 요청입니다. 입력 값을 확인하세요.")
 */
public record ApiErrorResponse<T>(
        ResponseType status,
        T data,
        String message) {...}
```

### 1-4. 사용자 인증 및 권한 관리
사용자 인증은 **JWT 액세스 토큰**을 활용하여 이루어지고, **Spring Security**의 필터 체인에서 `JwtAuthenticationFilter`가 `Authorization` 헤더의 **JWT**를 검증하는 역할을 수행합니다.

- **JWT 검증**: `JwtAuthenticationFilter`에서 요청의 `Authorization` 헤더를 확인하고, 유효한 토큰이면 `SecurityContext`에 사용자 정보를 저장합니다.
- **인증 실패 처리**: 토큰이 유효하지 않거나 존재하지 않을 경우, `AuthenticationEntryPoint`에서 `401 Unauthorized` 응답을 반환합니다.
- **인가 검증**: 특정 리소스에 대한 접근 권한이 부족한 경우, `AccessDeniedHandler`에서 `403 Forbidden` 응답을 반환합니다.
- **메서드 단위 권한 제어**: `@PreAuthorize`를 활용하여 특정 API 메서드에 대한 접근을 제한할 수 있습니다.

또한, 현재 인증된 사용자의 정보를 컨트롤러에서 쉽게 접근할 수 있도록 `@CurrentUser` 어노테이션을 정의하였습니다. 이는 **Spring Security**의 `SecurityContextHolder`에 저장된 사용자 정보를 간편하게 접근할 수 있도록 지원합니다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : loginUser")
public @interface CurrentUser {}
```

사용자의 인증 정보를 나타내는 `LoginUser` 객체는 사용자 식별 정보와 권한 정보를 포함합니다.

```java
/**
 * 인증된 사용자 정보를 담는 객체
 *
 * @param id        사용자 고유 ID
 * @param username  사용자 계정명: 이메일 형식
 * @param roleType  사용자 권한 유형
 */
public record LoginUser(
        Long id,
        String username,
        UserRoleType roleType
) {}
```

이를 활용하면 컨트롤러에서 현재 인증된 사용자를 다음과 같이 쉽게 접근할 수 있습니다.

```java
@PostMapping("/api")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public ResponseEntity<?> api(@CurrentUser LoginUser user) {...}
```

이를 통해 보안이 적용된 API에서 로그인한 사용자의 정보를 간편하게 활용할 수 있으며, `@PreAuthorize`를 이용하여 접근 권한을 제어할 수 있습니다.

## 2. API 문서 및 테스트
[Swagger](https://store-api.catsriding.com/swagger)와 [Spring REST Docs](https://store-api.catsriding.com/docs/index.html)를 통해 API 스펙을 확인하고 테스트할 수 있습니다. 

**Swagger**는 API 테스트를 위한 UI 환경을 제공하여 개발자뿐만 아니라 QA, 프런트엔드, 기획자 등 다양한 협업 인원이 직접 API를 실행하고 검증할 수 있도록 지원합니다. 반면, **Spring REST Docs**는 테스트 코드와 연계된 문서화를 제공하여 가독성이 높고 공식 문서로 활용하기 용이합니다. 이러한 두 가지 방식의 장점을 활용하여, 보다 효과적인 API 문서화를 지원하도록 구성하였습니다.

스웨거에서 API 테스트를 진행하기 위해서는 **JWT 액세스 토큰**이 필요하며, 아래 과정을 통해 로그인 후 발급받은 토큰을 인증 정보로 등록해야 합니다.

#### 2-1. Access Token 발급
[Swagger UI](https://store-api.catsriding.com/swagger)에 접속한 후, 아래 계정으로 로그인 API를 호출하여 JWT Access Token을 발급받을 수 있습니다.

- 아이디: `user@gmail.com`
- 패스워드: `1234`

아래 `curl` 명령어를 복사하여 터미널에서 실행하면, 로그인 요청을 바로 전송할 수 있습니다:

```shell
## 로그인 API 
curl -X "POST" "https://store-api.catsriding.com/login" \
     -H 'Content-Type: application/json' \
     -d $'{
  "username": "user@gmail.com",
  "password": "1234"
}'
```

로그인에 성공하면 다음과 같이 JWT 액세스 토큰이 포함된 응답을 받게 됩니다:

![store-api-server_00.png](https://github.com/user-attachments/assets/b1de93e1-d3b2-4cc4-a540-535cbc86f3f2)

```json
{
  "status": "SUCCESS",
  "data": {
    "tokenType": "Bearer",
    "accessToken": "Bearer eyJhbGciOi.WKERO.dsfpowkp...",
    "expiresIn": 1756767145797
  },
  "message": "로그인이 성공적으로 완료되었습니다."
}
```

#### 2-2. Authorization 헤더 등록
발급받은 액세스 토큰은 [Swagger UI](https://store-api.catsriding.com/swagger) 상단의 `Authorize` 버튼을 클릭하여 입력할 수 있습니다. 이때, `Bearer `(공백 포함 7자)는 자동으로 추가되므로, **토큰 문자열만 입력**해야 합니다. 이렇게 하면 `Authorization` 헤더에 토큰이 자동으로 추가되며, 인증이 필요한 API를 정상적으로 호출할 수 있습니다.

![store-api-server_01.png](https://github.com/user-attachments/assets/685d3f32-78d1-4321-a8ad-b236229b8582)


## 3. 데이터베이스 및 테이블 스키마
데이터베이스는 상품 관리 및 옵션 관리를 지원하기 위해 정규화된 관계형 데이터 모델을 사용합니다. 상품과 옵션의 관계를 명확히 정의하고 데이터 무결성을 유지하기 위해, **상품**, **옵션**, **옵션 값**을 각각 독립적인 테이블로 분리하였습니다. 이를 통해 옵션 유형에 따라 **입력형**(`INPUT`)과 **선택형**(`SELECT`)을 유연하게 확장할 수 있도록 설계되었습니다.

또한, 상품 상태를 관리할 수 있도록 `status_type` 컬럼을 추가하여 활성화 여부를 명확하게 구분하였으며, `is_deleted` 컬럼을 활용한 **논리적 삭제 방식**을 적용하여 데이터의 추적 및 복구가 가능하도록 하였습니다.

아래 다이어그램은 이러한 사용자, 상품, 상품 옵션, 옵션 값 간의 관계를 시각적으로 나타냅니다:

![store-api-server_02.png](https://github.com/user-attachments/assets/58ced3b1-c8ec-4ba1-a51c-fc6871d43c34)

데이터베이스 스키마는 아래와 같습니다:

```sql
create table users
(
    id          bigint unsigned primary key,                                                                      -- 유저 ID
    username    varchar(255) unique                                        not null,                              -- 아이디: 이메일
    password    varchar(255)                                               not null,                              -- 패스워드
    role_type   enum ('ROLE_DEV', 'ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST') not null default 'ROLE_GUEST',         -- 권한
    status_type enum ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'DELETED')        not null default 'ACTIVE',             -- 계정 상태
    created_at  datetime(6)                                                not null default current_timestamp(6), -- 등록일
    updated_at  datetime(6)                                                not null default current_timestamp(6)  -- 수정일
);

create table products
(
    id           bigint unsigned primary key,                                                -- 상품 ID
    user_id      bigint unsigned                      not null,                              -- 판매자 ID
    name         varchar(255)                         not null,                              -- 상품명
    description  text                                 not null,                              -- 상품 설명
    price        int unsigned                         not null default 10,                   -- 상품 가격
    delivery_fee int unsigned                         not null default 0,                    -- 배송비
    status_type  enum ('SALE', 'INACTIVE', 'DELETED') not null default 'SALE',               -- 상품 상태
    is_deleted   boolean                              not null default false,                -- 논리적 삭제(soft delete)
    created_at   datetime(6)                          not null default current_timestamp(6), -- 등록일
    updated_at   datetime(6)                          not null default current_timestamp(6), -- 수정일
    foreign key (user_id) references users (id)
);

create table product_options
(
    id          bigint unsigned primary key,                                    -- 상품 옵션 ID
    product_id  bigint unsigned          not null,                              -- 상품 ID
    name        varchar(25)              not null,                              -- 옵션명 (예: "색상", "사이즈")
    option_type enum ('INPUT', 'SELECT') not null default 'INPUT',              -- 옵션 타입: `INPUT`: 직접 입력 | `SELECT`: 선택
    usable      boolean                  not null default true,                 -- 옵션 사용 여부
    is_deleted  boolean                  not null default false,                -- 논리적 삭제(soft delete)
    created_at  datetime(6)              not null default current_timestamp(6), -- 등록일
    updated_at  datetime(6)              not null default current_timestamp(6), -- 수정일
    foreign key (product_id) references products (id)
);

create table product_option_values
(
    id         bigint unsigned primary key,                           -- 상품 옵션 값 ID
    option_id  bigint unsigned not null,                              -- 상품 옵션 ID
    name       varchar(30)     not null,                              -- 옵션 값 (예: "블랙", "화이트", "레드")
    price      int unsigned    not null default 0,                    -- 추가 금액
    usable     boolean         not null default true,                 -- 옵션 값 사용 여부
    is_deleted boolean         not null default false,                -- 논리적 삭제(soft delete)
    created_at datetime(6)     not null default current_timestamp(6), -- 등록일
    updated_at datetime(6)     not null default current_timestamp(6), -- 수정일
    foreign key (option_id) references product_options (id)
);
```

## 4. 코드 검증 및 API 문서화
코드의 정확성을 보장하고 API 문서를 자동 생성하기 위해 단위 테스트와 통합 테스트를 실행할 수 있습니다. 단위 테스트는 개별 비즈니스 로직을 검증하고, 통합 테스트는 전반적인 API 동작을 확인하며, 동시에 Spring REST Docs를 이용해 문서를 자동으로 생성합니다.

### 4-1. 단위 테스트
단위 테스트는 핵심 비즈니스 로직이 기대한 대로 동작하는지 검증합니다. 이 테스트는 `restdocs` 태그를 제외한 테스트 케이스를 실행합니다.

```shell
./gradlew test --rerun-tasks
```

- `--rerun-tasks`: 캐시를 무시하고 모든 테스트를 다시 실행합니다.

### 4-2. 통합 테스트 및 REST Docs 문서화
통합 테스트는 전체 API의 흐름을 검증하며, 동시에 **Spring REST Docs**를 활용하여 API 문서를 자동으로 생성합니다.
이를 위해 `restdocs` 태그가 포함된 테스트 케이스가 실행됩니다.

```shell
./gradlew restdocsTest --rerun-tasks
```

- `restdocsTest` API 문서화와 관련된 테스트를 실행하고, 문서 스니펫을 생성합니다.

## 5. 에러 처리 및 로깅
애플리케이션의 안정성과 유지보수성을 고려하여 예외 처리와 로깅 환경을 구성하였습니다. 예외 처리는 인증 및 API 요청 예외를 구분하여 관리하며, 로깅은 시스템 리소스를 효율적으로 활용할 수 있도록 설정하였습니다.

### 5-1. 예외 처리
예외 처리는 크게 인증 관련 예외와 API 예외로 나누어 관리합니다.

#### 인증 관련 예외 처리
**Spring Security**의 필터 체인을 활용하여 `JwtAuthenticationFilter`에서 JWT 검증을 수행하며, 인증 및 권한 관련 예외 발생 시 `AuthenticationEntryPoint`와 `AccessDeniedHandler`의 커스텀 구현체에서 처리하도록 구성하였습니다.

#### API 예외 처리 
API 예외 처리는 `@RestControllerAdvice`를 활용하여 전역적으로 관리됩니다. 비즈니스 로직에서 정책이나 상태에 따라 예외가 발생하면, 이를 `@ExceptionHandler`에서 적절한 HTTP 응답 코드와 함께 처리합니다. 모든 API 예외 응답은 `ApiErrorResponse<T>` 클래스를 사용하여 일관된 형식을 유지합니다.

```java
public record ApiErrorResponse<T>(
        ResponseType status,
        T data,
        String message) {

    public static <T> ApiErrorResponse<T> failure(T data, String message) {
        return new ApiErrorResponse<T>(FAILURE, data, message);
    }

    public static <T> ApiErrorResponse<T> unauthorized(String message) {
        return new ApiErrorResponse<T>(UNAUTHORIZED, null, message);
    }

    public static <T> ApiErrorResponse<T> forbidden(String message) {
        return new ApiErrorResponse<T>(FORBIDDEN, null, message);
    }
}
```

### 5-2. 로깅
애플리케이션의 운영 상태를 모니터링하고 문제를 효과적으로 분석할 수 있도록 로깅을 구성하였습니다.

#### 로깅 정책
클라이언트 요청 정보 및 중요한 데이터 변경 이벤트는 `INFO` 레벨로 기록하고, 정책 위반이나 유효하지 않은 요청 등 비즈니스 로직과 관련된 예외는 `WARN` 레벨로 남깁니다. 이를 통해 운영 환경에서 필요한 정보만을 효과적으로 관리할 수 있도록 하였습니다.

로그 파일은 **Logback**을 활용한 롤링 정책을 적용하여 일정 기간 단위로 로그 파일을 관리함으로써, 시스템 자원을 효율적으로 사용하면서도 로그 추적이 가능하도록 하였습니다.

- `maxFileSize`: 로그 파일 하나 당 10MB를 초과하면 새로운 파일이 생성됩니다.
- `maxHistory`: 30일치 로그를 유지합니다.

#### 애플리케이션 모니터링
현재 **Actuator** 엔드포인트를 활성화하여 애플리케이션의 기본적인 상태를 모니터링할 수 있으며, 필요에 따라 **Prometheus** 및 **Grafana**와 연동할 수 있도록 확장성을 고려하여 설계하였습니다.

```shell
## Prometheus
curl "https://store-api.catsriding.com/actuator/prometheus"
```

## 6. 애플리케이션 실행
로컬에서 애플리케이션을 실행하기 위해서는 `test` 또는 `local` 프로파일을 사용할 수 있습니다.

#### Test 프로파일 실행
`test` 프로파일로 실행하면 **H2 메모리 데이터베이스**를 사용하며, 별도의 환경 변수 설정 없이 다음 명령어로 실행할 수 있습니다.

```shell
./gradlew bootRun
```

#### Local 프로파일 실행
`local` 프로파일을 사용할 경우, MySQL 등 실제 데이터베이스에 연결해야 하며, 환경 변수로 데이터베이스 접속 정보와 JWT 서명 키를 설정해야 합니다.

필요한 환경 변수는 다음과 같습니다:

- `security.jwt.secret`: JWT 토큰 서명 키
- `storage.database.writer.url`: 쓰기(Writer) DB 호스트 주소
- `storage.database.writer.username`: 쓰기(Writer) DB 유저네임
- `storage.database.writer.password`: 쓰기(Writer) DB 패스워드
- `storage.database.reader.url`: 읽기(Reader) DB 호스트 주소
- `storage.database.reader.username`: 읽기(Reader) DB 유저네임
- `storage.database.reader.password`: 읽기(Reader) DB 패스워드

아래와 같이 명령어를 실행하여 `local` 프로파일로 실행할 수 있습니다.

```shell
./gradlew bootRun --args="--spring.profiles.active=local \
--security.jwt.secret=defaultSecret \
--storage.database.reader.password=password1234 \
--storage.database.reader.url=localhost:3306/sandbox \
--storage.database.reader.username=jin \
--storage.database.writer.password=password1234 \
--storage.database.writer.url=localhost:3306/sandbox \
--storage.database.writer.username=jin"
```

