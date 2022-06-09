package lab.mockito.more;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalculatorTestByAnnotation {

	@Spy // Spy the calculator class
	private Calculator calculator;

	@Test
	public void testAdd() {
		int a = 10;
		int b = 20;

		int result = calculator.add(a, b);
		assertEquals(30, result);

	}

	@Test
	public void testMultiply() {
		int a = 2;
		int b = 4;

		int result = calculator.multiply(a, b);

		verify(calculator, times(3)).add(anyInt(), anyInt());
		assertEquals(8, result);
	}
}
