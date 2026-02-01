package com.employee.exceptions;

public class EmployeeDoesNotExists extends RuntimeException {
	public EmployeeDoesNotExists(String e) {
		super(e);
	}
	 public EmployeeDoesNotExists(String message, Throwable cause) {
	        super(message, cause);
	    }
}
