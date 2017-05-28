package com.smithson.jmcserver.io.exception;

import com.smithson.jmcserver.io.constant.Status;

public class PossibleDataCorruptionException extends BadParsedParameterException {
	private static final long serialVersionUID = -5037966263762713378L;

	public PossibleDataCorruptionException(String message, Status error) {
		super(message, error);
	}

}
