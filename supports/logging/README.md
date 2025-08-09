# Logging 모듈

프로젝트 전체에서 사용할 통합 로깅 모듈입니다.

환경별로 최적화된 로그 설정이 자동으로 적용되고, 간편한 로거 사용을 위한 확장 함수를 제공합니다.

## 이게 뭔가요?

이 모듈을 추가하면:

- 📝 `logger()` 확장함수로 쉽게 로거 생성
- 🔧 환경별 로그 설정 자동 적용 (local/dev/test/prod)
- ⚡ 비동기 로깅으로 성능 최적화
- 📊 운영 환경에선 JSON 형태로 구조화된 로그 출력

## 어떻게 쓰나요?

### 1. 모듈 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation(project(":supports:logging"))
}
```

### 2. 바로 사용

```kotlin
import com.localtalk.logging.common.logger

class UserService {
    private val log = logger()  // 로그 설정

    fun createUser(user: User) {
        log.info("사용자 생성: {}", user.email)
        log.debug("상세 정보: {}", user)  // local 환경에서만 출력됨

        try {
            // 비즈니스 로직
        } catch (e: Exception) {
            log.error("오류 발생", e)  // 스택 트레이스도 함께 기록
        }
    }
}
```

### 3. 환경 설정

```bash
# application.yml 또는 실행 시 프로파일 지정
spring:
  profiles:
    active: local  # local, dev, test, prod 중 선택
```

## 환경별로 뭐가 다른가요?

| 환경        | 어떻게 출력되나요?                | 언제 써요?    |
|-----------|---------------------------|-----------|
| **local** | 콘솔에 하이라이트로, DEBUG까지 모든 로그 | 로컬 개발할 때  |
| **dev**   | 콘솔에 비동기로, 성능 최적화          | 개발 서버에서   |
| **prod**  | JSON 형태로 출력, ERROR만 별도 수집 | 운영 서버에서   |
| **test**  | 콘솔만, 외부 라이브러리 로그 최소화      | 테스트 실행할 때 |


## 생성되는 로그 파일

### 운영 환경에서만

```
logs/
└── error.log               # ERROR 레벨만 별도 수집 (JSON)
```

## 추가 설정이 필요하다면

### 특정 패키지 로그 레벨 조정

```yaml
# application.yml
logging:
  level:
    com.localtalk.user: TRACE      # 특정 패키지만 더 상세하게
    org.springframework: WARN      # Spring 로그는 줄이기
    org.hibernate.SQL: DEBUG       # SQL 쿼리 보고 싶을 때
```

## 모듈 구조

```
supports/logging/
├── src/main/kotlin/com/localtalk/logging/
│   └── LoggerExtensions.kt        # logger() 확장 함수 정의
└── src/main/resources/
    ├── logback-spring.xml         # 환경별 로그 설정
    └── appender/                  # 출력 방식별 세부 설정
        ├── console-appender.xml   # 콘솔 출력
        ├── json-appender.xml      # JSON 형태 출력  
        └── file-appender.xml      # 파일 출력
```
