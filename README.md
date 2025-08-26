# Local Talk Backend

> 지역 기반 행사 플랫폼 백엔드 서비스

## 📋 개요

Local Talk은 지역 기반 행사 서비스의 백엔드 API를 제공합니다. 사용자들이 특정 지역을 중심으로 다양한 행사를 발견하고 참여할 수 있는 플랫폼을 목표로 합니다.

## 🛠 기술 스택

### 💻 Language & Framework

![Kotlin](https://img.shields.io/badge/Kotlin-2.1-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)

### 🗄️ Database & Authentication

![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)

### 🛠️ Tools & Documentation

![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)

### 🧪 Testing

![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Testcontainers](https://img.shields.io/badge/Testcontainers-40AEF0?style=for-the-badge&logo=testcontainers&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## 🏗 프로젝트 구조

본 프로젝트는 멀티모듈 구조를 채택하여 관심사의 분리와 재사용성을 극대화했습니다.

```
local-talk-BE/
├── apps/                           # 실행 가능한 애플리케이션
│   └── localtalk-api/             # 메인 REST API 서버
├── modules/                        # 재사용 가능한 설정 모듈
│   ├── jpa/                       # JPA 설정 및 Base Entity
│   ├── web/                       # 웹 공통 설정 (Jackson, Filter 등)
│   ├── webclient/                 # 외부 API 호출 설정
│   └── s3/                        # AWS S3 연동 설정
├── supports/                       # 부가 기능 모듈
│   └── logging/                   # 로깅 설정 및 유틸리티
└── secrets/                        # 환경별 설정 파일
```

### 모듈 설계 기준

- **apps**: 구체적인 비즈니스 로직과 SpringBootApplication 구현체
- **modules**: 재사용 가능한 configuration과 추상화, 특정 구현에 종속되지 않음
- **supports**: 로깅, 모니터링과 같은 부가적인 기능을 지원하는 애드온 모듈

## 🚀 개발 환경 설정

### 필수 요구사항

- **Java 21** 이상
- **Docker & Docker Compose** (개발 환경용)
- **IDE**: IntelliJ IDEA 권장

### 1. 저장소 클론

```bash
git clone https://github.com/local-talk/local-talk-BE.git
cd local-talk-BE
```

### 2. 환경 설정

```bash
# secrets 디렉토리에 설정 파일 생성
cp secrets/secrets.properties.template secrets/secrets.properties

# 필요한 설정값 입력 (JWT secret, OAuth 설정 등)
vi secrets/secrets.properties
```

### 3. 개발 환경 실행

```bash
# Docker Compose로 개발용 인프라 실행
docker-compose -f docker/docker-compose.yml up -d

# MySQL, LocalStack(S3) 등이 실행됩니다
```

### 4. 애플리케이션 실행

```bash
# Gradle을 통한 실행
./gradlew :apps:localtalk-api:bootRun

# 또는 IDE에서 LocalTalkApiApplication.kt 실행
```

애플리케이션이 성공적으로 실행되면 `http://localhost:8080`에서 접근 가능합니다.

## 🧪 테스트

### 전체 테스트 실행

```bash
./gradlew test
```

### 특정 모듈 테스트

```bash
# API 애플리케이션 테스트만 실행
./gradlew :apps:localtalk-api:test

# 특정 테스트 클래스 실행
./gradlew test --tests "*SocialLoginControllerTest*"
```

## 📚 API 문서

다음 주소에서 API 문서를 확인할 수 있습니다.

- **Swagger UI**: https://localtalk.site/swagger-ui/index.html

## 🔧 개발 도구

### HTTP 클라이언트

`http/` 디렉토리에 IntelliJ HTTP Client 파일들이 준비되어 있습니다

해당 파일을 이용하여 엔드포인트를 쉽게 테스트할 수 있습니다.

## 🏛 아키텍처

### DDD 레이어 구조

```
├── entrypoint/          # Controller, DTO, Mapper (Presentation Layer)
├── application/         # Application Service (Application Layer)
├── domain/             # Entity, Service, Repository Interface (Domain Layer)
└── infrastructure/     # Repository 구현체, 외부 API Client (Infrastructure Layer)
```
