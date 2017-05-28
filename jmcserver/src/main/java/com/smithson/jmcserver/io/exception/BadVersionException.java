package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class BadVersionException extends JMCServerException {
	private static final long serialVersionUID = 9188137284145651237L;

	public BadVersionException(String message) {
		super(message, Status.BAD_VERSION_VALUE);
	}
}
