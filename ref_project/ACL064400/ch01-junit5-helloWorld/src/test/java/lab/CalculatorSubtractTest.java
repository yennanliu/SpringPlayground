package lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculatorSubtractTest {

	@Test
	public void subtractNumbers() {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.subtract(3, 1));
	}

}
