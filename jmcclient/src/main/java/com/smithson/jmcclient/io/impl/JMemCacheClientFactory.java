package com.smithson.jmcclient.io.impl;

import java.io.IOException;
import java.net.UnknownHostException;

import com.smithson.jmcclient.io.JMCClient;
import com.smithson.jmcclient.io.constant.ProtocolInfo;

public class JMemCacheClientFactory {
	protected JMemCacheClientFactory() {
		
	}
	
	public static JMemCacheClientFactory create() {
		return new JMemCacheClientFactory();
	}
	
	public JMCClient buildNewClient(String serverName, int serverPort) throws UnknownHostException, IOException {
		return new DefaultJMCClient(serverName, serverPort, new DefaultDataPreparing(ProtocolInfo.VERSION_1_0));
	}
}
