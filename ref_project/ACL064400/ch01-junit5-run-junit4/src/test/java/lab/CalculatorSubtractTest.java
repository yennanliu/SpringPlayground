package lab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorSubtractTest {

	@Test
	public void subtractNumbers() {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.subtract(3, 1));
	}

}
