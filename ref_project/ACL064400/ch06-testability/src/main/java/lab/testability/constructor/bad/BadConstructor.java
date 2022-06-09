package lab.testability.constructor.bad;

import lab.testability.DatabaseDependency;
import lab.testability.FileReadDependency;

public class BadConstructor {
	private DatabaseDependency dependency1;
	private FileReadDependency dependency2;

	public BadConstructor() {
		this.dependency1 = new DatabaseDependency();
		this.dependency2 = new FileReadDependency();
	}

	public Object someMethod(Object arg) {
		return arg;
	}
}
