package com.bugyal.imentor;

public class MentorException extends Exception {

	private static final long serialVersionUID = 1480099179914393681L;

	public MentorException(String message, Throwable cause) {
		super(message, cause);
	}

	public MentorException(String message) {
		super(message);
	}
}
