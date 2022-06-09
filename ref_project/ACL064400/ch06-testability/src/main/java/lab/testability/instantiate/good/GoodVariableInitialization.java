package lab.testability.instantiate.good;

import lab.testability.DatabaseDependency;

public class GoodVariableInitialization {
	DatabaseDependency dependency1;

	public GoodVariableInitialization(DatabaseDependency d) {
		this.dependency1 = d;
	}

	public Object someMethod(Object arg) {
		return arg;
	}
}
