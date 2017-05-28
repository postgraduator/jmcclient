package com.smithson.jmcserver.io.method;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.utils.IOUtilsForJMC;
import com.smithson.jmcserver.io.utils.Validators;

public abstract class MethodProcessor {
	private final static Logger LOGGER = LoggerFactory.getLogger(MethodProcessor.class);
	public final JMCMethod method;

	protected MethodProcessor(JMCMethod method) {
		super();
		this.method = method;
	}

	protected ByteBuffer extractContent(InputStream in, long size, long maxValue, String contentName)
			throws IOException {
		Validators.parsedParametersValidator(size, 1, maxValue,
				String.format("The wrong size of %s was encountered", contentName), Status.WRONG_SIZE_VALUE);
		LOGGER.debug("The size of {} equal {} was parsed", contentName, size);
		byte[] con = IOUtilsForJMC.toByteArray(in, size);
		Validators.dataIntegrityValidator(con.length, size,
				String.format("Posiible data corruption in read %s has been encountered", contentName),
				Status.POSSIBLE_DATA_CORRUPTION);
		return ByteBuffer.wrap(con);
	}

	public abstract InvokedRequestData processMethod(InputStream in, ProtocolInfo protocol) throws IOException;

	public abstract PreparedResponse doMethod(InvokedRequestData data, JMCDataAccessor accessor);
}
