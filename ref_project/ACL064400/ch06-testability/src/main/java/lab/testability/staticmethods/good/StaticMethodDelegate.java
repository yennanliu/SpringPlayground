package lab.testability.staticmethods.good;

import lab.testability.TestingImpedimentException;

public class StaticMethodDelegate {

	public static void aStaticMethod() {
		throw new TestingImpedimentException("Calls static method");
	}

	void delegate() {
		aStaticMethod();
	}
}
