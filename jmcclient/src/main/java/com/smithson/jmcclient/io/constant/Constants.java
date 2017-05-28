package com.smithson.jmcclient.io.constant;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Constants {
	private Constants() {
	}
	public static final int SERVER_PORT = 9010;
	public static final String SERVER_ADDRESS = "Localhost Server";
	
	public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
	public static final List<JMCMethod> IMPL_METHODS_V_1_0;
	public static final JMCMethod NO_METHOD;
	
	static {
		JMCMethod[] methodArray = { JMCMethod.PUT, JMCMethod.GET, JMCMethod.REMOVE, JMCMethod.REMOVE,
				JMCMethod.CLOSE_CONNECTION };
		IMPL_METHODS_V_1_0 = Collections.unmodifiableList(Arrays.asList(methodArray));
		NO_METHOD = JMCMethod.NO_METHOD;
	}
}
