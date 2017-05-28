package com.smithson.jmcserver.io.method;

import static com.smithson.jmcserver.io.constant.Constants.EMPTY_BUFFER;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;

public class MethodClear extends MethodProcessorV_1_0 {

	public MethodClear() {
		super(JMCMethod.CLEAR);
	}

	@Override
	public InvokedRequestData processMethod(InputStream in, ProtocolInfo protocol) throws IOException {
		return new DefaultProcessedData(protocol, method, EMPTY_BUFFER, 0L, TimeUnit.MILLISECONDS, EMPTY_BUFFER);
	}

	@Override
	public PreparedResponse doMethod(InvokedRequestData data, JMCDataAccessor accessor) {
		Status status = accessor.clear();
		return new DefaultPreparedResponse(prepareBufferForResponse(data.getProtocolInfo(), status, EMPTY_BUFFER), status);
	}

}
