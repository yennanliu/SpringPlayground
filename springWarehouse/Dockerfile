# Use an official OpenJDK runtime as a parent image
FROM openjdk:8-jre-slim

# Set the working directory to /app
WORKDIR /app

# Copy the compiled JAR file into the container at /app
COPY target/springWarehouse-0.0.1-SNAPSHOT.jar /app/springWarehouse.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 7777

# Run the Spring Boot application when the container starts
CMD ["java", "-jar", "springWarehouse.jar"]