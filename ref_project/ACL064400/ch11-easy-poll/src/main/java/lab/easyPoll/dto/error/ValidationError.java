package lab.easyPoll.dto.error;

public class ValidationError {

	private String code;
	private String message;

	public ValidationError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
