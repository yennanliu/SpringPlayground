package lab.testability;

public class DatabaseDependency {

	public DatabaseDependency() {
		throw new TestingImpedimentException("Calls database");
	}
}
