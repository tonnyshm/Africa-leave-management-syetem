# Dockerfile for auth-service
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/auth-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
