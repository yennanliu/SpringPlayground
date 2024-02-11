# Use official Maven image as the base image
FROM maven:3.8.3-openjdk-11-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files into the container
COPY pom.xml .

# Download all required dependencies into one layer
RUN mvn dependency:go-offline -B

# Copy the project source code into the container
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "target/FlinkRestService-0.0.1-SNAPSHOT.jar"]
