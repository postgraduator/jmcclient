package com.smithson.jmcserver.io;

import java.nio.ByteBuffer;

import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;

public interface ErrorMessageCreator {
	ByteBuffer createErrorMessage(ProtocolInfo protocol, Status status);
}
