package com.smithson.jmcserver.io.impl;

import java.util.Properties;

import com.smithson.jmcserver.io.JMCServer;
import com.smithson.jmcserver.io.ServerInfo;
import com.smithson.jmcserver.io.config.JMCServerConfig;
import static com.smithson.jmcserver.io.constant.Constants.*;

public class JMCServerFactory {
	protected JMCServerFactory() {
	}
	
	public static JMCServerFactory create() {
		return new JMCServerFactory();
	}
	
	public JMCServer createJMCServer(Properties serverProperties) {
		ServerInfo serverInfo;
		if (serverProperties != null) {
			serverInfo = new ServerInfo(serverProperties);
		} else {
			serverInfo = new ServerInfo(SERVER_PORT, ALIVE_THREAD_COUNT, MAX_THREAD_COUNT, SERVER_NAME);
		}
		JMCServerConfig serverConfig = new DefaultJMCServerConfig(serverInfo);
		return new DefaultJMCServer(serverConfig);
	}
}
