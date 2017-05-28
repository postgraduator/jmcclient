package com.smithson.jmcserver.io.constant;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.exception.UnsupportedMethodException;
import com.smithson.jmcserver.io.method.MethodClear;
import com.smithson.jmcserver.io.method.MethodCloseConnection;
import com.smithson.jmcserver.io.method.MethodGet;
import com.smithson.jmcserver.io.method.MethodProcessor;
import com.smithson.jmcserver.io.method.MethodPut;
import com.smithson.jmcserver.io.method.MethodRemove;

public final class Constants {
	private Constants() {
	}
	public static final int SERVER_PORT = 9010;
	public static final int ALIVE_THREAD_COUNT = 5;
	public static final int MAX_THREAD_COUNT = 10;
	public static final String SERVER_NAME = "Localhost Server";
	
	public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
	public static final List<MethodProcessor> IMPL_METHODS_V_1_0;
	public static final MethodProcessor NO_METHOD;
	
	static {
		MethodProcessor[] methodArray = { new MethodPut(), new MethodGet(), new MethodRemove(), new MethodClear(),
				new MethodCloseConnection() };
		IMPL_METHODS_V_1_0 = Collections.unmodifiableList(Arrays.asList(methodArray));
		NO_METHOD = new MethodProcessor(JMCMethod.NO_METHOD) {
			@Override
			public InvokedRequestData processMethod(InputStream in, ProtocolInfo protocol) throws IOException {
				throw new UnsupportedMethodException("The unsuported method was requested");
			}

			@Override
			public PreparedResponse doMethod(InvokedRequestData data, JMCDataAccessor accessor) {
				throw new UnsupportedMethodException("The unsuported method was encountred");
			}
			
		};
	}
}
