# MySQL Setup for HotelApp

## Overview
The HotelApp has been updated to use MySQL database instead of H2 in-memory database for persistent data storage.

## Configuration Profiles

### Default Profile (application.properties)
- Uses MySQL on `localhost:3306`
- Database: `hoteldb`
- Username: `root`
- Password: (empty)

### Docker Profile (application-docker.properties)
- Uses MySQL container service named `mysql`
- Optimized connection pool settings for containerized environment
- Database: `hoteldb`
- Username: `root`
- Password: (empty)

### Development Profile (application-dev.properties)
- Uses local MySQL on `localhost:3306`
- Database: `hoteldb_dev`
- Username: `root`
- Password: `root`
- Includes debug logging

## Running with Docker Compose

### Prerequisites
- Docker and Docker Compose installed
- No local MySQL instance running on port 3306 (or change the port mapping)

### Start the application
```bash
docker-compose up -d
```

This will:
1. Start MySQL 8.0 container with persistent volume
2. Wait for MySQL to be healthy
3. Start the HotelApp container
4. Initialize sample room data

### Access the application
- **Web UI**: http://localhost:8080
- **Swagger API**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### MySQL Container Access
```bash
# Connect to MySQL container (no password needed)
docker exec -it hotel-mysql mysql -u root hoteldb

# View logs
docker logs hotel-mysql
```

## Running Locally with MySQL

### Prerequisites
1. Install MySQL 8.0+ locally
2. Start MySQL with no root password:

```bash
# Start MySQL service (the root user should have no password)
mysql -u root

# Create the database
CREATE DATABASE hoteldb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Run the application
```bash
# Default profile
./mvnw spring-boot:run

# Development profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## Database Schema

The application uses JPA/Hibernate to automatically create and manage database schema:
- **rooms** table: Stores room information
- **bookings** table: Stores booking information with foreign key to rooms

### Schema Update Strategy
- **Default/Docker**: `update` - preserves data, updates schema
- **Development**: `create-drop` - recreates schema on each startup

## Connection Pool Settings

The application uses HikariCP for connection pooling:
- **Maximum Pool Size**: 10 (default), 20 (docker)
- **Minimum Idle**: 5
- **Connection Timeout**: 60 seconds

## Troubleshooting

### Common Issues

1. **Connection refused to MySQL**
   - Ensure MySQL is running and accessible
   - Check firewall settings
   - Verify credentials and database exist

2. **Table doesn't exist errors**
   - Check `spring.jpa.hibernate.ddl-auto` setting
   - Verify database permissions

3. **Docker container startup issues**
   - Check MySQL container health: `docker logs hotel-mysql`
   - Ensure no port conflicts on 3306

### Useful Commands

```bash
# Check running containers
docker ps

# View application logs
docker logs hotel-app

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild and restart
docker-compose up --build -d
```

## Environment Variables

For production deployments, override database configuration using environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://your-mysql-host:3306/hoteldb
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=
```