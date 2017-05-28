package com.smithson.jmcserver.io.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.Response;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;


class DefaultResponse implements Response {
	private final OutputStream out;

	public DefaultResponse(OutputStream out) {
		super();
		this.out = out;
	}

	@Override
	public Status writeResponse(InvokedRequestData data, JMCDataAccessor accessor, ProtocolInfo protocol) throws IOException {
		JMCMethod method = data.getMethod();
		PreparedResponse res = protocol.getMethodProcessor(method).doMethod(data, accessor); 
		out.write(res.getPreparedByteBuffer().array());
		return res.getStatusOfRequestProcessing();
	}


	@Override
	public OutputStream getOutputStream() {
		return out;
	}

}
