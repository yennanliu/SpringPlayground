# ── Stage 1: build ───────────────────────────────────────────────────────────
# Full Maven + JDK image — only used to compile and package the JAR.
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copy POM first so Docker caches dependency downloads separately from source changes.
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ── Stage 2: runtime ─────────────────────────────────────────────────────────
# Slim JRE-only image — no compiler, no Maven, no source code.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /build/target/ShoppingCart-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9999

# -XX:+UseContainerSupport  — respect cgroup CPU/memory limits
# -XX:MaxRAMPercentage=75   — use up to 75% of container memory for heap
# -Djava.security.egd      — faster SecureRandom, reduces Tomcat startup time
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
