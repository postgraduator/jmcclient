package com.smithson.jmcserver.io;

import java.nio.ByteBuffer;

import com.smithson.jmcserver.io.constant.Status;

public interface PreparedResponse {
	ByteBuffer getPreparedByteBuffer();
	Status getStatusOfRequestProcessing();
}
