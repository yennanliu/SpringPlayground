# Use the official Maven image with Java 8 for building the application
FROM maven:3.3-jdk-8-onbuild as builder

# Switch to the working directory for the build
WORKDIR /usr/src/app

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Create a lightweight Java 8 runtime image
FROM openjdk:8-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the compiled JAR file from the builder stage into the container
COPY --from=builder /usr/src/app/target/springWarehouse-0.0.1-SNAPSHOT.jar /app/springWarehouse.jar

# Expose port 7777 for the Spring Boot application
EXPOSE 7777

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "springWarehouse.jar"]
