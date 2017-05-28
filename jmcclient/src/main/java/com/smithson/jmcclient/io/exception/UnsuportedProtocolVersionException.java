package com.smithson.jmcclient.io.exception;

public class UnsuportedProtocolVersionException extends JMCClientException {
	private static final long serialVersionUID = 7750360993141782933L;

	public UnsuportedProtocolVersionException(String message) {
		super(message);
	}
}
