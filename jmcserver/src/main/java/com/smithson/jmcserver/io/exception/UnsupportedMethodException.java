package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class UnsupportedMethodException extends JMCServerException {
	private static final long serialVersionUID = 8721952304273354288L;

	public UnsupportedMethodException(String message) {
		super(message, Status.COMMAND_NOT_SUPPORTED);
	}
}
