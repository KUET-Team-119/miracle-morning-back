# 깃허브 액션으로 이미 빌드된 파일을 복사해서 가져오는 경우
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]

# Dockerfile에서 직접 프로젝트를 빌드하는 경우
# # 빌드 스테이지
# FROM openjdk:17-jdk-alpine AS Builder

# WORKDIR /miracle-morning-back

# # 소스 코드 복사
# COPY gradlew .
# COPY gradle gradle
# COPY build.gradle .
# COPY settings.gradle .
# COPY src src

# RUN chmod +x ./gradlew
# RUN ./gradlew bootJar

# # 실행 스테이지
# FROM openjdk:17-jdk-alpine

# # 빌드 스테이지에서 생성한 JAR 파일을 복사
# COPY --from=builder /miracle-morning-back/build/libs/*.jar app.jar

# # 인증 사진을 보관할 images 디렉토리 생성
# RUN mkdir -p /home/images

# # 실행 명령 설정
# ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]

# # /tmp 디렉토리를 호스트 시스템과 연결
# VOLUME /tmp