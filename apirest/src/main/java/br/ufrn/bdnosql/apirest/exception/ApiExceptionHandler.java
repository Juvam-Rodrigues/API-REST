package br.ufrn.bdnosql.apirest.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;
import br.ufrn.bdnosql.apirest.exception.custom.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;



@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorMessage> handleBadRequest(BadRequestException ex,  HttpServletRequest request){
		ErrorMessage error =  new ErrorMessage(
	            request,
	            HttpStatus.BAD_REQUEST,
	            ex.getMessage()
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(error);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorMessage> handleNotFound(NotFoundException ex,  HttpServletRequest request){
		ErrorMessage error =  new ErrorMessage(
	            request,
	            HttpStatus.NOT_FOUND,
	            ex.getMessage()
	    );

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(error);
	}
	
	
}
