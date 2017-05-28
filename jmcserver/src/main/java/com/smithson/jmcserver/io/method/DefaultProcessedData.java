package com.smithson.jmcserver.io.method;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;

final class DefaultProcessedData implements InvokedRequestData {
	private final ProtocolInfo protocol;
	private final JMCMethod method;
	private final ByteBuffer key;
	private final Long time;
	private final TimeUnit timeUnit;
	private final ByteBuffer content;

	public DefaultProcessedData(ProtocolInfo protocol, JMCMethod method, ByteBuffer key, Long time,
			TimeUnit timeUnit, ByteBuffer content) {
		super();
		this.protocol = protocol;
		this.method = method;
		this.key = key;
		this.time = time;
		this.timeUnit = timeUnit;
		this.content = content;
	}

	@Override
	public JMCMethod getMethod() {
		return method;
	}

	@Override
	public ByteBuffer getKey() {
		return key;
	}

	@Override
	public Long getTimeAmount() {
		return time;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	@Override
	public ByteBuffer getContent() {
		return content;
	}

	@Override
	public ProtocolInfo getProtocolInfo() {
		return protocol;
	}
}
