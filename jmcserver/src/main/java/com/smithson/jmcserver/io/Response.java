package com.smithson.jmcserver.io;

import java.io.IOException;
import java.io.OutputStream;

import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;

public interface Response {
	Status writeResponse(InvokedRequestData data, JMCDataAccessor accessor, ProtocolInfo protocol) throws IOException;
	OutputStream getOutputStream();
}
