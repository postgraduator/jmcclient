package com.smithson.jmcserver.io.config;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import com.smithson.jmcserver.io.ErrorMessageCreator;
import com.smithson.jmcserver.io.JMCClientRequestProcessor;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.ServerInfo;

public interface JMCServerConfig {
	ExecutorService getClientThreadPoolExecutor();
	ServerInfo getServerInfo();
	JMCDataAccessor getJMCDataAccessor();
	JMCClientRequestProcessor createClientRequestProcessor(Socket socket) throws IOException;
	ErrorMessageCreator getErrorMessageCreator();
}
