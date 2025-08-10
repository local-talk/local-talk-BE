# Web 모듈

Spring Boot 웹 애플리케이션을 위한 공통 웹 기능 모듈입니다.

HTTP 로그 자동 수집, JSON 응답 포맷팅, 그리고 웹 유틸리티들을 제공합니다.

## 이게 뭔가요?

이 모듈을 추가하면:

- 📝 HTTP 요청/응답 자동 로깅 (TraceID 포함)
- 📊 구조화된 REST API 응답 모델
- 🔧 Jackson JSON 설정 자동 적용 (snake_case, Kotlin 지원)

## 어떻게 쓰나요?

### 1. 모듈 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation(project(":modules:web"))
}
```

### 2. 바로 사용

#### REST API 응답

```kotlin
import com.localtalk.common.model.RestResponse
import org.springframework.http.HttpStatus

@RestController
class UserController {

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): RestResponse<User> {
        val user = userService.findById(id)
        return RestResponse.success(user, "사용자 조회 성공")
    }

    @PostMapping("/users")
    fun createUser(@RequestBody user: User): RestResponse<Unit> {
        userService.create(user)
        return RestResponse.success("사용자 생성 완료", HttpStatus.CREATED)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): RestResponse<Unit> {
        return RestResponse.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다")
    }
}
```

## 자동으로 뭐가 되나요?

### 1. HTTP 요청/응답 로깅

모든 HTTP 요청과 응답이 자동으로 로깅됩니다:

```
📥 [HTTP REQUEST]
  Method    : POST
  URL       : /api/users?page=1
{
  "name" : "홍길동",
  "email" : "hong@example.com"
}

📤 [HTTP RESPONSE]
  Status    : 201
  Duration  : 45ms
{
  "code" : 201,
  "data" : null,
  "message" : "사용자 생성 완료"
}
```

### 2. TraceID 자동 생성

각 요청마다 고유한 TraceID가 생성되어 로그에 포함됩니다.

### 3. Jackson 설정 자동 적용

- **snake_case** 프로퍼티 네이밍
- **Kotlin** 모듈 자동 등록
- **LocalDateTime** 등 Java Time API 지원
- **알려지지 않은 프로퍼티** 무시

## REST API 응답 형식

모든 API 응답은 일관된 형식을 갖습니다:

```json
{
  "code": 200,
  "data": { "응답 데이터가 들어감" },
  "message": "성공 메시지"
}
```

| 필드        | 타입     | 설명            |
|-----------|--------|---------------|
| `code`    | Int    | HTTP 상태 코드    |
| `data`    | T      | 응답 데이터        |
| `message` | String | 사용자에게 보여줄 메시지 |

## 모듈 구조

```
modules/web/
├── src/main/kotlin/com/localtalk/
│   ├── common/
│   │   ├── model/
│   │   │   └── RestResponse.kt              # 통일된 REST 응답 모델
│   │   └── util/
│   │       ├── ContentCachingResponseWrapperUtils.kt  # 응답 래퍼 확장함수
│   │       └── JacksonUtils.kt             # JSON 유틸리티
│   ├── config/
│   │   └── JacksonConfig.kt               # Jackson 자동 설정
│   └── infrastructure/http/
│       ├── filter/
│       │   └── MDCLoggingFilter.kt        # HTTP 로깅 필터
│       ├── formatter/
│       │   └── HttpLogFormatter.kt        # 로그 포맷터
│       └── model/
│           └── CachedBodyRequestWrapper.kt # 요청 본문 캐싱
└── src/test/kotlin/                       # 단위 테스트
```

## 설정 변경이 필요하다면

### HTTP 로깅 끄기

```yaml
# application.yml
logging:
  level:
    com.localtalk.infrastructure.http: OFF
```