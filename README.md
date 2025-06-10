# Weapon Master Backend

> Spring Boot 기반의 웨펀마스터 홈페이지 백엔드 API 서버입니다.  
> 포트폴리오 제출용으로 제작되었습니다.

---

## 기술 스택

- **Framework**: Spring Boot 3.4.0
- **Java Version**: 17
- **환경 변수 관리**: `application.yml` 파일을 통해 데이터베이스 및 기타 설정 값 관리
- **주요 라이브러리**: 
  - Spring Data JPA
  - Spring Web
  - Mustache (서버 템플릿 엔진)
  - Spring WebSocket
  - Resilience4j (RateLimiter, Retry)
  - Lombok
  - MySQL Connector/J
  - Apache Commons Lang3
 
---

## 폴더 구조

```
config
└── application.yml

src
├── db
│   └── CREATE.sql 
│   └── INSERT.sql
│ 
├── api
│   └── <기능 이름>
│       └── controller
│           └── *.java
│
├── common
│   └── util
│       └── ErrorUtil.java
│
├── config
│   └── WebConfig.java
│   └── 기타 필요한 config
│
├── modules
│   └── <기능 이름>
│       ├── entity
│       │   └── *.java
│       ├── repository
│       │   └── *.java
│       ├── service
│       │   └── *.java
│       └── 기타 필요한 클래스들
│
│   └── common
│       ├── ApiResponse.java
│       ├── GlobalExceptionHandler.java
│       └── MyUrl.java

build.gradle
```
💡 api, modules 등은 실제로는 src/main/java/... 경로 내에 있으며, 가독성을 위해 축약하여 표기하였습니다.  
db는 실제 경로인 src/db에 위치합니다.


---

## 실행 방법

> ⚠️ 실행 전에 `application.yml` 환경 변수 파일을 먼저 설정해 주세요.

```bash
# 전체 빌드 및 테스트
./gradlew build

# 테스트를 생략하고 실행 JAR만 생성
./gradlew clean bootJar

# 실행
java -jar build/libs/weapon-backend-0.0.1-SNAPSHOT.jar

# 또는 개발 서버 실행
./gradlew bootRun
```

---

## 환경 변수 (application.yml 예시)

```ini
server:
  port: 7070

spring:
  application:
    name: weaponMaster-backend

  datasource:
    url: jdbc:mysql://your-db-host:3306/your-db-name
    username: your-db-username
    password: your-db-password
    driver-class-name: com.mysql.cj.jdbc.Driver

neople:
  api:
    key: your-neople-api-key

cors:
  allowedOrigins: "http://localhost:8080"

weapon-master:
  url: "http://localhost:8080/weapon-front"

```




