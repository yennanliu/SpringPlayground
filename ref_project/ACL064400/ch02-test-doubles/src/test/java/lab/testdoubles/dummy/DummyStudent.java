package lab.testdoubles.dummy;

import lab.testdoubles.Student;

public class DummyStudent extends Student {
	protected DummyStudent() {
		super(null, null);
	}

	@Override
	public String getRollNumber() {
		throw new RuntimeException("Dummy student");
	}

	@Override
	public String getName() {
		throw new RuntimeException("Dummy student");
	}
}
