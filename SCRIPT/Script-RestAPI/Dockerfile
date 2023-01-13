
FROM maven:3.8.6-openjdk-11-slim

# Install git
RUN apt-get update && apt-get install -y git

# Clone the repository from GitHub
RUN git clone https://github.com/IdoCohen138/RestAPI_Project.git /app

WORKDIR /app

# Build the project with Maven
RUN mvn package -f pom.xml -Dmaven.test.skip  

EXPOSE 8080/tcp

# Set the entrypoint to debug!
#ENTRYPOINT ["tail", "-f", "/dev/null"]

# Set the default command to run the jar file
CMD ["java" ,"-jar", "/app/business/target/business-1.0.jar"]






