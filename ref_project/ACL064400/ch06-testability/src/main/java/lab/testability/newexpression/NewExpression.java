package lab.testability.newexpression;

import lab.testability.TestingImpedimentException;

public class NewExpression {
	public void myMethod() {
		throw new TestingImpedimentException("should not be called!");
	}
}
