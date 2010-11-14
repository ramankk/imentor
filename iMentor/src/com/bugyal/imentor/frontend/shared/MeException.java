package com.bugyal.imentor.frontend.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MeException extends Exception implements Serializable {

	// Required for GWT.
	public MeException() {
		
	}
	
	public MeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public MeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
