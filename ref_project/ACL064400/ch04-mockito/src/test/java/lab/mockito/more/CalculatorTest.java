package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
	
	// Test add()
	@Test
	public void Should_verify_result_When_spy_a_real_object() {
		// Given
		Calculator calculator = spy(new Calculator());
		int a = 10;
		int b = 20;
		// When
		int result = calculator.add(a, b);
		// Then
		assertEquals(30, result);
	}
	
	// Test multiply()
	@Test
	public void Should_verify_internal_interation_When_spy_a_real_object() {
		// Given
		Calculator calculator = spy(new Calculator());
		int a = 2;
		int b = 4;
		// When
		int result = calculator.multiply(a, b);
		// Then
		verify(calculator, times(3)).add(anyInt(), anyInt());
		assertEquals(8, result);
	}
	
	// Test multiply()
	@Test
	public void Should_not_verify_internal_interation_When_mock_object() {
		// Given
		Calculator calculator = mock(Calculator.class);
		int a = 2;
		int b = 4;
		// When
		int result = calculator.multiply(a, b);
		// Then
		verify(calculator, times(3)).add(anyInt(), anyInt());
		assertEquals(8, result);
	}
}
