package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class BadParsedParameterException extends JMCServerException {
	private static final long serialVersionUID = 4635620254730274431L;
	
	public BadParsedParameterException(String message, Status error) {
		super(message, error);
	}
	
	public BadParsedParameterException(String message, Throwable cause, Status error) {
		super(message, cause, error);
	}

}
