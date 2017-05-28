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

public class MethodGet extends MethodProcessorV_1_0 {

	public MethodGet() {
		super(JMCMethod.GET);
	}

	@Override
	public InvokedRequestData processMethod(InputStream in, ProtocolInfo protocol) throws IOException {
		long size = DataUtils.getValueFromInput(in, protocol.byteCountForKey());
		ByteBuffer key = extractContent(in, size, protocol.getMaxKeySize(), "key");
		return new DefaultProcessedData(protocol, method, key, 0L, TimeUnit.MILLISECONDS, EMPTY_BUFFER);
	}

	@Override
	public PreparedResponse doMethod(InvokedRequestData data, JMCDataAccessor accessor) {
		Status status;
		ByteBuffer content = accessor.get(data.getKey());
		byte[] conSize;
		if (content.equals(EMPTY_BUFFER)) {
			status = Status.NOT_FOUND;
			conSize = new byte[0];
		} else {
			status = Status.FOUND;
			conSize = DataUtils.valToByteArray(content.array().length,
					data.getProtocolInfo().byteCountForContentSize());
		}
		return new DefaultPreparedResponse(prepareBufferForResponse(data.getProtocolInfo(), status,
				ByteBuffer.wrap(DataUtils.mergeArrays(conSize, content.array()))), status);
	}

}
