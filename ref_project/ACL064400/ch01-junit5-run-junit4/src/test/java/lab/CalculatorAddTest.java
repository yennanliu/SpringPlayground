package lab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CalculatorAddTest {

	@Test
	public void addsTwoNumbers() {
		Calculator calculator = new Calculator();
		assertEquals(2, calculator.add(1, 1));
	}

}
