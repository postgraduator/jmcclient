package com.smithson.jmcserver;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smithson.jmcserver.io.JMCServer;
import com.smithson.jmcserver.io.impl.JMCServerFactory;

public class CLI {
	private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);
	private static final List<String> CMD_STOP_COMMAND = Collections
			.unmodifiableList(Arrays.asList("q", "quit", "exit"));

	public static void main(String[] args) {
		Thread.currentThread().setName("JMC Server main thread");
		try{
			JMCServerFactory factory = JMCServerFactory.create();
			Properties serverProperties = new Properties();
			try(InputStream in = CLI.class.getClassLoader().getResourceAsStream("server.properties")){
				serverProperties.load(in);
			}
			JMCServer jmcServer = factory.createJMCServer(serverProperties);
			jmcServer.start();
			waitForStopCommand(jmcServer);
			System.exit(0);
			
		}catch(Exception e) {
			LOGGER.error("Unexpected error was encountered. ", e);
		}
	}
	
	private static void waitForStopCommand(JMCServer jmcServer) {
		try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name())) {
			while(!CMD_STOP_COMMAND.contains(scanner.nextLine())) {
				LOGGER.error("Unsupported command was entered. To stop server type: " + CMD_STOP_COMMAND.toString());
			}
			jmcServer.stop();
		}
	}
}
