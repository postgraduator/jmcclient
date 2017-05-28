package com.smithson.jmcclient.io.exception;

public class JMCClientException extends RuntimeException {
	private static final long serialVersionUID = -1628670536847930380L;
	
	public JMCClientException(String message) {
		super(message);
	}

	public JMCClientException(Throwable cause) {
		super(cause);

	}

	public JMCClientException(String message, Throwable cause) {
		super(message, cause);

	}

	public JMCClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
