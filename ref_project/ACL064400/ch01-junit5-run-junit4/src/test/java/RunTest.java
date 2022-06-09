
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import lab.CalculatorAddTest;
import lab.CalculatorSubtractTest;
import lab.TestJUnit4Suite;

public class RunTest {

	public static void main(String[] args) {
		testSingle();
		System.out.println("-----------------------------------");
		testMultiple();
		System.out.println("-----------------------------------");
		testSuit();
	}

	private static void testSingle() {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		
		Result result =junit.run(CalculatorAddTest.class);
		resultReport(result);
	}

	private static void testMultiple() {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		
		Result result = junit.run(CalculatorAddTest.class, CalculatorSubtractTest.class);
		resultReport(result);
	}

	private static void testSuit() {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		Result result = junit.run(TestJUnit4Suite.class);
		resultReport(result);
	}

	public static void resultReport(Result result) {
		System.out.println(
				"Finished. Result: Failures: " + result.getFailureCount() + ". Ignored: " + result.getIgnoreCount()
						+ ". Tests run: " + result.getRunCount() + ". Time: " + result.getRunTime() + "ms.");
	}

}
