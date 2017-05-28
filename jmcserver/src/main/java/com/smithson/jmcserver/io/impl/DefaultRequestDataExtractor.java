package com.smithson.jmcserver.io.impl;

import java.io.IOException;
import java.io.InputStream;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.Version;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.utils.Validators;

class DefaultRequestDataExtractor implements RequestDataExtractor {
	private final InputStream in;
	
	public DefaultRequestDataExtractor(InputStream in) {
		super();
		this.in = in;
	}

	protected ProtocolInfo invokeProtocolInfo(InputStream in) throws IOException {
		Version ver = new Version((byte) in.read());
		ProtocolInfo protocol = ProtocolInfo.getProtocolInfoByVersion(ver);
		Validators.supportedVersionValidator(protocol);
		return protocol;
	}

	protected JMCMethod getJMCMethod(InputStream in, ProtocolInfo protocol) throws IOException {
		JMCMethod method  = JMCMethod.getMethodByID(in.read()); 
		Validators.supportedMethodvalidator(protocol.getVersion(), method);
		return method;
	}

	@Override
	public InvokedRequestData extract() throws IOException {
		ProtocolInfo protocol = invokeProtocolInfo(in);
		JMCMethod method = getJMCMethod(in, protocol);
		return protocol.getMethodProcessor(method).processMethod(in, protocol);
	}

	@Override
	public InputStream getInputStream() {
		return in;
	}

}
