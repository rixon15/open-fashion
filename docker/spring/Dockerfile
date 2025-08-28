# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven
COPY ../../pom.xml .
COPY ../../src ./src

# If using Gradle, copy build.gradle and settings.gradle
# COPY ../../build.gradle .
# COPY ../../settings.gradle .

# Build the application using Maven
# This will create the executable JAR in target/ (or build/libs/)
RUN mvn clean install -DskipTests # For Maven
# RUN gradle clean build -x test # For Gradle

# Stage 2: Create the final lightweight runtime image
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the builder stage
# Adjust the JAR name if it's different (e.g., eventhub-backend-0.0.1-SNAPSHOT.jar)
COPY --from=builder /app/target/eventhub-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application runs on (default is 8080)
EXPOSE 8080

# Set environment variables for the Spring Boot application
# This is crucial for connecting to the MySQL container
ENV SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/openFashion_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC'
ENV SPRING_DATASOURCE_USERNAME=eventhub_user
ENV SPRING_DATASOURCE_PASSWORD=eventhub_password
# For initial setup, consider 'validate' or 'none' for production
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
