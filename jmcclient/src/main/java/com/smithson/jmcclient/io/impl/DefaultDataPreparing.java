package com.smithson.jmcclient.io.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcclient.io.DataPreparing;
import com.smithson.jmcclient.io.constant.JMCMethod;
import com.smithson.jmcclient.io.constant.ProtocolInfo;
import com.smithson.jmcclient.io.utils.DataUtils;

class DefaultDataPreparing implements DataPreparing<String> {

	private final ProtocolInfo protocol;
	
	
	public DefaultDataPreparing(ProtocolInfo protocol) {
		super();
		this.protocol = protocol;
	}

	@Override
	public byte[] createRequestHead(JMCMethod method) {
		return new byte[]{protocol.getVersion().getFullVersion(), (byte)method.getMethodID()};
	}

	@Override
	public byte[] prepareKeyToRequest(String key) {
		byte[] keyLength = DataUtils.valToByteArray(key.length(), protocol.byteCountForKey());
		byte[] byteKey = key.getBytes();
		return DataUtils.mergeArrays(keyLength, byteKey);
	}

	@Override
	public byte[] prepareObjectToRequest(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objOut = new ObjectOutputStream(out);
		objOut.writeObject(obj);
		byte[] content = out.toByteArray();
		byte[] conSize = DataUtils.valToByteArray(content.length, protocol.byteCountForContentSize());
		objOut.close();
		out.close();
		return DataUtils.mergeArrays(conSize, content);
	}

	@Override
	public byte[] prepareTimeToRequest(long ttl, TimeUnit timeUnit) {
		byte[] ttlBytes = DataUtils.valToByteArray(ttl, protocol.byteCountForTimePeriod());
		byte[] timeUnitByte = new byte[]{(byte)timeUnit.ordinal()};
		return DataUtils.mergeArrays(ttlBytes, timeUnitByte);
	}

	@Override
	public ProtocolInfo getProtocolInfo() {
		return protocol;
	}

}
