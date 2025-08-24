# JPA 모듈

Spring Boot JPA 애플리케이션을 위한 데이터베이스 연결과 공통 엔티티 기능 모듈입니다.

MySQL 연결 설정, 공통 엔티티 클래스, 그리고 테스트 인프라를 제공합니다.

## 이게 뭔가요?

이 모듈을 추가하면:

- 🗄️ **HikariCP** 기반 고성능 MySQL 연결 풀 설정
- 📅 **베이스 엔티티** - 자동 audit 필드와 soft/hard delete 선택
- 🧪 **testFixtures** - MySQL TestContainer와 데이터베이스 정리 유틸리티
- ⚡ **JPA 최적화** - 배치 처리와 UTC 시간대 설정

## 어떻게 쓰나요?

### 1. 모듈 추가

```kotlin
// build.gradle.kts
dependencies {
    implementation(project(":modules:jpa"))
}
```

### 2. Entity 클래스 만들기

#### 소프트 딜리트가 필요한 엔티티 (회원, 게시글 등)

```kotlin
import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "member")
class Member(
    @Column(name = "name", nullable = false)
    val name: String,
    
    @Column(name = "email", nullable = false)
    val email: String,
) : SoftDeleteBaseEntity() {

    // 비즈니스 로직이나 validation을 추가하려면
    override fun validate() {
        require(email.contains("@")) { "유효하지 않은 이메일 형식입니다" }
    }
}
```

#### 하드 딜리트가 필요한 엔티티 (토큰, 로그 등)

```kotlin
import com.localtalk.domain.HardDeleteBaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Column(name = "token", nullable = false, unique = true)
    val token: String,
) : HardDeleteBaseEntity()
```

### 3. Repository 만들기

```kotlin
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>
```

### 4. 테스트 작성하기

```kotlin
import com.localtalk.config.MysqlTestContainerConfig
import com.localtalk.utils.JpaDatabaseCleaner
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(MysqlTestContainerConfig::class)
class MemberRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val jpaDatabaseCleaner: JpaDatabaseCleaner,
) {

    @AfterEach
    fun cleanup() {
        jpaDatabaseCleaner.truncateAllTables()
    }

    @Test
    fun `회원을 저장하고 조회할 수 있다`() {
        // given
        val member = Member(name = "홍길동", email = "hong@example.com")
        
        // when
        val savedMember = memberRepository.save(member)
        
        // then
        assertThat(savedMember.id).isGreaterThan(0L)
        assertThat(savedMember.createdAt).isNotNull()
        assertThat(savedMember.updatedAt).isNotNull()
    }
}
```

## 자동으로 뭐가 되나요?

### 1. 베이스 엔티티 선택 가이드

**사용 가능한 베이스 엔티티:**
- `SoftDeleteBaseEntity` - 소프트 딜리트 (deleted_at 필드 포함)
- `HardDeleteBaseEntity` - 하드 딜리트 (deleted_at 필드 없음)
- ❌ `BaseEntity` - 직접 사용 금지

**언제 어떤걸 쓰나요?**
- **SoftDeleteBaseEntity**: 회원, 게시글, 댓글 등 복구 가능성이 있는 데이터
- **HardDeleteBaseEntity**: 토큰, 로그, 임시 데이터 등 완전 삭제가 필요한 데이터

### 2. 자동으로 추가되는 필드들

```sql
-- SoftDeleteBaseEntity 사용시
CREATE TABLE member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,    -- 자동 생성 📅
    updated_at DATETIME(6) NOT NULL,    -- 자동 업데이트 📅
    deleted_at DATETIME(6) NULL         -- Soft Delete 🗑️
);

-- HardDeleteBaseEntity 사용시
CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,    -- 자동 생성 📅
    updated_at DATETIME(6) NOT NULL     -- 자동 업데이트 📅
    -- deleted_at 필드 없음!
);
```

### 3. Soft Delete 기능 (SoftDeleteBaseEntity만)

```kotlin
// 데이터를 실제로 삭제하지 않고 deleted_at만 설정
member.delete()  // deleted_at = 현재시간

// 삭제 취소
member.restore()  // deleted_at = null

// HardDeleteBaseEntity는 이 기능이 없습니다!
// refreshTokenRepository.delete(token)  // 진짜 삭제됨
```

### 4. 자동 Validation

