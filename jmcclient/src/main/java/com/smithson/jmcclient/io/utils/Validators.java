package com.smithson.jmcclient.io.utils;

import com.smithson.jmcclient.io.constant.ProtocolInfo;
import com.smithson.jmcclient.io.exception.UnsuportedProtocolVersionException;
import com.smithson.jmcclient.io.exception.PossibleDataCorruptionException;

public final class Validators {

	private Validators() {
	}

	public static void supportedVersionValidator(ProtocolInfo current, ProtocolInfo expected) {
		if (!current.equals(expected) || !current.equals(ProtocolInfo.VERSION_1_0)) {
			throw new UnsuportedProtocolVersionException(String.format("The %s is not supported", current));
		}
	};
	
	public static void dataIntegrityValidator(int param, long rightBoardOfRange, String message) {
		if(param != rightBoardOfRange){
			throw new PossibleDataCorruptionException(String.format(message + "(the read byte value %s must be equal %s)", param, rightBoardOfRange));
		}
	}

}
