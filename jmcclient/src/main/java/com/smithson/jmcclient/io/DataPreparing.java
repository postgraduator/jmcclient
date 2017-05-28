package com.smithson.jmcclient.io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcclient.io.constant.JMCMethod;
import com.smithson.jmcclient.io.constant.ProtocolInfo;

public interface DataPreparing<T> {
	byte[] createRequestHead(JMCMethod method);
	byte[] prepareKeyToRequest(T key);
	byte[] prepareObjectToRequest(Object obj) throws IOException;
	byte[] prepareTimeToRequest(long ttl, TimeUnit timeUnit);
	ProtocolInfo getProtocolInfo();
}
