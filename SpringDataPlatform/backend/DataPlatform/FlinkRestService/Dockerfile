# Multi-stage build — stage 1 builds the JAR, stage 2 is a minimal JRE-only runtime.

# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Download dependencies first (separate layer — cached unless pom.xml changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build
COPY src ./src
RUN mvn package -DskipTests -B

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
# eclipse-temurin:17-jre (Debian-based) supports both linux/amd64 and linux/arm64.
# The -alpine variant only supports amd64 and will fail on Apple Silicon.
FROM eclipse-temurin:17-jre

WORKDIR /app

# wget is required by the HEALTHCHECK below
RUN apt-get update && apt-get install -y --no-install-recommends wget && rm -rf /var/lib/apt/lists/*

# Non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Directories owned by the spring user
# upload-dir: stores uploaded Flink JARs (JarController)
# logs:        logback RollingFileAppender writes here
RUN mkdir -p /app/upload-dir /app/logs && chown -R spring:spring /app/upload-dir /app/logs

COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

USER spring

EXPOSE 9999

HEALTHCHECK --interval=30s --timeout=5s --start-period=90s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9999/actuator/health || exit 1

# Container-aware JVM flags:
#   UseContainerSupport  — reads cgroup memory/CPU limits instead of host values
#   MaxRAMPercentage     — cap heap at 75 % of the container's memory limit
#   security.egd         — faster SecureRandom startup on Linux
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
