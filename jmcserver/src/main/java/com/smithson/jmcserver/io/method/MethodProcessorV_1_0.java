package com.smithson.jmcserver.io.method;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.exception.BadParsedParameterException;
import com.smithson.jmcserver.io.utils.DataUtils;

public abstract class MethodProcessorV_1_0 extends MethodProcessor {
	protected MethodProcessorV_1_0(JMCMethod method) {
		super(method);
	}
	
	protected TimeUnit extractTimeUnit(InputStream in) throws BadParsedParameterException, IOException {
		int timeUnitOrdinal = 0;
		try {
			timeUnitOrdinal = (int) DataUtils.getValueFromInput(in, 1);
			return TimeUnit.values()[timeUnitOrdinal];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BadParsedParameterException("Bad time unit id was read from the stream", e, Status.BAD_TIME_UNIT);
		}
	}
	
	
	protected ByteBuffer prepareBufferForResponse(ProtocolInfo clientProtocolInfo, Status status, ByteBuffer content) {
		byte[] responseHead = new byte[] { clientProtocolInfo.getVersion().getFullVersion(), status.getStatusID() };
		byte[] byteContent = content.array();
		byte[] byteResponse = DataUtils.mergeArrays(responseHead, byteContent);
		return ByteBuffer.wrap(byteResponse);
	}
}
