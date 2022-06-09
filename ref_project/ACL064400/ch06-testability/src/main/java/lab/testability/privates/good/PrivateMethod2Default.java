package lab.testability.privates.good;

import lab.testability.TestingImpedimentException;

public class PrivateMethod2Default {
	
	public Object validate(Object arg) {
		if (arg == null) {
			showError("Null input");
		}
		return arg;
	}

	void showError(String msg) {
		throw new TestingImpedimentException("GUI need manual operation");
	}
}
