package com.smithson.jmcserver.io;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.smithson.jmcserver.io.ServerInfo;
import com.smithson.jmcserver.io.exception.IncorrectServerInfoException;

@RunWith(Enclosed.class)
public class ServerInfoTest {
	private static InputStream createProperties(int port, int aliveThreadCount, int maxThreadCount, String name) {
		StringBuilder propertiesContainer = new StringBuilder();
		propertiesContainer.append("server.port=").append(port).append("\n");
		propertiesContainer.append("server.thread.alive.count=").append(aliveThreadCount).append("\n");;
		propertiesContainer.append("server.thread.max.count=").append(maxThreadCount).append("\n");;
		if(name != null) {
			propertiesContainer.append("server.name=").append(name).append("\n");;
		}
		return new ByteArrayInputStream(propertiesContainer.toString().getBytes());
	}; 
	
	public static class CorrectParameterTest {
		private final int PORT = 9010;
		private final int ALIVE_THREAD_COUNT = 10;
		private final int MAX_THREAD_COUNT = 11; 
		private final String SERVER_NAME = "server";
		@Test
		public void defaultServerInfoTest() {
			ServerInfo si = new ServerInfo(PORT, ALIVE_THREAD_COUNT, MAX_THREAD_COUNT, SERVER_NAME);
			assertEquals(PORT, si.getServerPort());
			assertEquals(ALIVE_THREAD_COUNT, si.getAliveThreadCount());
			assertEquals(MAX_THREAD_COUNT, si.getMaxThreadCount());
			assertEquals(SERVER_NAME, si.getName());
		}
		@Test
		public void defaultServerInfoWithPropertiesTest() throws IOException {
			Properties file = new Properties();
			file.load(createProperties(PORT, ALIVE_THREAD_COUNT, MAX_THREAD_COUNT, SERVER_NAME));
			ServerInfo si = new ServerInfo(file);
			assertEquals(PORT, si.getServerPort());
			assertEquals(ALIVE_THREAD_COUNT, si.getAliveThreadCount());
			assertEquals(MAX_THREAD_COUNT, si.getMaxThreadCount());
			assertEquals(SERVER_NAME, si.getName());
		}
	}
	
	@RunWith(Parameterized.class)
	public static class IncorrectParametersTest {
		private int serverPort;
		private int aliveThreadCount;
		private int maxThreadCount;
		private String name;
		public IncorrectParametersTest(int serverPort, int aliveThreadCount, int maxThreadCount, String name) {
			super();
			this.serverPort = serverPort;
			this.aliveThreadCount = aliveThreadCount;
			this.maxThreadCount = maxThreadCount;
			this.name = name;
		}
		
		@Test(expected = IncorrectServerInfoException.class)
		public void defultServerInfoTest() {
			new ServerInfo(serverPort, aliveThreadCount, maxThreadCount, name);
		}
		
		@Test(expected = IncorrectServerInfoException.class)
		public void defultServerInfoTestWithProperties() throws IOException {
			Properties file = new Properties();
			file.load(createProperties(serverPort, aliveThreadCount, maxThreadCount, name));
			new ServerInfo(file);
		}
		
		@Parameterized.Parameters
		public static List<Object[]> getParameters() {
			return Arrays.asList(new Object[][] {
				new Object[] {0, 1, 1, "server"},
				new Object[] {100000, 1, 1, "server"},
				new Object[] {9010, 0, 1, "server"},
				new Object[] {9010, 10, 9, "server"},
				new Object[] {9010, 10, 11, ""},
				new Object[] {9010, 10, 11, null},
			});
		}
	}

}
