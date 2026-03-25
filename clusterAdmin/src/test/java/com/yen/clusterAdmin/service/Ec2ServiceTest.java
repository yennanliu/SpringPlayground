package com.yen.clusterAdmin.service;

import com.yen.clusterAdmin.config.Ec2Properties;
import com.yen.clusterAdmin.exception.Ec2OperationException;
import com.yen.clusterAdmin.model.entity.Node;
import com.yen.clusterAdmin.model.enums.NodeStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class Ec2ServiceTest {

    @Mock
    private Ec2Client ec2Client;

    @Mock
    private Ec2ClientFactory ec2ClientFactory;

    @Mock
    private Ec2Properties ec2Properties;

    private Ec2Service ec2Service;

    private static final String TEST_REGION = "us-east-1";

    @BeforeEach
    void setUp() {
        ec2Service = new Ec2Service(ec2ClientFactory, ec2Properties);
        when(ec2ClientFactory.getClient(anyString())).thenReturn(ec2Client);
        when(ec2ClientFactory.getDefaultRegion()).thenReturn(TEST_REGION);
    }

    @Nested
    @DisplayName("launchInstance")
    class LaunchInstanceTests {

        @Test
        @DisplayName("should launch instance successfully")
        void shouldLaunchInstanceSuccessfully() {
            when(ec2Properties.getAmi()).thenReturn("ami-12345");
            when(ec2Properties.getInstanceType()).thenReturn("t3.medium");
            when(ec2Properties.getKeyName()).thenReturn(null);
            when(ec2Properties.getSecurityGroupId()).thenReturn(null);
            when(ec2Properties.getSubnetId()).thenReturn(null);

            Instance instance = Instance.builder()
                    .instanceId("i-newinstance")
                    .build();

            RunInstancesResponse runResponse = mock(RunInstancesResponse.class);
            when(runResponse.instances()).thenReturn(List.of(instance));
            when(runResponse.responseMetadata()).thenReturn(mock(software.amazon.awssdk.services.ec2.model.Ec2ResponseMetadata.class));
            when(runResponse.responseMetadata().requestId()).thenReturn("req-12345");

            when(ec2Client.runInstances(any(RunInstancesRequest.class))).thenReturn(runResponse);
            when(ec2Client.createTags(any(CreateTagsRequest.class)))
                    .thenReturn(CreateTagsResponse.builder().build());

            String instanceId = ec2Service.launchInstance("test-worker", "t3.small", null, TEST_REGION);

            assertThat(instanceId).isEqualTo("i-newinstance");
            verify(ec2ClientFactory).getClient(TEST_REGION);
            verify(ec2Client).runInstances(any(RunInstancesRequest.class));
            verify(ec2Client).createTags(any(CreateTagsRequest.class));
        }

        @Test
        @DisplayName("should launch instance in specified region")
        void shouldLaunchInstanceInSpecifiedRegion() {
            String tokyoRegion = "ap-northeast-1";
            when(ec2Properties.getAmi()).thenReturn("ami-12345");
            when(ec2Properties.getInstanceType()).thenReturn("t3.medium");
            when(ec2Properties.getKeyName()).thenReturn(null);
            when(ec2Properties.getSecurityGroupId()).thenReturn(null);
            when(ec2Properties.getSubnetId()).thenReturn(null);

            Instance instance = Instance.builder()
                    .instanceId("i-tokyo-instance")
                    .build();

            RunInstancesResponse runResponse = mock(RunInstancesResponse.class);
            when(runResponse.instances()).thenReturn(List.of(instance));
            when(runResponse.responseMetadata()).thenReturn(mock(software.amazon.awssdk.services.ec2.model.Ec2ResponseMetadata.class));
            when(runResponse.responseMetadata().requestId()).thenReturn("req-tokyo-12345");

            when(ec2Client.runInstances(any(RunInstancesRequest.class))).thenReturn(runResponse);
            when(ec2Client.createTags(any(CreateTagsRequest.class)))
                    .thenReturn(CreateTagsResponse.builder().build());

            String instanceId = ec2Service.launchInstance("test-tokyo", "t3.small", null, tokyoRegion);

            assertThat(instanceId).isEqualTo("i-tokyo-instance");
            verify(ec2ClientFactory).getClient(tokyoRegion);
        }

        @Test
        @DisplayName("should throw when no instances returned")
        void shouldThrowWhenNoInstancesReturned() {
            when(ec2Properties.getAmi()).thenReturn("ami-12345");
            when(ec2Properties.getInstanceType()).thenReturn("t3.medium");

            RunInstancesResponse emptyResponse = RunInstancesResponse.builder()
                    .instances(List.of())
                    .build();

            when(ec2Client.runInstances(any(RunInstancesRequest.class))).thenReturn(emptyResponse);

            assertThatThrownBy(() -> ec2Service.launchInstance("test", null, null, TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class)
                    .hasMessageContaining("No instances returned");
        }

        @Test
        @DisplayName("should throw on EC2 exception")
        void shouldThrowOnEc2Exception() {
            when(ec2Properties.getAmi()).thenReturn("ami-12345");
            when(ec2Properties.getInstanceType()).thenReturn("t3.medium");

            when(ec2Client.runInstances(any(RunInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("EC2 error").build());

            assertThatThrownBy(() -> ec2Service.launchInstance("test", null, null, TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class)
                    .hasMessageContaining("Failed to launch");
        }
    }

    @Nested
    @DisplayName("startInstance")
    class StartInstanceTests {

        @Test
        @DisplayName("should start instance successfully")
        void shouldStartInstanceSuccessfully() {
            StartInstancesResponse response = mock(StartInstancesResponse.class);
            when(response.responseMetadata()).thenReturn(mock(software.amazon.awssdk.services.ec2.model.Ec2ResponseMetadata.class));
            when(response.responseMetadata().requestId()).thenReturn("req-start-12345");
            when(ec2Client.startInstances(any(StartInstancesRequest.class))).thenReturn(response);

            ec2Service.startInstance("i-12345", TEST_REGION);

            verify(ec2ClientFactory).getClient(TEST_REGION);
            verify(ec2Client).startInstances(any(StartInstancesRequest.class));
        }

        @Test
        @DisplayName("should throw on EC2 exception")
        void shouldThrowOnEc2Exception() {
            when(ec2Client.startInstances(any(StartInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("Cannot start").build());

            assertThatThrownBy(() -> ec2Service.startInstance("i-12345", TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class)
                    .hasMessageContaining("Failed to start");
        }
    }

    @Nested
    @DisplayName("stopInstance")
    class StopInstanceTests {

        @Test
        @DisplayName("should stop instance successfully")
        void shouldStopInstanceSuccessfully() {
            StopInstancesResponse response = mock(StopInstancesResponse.class);
            when(response.responseMetadata()).thenReturn(mock(software.amazon.awssdk.services.ec2.model.Ec2ResponseMetadata.class));
            when(response.responseMetadata().requestId()).thenReturn("req-stop-12345");
            when(ec2Client.stopInstances(any(StopInstancesRequest.class))).thenReturn(response);

            ec2Service.stopInstance("i-12345", TEST_REGION);

            verify(ec2ClientFactory).getClient(TEST_REGION);
            verify(ec2Client).stopInstances(any(StopInstancesRequest.class));
        }

        @Test
        @DisplayName("should throw on EC2 exception")
        void shouldThrowOnEc2Exception() {
            when(ec2Client.stopInstances(any(StopInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("Cannot stop").build());

            assertThatThrownBy(() -> ec2Service.stopInstance("i-12345", TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class)
                    .hasMessageContaining("Failed to stop");
        }
    }

    @Nested
    @DisplayName("terminateInstance")
    class TerminateInstanceTests {

        @Test
        @DisplayName("should terminate instance successfully")
        void shouldTerminateInstanceSuccessfully() {
            TerminateInstancesResponse response = mock(TerminateInstancesResponse.class);
            when(response.responseMetadata()).thenReturn(mock(software.amazon.awssdk.services.ec2.model.Ec2ResponseMetadata.class));
            when(response.responseMetadata().requestId()).thenReturn("req-terminate-12345");
            when(ec2Client.terminateInstances(any(TerminateInstancesRequest.class))).thenReturn(response);

            ec2Service.terminateInstance("i-12345", TEST_REGION);

            verify(ec2ClientFactory).getClient(TEST_REGION);
            verify(ec2Client).terminateInstances(any(TerminateInstancesRequest.class));
        }

        @Test
        @DisplayName("should throw on EC2 exception")
        void shouldThrowOnEc2Exception() {
            when(ec2Client.terminateInstances(any(TerminateInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("Cannot terminate").build());

            assertThatThrownBy(() -> ec2Service.terminateInstance("i-12345", TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class)
                    .hasMessageContaining("Failed to terminate");
        }
    }

    @Nested
    @DisplayName("describeInstance")
    class DescribeInstanceTests {

        @Test
        @DisplayName("should return instance when found")
        void shouldReturnInstanceWhenFound() {
            Instance instance = Instance.builder()
                    .instanceId("i-12345")
                    .privateIpAddress("10.0.1.100")
                    .build();

            Reservation reservation = Reservation.builder()
                    .instances(instance)
                    .build();

            DescribeInstancesResponse response = DescribeInstancesResponse.builder()
                    .reservations(reservation)
                    .build();

            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class))).thenReturn(response);

            Optional<Instance> result = ec2Service.describeInstance("i-12345", TEST_REGION);

            assertThat(result).isPresent();
            assertThat(result.get().instanceId()).isEqualTo("i-12345");
            verify(ec2ClientFactory).getClient(TEST_REGION);
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            DescribeInstancesResponse response = DescribeInstancesResponse.builder()
                    .reservations(List.of())
                    .build();

            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class))).thenReturn(response);

            Optional<Instance> result = ec2Service.describeInstance("i-notfound", TEST_REGION);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty on EC2 exception")
        void shouldReturnEmptyOnEc2Exception() {
            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("Error").build());

            Optional<Instance> result = ec2Service.describeInstance("i-12345", TEST_REGION);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("mapEc2StateToNodeStatus")
    class MapEc2StateTests {

        @Test
        @DisplayName("should map PENDING to PENDING")
        void shouldMapPending() {
            InstanceState state = InstanceState.builder().name(InstanceStateName.PENDING).build();
            assertThat(ec2Service.mapEc2StateToNodeStatus(state)).isEqualTo(NodeStatus.PENDING);
        }

        @Test
        @DisplayName("should map RUNNING to RUNNING")
        void shouldMapRunning() {
            InstanceState state = InstanceState.builder().name(InstanceStateName.RUNNING).build();
            assertThat(ec2Service.mapEc2StateToNodeStatus(state)).isEqualTo(NodeStatus.RUNNING);
        }

        @Test
        @DisplayName("should map STOPPED to STOPPED")
        void shouldMapStopped() {
            InstanceState state = InstanceState.builder().name(InstanceStateName.STOPPED).build();
            assertThat(ec2Service.mapEc2StateToNodeStatus(state)).isEqualTo(NodeStatus.STOPPED);
        }

        @Test
        @DisplayName("should map TERMINATED to TERMINATED")
        void shouldMapTerminated() {
            InstanceState state = InstanceState.builder().name(InstanceStateName.TERMINATED).build();
            assertThat(ec2Service.mapEc2StateToNodeStatus(state)).isEqualTo(NodeStatus.TERMINATED);
        }

        @Test
        @DisplayName("should return PENDING for null state")
        void shouldReturnPendingForNull() {
            assertThat(ec2Service.mapEc2StateToNodeStatus(null)).isEqualTo(NodeStatus.PENDING);
        }
    }

    @Nested
    @DisplayName("syncNodeFromEc2")
    class SyncNodeFromEc2Tests {

        @Test
        @DisplayName("should sync node with EC2 data")
        void shouldSyncNodeWithEc2Data() {
            Node node = new Node();
            node.setInstanceId("i-12345");
            node.setRegion(TEST_REGION);

            Instance instance = Instance.builder()
                    .instanceId("i-12345")
                    .privateIpAddress("10.0.1.100")
                    .publicIpAddress("54.1.2.3")
                    .instanceType(InstanceType.T3_MEDIUM)
                    .placement(Placement.builder().availabilityZone("us-east-1a").build())
                    .state(InstanceState.builder().name(InstanceStateName.RUNNING).build())
                    .build();

            Reservation reservation = Reservation.builder()
                    .instances(instance)
                    .build();

            DescribeInstancesResponse response = DescribeInstancesResponse.builder()
                    .reservations(reservation)
                    .build();

            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class))).thenReturn(response);

            ec2Service.syncNodeFromEc2(node);

            assertThat(node.getPrivateIp()).isEqualTo("10.0.1.100");
            assertThat(node.getPublicIp()).isEqualTo("54.1.2.3");
            assertThat(node.getStatus()).isEqualTo(NodeStatus.RUNNING);
            verify(ec2ClientFactory).getClient(TEST_REGION);
        }

        @Test
        @DisplayName("should not sync when instanceId is null")
        void shouldNotSyncWhenInstanceIdNull() {
            Node node = new Node();
            node.setInstanceId(null);

            ec2Service.syncNodeFromEc2(node);

            verify(ec2Client, never()).describeInstances(any(DescribeInstancesRequest.class));
        }
    }

    @Nested
    @DisplayName("listManagedInstances")
    class ListManagedInstancesTests {

        @Test
        @DisplayName("should return managed instances")
        void shouldReturnManagedInstances() {
            Instance instance1 = Instance.builder().instanceId("i-1").build();
            Instance instance2 = Instance.builder().instanceId("i-2").build();

            Reservation reservation = Reservation.builder()
                    .instances(instance1, instance2)
                    .build();

            DescribeInstancesResponse response = DescribeInstancesResponse.builder()
                    .reservations(reservation)
                    .build();

            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class))).thenReturn(response);

            List<Instance> result = ec2Service.listManagedInstances(TEST_REGION);

            assertThat(result).hasSize(2);
            verify(ec2ClientFactory).getClient(TEST_REGION);
        }

        @Test
        @DisplayName("should throw on EC2 exception")
        void shouldThrowOnEc2Exception() {
            when(ec2Client.describeInstances(any(DescribeInstancesRequest.class)))
                    .thenThrow(Ec2Exception.builder().message("Error").build());

            assertThatThrownBy(() -> ec2Service.listManagedInstances(TEST_REGION))
                    .isInstanceOf(Ec2OperationException.class);
        }
    }
}
