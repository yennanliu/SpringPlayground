# Cluster Admin

A Spring Boot application for managing AWS EC2 worker nodes with health monitoring, automatic status synchronization, and a web-based dashboard.

## Quick Ref
- Access the application:
   - Web UI: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

## Cmd

```bash
aws ec2 describe-instances --region us-east-1
```

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [System Design](#system-design)
- [How It Works](#how-it-works)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [Development](#development)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)

## Overview

Cluster Admin is a POC (Proof of Concept) system that provides:

- **Node Management**: Create, start, stop, terminate EC2 instances
- **Health Monitoring**: Automatic health checks with configurable thresholds
- **Status Synchronization**: Sync node status with actual EC2 instance state
- **Web Dashboard**: Real-time visualization of cluster status
- **REST API**: Full programmatic access to all features

### Technology Stack

| Component | Technology |
|-----------|------------|
| Backend | Spring Boot 4.0.3, Java 17+ |
| Database | H2 (dev) / PostgreSQL (prod) |
| Cloud | AWS SDK v2 (EC2) |
| API Docs | OpenAPI 3 / Swagger UI |
| Frontend | Vanilla HTML/CSS/JavaScript |

## Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              Cluster Admin                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐                │
│  │   Web UI     │     │  REST API    │     │   Swagger    │                │
│  │  (Static)    │     │ Controllers  │     │     UI       │                │
│  └──────┬───────┘     └──────┬───────┘     └──────┬───────┘                │
│         │                    │                    │                         │
│         └────────────────────┼────────────────────┘                         │
│                              │                                              │
│                              ▼                                              │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                         Service Layer                                  │ │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐       │ │
│  │  │   NodeService   │  │   Ec2Service    │  │ HealthMonitor   │       │ │
│  │  │                 │  │                 │  │    Service      │       │ │
│  │  │ - CRUD ops      │  │ - Launch        │  │                 │       │ │
│  │  │ - Start/Stop    │◄─┤ - Start/Stop    │  │ - Health checks │       │ │
│  │  │ - Terminate     │  │ - Terminate     │  │ - Cluster status│       │ │
│  │  │ - Sync          │  │ - Describe      │  │ - Auto-recovery │       │ │
│  │  └────────┬────────┘  └────────┬────────┘  └────────┬────────┘       │ │
│  └───────────┼────────────────────┼────────────────────┼─────────────────┘ │
│              │                    │                    │                    │
│              ▼                    ▼                    │                    │
│  ┌────────────────────┐  ┌────────────────────┐       │                    │
│  │   NodeRepository   │  │    AWS EC2 API     │◄──────┘                    │
│  │      (JPA)         │  │                    │                            │
│  └─────────┬──────────┘  └─────────┬──────────┘                            │
│            │                       │                                        │
│            ▼                       ▼                                        │
│  ┌────────────────────┐  ┌────────────────────┐                            │
│  │     H2 / RDS       │  │   AWS EC2 Fleet    │                            │
│  │     Database       │  │                    │                            │
│  └────────────────────┘  └────────────────────┘                            │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Package Structure

```
src/main/java/com/yen/clusterAdmin/
├── ClusterAdminApplication.java    # Main entry point
├── config/
│   ├── AwsConfig.java              # AWS EC2 client configuration
│   ├── Ec2Properties.java          # EC2 default settings
│   ├── HealthMonitorProperties.java # Health check settings
│   └── SchedulerConfig.java        # Spring scheduler config
├── controller/
│   ├── NodeController.java         # Node CRUD + actions API
│   ├── ClusterController.java      # Cluster status/health API
│   └── WorkerController.java       # Worker heartbeat API
├── service/
│   ├── NodeService.java            # Node business logic
│   ├── Ec2Service.java             # AWS EC2 operations
│   └── HealthMonitorService.java   # Health monitoring logic
├── repository/
│   └── NodeRepository.java         # JPA repository
├── model/
│   ├── entity/
│   │   └── Node.java               # Node JPA entity
│   ├── dto/
│   │   ├── NodeDTO.java
│   │   ├── NodeCreateRequest.java
│   │   ├── ClusterStatusDTO.java
│   │   ├── ClusterHealthDTO.java
│   │   └── HeartbeatRequest.java
│   └── enums/
│       └── NodeStatus.java         # PENDING, RUNNING, etc.
└── exception/
    ├── NodeNotFoundException.java
    ├── Ec2OperationException.java
    └── GlobalExceptionHandler.java
```

## System Design

### Data Model

#### Node Entity

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Primary key |
| name | String | Human-readable node name |
| instanceId | String | AWS EC2 instance ID |
| status | Enum | PENDING, RUNNING, UNHEALTHY, STOPPED, TERMINATED |
| instanceType | String | EC2 instance type (e.g., t3.medium) |
| availabilityZone | String | AWS AZ (e.g., us-east-1a) |
| privateIp | String | Private IP address |
| publicIp | String | Public IP address (if assigned) |
| cpuUsage | Double | CPU utilization percentage |
| memoryUsage | Double | Memory utilization percentage |
| failedHealthChecks | Integer | Consecutive failed health check count |
| lastHeartbeat | Instant | Last successful heartbeat timestamp |
| createdAt | Instant | Node creation timestamp |
| updatedAt | Instant | Last update timestamp |

### State Machine

```
                    ┌─────────────┐
                    │   CREATE    │
                    └──────┬──────┘
                           │
                           ▼
                    ┌─────────────┐
          ┌────────│   PENDING   │────────┐
          │        └──────┬──────┘        │
          │               │               │
          │ (EC2 launch   │ (EC2 launch   │ (timeout/
          │  failed)      │  success)     │  error)
          │               │               │
          ▼               ▼               ▼
   ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
   │   STOPPED   │ │   RUNNING   │ │ TERMINATED  │
   └──────┬──────┘ └──────┬──────┘ └─────────────┘
          │               │
          │ start()       │ (health check
          └───────────────┤  failures)
                          │
          ┌───────────────┤
          │               │
          │ (recovered)   ▼
          │        ┌─────────────┐
          └────────│  UNHEALTHY  │
                   └──────┬──────┘
                          │
                          │ stop()/terminate()
                          ▼
                   ┌─────────────┐
                   │   STOPPED   │
                   │     or      │
                   │ TERMINATED  │
                   └─────────────┘
```

### Health Monitoring

The health monitor runs scheduled checks (default: every 30 seconds):

1. **Active Nodes Check**: Queries all RUNNING and UNHEALTHY nodes
2. **Health Probe**: Attempts HTTP health check to node's IP (port 8080)
3. **Failure Tracking**: Increments `failedHealthChecks` on failure
4. **Status Transition**: Marks node UNHEALTHY after threshold (default: 3)
5. **Recovery**: Resets counter and restores RUNNING status on success

```
Health Check Flow:

  ┌─────────┐    ┌─────────────┐    ┌──────────┐
  │ Running │───▶│ Health Check│───▶│ Success? │
  │  Nodes  │    │   (HTTP)    │    └────┬─────┘
  └─────────┘    └─────────────┘         │
                                    Yes  │  No
                                   ┌─────┴─────┐
                                   ▼           ▼
                            ┌──────────┐ ┌──────────┐
                            │  Reset   │ │Increment │
                            │ Counter  │ │ Counter  │
                            └──────────┘ └────┬─────┘
                                              │
                                              ▼
                                       ┌────────────┐
                                       │ >= Thresh? │
                                       └──────┬─────┘
                                         Yes  │  No
                                        ┌─────┴─────┐
                                        ▼           ▼
                                 ┌───────────┐ ┌────────┐
                                 │   Mark    │ │  Keep  │
                                 │ UNHEALTHY │ │ Status │
                                 └───────────┘ └────────┘
```

## How It Works

### EC2 Instance Lifecycle Management

#### 1. Creating a Node

When you create a node through the API or UI:

```
User Request                    Cluster Admin                      AWS EC2
     │                               │                                │
     │  POST /api/v1/nodes           │                                │
     │  {name, instanceType, zone}   │                                │
     │──────────────────────────────▶│                                │
     │                               │                                │
     │                               │  RunInstances API              │
     │                               │  (AMI, type, zone, tags)       │
     │                               │───────────────────────────────▶│
     │                               │                                │
     │                               │◀───────────────────────────────│
     │                               │  instanceId: i-xxx             │
     │                               │                                │
     │                               │  Save to DB                    │
     │                               │  (status: PENDING)             │
     │                               │                                │
     │◀──────────────────────────────│                                │
     │  Node created                 │                                │
     │  {id, instanceId, status}     │                                │
```

#### 2. Starting a Stopped Node

```java
// NodeService.startNode()
1. Validate node exists and is in STOPPED/PENDING state
2. Call EC2 StartInstances API
3. Update node status to PENDING
4. EC2 eventually transitions instance to "running"
5. Next sync/health-check updates node to RUNNING
```

#### 3. Stopping a Running Node

```java
// NodeService.stopNode()
1. Validate node exists and is RUNNING/UNHEALTHY
2. Call EC2 StopInstances API
3. Update node status to STOPPED
4. EC2 gracefully stops the instance
```

#### 4. Terminating a Node

```java
// NodeService.terminateNode()
1. Validate node is not already TERMINATED
2. Call EC2 TerminateInstances API
3. Update node status to TERMINATED
4. EC2 terminates and eventually deletes the instance
```

### EC2 Synchronization

The system keeps node records in sync with actual EC2 state:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Local DB      │     │  Sync Service   │     │    AWS EC2      │
│                 │     │                 │     │                 │
│ Node: worker-1  │     │                 │     │ Instance: i-xxx │
│ status: RUNNING │     │ DescribeInst.   │     │ state: stopped  │
│ ip: null        │────▶│ ───────────────▶│────▶│ ip: 10.0.1.5    │
│                 │     │                 │     │                 │
│                 │     │ Update Node     │     │                 │
│ status: STOPPED │◀────│ ◀───────────────│◀────│                 │
│ ip: 10.0.1.5    │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

**Sync triggers:**
- Manual: `POST /api/v1/nodes/{id}/sync` or `POST /api/v1/cluster/sync`
- Automatic: During health checks (when EC2 integration is enabled)

### Worker Heartbeat (Optional)

Worker nodes can report their status back to the cluster:

```
Worker Node                     Cluster Admin
     │                               │
     │  POST /api/v1/worker/heartbeat│
     │  {nodeId, cpuUsage, memUsage} │
     │──────────────────────────────▶│
     │                               │
     │                               │  Update metrics
     │                               │  Reset health counter
     │                               │  Update lastHeartbeat
     │                               │
     │◀──────────────────────────────│
     │  200 OK                       │
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- AWS Account (for EC2 integration)
- AWS CLI configured (or environment variables)

### Quick Start (Local Development)

1. **Clone and build:**
   ```bash
   git clone <repository-url>
   cd clusterAdmin
   mvn clean package -DskipTests
   ```

2. **Run with EC2 disabled (local testing):**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application:**
   - Web UI: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console

### Running with EC2 Integration

1. **Configure AWS credentials:**
   ```bash
   # Option 1: AWS CLI profile
   aws configure

   # Option 2: Environment variables
   export AWS_ACCESS_KEY_ID=your-key
   export AWS_SECRET_ACCESS_KEY=your-secret
   export AWS_REGION=us-east-1
   ```

2. **Update application.properties:**
   ```properties
   cluster.ec2.enabled=true
   cluster.ec2.ami=ami-0c55b159cbfafe1f0
   cluster.ec2.security-group-id=sg-xxxxx
   cluster.ec2.subnet-id=subnet-xxxxx
   cluster.ec2.key-name=your-key-pair
   ```

3. **Run:**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

## Configuration

### Application Properties

```properties
# Server
server.port=8080

# Database (H2 for dev)
spring.datasource.url=jdbc:h2:mem:clusterdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# EC2 Integration
cluster.ec2.enabled=false
cluster.ec2.region=us-east-1
cluster.ec2.ami=ami-0c55b159cbfafe1f0
cluster.ec2.instance-type=t3.medium
cluster.ec2.key-name=
cluster.ec2.security-group-id=
cluster.ec2.subnet-id=

# Health Monitoring
cluster.health.enabled=true
cluster.health.interval=30000
cluster.health.timeout=5000
cluster.health.unhealthy-threshold=3
```

### AWS IAM Permissions

Required IAM policy for EC2 operations:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:RunInstances",
        "ec2:StartInstances",
        "ec2:StopInstances",
        "ec2:TerminateInstances",
        "ec2:DescribeInstances",
        "ec2:CreateTags"
      ],
      "Resource": "*"
    }
  ]
}
```

## API Reference

### Node Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/nodes` | List all nodes |
| GET | `/api/v1/nodes/{id}` | Get node by ID |
| POST | `/api/v1/nodes` | Create new node |
| PUT | `/api/v1/nodes/{id}` | Update node |
| DELETE | `/api/v1/nodes/{id}` | Delete node |
| POST | `/api/v1/nodes/{id}/start` | Start node |
| POST | `/api/v1/nodes/{id}/stop` | Stop node |
| POST | `/api/v1/nodes/{id}/terminate` | Terminate node |
| POST | `/api/v1/nodes/{id}/sync` | Sync with EC2 |

### Cluster Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/cluster/status` | Get cluster status |
| GET | `/api/v1/cluster/health` | Get cluster health |
| POST | `/api/v1/cluster/sync` | Sync all nodes with EC2 |
| POST | `/api/v1/cluster/health-check` | Trigger health check |

### Worker API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/worker/heartbeat` | Report heartbeat |
| POST | `/api/v1/worker/register` | Register new worker |

### Example Requests

**Create Node:**
```bash
curl -X POST http://localhost:8080/api/v1/nodes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "worker-001",
    "instanceType": "t3.medium",
    "availabilityZone": "us-east-1a"
  }'
```

**Get Cluster Status:**
```bash
curl http://localhost:8080/api/v1/cluster/status
```

**Response:**
```json
{
  "totalNodes": 5,
  "runningNodes": 3,
  "pendingNodes": 1,
  "stoppedNodes": 1,
  "unhealthyNodes": 0,
  "terminatedNodes": 0,
  "averageCpuUsage": 45.5,
  "averageMemoryUsage": 62.3,
  "lastUpdated": "2024-01-15T10:30:00Z"
}
```

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=NodeServiceTest
```

### Test Coverage

| Package | Classes | Coverage |
|---------|---------|----------|
| service | NodeService, Ec2Service, HealthMonitorService | ~85% |
| controller | NodeController, ClusterController, WorkerController | ~80% |
| repository | NodeRepository | ~90% |

### Debug Mode

**Enable debug logging:**
```properties
logging.level.com.yen.clusterAdmin=DEBUG
logging.level.org.springframework.web=DEBUG
```

**Remote debugging:**
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

Then attach your IDE debugger to port 5005.

### Code Style

- Follow standard Java conventions
- Use meaningful variable names
- Add JavaDoc for public methods
- Keep methods under 30 lines when possible

## Deployment

### Docker Deployment

**Dockerfile:**
```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/clusterAdmin-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build and run:**
```bash
mvn clean package -DskipTests
docker build -t cluster-admin .
docker run -p 8080:8080 \
  -e CLUSTER_EC2_ENABLED=true \
  -e AWS_ACCESS_KEY_ID=xxx \
  -e AWS_SECRET_ACCESS_KEY=xxx \
  cluster-admin
```

### AWS ECS Deployment

1. **Push to ECR:**
   ```bash
   aws ecr get-login-password | docker login --username AWS --password-stdin <account>.dkr.ecr.<region>.amazonaws.com
   docker tag cluster-admin:latest <account>.dkr.ecr.<region>.amazonaws.com/cluster-admin:latest
   docker push <account>.dkr.ecr.<region>.amazonaws.com/cluster-admin:latest
   ```

2. **Create ECS Task Definition** with:
   - Container image from ECR
   - IAM role with EC2 permissions
   - Environment variables for configuration

3. **Create ECS Service** with:
   - Application Load Balancer
   - Auto-scaling based on CPU/memory

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cluster-admin
spec:
  replicas: 2
  selector:
    matchLabels:
      app: cluster-admin
  template:
    metadata:
      labels:
        app: cluster-admin
    spec:
      containers:
      - name: cluster-admin
        image: cluster-admin:latest
        ports:
        - containerPort: 8080
        env:
        - name: CLUSTER_EC2_ENABLED
          value: "true"
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
---
apiVersion: v1
kind: Service
metadata:
  name: cluster-admin
spec:
  selector:
    app: cluster-admin
  ports:
  - port: 80
    targetPort: 8080
  type: LoadBalancer
```

### Production Checklist

- [ ] Use external database (PostgreSQL/RDS) instead of H2
- [ ] Configure proper AWS IAM roles (not access keys)
- [ ] Enable HTTPS with valid certificates
- [ ] Set up monitoring (CloudWatch, Prometheus)
- [ ] Configure log aggregation (CloudWatch Logs, ELK)
- [ ] Set up alerting for unhealthy nodes
- [ ] Implement backup strategy for database
- [ ] Configure auto-scaling for the admin service itself

## Troubleshooting

### Common Issues

**1. EC2 operations fail with credentials error:**
```
Ec2OperationException: Failed to launch instance
Caused by: SdkClientException: Unable to load credentials
```
**Solution:** Configure AWS credentials via CLI, environment variables, or IAM role.

**2. Health checks always fail:**
```
Node transitions to UNHEALTHY immediately
```
**Solution:** Ensure the worker nodes have port 8080 accessible and respond to `/health` endpoint.

**3. Nodes stuck in PENDING:**
```
Node never transitions to RUNNING
```
**Solution:**
- Check EC2 console for instance status
- Verify security group allows inbound traffic
- Check subnet has internet access (for public AMIs)

**4. Java version compatibility:**
```
IllegalArgumentException: Unmatched bit position
```
**Solution:** Ensure Maven compiler plugin targets Java 17:
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <configuration>
    <release>17</release>
  </configuration>
</plugin>
```

### Logs

**Application logs:**
```bash
# When running with Maven
mvn spring-boot:run 2>&1 | tee app.log

# When running JAR
java -jar target/clusterAdmin-*.jar > app.log 2>&1
```

**Enable SQL logging:**
```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

### Health Check Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Spring Boot health (if actuator enabled) |
| `/api/v1/cluster/health` | Cluster-wide health status |

## License

This project is for educational/POC purposes.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests: `mvn test`
5. Submit a pull request
