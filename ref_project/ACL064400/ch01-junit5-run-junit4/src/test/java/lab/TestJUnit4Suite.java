package lab;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	CalculatorAddTest.class, 
	CalculatorSubtractTest.class })
public class TestJUnit4Suite {
}