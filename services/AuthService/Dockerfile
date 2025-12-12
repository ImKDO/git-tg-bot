FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew \
    && ./gradlew --no-daemon --stacktrace help

COPY src src

ARG SKIP_TESTS=true
RUN if [ "$SKIP_TESTS" = "true" ]; then \
        ./gradlew --no-daemon clean bootJar -x test; \
    else \
        ./gradlew --no-daemon clean bootJar; \
    fi

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS=""

COPY --from=build /workspace/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
