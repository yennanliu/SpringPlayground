# Docker Setup for ChatAppV2

This guide explains how to set up and run PostgreSQL and Redis databases using Docker for the ChatAppV2 application.

## Prerequisites

- Docker installed on your system ([Download Docker](https://www.docker.com/products/docker-desktop))
- Docker Compose (included with Docker Desktop)

## Quick Start

### 1. Start the Databases

From the project root directory (`springChatRoomV2/`), run:

```bash
docker-compose up -d
```

This will:
- Start PostgreSQL on `localhost:5432`
- Start Redis on `localhost:6379`
- Create persistent volumes for data storage
- Run health checks to ensure services are ready

### 2. Verify Services are Running

```bash
# Check container status
docker-compose ps

# Check PostgreSQL logs
docker-compose logs postgres

# Check Redis logs
docker-compose logs redis
```

You should see both services as "healthy" after a few seconds.

### 3. Test Database Connections

**PostgreSQL:**
```bash
# Connect to PostgreSQL using psql
docker exec -it chatapp-postgres psql -U postgres -d chatapp

# Or test connection
docker exec chatapp-postgres pg_isready -U postgres
```

**Redis:**
```bash
# Connect to Redis using redis-cli
docker exec -it chatapp-redis redis-cli

# Test with PING command
docker exec chatapp-redis redis-cli ping
# Should return: PONG
```

### 4. Start Your Spring Boot Backend

The backend is already configured to connect to these databases:

```bash
cd backend/ChatAppV2
./mvnw spring-boot:run
```

## Configuration Details

### PostgreSQL
- **Image**: `postgres:16-alpine`
- **Port**: `5432`
- **Database**: `chatapp`
- **Username**: `postgres`
- **Password**: `password` (change in production!)
- **Data Volume**: `postgres_data`

### Redis
- **Image**: `redis:7-alpine`
- **Port**: `6379`
- **Persistence**: AOF (Append Only File) enabled
- **Data Volume**: `redis_data`

## Common Commands

### Stop Services
```bash
docker-compose stop
```

### Start Stopped Services
```bash
docker-compose start
```

### Stop and Remove Containers
```bash
docker-compose down
```

### Stop and Remove Containers + Volumes (⚠️ Deletes all data)
```bash
docker-compose down -v
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f postgres
docker-compose logs -f redis
```

### Restart Services
```bash
docker-compose restart
```

### Rebuild Services (if you change docker-compose.yml)
```bash
docker-compose up -d --force-recreate
```

## Troubleshooting

### Port Already in Use

If you see an error like "port is already allocated":

**PostgreSQL (5432):**
```bash
# Check what's using the port
lsof -i :5432

# Stop existing PostgreSQL (macOS with Homebrew)
brew services stop postgresql

# Or kill the process
kill -9 <PID>
```

**Redis (6379):**
```bash
# Check what's using the port
lsof -i :6379

# Stop existing Redis (macOS with Homebrew)
brew services stop redis
```

### Connection Refused

If your Spring Boot app can't connect:

1. Verify containers are running and healthy:
```bash
docker-compose ps
```

2. Check if services are listening:
```bash
docker exec chatapp-postgres pg_isready -U postgres
docker exec chatapp-redis redis-cli ping
```

3. Check backend configuration in `backend/ChatAppV2/src/main/resources/application.yml`:
   - PostgreSQL URL should be `jdbc:postgresql://localhost:5432/chatapp`
   - Redis host should be `localhost`

### Database Doesn't Exist

If you see "database does not exist" error:

```bash
# Connect and create manually
docker exec -it chatapp-postgres psql -U postgres
CREATE DATABASE chatapp;
\q
```

### Reset Everything

To start fresh (⚠️ deletes all data):

```bash
# Stop and remove everything
docker-compose down -v

# Start fresh
docker-compose up -d
```

## Production Considerations

For production deployment, you should:

1. **Change default passwords** in `docker-compose.yml`
2. **Use environment variables** for sensitive data
3. **Enable Redis authentication**:
   ```yaml
   redis:
     command: redis-server --requirepass your_redis_password
   ```
4. **Use stronger PostgreSQL password**
5. **Configure backups** for both databases
6. **Use Docker secrets** or external secret management
7. **Set resource limits**:
   ```yaml
   services:
     postgres:
       deploy:
         resources:
           limits:
             cpus: '1'
             memory: 1G
   ```

## Connecting from Other Tools

### PostgreSQL GUI Clients
- **Host**: `localhost`
- **Port**: `5432`
- **Database**: `chatapp`
- **User**: `postgres`
- **Password**: `password`

Popular clients: pgAdmin, DBeaver, DataGrip, TablePlus

### Redis GUI Clients
- **Host**: `localhost`
- **Port**: `6379`
- **No password** (default setup)

Popular clients: RedisInsight, Another Redis Desktop Manager, Medis

## Data Persistence

Data is persisted in Docker volumes:
- `postgres_data` - PostgreSQL database files
- `redis_data` - Redis AOF and RDB files

These volumes survive container restarts and recreations. They're only deleted with:
```bash
docker-compose down -v
```

To backup data:
```bash
# PostgreSQL backup
docker exec chatapp-postgres pg_dump -U postgres chatapp > backup.sql

# Redis backup
docker exec chatapp-redis redis-cli BGSAVE
docker cp chatapp-redis:/data/dump.rdb ./redis-backup.rdb
```

## Environment Variables

Create a `.env` file in the project root (copy from `.env.example`):

```bash
cp .env.example .env
```

Edit values as needed. Docker Compose will automatically load this file.

## Health Checks

Both services have health checks configured:
- **PostgreSQL**: `pg_isready` command every 10s
- **Redis**: `redis-cli ping` command every 10s

Spring Boot will wait for healthy services before connecting.
