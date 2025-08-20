# S3 모듈

AWS S3 연동을 위한 Spring Cloud AWS 기반 설정 모듈입니다.

S3Client 자동 설정과 기본 Properties를 제공하며, LocalStack을 통한 테스트 환경을 지원합니다.

## 🎯 목적

- **Spring Cloud AWS S3 설정**: S3Client Auto Configuration
- **환경별 설정 관리**: 운영/로컬/테스트 환경별 S3 엔드포인트 설정
- **테스트 인프라**: LocalStack TestContainer로 통합 테스트 지원
- **재사용성**: 다른 프로젝트에서도 그대로 사용 가능한 범용 S3 설정 모듈

## 📦 제공하는 기능

### Auto Configuration
- `S3Config`: Spring Cloud AWS S3 설정 활성화
- `S3Properties`: `@ConfigurationProperties`로 기본 버킷/리전 설정

### 자동 등록되는 Bean
```kotlin
// Spring Cloud AWS에서 자동 제공
S3Client           // AWS S3 클라이언트
S3AsyncClient      // 비동기 S3 클라이언트 (선택적)
```

## 🚀 사용법

### 1. 의존성 추가

#### apps 프로젝트의 build.gradle.kts
```kotlin
dependencies {
    implementation(project(":modules:s3"))
}
```

### 2. 설정 파일 import

#### application.yml
```yaml
spring:
  config:
    import:
      - s3.yml
```

### 3. AWS 설정 (운영환경)

#### application-prod.yml
```yaml
spring:
  cloud:
    aws:
      credentials:
        access-key: ${AWS_ACCESS_KEY_ID}
        secret-key: ${AWS_SECRET_ACCESS_KEY}

storage:
  s3:
    default-bucket: ${S3_BUCKET_NAME}
```

### 4. 코드에서 사용

#### S3Client 직접 사용
```kotlin
@Service
class ImageService(
    val s3Client: S3Client,
    val s3Properties: S3Properties
) {
    
    fun uploadImage(file: MultipartFile): String {
        val key = generateImageKey(file.originalFilename)
        val putRequest = PutObjectRequest.builder()
            .bucket(s3Properties.defaultBucket)
            .key(key)
            .contentType(file.contentType)
            .build()
            
        s3Client.putObject(putRequest, RequestBody.fromInputStream(file.inputStream, file.size))
        return generateS3Url(key)
    }
}
```

#### Presigned URL 생성
```kotlin
@Service
class PresignedUrlService(val s3Client: S3Client, val s3Properties: S3Properties) {
    
    fun generateDownloadUrl(key: String): URL {
        val presigner = S3Presigner.create()
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(s3Properties.defaultBucket)
            .key(key)
            .build()
            
        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(15))
            .getObjectRequest(getObjectRequest)
            .build()
            
        return presigner.presignGetObject(presignRequest).url()
    }
}
```

## ⚙️ 기본 설정값

```yaml
storage:
  s3:
    default-bucket: localtalk-storage
    region: ap-northeast-2
```

## 🧪 테스트 인프라 (testFixtures)

### 다른 모듈에서 testFixtures 사용하기

```kotlin
// build.gradle.kts
dependencies {
    testImplementation(testFixtures(project(":modules:s3")))
}
```

### LocalStack S3 테스트

```kotlin
@SpringBootTest
@ActiveProfiles("test")
@Import(LocalStackS3Config::class)
class S3IntegrationTest(
    val s3Client: S3Client,
    val s3TestFixtures: S3TestFixtures
) {

    @BeforeEach
    fun setUp() {
        s3TestFixtures.setupTestBucket("test-bucket")
    }

    @AfterEach
    fun tearDown() {
        s3TestFixtures.teardownTestBucket("test-bucket")
    }

    @Test
    fun `S3 파일 업로드가 성공한다`() {
        // given
        val putRequest = PutObjectRequest.builder()
            .bucket("test-bucket")
            .key("test/file.txt")
            .build()
        
        // when
        s3Client.putObject(putRequest, RequestBody.fromString("test content"))
        
        // then
        val headRequest = HeadObjectRequest.builder()
            .bucket("test-bucket")
            .key("test/file.txt")
            .build()
        assertDoesNotThrow { s3Client.headObject(headRequest) }
    }
}
```

### S3TestFixtures 기능

LocalStack S3 환경에서 테스트를 위한 유틸리티:

```kotlin
s3TestFixtures.setupTestBucket("my-bucket")      // 테스트 버킷 생성
s3TestFixtures.cleanupBucket("my-bucket")        // 버킷 내 모든 파일 삭제
s3TestFixtures.teardownTestBucket("my-bucket")   // 버킷 완전 삭제
```

## 🏗️ 모듈 구조

```
modules/s3/
├── src/main/kotlin/com/localtalk/s3/config/
│   ├── S3Config.kt              # S3 설정 활성화
│   └── S3Properties.kt          # Configuration Properties
├── src/main/resources/
│   └── s3.yml                   # S3 기본 설정 (환경별)
├── src/testFixtures/kotlin/com/localtalk/s3/config/
│   ├── LocalStackS3Config.kt    # LocalStack TestContainer 설정
│   └── S3TestFixtures.kt        # S3 테스트 유틸리티
└── build.gradle.kts             # spring-cloud-aws-starter-s3 의존성
```

### 핵심 클래스

#### S3Properties
```kotlin
@ConfigurationProperties(prefix = "storage.s3")
data class S3Properties(
    val defaultBucket: String,
    val region: String
)
```

#### LocalStack 통합
- **자동 시작**: Docker TestContainer로 LocalStack 자동 실행
- **설정 주입**: DynamicPropertySource로 엔드포인트/인증 자동 설정
- **테스트 격리**: 각 테스트별 버킷 생성/삭제

## 🚫 주의사항

### 이 모듈은 제공하지 않음
- ❌ 구체적인 파일 업로드/다운로드 로직
- ❌ MultipartFile 처리나 파일 검증
- ❌ 비즈니스 도메인에 특화된 기능
- ❌ Template 패턴이나 추상화 레이어

### 올바른 사용법
- ✅ S3Client Bean과 기본 설정만 제공
- ✅ 구체적인 파일 처리는 apps/infrastructure/storage에서 구현
- ✅ 환경별 설정 override 가능 (local: LocalStack, prod: AWS S3)

## 설정 변경이 필요하다면

### 기본 버킷명 변경
```yaml
# application.yml
storage:
  s3:
    default-bucket: my-custom-bucket
```

### 리전 변경
```yaml
# application.yml
storage:
  s3:
    region: us-west-2
```

## settings.gradle.kts 등록

modules/s3 모듈을 프로젝트에 등록:

```kotlin
include("modules:s3")
```