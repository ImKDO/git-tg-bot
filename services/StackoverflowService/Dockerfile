FROM gradle:8-jdk24-alpine AS builder
WORKDIR /app

# Install ca-certificates for proper TLS handling
RUN apk add --no-cache ca-certificates && update-ca-certificates

# Copy gradle wrapper and settings
COPY settings.gradle.kts build.gradle.kts* ./
COPY gradle ./gradle

# Copy common module
COPY common ./common

# Copy StackoverflowService
COPY StackoverflowService ./StackoverflowService

# Build the application with retry logic
RUN gradle :StackoverflowService:build -x test --no-daemon \
    --stacktrace \
    -Dorg.gradle.internal.http.connectionTimeout=120000 \
    -Dorg.gradle.internal.http.socketTimeout=120000 \
    || gradle :StackoverflowService:build -x test --no-daemon \
    -Dorg.gradle.internal.http.connectionTimeout=120000 \
    -Dorg.gradle.internal.http.socketTimeout=120000

FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=builder /app/StackoverflowService/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:TieredStopAtLevel=1 -XX:+UseParallelGC"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

