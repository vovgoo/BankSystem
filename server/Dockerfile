FROM gradle:8.10-jdk21 AS builder

WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]