```kotlin
@Entity
class Member : SoftDeleteBaseEntity() {
    override fun validate() {
        require(name.isNotBlank()) { "이름은 필수입니다" }
        require(email.contains("@")) { "올바른 이메일 형식이 아닙니다" }
    }
}

// 저장할 때마다 자동으로 validate() 실행
memberRepository.save(member)  // validate() 호출됨
```

## 테스트 인프라 (testFixtures)

### 다른 모듈에서 testFixtures 사용하기

```kotlin
// build.gradle.kts
dependencies {
    testImplementation(testFixtures(project(":modules:jpa")))
}
```

### MysqlTestContainerConfig

자동으로 MySQL TestContainer를 시작하고 설정합니다:

```kotlin
@SpringBootTest
@Import(MysqlTestContainerConfig::class)  // 이것만 추가하면 끝!
class MyIntegrationTest {
    // 실제 MySQL 8.0이 Docker로 자동 시작됩니다
    // UTF8MB4 인코딩으로 설정됩니다
}
```

### JpaDatabaseCleaner

테스트 간 데이터 정리를 자동화합니다:

```kotlin
@AfterEach
fun cleanup() {
    jpaDatabaseCleaner.truncateAllTables()
}
```

- 모든 테이블을 자동 탐지해서 TRUNCATE
- FOREIGN KEY 제약 조건 안전하게 처리
- 테스트 격리 보장

## 데이터베이스 설정

### 기본 설정 (`jpa.yml`)

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none  # 운영환경에서는 none
    properties:
      hibernate:
        default_batch_fetch_size: 100    # N+1 문제 방지
        timezone.default_storage: NORMALIZE_UTC  # UTC 저장
        jdbc.time_zone: UTC              # 시간대 통일

datasource:
  hikari:
    master:
      maximum-pool-size: 40              # 커넥션 풀 크기
      minimum-idle: 30
      connection-timeout: 3000           # 3초 타임아웃
```

### 환경별 설정

#### Local/Test 환경
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: create      # 테이블 자동 생성
    show-sql: true          # SQL 로그 출력
```

#### Production 환경
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: none        # 수동 마이그레이션만
    show-sql: false         # SQL 로그 끄기
```

## 모듈 구조

```
modules/jpa/
├── src/main/kotlin/com/localtalk/
│   ├── config/
│   │   └── DataSourceConfig.kt           # HikariCP 설정
│   ├── domain/
│   │   └── BaseEntity.kt                 # 공통 엔티티 기능
│   └── resources/
│       └── jpa.yml                       # JPA 설정 파일
├── src/testFixtures/kotlin/com/localtalk/
│   ├── config/
│   │   └── MysqlTestContainerConfig.kt   # TestContainer 설정
│   └── utils/
│       └── JpaDatabaseCleaner.kt         # 테스트 데이터 정리
└── build.gradle.kts                      # testFixtures 플러그인
```

## 설정 변경이 필요하다면

### 커넥션 풀 크기 조정

```yaml
# application.yml
datasource:
  hikari:
    master:
      maximum-pool-size: 20   # 기본값 40
      minimum-idle: 10        # 기본값 30
```

### Hibernate SQL 로깅 켜기

```yaml
# application.yml
spring:
  jpa:
    show-sql: true
    
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### testFixtures 없이 사용하기

testFixtures 의존성을 추가하지 않으면 테스트 유틸리티 없이도 JPA 모듈을 사용할 수 있습니다:

```kotlin
// build.gradle.kts
dependencies {
    implementation(project(":modules:jpa"))
    // testImplementation(testFixtures(project(":modules:jpa")))  # 생략 가능
}
```

## 트러블슈팅

### Q. "Table doesn't exist" 오류가 발생해요
A. `spring.jpa.hibernate.ddl-auto` 설정을 확인하세요. `test` 프로파일에서는 `create`로 설정되어야 합니다.

### Q. 시간대 관련 오류가 발생해요
A. 모든 시간은 UTC로 저장됩니다. 애플리케이션에서 시간대 변환을 처리해주세요.

### Q. N+1 문제가 발생해요
A. `@BatchSize`나 `@EntityGraph`를 사용하거나, `default_batch_fetch_size: 100` 설정을 활용하세요.

### Q. testFixtures가 동작하지 않아요
A. Docker가 실행 중인지 확인하고, `@ActiveProfiles("test")`가 설정되어 있는지 확인하세요.