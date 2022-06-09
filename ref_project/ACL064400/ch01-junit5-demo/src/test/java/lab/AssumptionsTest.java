package lab;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssumptionsTest {

	@BeforeEach
	void setupThis() {
		System.setProperty("ENV", "TEST");
	}

	@Test
	void assumeTrueIsPassed() {
		assumeTrue("TEST".equals(System.getProperty("ENV")), this::assumeMsg);
		out.println("assumeTrueIsPassed(): " + System.getProperty("ENV"));
	}

	@Test
	void assumeTrueIsFailed() {
		assumeTrue("PROD".equals(System.getProperty("ENV")), this::assumeMsg);
		out.println("assumeTrueIsFailed(): " + System.getProperty("ENV"));
	}

	@Test
	void assertTrueIsPassed() {
		assertTrue("TEST".equals(System.getProperty("ENV")), this::assertMsg);
		out.println("assertTrueIsPassed(): " + System.getProperty("ENV"));
	}

	@Test
	void assertTrueIsFailed() {
		assertTrue("PROD".equals(System.getProperty("ENV")), this::assertMsg);
		out.println("assertTrueIsFailed(): " + System.getProperty("ENV"));
	}

	@Test
	void assumeFalseIsPassed() {
		assumeFalse("PROD".equals(System.getProperty("ENV")), this::assumeMsg);
		out.println("assumeFalseIsPassed(): " + System.getProperty("ENV"));
	}

	@Test
	void assumeFalseIsFailed() {
		assumeFalse("TEST".equals(System.getProperty("ENV")), this::assumeMsg);
		out.println("assumeFalseIsFailed(): " + System.getProperty("ENV"));
	}

	private String assumeMsg() {
		return "Jim->the assumption is invalid!";
	}

	private String assertMsg() {
		return "Jim->the assertion is invalid!";
	}
}
