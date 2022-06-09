package lab.mockito.more;

public class Error {
	private StackTraceElement[] errorTrace;
	private String errorCode;

	public StackTraceElement[] getErrorTrace() {
		return errorTrace;
	}

	public void setErrorTrace(StackTraceElement[] errorTrace) {
		this.errorTrace = errorTrace;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
