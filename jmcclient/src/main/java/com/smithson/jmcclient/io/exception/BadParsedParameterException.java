package com.smithson.jmcclient.io.exception;

public class BadParsedParameterException extends JMCClientException {
	private static final long serialVersionUID = 4635620254730274431L;
	
	public BadParsedParameterException(String message) {
		super(message);
	}
	
	public BadParsedParameterException(String message, Throwable cause) {
		super(message, cause);
	}

}
