# WebClient 모듈

외부 API 호출을 위한 공통 WebClient 설정 및 Bean 제공 모듈입니다.

## 🎯 목적

- **Spring MVC + WebClient 아키텍처**: 내부는 Spring MVC, 외부 호출만 WebClient 활용
- **공통 설정 통합**: 타임아웃, 연결풀, 메모리 제한 등 외부 API 호출 공통 설정
- **재사용성**: 다른 프로젝트에서도 그대로 사용 가능한 범용 WebClient 모듈

## 📦 제공하는 기능

### Auto Configuration
- `WebClientConfig`: Spring Boot Auto Configuration으로 WebClient Bean 자동 등록
- `WebClientProperties`: `@ConfigurationProperties`로 설정값 바인딩

### 제공되는 Bean
```kotlin
@Bean
fun webClientBuilder(): WebClient.Builder   

@Bean  
fun defaultWebClient(): WebClient        
```

### 설정 가능한 항목
- **타임아웃 설정**: connect, response, read, write timeout
- **메모리 제한**: 응답 메모리 사이즈 제한
- **연결 관리**: 최대 연결 수, 대기 시간

## 🚀 사용법

### 1. 의존성 추가

#### apps 프로젝트의 build.gradle.kts
```kotlin
dependencies {
    implementation(project(":modules:webclient"))
}
```

### 2. 설정 파일 import

#### application.yml
```yaml
spring:
  config:
    import:
      - webclient.yml
```

### 3. 설정값 커스터마이징 (선택사항)

기본값을 오버라이드하려면 application.yml에 추가:

```yaml
client:
  webclient:
    connect-timeout: 10s        # 기본: 30s
    response-timeout: 15s       # 기본: 30s  
    read-timeout: 20s          # 기본: 30s
    write-timeout: 10s         # 기본: 30s
    max-in-memory-size: 5242880 # 5MB (기본: 10MB)
    max-connections: 300       # 기본: 500
    pending-acquire-timeout: 30s # 기본: 60s
```

### 4. 코드에서 사용

#### 기본 WebClient 사용
```kotlin
@Service
class ExternalApiService(
    private val webClient: WebClient  // 자동 주입
) {
    suspend fun callKakaoApi(): String {
        return webClient
            .get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .bodyToMono<String>()
            .awaitSingle()
    }
}
```

#### 커스텀 WebClient 생성
```kotlin
@Service  
class AppleApiService(
    private val webClientBuilder: WebClient.Builder
) {
    private val appleWebClient = webClientBuilder
        .baseUrl("https://appleid.apple.com")
        .defaultHeader("Content-Type", "application/json")
        .build()
        
    suspend fun validateToken(identityToken: String): AppleTokenInfo {
        return appleWebClient
            .post()
            .uri("/auth/token")
            .bodyValue(mapOf("id_token" to identityToken))
            .retrieve()
            .bodyToMono<AppleTokenInfo>()
            .awaitSingle()
    }
}
```

## ⚙️ 기본 설정값

```yaml
client:
  webclient:
    connect-timeout: 30s        # 연결 타임아웃
    response-timeout: 30s       # 응답 타임아웃  
    read-timeout: 30s          # 읽기 타임아웃
    write-timeout: 30s         # 쓰기 타임아웃
    max-in-memory-size: 10485760 # 10MB 응답 크기 제한
    max-connections: 500       # 최대 연결 수
    pending-acquire-timeout: 60s # 연결 대기 시간
```

## 🏗️ 내부 구조

```
modules/webclient/
├── src/main/kotlin/com/localtalk/webclient/
│   └── config/
│       ├── WebClientConfig.kt           # Auto Configuration
│       └── WebClientProperties.kt       # Configuration Properties
├── src/main/resources/
│   └── webclient.yml                   # 기본 설정
└── build.gradle.kts                    # 모듈 의존성
```

### 핵심 클래스

#### WebClientConfig
```kotlin
@Configuration
@EnableConfigurationProperties(WebClientProperties::class)
class WebClientConfig(private val properties: WebClientProperties) {
    // WebClient Bean 생성 및 설정 적용
}
```

#### WebClientProperties  
```kotlin
@ConfigurationProperties(prefix = "client.webclient")
data class WebClientProperties(
    val connectTimeout: Duration = Duration.ofSeconds(30),
    // ... 기타 설정
)
```

## 🚫 주의사항

### 이 모듈은 제공하지 않음
- ❌ 구체적인 API 호출 로직 (OAuth2 토큰 교환, 사용자 정보 조회 등)
- ❌ 특정 서비스에 종속적인 설정 (Kakao, Apple 등)
- ❌ 비즈니스 로직 구현

### 올바른 사용법
- ✅ 외부 API 호출 시에만 사용 (OAuth2 provider, 3rd party API)
- ✅ 내부 서비스 간 통신은 일반적인 Spring MVC 방식 사용
- ✅ Coroutine/suspend function과 함께 사용하여 성능 최적화
