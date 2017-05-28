package com.smithson.jmcclient.io.exception;


public class PossibleDataCorruptionException extends BadParsedParameterException {
	private static final long serialVersionUID = -5037966263762713378L;

	public PossibleDataCorruptionException(String message) {
		super(message);
	}

}
