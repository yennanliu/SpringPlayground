package com.yen.clusterAdmin.repository;

import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class NodeRepositoryTest {

    @Autowired
    private NodeRepository nodeRepository;

    private Node runningNode;
    private Node stoppedNode;
    private Node unhealthyNode;

    @BeforeEach
    void setUp() {
        nodeRepository.deleteAll();

        runningNode = new Node();
        runningNode.setName("running-worker");
        runningNode.setInstanceId("i-running");
        runningNode.setStatus(NodeStatus.RUNNING);
        runningNode.setAvailabilityZone("us-east-1a");

        stoppedNode = new Node();
        stoppedNode.setName("stopped-worker");
        stoppedNode.setInstanceId("i-stopped");
        stoppedNode.setStatus(NodeStatus.STOPPED);
        stoppedNode.setAvailabilityZone("us-east-1b");

        unhealthyNode = new Node();
        unhealthyNode.setName("unhealthy-worker");
        unhealthyNode.setInstanceId("i-unhealthy");
        unhealthyNode.setStatus(NodeStatus.UNHEALTHY);
        unhealthyNode.setAvailabilityZone("us-east-1a");
    }

    @Nested
    @DisplayName("findByInstanceId")
    class FindByInstanceIdTests {

        @Test
        @DisplayName("should find node by instanceId")
        void shouldFindNodeByInstanceId() {
            nodeRepository.save(runningNode);

            Optional<Node> result = nodeRepository.findByInstanceId("i-running");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("running-worker");
        }

        @Test
        @DisplayName("should return empty when instanceId not found")
        void shouldReturnEmptyWhenNotFound() {
            Optional<Node> result = nodeRepository.findByInstanceId("i-notfound");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByStatus")
    class FindByStatusTests {

        @Test
        @DisplayName("should find all nodes with given status")
        void shouldFindAllNodesWithStatus() {
            nodeRepository.save(runningNode);

            Node runningNode2 = new Node();
            runningNode2.setName("running-worker-2");
            runningNode2.setInstanceId("i-running-2");
            runningNode2.setStatus(NodeStatus.RUNNING);
            nodeRepository.save(runningNode2);

            nodeRepository.save(stoppedNode);

            List<Node> result = nodeRepository.findByStatus(NodeStatus.RUNNING);

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(n -> n.getStatus() == NodeStatus.RUNNING);
        }

        @Test
        @DisplayName("should return empty list when no nodes with status")
        void shouldReturnEmptyListWhenNoNodesWithStatus() {
            nodeRepository.save(runningNode);

            List<Node> result = nodeRepository.findByStatus(NodeStatus.TERMINATED);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByAvailabilityZone")
    class FindByAvailabilityZoneTests {

        @Test
        @DisplayName("should find all nodes in availability zone")
        void shouldFindAllNodesInAvailabilityZone() {
            nodeRepository.save(runningNode);
            nodeRepository.save(unhealthyNode);
            nodeRepository.save(stoppedNode);

            List<Node> result = nodeRepository.findByAvailabilityZone("us-east-1a");

            assertThat(result).hasSize(2);
            assertThat(result).allMatch(n -> "us-east-1a".equals(n.getAvailabilityZone()));
        }
    }

    @Nested
    @DisplayName("countByStatus")
    class CountByStatusTests {

        @Test
        @DisplayName("should count nodes by status")
        void shouldCountNodesByStatus() {
            nodeRepository.save(runningNode);

            Node runningNode2 = new Node();
            runningNode2.setName("running-worker-2");
            runningNode2.setInstanceId("i-running-2");
            runningNode2.setStatus(NodeStatus.RUNNING);
            nodeRepository.save(runningNode2);

            nodeRepository.save(stoppedNode);

            long runningCount = nodeRepository.countByStatus(NodeStatus.RUNNING);
            long stoppedCount = nodeRepository.countByStatus(NodeStatus.STOPPED);
            long terminatedCount = nodeRepository.countByStatus(NodeStatus.TERMINATED);

            assertThat(runningCount).isEqualTo(2);
            assertThat(stoppedCount).isEqualTo(1);
            assertThat(terminatedCount).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("existsByInstanceId")
    class ExistsByInstanceIdTests {

        @Test
        @DisplayName("should return true when instanceId exists")
        void shouldReturnTrueWhenExists() {
            nodeRepository.save(runningNode);

            boolean exists = nodeRepository.existsByInstanceId("i-running");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("should return false when instanceId does not exist")
        void shouldReturnFalseWhenNotExists() {
            boolean exists = nodeRepository.existsByInstanceId("i-notfound");

            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("save operations")
    class SaveOperationsTests {

        @Test
        @DisplayName("should set createdAt on persist")
        void shouldSetCreatedAtOnPersist() {
            Node newNode = new Node();
            newNode.setName("new-worker");
            newNode.setStatus(NodeStatus.PENDING);

            Node saved = nodeRepository.save(newNode);

            assertThat(saved.getCreatedAt()).isNotNull();
            assertThat(saved.getUpdatedAt()).isNotNull();
        }

        @Test
        @DisplayName("should default status to PENDING if null")
        void shouldDefaultStatusToPending() {
            Node newNode = new Node();
            newNode.setName("new-worker");
            // status is null

            Node saved = nodeRepository.save(newNode);

            assertThat(saved.getStatus()).isEqualTo(NodeStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("unique constraints")
    class UniqueConstraintsTests {

        @Test
        @DisplayName("should allow null instanceId")
        void shouldAllowNullInstanceId() {
            Node node1 = new Node();
            node1.setName("worker-1");
            node1.setInstanceId(null);
            node1.setStatus(NodeStatus.PENDING);

            Node node2 = new Node();
            node2.setName("worker-2");
            node2.setInstanceId(null);
            node2.setStatus(NodeStatus.PENDING);

            nodeRepository.save(node1);
            nodeRepository.save(node2);

            assertThat(nodeRepository.count()).isEqualTo(2);
        }
    }
}
