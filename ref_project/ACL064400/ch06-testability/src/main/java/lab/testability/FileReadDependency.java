package lab.testability;

public class FileReadDependency {

	public FileReadDependency() {
		throw new TestingImpedimentException("Reads file");
	}
}
