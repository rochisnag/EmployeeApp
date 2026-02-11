package com.employee.exceptions;

public class DataAccessException extends RuntimeException{
	public DataAccessException(String e) {
		super(e);
	}
	 public DataAccessException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
