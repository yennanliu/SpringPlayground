package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.inOrder;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InOrderTest {
	@Mock
	private Service1 service1;
	@Mock
	private Service2 service2;

	@Test
	public void Should_passed_When_verify_correct_order() {
		// When
		service2.call2(Arrays.asList("a", "b"));
		service1.call1(Arrays.asList("a", "b"));
		// Then
		InOrder inOrder = inOrder(service1, service2);
		inOrder.verify(service2).call2(anyList());
		inOrder.verify(service1).call1(anyList());
	}

	@Test
	public void Should_failed_When_verify_incorrect_order() {
		// When
		service2.call2(Arrays.asList("a", "b"));
		service1.call1(Arrays.asList("a", "b"));
		// Then
		InOrder inOrder = inOrder(service1, service2);
		inOrder.verify(service1).call1(anyList());
		inOrder.verify(service2).call2(anyList());
	}
}
