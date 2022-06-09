package lab.mockito.more;

public class ErrorHandlerImpl implements ErrorHandler {

	@Override
	public void mapTo(Error error) {
		error.setErrorCode(genCode(error.getErrorTrace()));
	}

	private String genCode(StackTraceElement[] trace) {
		return null;
	}
}
