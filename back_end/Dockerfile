# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first for caching dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the app
RUN ./mvnw package -DskipTests

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "target/yugen-0.0.1-SNAPSHOT.jar"]