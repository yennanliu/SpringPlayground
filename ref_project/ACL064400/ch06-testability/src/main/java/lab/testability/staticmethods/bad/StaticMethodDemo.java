package lab.testability.staticmethods.bad;

import lab.testability.TestingImpedimentException;

public class StaticMethodDemo {

	public static void aStaticMethod() {
		throw new TestingImpedimentException("Calls static method");
	}

}
