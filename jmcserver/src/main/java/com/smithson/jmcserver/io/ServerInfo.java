package com.smithson.jmcserver.io;

import java.util.Objects;
import java.util.Properties;

import com.smithson.jmcserver.io.exception.IncorrectServerInfoException;

public class ServerInfo {
	private int serverPort;
	private int aliveThreadCount;
	private int maxThreadCount;
	private String name;
	
	private void validator(int param, int leftBoardOfRange, int rightBoardOfRange, String message) throws IncorrectServerInfoException {
		if(param < leftBoardOfRange) {
			throw new IncorrectServerInfoException(String.format(message + "the value ? must be more than ?)", param, leftBoardOfRange));
		} else if(param > rightBoardOfRange){
			throw new IncorrectServerInfoException(String.format(message + "the value ? must be less than ?)", param, rightBoardOfRange));
		}
	}
	
	private void serverInfoValidator() {
		validator(serverPort, 1, 65535, "The incorrect port number was passed (");
		validator(aliveThreadCount, 1, Integer.MAX_VALUE, "The incorrect thread number was passed (");
		validator(maxThreadCount, aliveThreadCount, Integer.MAX_VALUE, "The incorrect maximum thread number was passed (");
	}
	
	private void serverNameValidator() throws IncorrectServerInfoException {
		try {
			if (name.isEmpty()) {
				this.name = null;
			}
			Objects.requireNonNull(name);
		} catch (NullPointerException e) {
			throw new IncorrectServerInfoException("The server name was null or empty", e);
		}
	}
	
	public ServerInfo(Properties serverProp) {
		this.serverPort = Integer.parseInt(serverProp.getProperty("server.port"));
		this.aliveThreadCount = Integer.parseInt(serverProp.getProperty("server.thread.alive.count"));
		this.maxThreadCount = Integer.parseInt(serverProp.getProperty("server.thread.max.count"));
		this.name = serverProp.getProperty("server.name");
		serverInfoValidator();
		serverNameValidator();
	}

	public ServerInfo(int serverPort, int aliveThreadCount, int maxThreadCount, String name) {
		super();
		this.serverPort = serverPort;
		this.aliveThreadCount = aliveThreadCount;
		this.maxThreadCount = maxThreadCount;
		this.name = name;
		serverInfoValidator();
		serverNameValidator();
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getAliveThreadCount() {
		return aliveThreadCount;
	}

	public int getMaxThreadCount() {
		return maxThreadCount;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return String.format("Server Info:\n Port=%s,\n The coount of alive thread %s\n Maximum thread count %s\n Server name=%s", serverPort,
				aliveThreadCount, maxThreadCount, name);
	}

}
