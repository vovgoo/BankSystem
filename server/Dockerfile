FROM gradle:8.10-jdk21 AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./ 
COPY src ./src

RUN gradle clean build -x test

FROM amazoncorretto:21-alpine-jdk

RUN apk add --no-cache curl && \
    adduser -D appuser

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

USER appuser

HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]