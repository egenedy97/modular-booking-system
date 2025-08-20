# Docker Optimization Guide

## Overview

This guide covers the optimizations implemented in your Docker setup for the Modular Booking System.

## Key Optimizations Implemented

### 1. Base Image Optimization

- **Alpine Linux**: Switched from `postgres:16` to `postgres:16-alpine` for smaller image sizes
- **Multi-stage builds**: Implemented in Dockerfile to reduce final image size
- **Specific versions**: Pinned to exact versions for reproducibility

### 2. Security Improvements

- **Non-root user**: Application runs as non-root user (appuser:appgroup)
- **Read-only volumes**: Config files mounted as read-only (`:ro`)
- **Localhost binding**: All ports bound to `127.0.0.1` for security
- **Signal handling**: Using `dumb-init` for proper signal handling

### 3. Resource Management

- **Memory limits**: Set appropriate memory limits for each service
- **CPU limits**: CPU resource constraints to prevent resource hogging
- **Resource reservations**: Guaranteed resources for critical services

### 4. Health Checks

- **PostgreSQL**: `pg_isready` command to verify database connectivity
- **Redis**: `redis-cli ping` for Redis health verification
- **RabbitMQ**: `rabbitmq-diagnostics ping` for message broker health
- **Kong**: `kong health` for API gateway health
- **Application**: HTTP health check endpoint

### 5. Logging Optimization

- **Log rotation**: JSON file driver with size and file limits
- **Log levels**: Production-appropriate log levels (warn instead of debug)
- **Structured logging**: JSON format for better log parsing

### 6. Performance Optimizations

- **JVM tuning**: Container-aware JVM settings with G1GC
- **Redis optimization**: Memory policies and persistence settings
- **PostgreSQL tuning**: Shared preload libraries for monitoring
- **Kong optimization**: Auto worker processes and connections

### 7. Volume Management

- **Named volumes**: Proper volume drivers and management
- **Log persistence**: Separate log volumes for each service
- **Data persistence**: Persistent data storage for databases

## File Structure

```
├── docker-compose.yml          # Development/Testing setup
├── docker-compose.prod.yml     # Production-optimized setup
├── Dockerfile                  # Multi-stage application build
├── .dockerignore              # Build optimization exclusions
└── DOCKER_OPTIMIZATION_GUIDE.md # This guide
```

## Usage

### Development/Testing

```bash
docker-compose up -d
```

### Production

```bash
# Copy and customize environment variables
cp .env.example .env
# Edit .env with your production values

# Start production stack
docker-compose -f docker-compose.prod.yml up -d
```

## Environment Variables

Create a `.env` file with these variables:

```bash
# Database
POSTGRES_DB=bookingdb
POSTGRES_USER=bookinguser
POSTGRES_PASSWORD=your_secure_password

# RabbitMQ
RABBITMQ_USER=guest
RABBITMQ_PASS=guest
RABBITMQ_ERLANG_COOKIE=SWQOKODSQALRPCLNMEQG

# Application
SPRING_PROFILES_ACTIVE=prod
```

## Additional Optimization Tips

### 1. Build Optimization

- Use `.dockerignore` to exclude unnecessary files
- Leverage Docker layer caching
- Multi-stage builds for smaller final images

### 2. Runtime Optimization

- Monitor resource usage with `docker stats`
- Use `docker system prune` regularly
- Implement proper backup strategies for volumes

### 3. Network Optimization

- Use custom networks for service isolation
- Implement proper service discovery
- Consider using Docker Swarm for production scaling

### 4. Monitoring

- Implement proper logging aggregation
- Use health checks for service monitoring
- Monitor resource usage and set alerts

## Performance Metrics

### Expected Improvements

- **Image size**: 30-50% reduction with Alpine images
- **Memory usage**: 20-30% reduction with resource limits
- **Startup time**: Faster startup with health checks
- **Security**: Improved with non-root users and read-only mounts

### Resource Requirements

- **PostgreSQL**: 512M-1G RAM, 0.5-1.0 CPU
- **Redis**: 256M-512M RAM, 0.25-0.5 CPU
- **RabbitMQ**: 256M-512M RAM, 0.25-0.5 CPU
- **Kong**: 256M-512M RAM, 0.25-0.5 CPU
- **Application**: 512M-1G RAM, 0.5-1.0 CPU

## Troubleshooting

### Common Issues

1. **Port conflicts**: Check if ports are already in use
2. **Permission issues**: Ensure proper volume permissions
3. **Resource constraints**: Monitor resource usage and adjust limits
4. **Health check failures**: Verify service configurations

### Debug Commands

```bash
# Check service status
docker-compose ps

# View logs
docker-compose logs [service-name]

# Check resource usage
docker stats

# Inspect containers
docker inspect [container-name]
```

## Best Practices

1. **Always use specific image tags**
2. **Implement health checks for all services**
3. **Set appropriate resource limits**
4. **Use read-only mounts where possible**
5. **Implement proper logging strategies**
6. **Regular security updates**
7. **Backup strategies for persistent data**
8. **Monitor and alert on resource usage**

## Security Considerations

- All external ports bound to localhost
- Non-root user execution
- Read-only configuration mounts
- Environment variable configuration
- Regular security updates
- Proper network isolation
