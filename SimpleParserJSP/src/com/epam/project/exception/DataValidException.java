package com.epam.project.exception;

public class DataValidException extends Exception {
	private static final long serialVersionUID = 1L;

	public DataValidException() {
	}

	public DataValidException(String message) {
		super(message);
	}

	public DataValidException(String arg0, Exception arg1) {
		super(arg0, arg1);
	}

	public DataValidException(Exception arg0) {
		super(arg0);
	}

}
