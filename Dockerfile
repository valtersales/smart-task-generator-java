# Multi-stage Dockerfile to optimize image size

# Stage 1: Build
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and compile
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only the compiled JAR from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Configure JVM for containerized environments
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

