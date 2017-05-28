package com.smithson.jmcserver.io;

import java.io.IOException;

public interface JMCClientRequestProcessor extends Runnable {
	void service() throws IOException;
	void destroy() throws IOException;
}
