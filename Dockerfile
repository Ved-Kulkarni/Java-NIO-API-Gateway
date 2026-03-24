FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/http-server-1.0-SNAPSHOT.jar app.jar

EXPOSE 9091

CMD ["java", "-cp", "app.jar", "com.ved.App"]