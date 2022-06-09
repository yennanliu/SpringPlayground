package lab;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static java.lang.System.out;

@RunWith(JUnitPlatform.class)
public class LifecycleTest {

	@BeforeAll
	static void setup() {
		out.println("@BeforeAll executed");
		out.println("-------------------------------");
	}

	@BeforeAll
	static void setup2() {
		out.println("@BeforeAll executed 2");
		out.println("-------------------------------");
	}

	@BeforeEach
	void setupThis() {
		out.println("@BeforeEach executed");
	}

	@Test
	void testCommon(TestInfo testInfo) {
		out.println("@Test executed: " + testInfo.getTestMethod().get());
		Assertions.assertEquals(4, Calculator.add(2, 2));
	}

	@RepeatedTest(3)
	void testRepeat(RepetitionInfo repeat) {
		out.println("@RepeatedTest executed -> " + repeat.getCurrentRepetition());
		Assertions.assertEquals(2, Calculator.add(1, 1), "1 + 1 should equal 2");
	}

	@Disabled
	@Test
	void testDisable() {
		out.println("@Disabled executed");
		Assertions.assertEquals(6, Calculator.add(2, 4));
	}

	@AfterEach
	void tearThis() {
		out.println("@AfterEach executed");
		out.println("-------------------------------");
	}

	@AfterAll
	static void tearDown() {
		out.println("@AfterAll executed");
		out.println("-------------------------------");
	}

	@AfterAll
	static void tearDown2() {
		out.println("@AfterAll executed2");
		out.println("-------------------------------");
	}
}
