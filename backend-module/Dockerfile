# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy generic POMs first to cache dependencies
COPY pom.xml .
COPY ejb-module/pom.xml ejb-module/
COPY backend-module/pom.xml backend-module/
# (Optional) RUN mvn dependency:go-offline -B 

# Copy source code
COPY ejb-module/src ejb-module/src
COPY backend-module/src backend-module/src

# Build the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built artifact from builder stage
COPY --from=builder /app/backend-module/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables (can be overridden)
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
