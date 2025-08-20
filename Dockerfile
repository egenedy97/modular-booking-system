# Build and run in a single stage
FROM maven:3.9.6-eclipse-temurin-17-alpine

# Set working directory
WORKDIR /app

# Copy pom.xml first for Maven cache
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application (Java 17, skip tests)
RUN mvn clean package -DskipTests -B

# Install runtime dependencies
RUN apk add --no-cache dumb-init wget bash

# Create logs directory
RUN mkdir -p /app/logs

# Switch to non-root for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup && \
    chown -R appuser:appgroup /app
USER appuser

# Expose default port
EXPOSE 8090


# Start app
ENTRYPOINT ["dumb-init", "--"]

CMD ["sh", "-c", "\
    java \
        -XX:+UseContainerSupport \
        -XX:MaxRAMPercentage=75.0 \
        -XX:+UseG1GC \
        -XX:+UseStringDeduplication \
        -Djava.security.egd=file:/dev/./urandom \
        -Dspring.application.name=${SPRING_APPLICATION_NAME:-booking-app} \
        -Damadeus.api.key=${AMADEUS_API_KEY} \
        -Damadeus.api.secret=${AMADEUS_API_SECRET} \
        -Dserver.port=${SERVER_PORT:-8090} \
        -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev} \
        -Dspring.datasource.url=${SPRING_DATASOURCE_URL:-jdbc:postgresql://postgres:5432/modular-booking-system} \
        -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME:-postgres} \
        -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD:-mysecretpassword} \
        -Dspring.redis.host=${SPRING_DATA_REDIS_HOST:-localhost} \
        -Dspring.redis.port=${SPRING_DATA_REDIS_PORT:-6379} \
        -Dspring.rabbitmq.host=${SPRING_RABBITMQ_HOST:-localhost} \
        -Dspring.rabbitmq.port=${SPRING_RABBITMQ_PORT:-5672} \
        -Dspring.rabbitmq.username=${SPRING_RABBITMQ_USERNAME:-guest} \
        -Dspring.rabbitmq.password=${SPRING_RABBITMQ_PASSWORD:-guest} \
        -jar target/*.jar \
"]
