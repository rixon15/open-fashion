FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /app

COPY ../../pom.xml .
COPY ../../src ./src


RUN mvn clean install -DskipTests # For Maven

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/eventhub-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/openFashion_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC'
ENV SPRING_DATASOURCE_USERNAME=eventhub_user
ENV SPRING_DATASOURCE_PASSWORD=eventhub_password
# For initial setup, consider 'validate' or 'none' for production
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
