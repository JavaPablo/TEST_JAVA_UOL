# Use a Java base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY target/email-management-api.jar email-management-api-0.0.1.jar

# Command to run the application
CMD ["java", "-jar", "email-management-api.jar"]
