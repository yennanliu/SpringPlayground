package lab.easyPoll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestControllerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RestControllerException() {
	}

	public RestControllerException(String message) {
		super(message);
	}

	public RestControllerException(Throwable cause) {
		super(cause);
	}

	public RestControllerException(String message, Throwable cause) {
		super(message, cause);
	}
}
