package com.smithson.jmcserver.io.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smithson.jmcserver.io.JMCServer;
import com.smithson.jmcserver.io.config.JMCServerConfig;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.exception.JMCServerException;

class DefaultJMCServer implements JMCServer {
	private final static Logger LOGGER = LoggerFactory.getLogger(DefaultJMCServer.class);
	private final JMCServerConfig serverConfig;
	private final ServerSocket serverSocket;
	private final Thread serverThread;
	private boolean serverStopped = true;

	public DefaultJMCServer(JMCServerConfig serverConfig) {
		this.serverConfig = serverConfig;
		this.serverSocket = createServerSocket();
		this.serverThread = createMainServerThread();
	}

	protected ServerSocket createServerSocket() {
		try {
			ServerSocket serverSocket = new ServerSocket(serverConfig.getServerInfo().getServerPort());
			serverSocket.setReuseAddress(true);
			return serverSocket;
		} catch (IOException e) {
			throw new JMCServerException(
					"Server can not create server socket with port=" + serverConfig.getServerInfo().getServerPort(), e,
					Status.ERROR);
		}
	}

	protected Thread createMainServerThread() {
		Thread serverThread = new Thread(this, "JMC ServerThread");
		return serverThread;
	}

	@Override
	public void start() {
		if (serverThread.getState() == Thread.State.NEW) {
			serverThread.start();
			serverStopped = false;
			Runtime.getRuntime().addShutdownHook(getShutdownHook());
			LOGGER.info("JMC Server has started succesfully \n" + serverConfig.getServerInfo());
		} else {
			LOGGER.warn("The JMC server has already started. Operation was skipped");
		}

	}

	protected void terminateJMCServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.warn("Error was encountered during close server socket: " + e.getMessage(), e);
		}
		serverStopped = true;
		serverConfig.getJMCDataAccessor().clear();
		serverConfig.getClientThreadPoolExecutor().shutdownNow();
		LOGGER.info("JMC Server was stopped");
	}

	protected Thread getShutdownHook() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				if (!serverStopped) {
					terminateJMCServer();
				}
			}
		}, "ShutdownHook");
	}

	@Override
	public void stop() {
		if (!serverStopped) {
			LOGGER.info("JMC Server stop command was requested. Preparing for stopping ...");
			serverThread.interrupt();
			terminateJMCServer();

		} else {
			LOGGER.info("JMC Server is not working now");
		}
	}

	@Override
	public void run() {
		ExecutorService clientThreadPool = serverConfig.getClientThreadPoolExecutor();
		while (!serverThread.isInterrupted()) {
			try {
				Socket socket = serverSocket.accept();
				socket.setKeepAlive(true);
				try {
					clientThreadPool.submit(serverConfig.createClientRequestProcessor(socket));
				} catch (JMCServerException e) {
					LOGGER.error(" During request processing error was encountered " + e.getMessage(), e);
				} catch (RejectedExecutionException e) {
					socket.getOutputStream().write(serverConfig.getErrorMessageCreator()
							.createErrorMessage(ProtocolInfo.VERSION_1_0, Status.CONNECTION_REFUSED).array());
					socket.close();
				}
			} catch (IOException e) {
				if (!serverSocket.isClosed()) {
					LOGGER.error(" Client socket can not be accepted " + e.getMessage(), e);
				}
				if (!serverStopped) {
					terminateJMCServer();
				}
				break;
			}
		}

	}

}
