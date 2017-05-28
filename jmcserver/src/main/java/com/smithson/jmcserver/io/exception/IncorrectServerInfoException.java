package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class IncorrectServerInfoException extends JMCServerException{
	private static final long serialVersionUID = 608740556184875086L;

	public IncorrectServerInfoException(String message, Throwable cause) {
		super(message, cause, Status.ERROR);
	}

	public IncorrectServerInfoException(String message) {
		super(message, Status.ERROR);
	}

	

}
