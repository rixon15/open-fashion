FROM amazoncorretto:25-alpine-jdk AS builder

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests



FROM amazoncorretto:25-alpine AS runner

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/target/*.jar app.jar

ENV JAVA_OPTS="-XX:+USESZGC -XX:+ZGenerational"
# improveed ZGC of java 25

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]