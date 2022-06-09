package lab.testability.privates.bad;

import lab.testability.TestingImpedimentException;

public class PrivateMethodDemo {

	public Object validate(Object arg) {
		if (arg == null) {
			showError("Null input");
		}
		return arg;
	}

	private void showError(String msg) {
		throw new TestingImpedimentException("GUI need manual operation");
	}
}
