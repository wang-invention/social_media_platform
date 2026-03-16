FROM openjdk:17-jdk-slim
COPY target/chat-service.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]