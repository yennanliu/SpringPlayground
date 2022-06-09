package lab.testability.instantiate.bad;

import lab.testability.DatabaseDependency;

public class BadVariableInitialization {
	DatabaseDependency dependency1 = new DatabaseDependency();

	public Object someMethod(Object arg) {
		return arg;
	}
}
