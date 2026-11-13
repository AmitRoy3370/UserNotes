# ==========================================
# Stage 1: Build the application
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml first to cache the dependencies
COPY pom.xml .

# Download all dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy the rest of the project source code
COPY src ./src

# Build the application, skipping tests to speed up the deployment
RUN mvn clean package -DskipTests

# ==========================================
# Stage 2: Run the application
# ==========================================
FROM eclipse-temurin:21-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
# (It renames the jar to app.jar for simplicity)
COPY --from=build /app/target/*.jar app.jar

# Render dynamically assigns a PORT via environment variable.
# Spring Boot reads the SERVER_PORT env var automatically, 
# but it's good practice to expose a default port for documentation.
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]