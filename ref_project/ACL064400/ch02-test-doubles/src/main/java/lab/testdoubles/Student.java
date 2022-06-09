package lab.testdoubles;

public class Student {
	private final String rollNumber;
	private final String name;

	public Student(String rollNumber, String name) {
		this.rollNumber = rollNumber;
		this.name = name;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public String getName() {
		return name;
	}
}
