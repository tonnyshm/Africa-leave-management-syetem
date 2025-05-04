# Dockerfile for leave-service
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/leave-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
