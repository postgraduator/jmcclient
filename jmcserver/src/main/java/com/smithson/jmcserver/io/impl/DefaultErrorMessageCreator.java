package com.smithson.jmcserver.io.impl;

import java.nio.ByteBuffer;

import com.smithson.jmcserver.io.ErrorMessageCreator;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;

class DefaultErrorMessageCreator implements ErrorMessageCreator {

	@Override
	public ByteBuffer createErrorMessage(ProtocolInfo protocol, Status status) {
		return ByteBuffer.wrap(new byte[]{ protocol.getVersion().getFullVersion(),
				status.getStatusID() });
	}

}
