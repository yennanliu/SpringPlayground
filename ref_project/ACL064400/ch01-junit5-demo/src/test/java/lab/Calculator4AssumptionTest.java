package lab;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

public class Calculator4AssumptionTest {
	@Test
	public void testAdd() {
		int a = 8;
		int b = 5;
		int additionResult = Calculator4Assumption.add(a, b);
		Assertions.assertTrue(a + b == additionResult);
	}
	@Test
	public void testMultiply() {
		int a = 8;
		int b = 5;
		int additionResult = Calculator4Assumption.add(a, b);
		Assumptions.assumeTrue(a + b == additionResult);
		int multiplicationResult = Calculator4Assumption.multiply(a, b);
		Assertions.assertTrue(a * b == multiplicationResult);
	}
}
