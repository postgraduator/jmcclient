package com.smithson.jmcserver.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.ErrorMessageCreator;
import com.smithson.jmcserver.io.JMCClientRequestProcessor;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.Response;
import com.smithson.jmcserver.io.ServerInfo;
import com.smithson.jmcserver.io.config.JMCClientRequestProcessorConfig;
import com.smithson.jmcserver.io.config.JMCServerConfig;

public class DefaultJMCServerConfig implements JMCServerConfig {
	private final JMCDataAccessor cache;
	private final ServerInfo serverInfo;
	private final ExecutorService clientThreadPool;
	private final ErrorMessageCreator errorMessageCreator;

	public DefaultJMCServerConfig(ServerInfo serverInfo) {
		super();
		this.serverInfo = serverInfo;
		this.clientThreadPool = createClientThreadPool();
		this.cache = createJMCDataAccessor();
		this.errorMessageCreator = createErrorMessageCreator();
	}
	
	protected ExecutorService createClientThreadPool() {
		return new ThreadPoolExecutor(serverInfo.getAliveThreadCount(), serverInfo.getMaxThreadCount(),
				60L, TimeUnit.MICROSECONDS, new SynchronousQueue<Runnable>());
	}
	
	protected JMCDataAccessor createJMCDataAccessor() {
		return new DefaultJMCDataAccessor(creatDataStorageMap(), createCleanServiceExecutorForTempContent());
	}
	
	protected ErrorMessageCreator createErrorMessageCreator() {
		return new DefaultErrorMessageCreator();
	}

	protected ScheduledExecutorService createCleanServiceExecutorForTempContent() {
		return Executors.newSingleThreadScheduledExecutor();
	}
	
	protected ConcurrentMap<ByteBuffer, ByteBuffer> creatDataStorageMap() {
		return new ConcurrentHashMap<ByteBuffer, ByteBuffer>();
	}
	
	protected JMCClientRequestProcessorConfig createJMCClientRequestProcessorConfig(Socket socket) throws IOException {
		RequestDataExtractor req = createAndGetRequest(socket.getInputStream());
		Response res = createAndGetResponse(socket.getOutputStream());
		return new DefaultJMCClientRequestProcessorConfig(cache, req, res, getErrorMessageCreator());
	}

	protected RequestDataExtractor createAndGetRequest(InputStream in) {
		return new DefaultRequestDataExtractor(in);
	}

	protected Response createAndGetResponse(OutputStream out) {
		return new DefaultResponse(out);
	}
	
	protected JMCClientRequestProcessor createJMCClientRequestProcessor(JMCClientRequestProcessorConfig config) {
		return new DefaultJMCClientRequestProcessor(config);
	}

	@Override
	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	@Override
	public JMCClientRequestProcessor createClientRequestProcessor(Socket socket) throws IOException {
		JMCClientRequestProcessorConfig config = createJMCClientRequestProcessorConfig(socket);
		return createJMCClientRequestProcessor(config);
	}

	@Override
	public JMCDataAccessor getJMCDataAccessor() {
		return cache;
	}

	@Override
	public ErrorMessageCreator getErrorMessageCreator() {
		return errorMessageCreator;
	}
	
	@Override
	public ExecutorService getClientThreadPoolExecutor() {
		return clientThreadPool;
	}
}
