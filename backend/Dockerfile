FROM eclipse-temurin:22-jdk-alpine AS dev

# Set environment variables
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Set working directory
WORKDIR /app

RUN apk add --no-cache \
    maven \
    git \
    curl \
    jq

# Copy project files
COPY . .


# Expose Spring Boot default port
EXPOSE 8080

# Keep container running for DevContainers
CMD ["sleep", "infinity"]

FROM eclipse-temurin:22-jre-alpine as prod

# Set working directory
WORKDIR /app

# TODO: fix prod build
# RUN backend/.mvnw clean package -DskipTests
COPY --from=builder /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


