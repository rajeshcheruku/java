# -----------------------------
# Stage 1: Optional Build Stage
# -----------------------------
ARG BUILD_JAR=true
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

ARG BUILD_JAR
RUN if [ "$BUILD_JAR" = "true" ]; then mvn clean package -DskipTests; fi

# -----------------------------
# Stage 2: Runtime Stage
# -----------------------------
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

ARG BUILD_JAR

# If we built inside Docker, copy from build stage
COPY --from=build /app/target/*.jar app.jar

# If JAR is pre-built on host, copy from host folder (only if BUILD_JAR=false)
# This only works if you ensure target/*.jar exists on host
# Otherwise Docker will fail
# COPY target/*.jar app.jar  

RUN chown appuser:appgroup app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
