package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class JMCServerException extends RuntimeException {
	private static final long serialVersionUID = -1628670536847930380L;

	private final Status status;
	
	public JMCServerException(String message, Status status) {
		super(message);
		this.status = status;
	}

	public JMCServerException(Throwable cause, Status status) {
		super(cause);
		this.status = status;
	}

	public JMCServerException(String message, Throwable cause, Status status) {
		super(message, cause);
		this.status = status;
	}

	public JMCServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Status status) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}

}
