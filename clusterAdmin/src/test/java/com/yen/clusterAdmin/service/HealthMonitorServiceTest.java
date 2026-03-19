package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.config.HealthMonitorProperties;
import com.yen.clusterAdmin.model.dto.ClusterHealthDTO;
import com.yen.clusterAdmin.model.dto.ClusterStatusDTO;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import com.yen.clusterAdmin.repository.NodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthMonitorServiceTest {

    @Mock
    private NodeRepository nodeRepository;

    @Mock
    private Ec2Service ec2Service;

    @Mock
    private HealthMonitorProperties healthProperties;

    @InjectMocks
    private HealthMonitorService healthMonitorService;

    private Node runningNode;
    private Node unhealthyNode;

    @BeforeEach
    void setUp() {
        runningNode = new Node();
        runningNode.setId(UUID.randomUUID());
        runningNode.setName("running-worker");
        runningNode.setStatus(NodeStatus.RUNNING);
        runningNode.setCpuUsage(45.0);
        runningNode.setMemoryUsage(60.0);
        runningNode.setFailedHealthChecks(0);
        runningNode.setCreatedAt(Instant.now());

        unhealthyNode = new Node();
        unhealthyNode.setId(UUID.randomUUID());
        unhealthyNode.setName("unhealthy-worker");
        unhealthyNode.setStatus(NodeStatus.UNHEALTHY);
        unhealthyNode.setFailedHealthChecks(3);
        unhealthyNode.setLastHeartbeat(Instant.now().minusSeconds(300));
        unhealthyNode.setCreatedAt(Instant.now());

        ReflectionTestUtils.setField(healthMonitorService, "ec2Enabled", false);
    }

    @Nested
    @DisplayName("getClusterStatus")
    class GetClusterStatusTests {

        @Test
        @DisplayName("should return cluster status with all counts")
        void shouldReturnClusterStatusWithAllCounts() {
            when(nodeRepository.count()).thenReturn(5L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(3L);
            when(nodeRepository.countByStatus(NodeStatus.PENDING)).thenReturn(1L);
            when(nodeRepository.countByStatus(NodeStatus.STOPPED)).thenReturn(0L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(1L);
            when(nodeRepository.countByStatus(NodeStatus.TERMINATED)).thenReturn(0L);
            when(nodeRepository.findByStatus(NodeStatus.RUNNING)).thenReturn(List.of(runningNode));

            ClusterStatusDTO status = healthMonitorService.getClusterStatus();

            assertThat(status.getTotalNodes()).isEqualTo(5L);
            assertThat(status.getRunningNodes()).isEqualTo(3L);
            assertThat(status.getPendingNodes()).isEqualTo(1L);
            assertThat(status.getUnhealthyNodes()).isEqualTo(1L);
            assertThat(status.getLastUpdated()).isNotNull();
        }

        @Test
        @DisplayName("should calculate average metrics for running nodes")
        void shouldCalculateAverageMetrics() {
            Node node1 = new Node();
            node1.setCpuUsage(40.0);
            node1.setMemoryUsage(50.0);

            Node node2 = new Node();
            node2.setCpuUsage(60.0);
            node2.setMemoryUsage(70.0);

            when(nodeRepository.count()).thenReturn(2L);
            when(nodeRepository.countByStatus(any())).thenReturn(0L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(2L);
            when(nodeRepository.findByStatus(NodeStatus.RUNNING)).thenReturn(List.of(node1, node2));

            ClusterStatusDTO status = healthMonitorService.getClusterStatus();

            assertThat(status.getAverageCpuUsage()).isEqualTo(50.0);
            assertThat(status.getAverageMemoryUsage()).isEqualTo(60.0);
        }

        @Test
        @DisplayName("should return null averages when no metrics")
        void shouldReturnNullAveragesWhenNoMetrics() {
            Node nodeWithoutMetrics = new Node();
            nodeWithoutMetrics.setCpuUsage(null);
            nodeWithoutMetrics.setMemoryUsage(null);

            when(nodeRepository.count()).thenReturn(1L);
            when(nodeRepository.countByStatus(any())).thenReturn(0L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(1L);
            when(nodeRepository.findByStatus(NodeStatus.RUNNING)).thenReturn(List.of(nodeWithoutMetrics));

            ClusterStatusDTO status = healthMonitorService.getClusterStatus();

            assertThat(status.getAverageCpuUsage()).isNull();
            assertThat(status.getAverageMemoryUsage()).isNull();
        }
    }

    @Nested
    @DisplayName("getClusterHealth")
    class GetClusterHealthTests {

        @Test
        @DisplayName("should return EMPTY when no nodes")
        void shouldReturnEmptyWhenNoNodes() {
            when(nodeRepository.count()).thenReturn(0L);
            when(nodeRepository.countByStatus(any())).thenReturn(0L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of());

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getStatus()).isEqualTo("EMPTY");
            assertThat(health.getTotalNodes()).isEqualTo(0);
        }

        @Test
        @DisplayName("should return HEALTHY when all nodes running")
        void shouldReturnHealthyWhenAllNodesRunning() {
            when(nodeRepository.count()).thenReturn(3L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(3L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(0L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of());

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getStatus()).isEqualTo("HEALTHY");
            assertThat(health.getHealthyNodes()).isEqualTo(3L);
            assertThat(health.getUnhealthyNodes()).isEqualTo(0L);
        }

        @Test
        @DisplayName("should return DEGRADED when some nodes unhealthy")
        void shouldReturnDegradedWhenSomeNodesUnhealthy() {
            when(nodeRepository.count()).thenReturn(5L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(4L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(1L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of(unhealthyNode));

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getStatus()).isEqualTo("DEGRADED");
            assertThat(health.getUnhealthyNodesList()).hasSize(1);
        }

        @Test
        @DisplayName("should return UNHEALTHY when majority unhealthy")
        void shouldReturnUnhealthyWhenMajorityUnhealthy() {
            when(nodeRepository.count()).thenReturn(4L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(1L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(3L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of(unhealthyNode));

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getStatus()).isEqualTo("UNHEALTHY");
        }

        @Test
        @DisplayName("should return PARTIAL when some nodes not running")
        void shouldReturnPartialWhenSomeNodesNotRunning() {
            when(nodeRepository.count()).thenReturn(5L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(3L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(0L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of());

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getStatus()).isEqualTo("PARTIAL");
        }

        @Test
        @DisplayName("should include unhealthy node details")
        void shouldIncludeUnhealthyNodeDetails() {
            when(nodeRepository.count()).thenReturn(2L);
            when(nodeRepository.countByStatus(NodeStatus.RUNNING)).thenReturn(1L);
            when(nodeRepository.countByStatus(NodeStatus.UNHEALTHY)).thenReturn(1L);
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of(unhealthyNode));

            ClusterHealthDTO health = healthMonitorService.getClusterHealth();

            assertThat(health.getUnhealthyNodesList()).hasSize(1);
            assertThat(health.getUnhealthyNodesList().get(0).getName()).isEqualTo("unhealthy-worker");
            assertThat(health.getUnhealthyNodesList().get(0).getFailedChecks()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("checkNodeHealth")
    class CheckNodeHealthTests {

        @Test
        @DisplayName("should reset failed checks when healthy")
        void shouldResetFailedChecksWhenHealthy() {
            // Node without IP, EC2 disabled - should be considered healthy
            runningNode.setPrivateIp(null);
            runningNode.setPublicIp(null);
            runningNode.setFailedHealthChecks(2);

            when(nodeRepository.save(any(Node.class))).thenReturn(runningNode);

            healthMonitorService.checkNodeHealth(runningNode);

            assertThat(runningNode.getFailedHealthChecks()).isEqualTo(0);
            assertThat(runningNode.getLastHeartbeat()).isNotNull();
        }

        @Test
        @DisplayName("should mark node unhealthy after threshold")
        void shouldMarkNodeUnhealthyAfterThreshold() {
            runningNode.setPrivateIp("10.0.1.100"); // Has IP, will fail health check
            runningNode.setFailedHealthChecks(2);

            when(healthProperties.getUnhealthyThreshold()).thenReturn(3);
            when(healthProperties.getTimeout()).thenReturn(100L); // Short timeout for test
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> inv.getArgument(0));

            healthMonitorService.checkNodeHealth(runningNode);

            // After failed check, should be 3 which equals threshold
            assertThat(runningNode.getFailedHealthChecks()).isEqualTo(3);
            assertThat(runningNode.getStatus()).isEqualTo(NodeStatus.UNHEALTHY);
        }

        @Test
        @DisplayName("should recover unhealthy node when healthy")
        void shouldRecoverUnhealthyNodeWhenHealthy() {
            unhealthyNode.setPrivateIp(null); // No IP, EC2 disabled = healthy
            unhealthyNode.setPublicIp(null);

            when(nodeRepository.save(any(Node.class))).thenReturn(unhealthyNode);

            healthMonitorService.checkNodeHealth(unhealthyNode);

            assertThat(unhealthyNode.getStatus()).isEqualTo(NodeStatus.RUNNING);
            assertThat(unhealthyNode.getFailedHealthChecks()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("performHealthChecks")
    class PerformHealthChecksTests {

        @Test
        @DisplayName("should check all active nodes")
        void shouldCheckAllActiveNodes() {
            when(healthProperties.isEnabled()).thenReturn(true);
            when(nodeRepository.findByStatus(NodeStatus.RUNNING)).thenReturn(List.of(runningNode));
            when(nodeRepository.findByStatus(NodeStatus.UNHEALTHY)).thenReturn(List.of(unhealthyNode));
            when(nodeRepository.save(any(Node.class))).thenAnswer(inv -> inv.getArgument(0));

            healthMonitorService.performHealthChecks();

            verify(nodeRepository, times(2)).save(any(Node.class));
        }

        @Test
        @DisplayName("should skip when health checks disabled")
        void shouldSkipWhenHealthChecksDisabled() {
            when(healthProperties.isEnabled()).thenReturn(false);

            healthMonitorService.performHealthChecks();

            verify(nodeRepository, never()).findByStatus(any());
        }
    }

    @Nested
    @DisplayName("syncAllNodesWithEc2")
    class SyncAllNodesWithEc2Tests {

        @Test
        @DisplayName("should sync nodes with EC2 when enabled")
        void shouldSyncNodesWithEc2WhenEnabled() {
            ReflectionTestUtils.setField(healthMonitorService, "ec2Enabled", true);

            Node nodeWithInstance = new Node();
            nodeWithInstance.setInstanceId("i-12345");

            when(nodeRepository.findAll()).thenReturn(List.of(nodeWithInstance));
            when(nodeRepository.save(any(Node.class))).thenReturn(nodeWithInstance);

            healthMonitorService.syncAllNodesWithEc2();

            verify(ec2Service).syncNodeFromEc2(nodeWithInstance);
            verify(nodeRepository).save(nodeWithInstance);
        }

        @Test
        @DisplayName("should skip nodes without instanceId")
        void shouldSkipNodesWithoutInstanceId() {
            ReflectionTestUtils.setField(healthMonitorService, "ec2Enabled", true);

            Node nodeWithoutInstance = new Node();
            nodeWithoutInstance.setInstanceId(null);

            when(nodeRepository.findAll()).thenReturn(List.of(nodeWithoutInstance));

            healthMonitorService.syncAllNodesWithEc2();

            verify(ec2Service, never()).syncNodeFromEc2(any());
        }

        @Test
        @DisplayName("should do nothing when EC2 disabled")
        void shouldDoNothingWhenEc2Disabled() {
            ReflectionTestUtils.setField(healthMonitorService, "ec2Enabled", false);

            healthMonitorService.syncAllNodesWithEc2();

            verify(nodeRepository, never()).findAll();
        }
    }
}
