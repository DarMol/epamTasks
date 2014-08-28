package com.epam.project.exception;

public class LogicException extends Exception {
	private static final long serialVersionUID = 1L;

	public LogicException() {
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LogicException(Throwable arg0) {
		super(arg0);
	}

}
