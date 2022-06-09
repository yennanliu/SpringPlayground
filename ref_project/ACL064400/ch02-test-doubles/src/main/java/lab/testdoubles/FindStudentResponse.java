package lab.testdoubles;

public class FindStudentResponse {
	private final String errorMessage;
	private final Student student;

	public FindStudentResponse(String errorMessage, Student student) {
		this.errorMessage = errorMessage;
		this.student = student;
	}

	public boolean isSuccess() {
		return null == errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Student getStudent() {
		return student;
	}
}
