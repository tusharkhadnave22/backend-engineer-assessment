# Use a base image with OpenJDK 11 for building
FROM gradle:7.2.0-jdk11 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Gradle files to the container
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download and cache the dependencies
RUN gradle --no-daemon resolveDependencies

# Copy the source code to the container
COPY src ./src

# Build the application
RUN gradle --no-daemon build

# Create a new lightweight image for running the application
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the new image
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "app.jar"]
