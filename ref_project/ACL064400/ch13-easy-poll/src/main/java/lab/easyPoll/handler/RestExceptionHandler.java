package lab.easyPoll.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lab.easyPoll.dto.error.ErrorDetail;
import lab.easyPoll.dto.error.ValidationError;
import lab.easyPoll.exception.ResourceNotFoundException;
import lab.easyPoll.exception.RestControllerException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handle404Exception(ResourceNotFoundException ex,
			HttpServletRequest request) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTitle("Resource Not Found");
		errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());
		errorDetail.setTimestamp(new Date().getTime());
		errorDetail.setPath(getURL(request));

		return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(RestControllerException.class)
	public ResponseEntity<?> handle500Exception(RestControllerException ex,
			HttpServletRequest request) {

		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTitle("Request Validation Error");
		errorDetail.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());
		errorDetail.setTimestamp(new Date().getTime());
		errorDetail.setPath(getURL(request));

		return new ResponseEntity<>(errorDetail, null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// create ErrorDetail
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTitle("Input Validation Failed");
		errorDetail.setStatus(status.value());
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());
		errorDetail.setTimestamp(System.currentTimeMillis());
		
		String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		errorDetail.setPath(path);

		// create ValidationError, and add to ErrorDetail.errors
		Map<String, List<ValidationError>> errorMap = errorDetail.getErrors();
		
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError fe : fieldErrors) {
			
			String errorField = fe.getField();	// question, options
			
			List<ValidationError> validationErrorList = errorMap.get(errorField);
			if (validationErrorList == null) {
				validationErrorList = new ArrayList<ValidationError>();
				errorMap.put(errorField, validationErrorList);
			}
			
			String errorCode = fe.getCode(); // NotEmpty, Size
			String message =  fe.getDefaultMessage();	// default
	//		String message =  messageSource.getMessage(fe, Locale.US);	// custom
			
			ValidationError validationError = new ValidationError(errorCode, message);
			validationErrorList.add(validationError);
		}

		return handleExceptionInternal(ex, errorDetail, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setTitle("Message Not Readable");
		errorDetail.setStatus(status.value());
		errorDetail.setDetail(ex.getMessage());
		errorDetail.setDeveloperMessage(ex.getClass().getName());
		errorDetail.setTimestamp(System.currentTimeMillis());
		
		String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		errorDetail.setPath(path);

		return handleExceptionInternal(ex, errorDetail, headers, status, request);
	}

	private static String getURL(HttpServletRequest req) {

		String scheme = req.getScheme(); // http
		String serverName = req.getServerName(); // hostname.com
		int serverPort = req.getServerPort(); // 80
		String contextPath = req.getContextPath(); // /mywebapp
		String servletPath = req.getServletPath(); // /servlet/MyServlet
		String pathInfo = req.getPathInfo(); // /a/b;c=123
		String queryString = req.getQueryString(); // d=789

		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(serverName);

		if (serverPort != 80 && serverPort != 443) {
			url.append(":").append(serverPort);
		}
		url.append(contextPath).append(servletPath);
		if (pathInfo != null) {
			url.append(pathInfo);
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}

}
