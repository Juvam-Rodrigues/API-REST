package br.ufrn.bdnosql.apirest.exception;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

public class ErrorMessage {
	private String path;
	private String method;
	private int status;
	private String error;
	private String message;
	
	public ErrorMessage() {
	}

	public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
		this.path = request.getRequestURI();
		this.method = request.getMethod();
		this.status = status.value();
		this.error = status.getReasonPhrase(); 
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
