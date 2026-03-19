# Cluster Admin - Backend System Specification

> POC for managing worker nodes on AWS EC2

## 1. Overview

### 1.1 Purpose
Build a backend system that manages a cluster of worker nodes running on AWS EC2 instances. The system will provide APIs to provision, monitor, and control worker nodes for distributed workloads.

### 1.2 Scope (POC)
- Register and manage EC2-based worker nodes
- Monitor node health and status
- Basic cluster operations (add/remove nodes)
- Simple task distribution model

### 1.3 Technology Stack
| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 4.0.3 |
| Language | Java 17 |
| Cloud | AWS (EC2, SDK v2) |
| Database | H2 (dev) / PostgreSQL (prod) |
| API | REST (OpenAPI 3.0) |
| Build | Maven |

---

## 2. Architecture

### 2.1 High-Level Design

```
┌─────────────────────────────────────────────────────────────┐
│                     Cluster Admin Server                     │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  REST API    │  │  Scheduler   │  │  Health      │      │
│  │  Controller  │  │  Service     │  │  Monitor     │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                 │               │
│  ┌──────┴─────────────────┴─────────────────┴───────┐      │
│  │              Core Services Layer                  │      │
│  │  ┌────────────┐  ┌────────────┐  ┌────────────┐  │      │
│  │  │ NodeService│  │EC2Service  │  │TaskService │  │      │
│  │  └────────────┘  └────────────┘  └────────────┘  │      │
│  └──────────────────────────────────────────────────┘      │
│         │                                                   │
│  ┌──────┴───────────────────────────────────────────┐      │
│  │              Data Layer (JPA Repository)          │      │
│  └───────────────────────────────────────────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               ▼               ▼
     ┌──────────┐    ┌──────────┐    ┌──────────┐
     │ Worker   │    │ Worker   │    │ Worker   │
     │ Node 1   │    │ Node 2   │    │ Node N   │
     │ (EC2)    │    │ (EC2)    │    │ (EC2)    │
     └──────────┘    └──────────┘    └──────────┘
```

### 2.2 Package Structure

```
com.yen.clusterAdmin/
├── ClusterAdminApplication.java
├── config/
│   ├── AwsConfig.java
│   ├── SecurityConfig.java
│   └── SchedulerConfig.java
├── controller/
│   ├── NodeController.java
│   ├── ClusterController.java
│   └── TaskController.java
├── service/
│   ├── NodeService.java
│   ├── Ec2Service.java
│   ├── TaskService.java
│   └── HealthMonitorService.java
├── repository/
│   ├── NodeRepository.java
│   └── TaskRepository.java
├── model/
│   ├── entity/
│   │   ├── Node.java
│   │   └── Task.java
│   ├── dto/
│   │   ├── NodeDTO.java
│   │   ├── NodeCreateRequest.java
│   │   └── TaskDTO.java
│   └── enums/
│       ├── NodeStatus.java
│       └── TaskStatus.java
└── exception/
    ├── NodeNotFoundException.java
    └── ClusterException.java
```

---

## 3. Data Models

### 3.1 Node Entity

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Primary key |
| instanceId | String | AWS EC2 instance ID |
| name | String | Human-readable node name |
| privateIp | String | Private IP address |
| publicIp | String | Public IP address (nullable) |
| status | NodeStatus | PENDING, RUNNING, STOPPED, TERMINATED |
| instanceType | String | EC2 instance type (e.g., t3.medium) |
| availabilityZone | String | AWS AZ |
| cpuUsage | Double | Current CPU % |
| memoryUsage | Double | Current memory % |
| lastHeartbeat | Timestamp | Last health check time |
| createdAt | Timestamp | Node creation time |
| updatedAt | Timestamp | Last update time |

### 3.2 Task Entity (Optional for POC)

| Field | Type | Description |
|-------|------|-------------|
| id | UUID | Primary key |
| nodeId | UUID | Assigned worker node |
| type | String | Task type identifier |
| payload | JSON | Task parameters |
| status | TaskStatus | QUEUED, RUNNING, COMPLETED, FAILED |
| result | JSON | Task result (nullable) |
| createdAt | Timestamp | Task creation time |
| completedAt | Timestamp | Task completion time |

### 3.3 Enums

**NodeStatus:**
- `PENDING` - EC2 instance launching
- `RUNNING` - Node active and healthy
- `UNHEALTHY` - Node not responding
- `STOPPED` - EC2 instance stopped
- `TERMINATED` - EC2 instance terminated

---

## 4. API Specification

### 4.1 Node Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/nodes` | List all nodes |
| GET | `/api/v1/nodes/{id}` | Get node by ID |
| POST | `/api/v1/nodes` | Register/create new node |
| PUT | `/api/v1/nodes/{id}` | Update node info |
| DELETE | `/api/v1/nodes/{id}` | Remove node from cluster |
| POST | `/api/v1/nodes/{id}/start` | Start EC2 instance |
| POST | `/api/v1/nodes/{id}/stop` | Stop EC2 instance |
| POST | `/api/v1/nodes/{id}/terminate` | Terminate EC2 instance |

