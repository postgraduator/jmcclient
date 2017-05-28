package com.smithson.jmcclient.io.exception;

public class BadVersionException extends JMCClientException {
	private static final long serialVersionUID = 9188137284145651237L;

	public BadVersionException(String message) {
		super(message);
	}
}
