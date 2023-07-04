# Use a base image with Java installed
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/ServiceContractApp-0.0.1-SNAPSHOT.jar /app/ServiceContractApp-0.0.1-SNAPSHOT.jar

# Expose the port on which your application listens
EXPOSE 8080

# Set the command to run your application when the container starts
CMD ["java", "-jar", "ServiceContractApp-0.0.1-SNAPSHOT.jar"]