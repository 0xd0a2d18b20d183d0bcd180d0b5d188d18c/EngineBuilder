# Use lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine as builder

# Set working directory
WORKDIR /app

# Copy build files and download dependencies
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
RUN ./gradlew --no-daemon build || return 0

# Copy source code
COPY . .

# Build the application
RUN ./gradlew clean bootJar --no-daemon

# --- Production image ---
FROM eclipse-temurin:17-jre-alpine

# App directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
