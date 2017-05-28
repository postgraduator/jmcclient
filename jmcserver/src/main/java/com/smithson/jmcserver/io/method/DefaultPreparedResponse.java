package com.smithson.jmcserver.io.method;

import java.nio.ByteBuffer;

import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.Status;

class DefaultPreparedResponse implements PreparedResponse {
	private final ByteBuffer response;
	private final Status status;
	public DefaultPreparedResponse(ByteBuffer response, Status status) {
		super();
		this.response = response;
		this.status = status;
	}

	@Override
	public ByteBuffer getPreparedByteBuffer() {
		return response;
	}

	@Override
	public Status getStatusOfRequestProcessing() {
		return status;
	}

}
