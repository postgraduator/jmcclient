package com.smithson.jmcserver.io.impl;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.exception.BadVersionException;
import com.smithson.jmcserver.io.exception.UnsupportedMethodException;
import com.smithson.jmcserver.io.method.MethodProcessor;
import com.smithson.jmcserver.io.utils.DataUtils;

@RunWith(Enclosed.class)
public class DefaultRequestTest {
	private static final ProtocolInfo PROTOCOL = ProtocolInfo.VERSION_1_0;
	private static final String KEY = "The arbitrary key";
	private static final String CONTENT = "The arbitrary content";
	private static final long TTL = 1000L;
	private static final TimeUnit timeUnit = TimeUnit.DAYS;

	private static ByteBuffer transformContentToByte(Object content) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream outObj = new ObjectOutputStream(out);
		outObj.writeObject(content);
		return ByteBuffer.wrap(out.toByteArray());
	}

	private static ByteBuffer createCommonRequest(byte fullVer, int methodID, ByteBuffer wrappedContent, long ttl,
			int timeUnit) throws IOException {
		byte[] head = new byte[] { fullVer, (byte) methodID };
		byte[] key = KEY.getBytes();
		byte[] keySize = DataUtils.valToByteArray(key.length, PROTOCOL.byteCountForKey());
		byte[] timeAmount = DataUtils.valToByteArray(ttl, PROTOCOL.byteCountForTimePeriod());

		byte[] content = wrappedContent.array();
		byte[] conSize = DataUtils.valToByteArray(content.length, PROTOCOL.byteCountForContentSize());

		ByteBuffer requestBuffer = ByteBuffer.wrap(new byte[head.length + keySize.length + key.length
				+ timeAmount.length + 1 + conSize.length + content.length]);
		requestBuffer.mark();
		requestBuffer.put(head).put(keySize).put(key).put(timeAmount).put((byte) timeUnit).put(conSize).put(content);
		requestBuffer.reset();
		return requestBuffer;
	}

	@RunWith(Parameterized.class)
	public static class MethodProcessorChooserTest {
		private final JMCMethod method;

		private final InputStream in;
		private RequestDataExtractor req;

		public MethodProcessorChooserTest(MethodProcessor methodProcessor) throws IOException {
			this.method = methodProcessor.method;
			this.in = new ByteArrayInputStream(createCommonRequest(PROTOCOL.getVersion().getFullVersion(),
					method.getMethodID(), transformContentToByte(CONTENT), TTL, timeUnit.ordinal()).array());
		}

		@Test
		public void methodExtractTest() throws IOException {
			req = new DefaultRequestDataExtractor(in);
			InvokedRequestData data = req.extract();
			assertEquals(method, data.getMethod());
		}

		@Parameterized.Parameters
		public static List<Object[]> getParameters() {
			MethodProcessor[] methods = PROTOCOL.getSupportedJMCMethods().toArray(new MethodProcessor[0]);
			Object[][] testedMethods = new Object[methods.length][1];
			for (int i = 0; i < testedMethods.length; i++) {
				testedMethods[i][0] = methods[i];
			}
			return Arrays.asList(testedMethods);
		}
	}

	public static class ErrorBehaviourTest {
		private final byte ERR_FULL_VER = -1;
		private final int WRONG_METHOD_ID = 126;

		@Test(expected = BadVersionException.class)
		public void wrongVerTest() throws IOException {
			ByteBuffer wrappedContent = transformContentToByte(CONTENT);
			ByteBuffer wrongRequest = createCommonRequest(ERR_FULL_VER, WRONG_METHOD_ID, wrappedContent, TTL,
					timeUnit.ordinal());
			(new DefaultRequestDataExtractor(new ByteArrayInputStream(wrongRequest.array()))).extract();
		}

		@Test(expected=UnsupportedMethodException.class)
		public void wrongMethIDTest() throws IOException {
			ByteBuffer wrappedContent = transformContentToByte(CONTENT);
			ByteBuffer wrongRequest = createCommonRequest(PROTOCOL.getVersion().getFullVersion(), WRONG_METHOD_ID,
					wrappedContent, TTL, timeUnit.ordinal());
			(new DefaultRequestDataExtractor(new ByteArrayInputStream(wrongRequest.array()))).extract();
		}
	}

}