### 4.2 Cluster Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/cluster/status` | Get cluster overview |
| GET | `/api/v1/cluster/health` | Health check all nodes |
| POST | `/api/v1/cluster/scale` | Scale cluster (add/remove nodes) |

### 4.3 Request/Response Examples

**Create Node Request:**
```json
POST /api/v1/nodes
{
  "name": "worker-001",
  "instanceType": "t3.medium",
  "availabilityZone": "us-east-1a",
  "tags": {
    "env": "poc",
    "team": "platform"
  }
}
```

**Node Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "instanceId": "i-0123456789abcdef0",
  "name": "worker-001",
  "privateIp": "10.0.1.100",
  "publicIp": null,
  "status": "RUNNING",
  "instanceType": "t3.medium",
  "availabilityZone": "us-east-1a",
  "cpuUsage": 15.5,
  "memoryUsage": 42.3,
  "lastHeartbeat": "2026-03-19T10:30:00Z",
  "createdAt": "2026-03-19T09:00:00Z"
}
```

**Cluster Status Response:**
```json
{
  "totalNodes": 5,
  "runningNodes": 4,
  "unhealthyNodes": 1,
  "averageCpuUsage": 35.2,
  "averageMemoryUsage": 58.7,
  "lastUpdated": "2026-03-19T10:30:00Z"
}
```

---

## 5. AWS Integration

### 5.1 Required AWS Services
- **EC2**: Worker node instances
- **IAM**: Service credentials and roles
- **VPC**: Network configuration (optional for POC)
- **CloudWatch**: Metrics collection (optional)

### 5.2 IAM Permissions Required

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:DescribeInstances",
        "ec2:RunInstances",
        "ec2:StartInstances",
        "ec2:StopInstances",
        "ec2:TerminateInstances",
        "ec2:CreateTags"
      ],
      "Resource": "*"
    }
  ]
}
```

### 5.3 AWS SDK Configuration

**application.properties:**
```properties
# AWS Configuration
aws.region=us-east-1
aws.accessKeyId=${AWS_ACCESS_KEY_ID}
aws.secretKey=${AWS_SECRET_ACCESS_KEY}

# EC2 Defaults
ec2.default.ami=ami-xxxxxxxxxxxxxxxxx
ec2.default.instanceType=t3.medium
ec2.default.keyName=cluster-admin-key
ec2.default.securityGroupId=sg-xxxxxxxxxxxxxxxxx
ec2.default.subnetId=subnet-xxxxxxxxxxxxxxxxx
```

---

## 6. Health Monitoring

### 6.1 Health Check Strategy
- **Interval**: Every 30 seconds
- **Timeout**: 5 seconds
- **Unhealthy Threshold**: 3 consecutive failures
- **Check Method**: HTTP GET to node's health endpoint

### 6.2 Metrics Collected
- CPU utilization
- Memory usage
- Disk usage
- Network I/O
- Process count

### 6.3 Alerting (Future)
- Node becomes unhealthy
- Cluster capacity below threshold
- High resource utilization

---

## 7. Dependencies (pom.xml additions)

```xml
<!-- AWS SDK v2 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>ec2</artifactId>
    <version>2.25.0</version>
</dependency>

<!-- Spring Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 Database (dev) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- OpenAPI/Swagger -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>

<!-- Lombok (optional) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

---

## 8. Implementation Phases

### Phase 1: Foundation (Core)
- [x] Project setup (done)
- [ ] Add Maven dependencies
- [ ] Create data models (Node entity)
- [ ] Setup JPA repositories
- [ ] Create basic NodeService
- [ ] Implement REST controllers
- [ ] Add H2 database config

### Phase 2: AWS Integration
- [ ] Configure AWS SDK
- [ ] Implement Ec2Service
- [ ] Wire EC2 operations to NodeService
- [ ] Test with real EC2 instances

### Phase 3: Health Monitoring
- [ ] Create HealthMonitorService
- [ ] Implement scheduled health checks
- [ ] Update node status based on health
- [ ] Add cluster status endpoint

### Phase 4: Enhancements (Optional)
- [ ] Add authentication (Spring Security)
- [ ] Task distribution system
- [ ] Auto-scaling logic
- [ ] CloudWatch integration
- [ ] WebSocket for real-time updates

---

## 9. Configuration Files

### application.properties (Development)
```properties
spring.application.name=clusterAdmin
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:clusterdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# AWS (override via env variables)
aws.region=us-east-1

# Health Monitor
cluster.health.check.interval=30000
cluster.health.check.timeout=5000

# OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 10. Run Commands

```bash
# Build
mvn clean package

# Run (development)
mvn spring-boot:run

# Run JAR
java -jar target/clusterAdmin-0.0.1-SNAPSHOT.jar

# Run with AWS credentials
AWS_ACCESS_KEY_ID=xxx AWS_SECRET_ACCESS_KEY=xxx java -jar target/clusterAdmin-0.0.1-SNAPSHOT.jar

# Run tests
mvn test
```

---

## 11. References

- [AWS SDK for Java v2 Documentation](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [EC2 API Reference](https://docs.aws.amazon.com/AWSEC2/latest/APIReference/Welcome.html)
