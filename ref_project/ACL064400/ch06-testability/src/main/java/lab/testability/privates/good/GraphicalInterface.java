package lab.testability.privates.good;

import lab.testability.TestingImpedimentException;

public class GraphicalInterface {

	public void showMessage(String msg) {
		throw new TestingImpedimentException("GUI need manual operation");
	}
}
