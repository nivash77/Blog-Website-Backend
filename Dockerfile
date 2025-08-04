# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy your Spring Boot JAR file into the container
COPY target/*.jar app.jar

# Expose port (change if your Spring Boot app uses a different one)
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
