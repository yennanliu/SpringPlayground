package lab.easyPoll.dto.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorDetail {

	private String title;
	private int status;
	private String detail;
	private long timestamp;
	private String developerMessage;
	private String path;
	private Map<String, List<ValidationError>> errors = new HashMap<>();

	public Map<String, List<ValidationError>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<ValidationError>> errors) {
		this.errors = errors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
}
