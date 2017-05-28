package com.smithson.jmcserver.io;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;

public interface InvokedRequestData {
	JMCMethod getMethod();
	ByteBuffer getKey();
	Long getTimeAmount();
	TimeUnit getTimeUnit();
	ByteBuffer getContent();
	ProtocolInfo getProtocolInfo();
}
