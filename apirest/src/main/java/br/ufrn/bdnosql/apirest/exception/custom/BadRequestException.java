package br.ufrn.bdnosql.apirest.exception.custom;

public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BadRequestException(String message) {
		super(message);
	}
	

}
