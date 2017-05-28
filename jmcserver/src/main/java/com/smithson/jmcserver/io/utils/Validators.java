package com.smithson.jmcserver.io.utils;

import com.smithson.jmcserver.io.Version;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.exception.BadParsedParameterException;
import com.smithson.jmcserver.io.exception.PossibleDataCorruptionException;
import com.smithson.jmcserver.io.exception.UnsuportedProtocolVersionException;
import com.smithson.jmcserver.io.exception.UnsupportedMethodException;

public final class Validators {

	private Validators() {
	}

	public static void supportedVersionValidator(ProtocolInfo current) {
		if (current == ProtocolInfo.UNSUPORTED) {
			throw new UnsuportedProtocolVersionException(String.format("The %s is not supported", current));
		}
	};

	public static void supportedMethodvalidator(Version protocolVer, JMCMethod method) {
		if (method.equals(JMCMethod.NO_METHOD)) {
			throw new UnsupportedMethodException(String.format(
					"The passed method id to stream is not recognized by server (the used protocl %s) ", protocolVer));
		}
	}
	
	public static void parsedParametersValidator(long param, int leftBoardOfRange, long rightBoardOfRange, String message, Status error) {
		if(param < leftBoardOfRange) {
			throw new BadParsedParameterException(String.format(message + "(the value %s must be more than %s)", param, leftBoardOfRange), error);
		} else if(param > rightBoardOfRange){
			throw new BadParsedParameterException(String.format(message + "(the value %s must be less than %s)", param, rightBoardOfRange), error);
		}
	}
	
	public static void dataIntegrityValidator(int param, long rightBoardOfRange, String message, Status error) {
		if(param != rightBoardOfRange){
			throw new PossibleDataCorruptionException(String.format(message + "(the read byte value %s must be equal %s)", param, rightBoardOfRange), error);
		}
	}

}
