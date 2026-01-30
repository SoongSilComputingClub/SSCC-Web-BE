# SSCC-Web-BE Code Review Style Guide

이 프로젝트는 Spring Boot 3.5 / Java 17 백엔드이며, 아래 규칙을 기준으로 코드 리뷰를 수행한다.

## 1) API 응답 규칙

- 모든 Controller 응답은 `ApiResponse<T>`로 감싸야 한다 (`ApiResponse.success()`, `ApiResponse.created()`, `ApiResponse.fail()`).
- Controller는 Entity를 직접 반환하거나 파라미터로 받지 않는다. 반드시 DTO를 사용한다.
- 성공 응답은 `CommonSuccessCode`, 실패 응답은 도메인별 `ErrorCode` enum을 사용한다.

## 2) 예외 처리

- 비즈니스 예외는 `GeneralException`을 상속하고 도메인별 `ErrorCode` enum을 정의하여 던진다 (`ErrorCode` 인터페이스 구현).
- `RuntimeException`이나 `IllegalStateException`을 직접 던지지 않는다.
- 클라이언트 원인(4xx)과 서버 원인(5xx)을 명확히 구분한다.
- 새 에러 코드 추가 시 `HttpStatus`, `code` 문자열, `message`(한글) 세 필드를 모두 채운다.

## 3) 트랜잭션 & DB

- 조회 전용 서비스 메서드에는 `@Transactional(readOnly = true)`를 사용한다.
- N+1 가능성이 있으면 근거와 함께 지적하고, `@EntityGraph` 또는 `fetch join` 해결 방안을 제시한다.
- QueryDSL 사용 시 `BooleanExpression`을 조합하여 동적 쿼리를 구성한다.

## 4) 코드 스타일

- **포매팅**: Google Java Format (AOSP, 4-space 인덴트) — `./gradlew spotlessApply`로 자동 적용.
- **Import 순서**: `java` → `javax` → `jakarta` → `org` → `net` → `com` → 기타 → `lombok` (Spotless가 강제).
- **Checkstyle**: Naver Java 컨벤션 (Java 17 / Jakarta 수정 버전) 적용. `./gradlew checkstyleMain`으로 검증.
- 메서드/변수 이름은 역할이 드러나게 작성하고, 약어를 남발하지 않는다.
- 매직넘버/문자열은 반드시 상수로 추출한다.

## 5) 테스트

- 신규 비즈니스 로직은 최소 단위 테스트 1개 이상 작성한다.
- 경계값과 예외 케이스를 각각 1개 이상 포함한다.
- 테스트는 H2 인메모리 DB 위에서 실행된다 (`application-local.yaml`).

## 6) 리뷰 코멘트 스타일

- **"왜 문제인지"** + **"어떻게 고치면 좋은지"** 를 함께 제시한다.
- 한글로 작성한다.
- severity가 낮은 사항(nit)은 접두어 `[nit]`을 붙여 구분한다.
