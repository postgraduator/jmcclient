package com.smithson.jmcserver.io.method;

import static com.smithson.jmcserver.io.constant.Constants.EMPTY_BUFFER;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.utils.DataUtils;
import com.smithson.jmcserver.io.utils.Validators;

public class MethodPut extends MethodProcessorV_1_0 {

	public MethodPut() {
		super(JMCMethod.PUT);
	}

	@Override
	public InvokedRequestData processMethod(InputStream in, ProtocolInfo protocol) throws IOException {
		long size = DataUtils.getValueFromInput(in, protocol.byteCountForKey());
		ByteBuffer key = extractContent(in, size, protocol.getMaxKeySize(), "key");
		long time = DataUtils.getValueFromInput(in, protocol.byteCountForTimePeriod());
		Validators.parsedParametersValidator(time, 0, Long.MAX_VALUE, "The negative time amount was parsed.", Status.NEGATIVE_TIME);
		TimeUnit timeUnit = extractTimeUnit(in);
		size = DataUtils.getValueFromInput(in, protocol.byteCountForContentSize());
		ByteBuffer content = extractContent(in, size, protocol.getMaxContentSize(), "content");		
		return new DefaultProcessedData(protocol, method, key, time, timeUnit, content);
	}

	@Override
	public PreparedResponse doMethod(InvokedRequestData data, JMCDataAccessor accessor) {
		Status status = accessor.put(data.getKey(), data.getContent(), data.getTimeAmount(), data.getTimeUnit());
		return new DefaultPreparedResponse(prepareBufferForResponse(data.getProtocolInfo(), status, EMPTY_BUFFER), status);
	}

}
