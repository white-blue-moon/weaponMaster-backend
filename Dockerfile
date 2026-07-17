########################################
# 1) Build stage (Gradle bootJar)
########################################
FROM --platform=$BUILDPLATFORM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# 의존성 캐시 레이어: 빌드 스크립트만 먼저 복사
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# 소스 복사 후 빌드
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar

########################################
# 2) Runtime stage
########################################
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

# Docker 환경용 설정 (Spring Boot 가 ./config/application.yml 자동 로드)
COPY docker/application-docker.yml config/application.yml

EXPOSE 7070
ENTRYPOINT ["java", "-jar", "app.jar"]
