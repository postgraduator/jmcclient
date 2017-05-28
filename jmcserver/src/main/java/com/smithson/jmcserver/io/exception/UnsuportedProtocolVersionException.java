package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class UnsuportedProtocolVersionException extends JMCServerException {
	private static final long serialVersionUID = 7750360993141782933L;

	public UnsuportedProtocolVersionException(String message) {
		super(message, Status.NOT_SUPPORTED_PROTOCOL);
	}
}
