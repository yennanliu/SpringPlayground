package lab.testability.constructor.good;

import lab.testability.DatabaseDependency;
import lab.testability.FileReadDependency;

public class GoodConstructor {
	private DatabaseDependency dep1;
	private FileReadDependency dep2;

	public GoodConstructor(DatabaseDependency dep1, FileReadDependency dep2) {
		this.dep1 = dep1;
		this.dep2 = dep2;
	}

	public Object someMethod(Object arg) {
		return arg;
	}
}
