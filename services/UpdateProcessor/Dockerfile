FROM gradle:8-jdk21-alpine AS builder
WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

COPY src ./src
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:TieredStopAtLevel=1 -XX:+UseParallelGC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]