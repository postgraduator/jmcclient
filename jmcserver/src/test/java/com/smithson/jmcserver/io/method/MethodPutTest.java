package com.smithson.jmcserver.io.method;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.exception.BadParsedParameterException;
import com.smithson.jmcserver.io.utils.DataUtils;

public class MethodPutTest {

	private final ProtocolInfo PROTOCOL = ProtocolInfo.VERSION_1_0;
	private final JMCMethod EXPECTED_METHOD = JMCMethod.PUT;
	private final String KEY = "The arbitrary key";
	private final Object CONTENT = "The arbitrary content";
	private final MethodProcessor methodProcessor = new MethodPut();
	private final TimeUnit TIME_UNIT = TimeUnit.DAYS;
	private final long TTL = 1000l;

	private ByteBuffer requestBuffer;
	private ByteBuffer responseAddedBuffer;
	private ByteBuffer responseRemovedBuffer;
	private ByteBuffer byteContent;
	private JMCDataAccessor accessor = Mockito.mock(JMCDataAccessor.class);
	private InputStream in;
	private int BAD_TIME_UNIT_ORDINAL = 100;
	private int BAD_AMOUNT_OF_TIME = -1;

	private ByteBuffer createPutResponse(Status status) {
		ByteBuffer responseBuffer = ByteBuffer.wrap(new byte[2]);
		responseBuffer.mark();
		responseBuffer.put(PROTOCOL.getVersion().getFullVersion());
		responseBuffer.put(status.getStatusID());
		responseBuffer.reset();
		return responseBuffer;
	}

	@Before
	public void setUpResponse() {
		responseAddedBuffer = createPutResponse(Status.ADDED);
		responseRemovedBuffer = createPutResponse(Status.REPLACED);
	}

	private byte[] transformContentToByte(Object content) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream outObj = new ObjectOutputStream(out);
		outObj.writeObject(content);
		return out.toByteArray();
	}

	private ByteBuffer setRequestWithCurrentTimeAndContent(ByteBuffer wrappedContent, long ttl, int timeUnit)
			throws IOException {
		byte[] key = KEY.getBytes();
		byte[] keySize = DataUtils.valToByteArray(key.length, PROTOCOL.byteCountForKey());
		byte[] timeAmount = DataUtils.valToByteArray(ttl, PROTOCOL.byteCountForTimePeriod());

		byte[] content = wrappedContent.array();
		byte[] conSize = DataUtils.valToByteArray(content.length, PROTOCOL.byteCountForContentSize());

		ByteBuffer requestBuffer = ByteBuffer.wrap(
				new byte[2 + keySize.length + key.length + timeAmount.length + 1 + conSize.length + content.length]);
		requestBuffer.mark();
		requestBuffer.put(keySize).put(key).put(timeAmount).put((byte) timeUnit).put(conSize).put(content);
		requestBuffer.reset();
		return requestBuffer;
	}

	@Before
	public void setUpRequest() throws IOException {
		byteContent = ByteBuffer.wrap(transformContentToByte(CONTENT));

		requestBuffer = setRequestWithCurrentTimeAndContent(byteContent, TTL, TIME_UNIT.ordinal());
		in = new ByteArrayInputStream(requestBuffer.array());

	}

	@Test
	public void putRequestStreamParserTest() throws IOException {
		InvokedRequestData data = methodProcessor.processMethod(in, PROTOCOL);
		assertEquals(PROTOCOL, data.getProtocolInfo());
		assertEquals(EXPECTED_METHOD, data.getMethod());
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		assertEquals(key, data.getKey());
		assertEquals(Long.valueOf(TTL), data.getTimeAmount());
		assertEquals(TIME_UNIT, data.getTimeUnit());
		assertEquals(byteContent, data.getContent());
	}

	@Test
	public void putResponseStatusAddTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.put(key, byteContent, TTL, TIME_UNIT)).thenReturn(Status.ADDED);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.ADDED, response.getStatusOfRequestProcessing());
		assertEquals(responseAddedBuffer, response.getPreparedByteBuffer());
	}
	
	@Test
	public void putResponseStatusReplacedTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.put(key, byteContent, TTL, TIME_UNIT)).thenReturn(Status.REPLACED);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.REPLACED, response.getStatusOfRequestProcessing());
		assertEquals(responseRemovedBuffer, response.getPreparedByteBuffer());
	}
	
	@Test(expected=BadParsedParameterException.class)
	public void badTimeUnitTest() throws IOException {
		ByteBuffer requestWithBadTimeUnit = setRequestWithCurrentTimeAndContent(byteContent, TTL, BAD_TIME_UNIT_ORDINAL);
		methodProcessor.processMethod(new ByteArrayInputStream(requestWithBadTimeUnit.array()), PROTOCOL);
	}
	
	@Test(expected=BadParsedParameterException.class)
	public void badTimeAmountTest() throws IOException {
		ByteBuffer requestWithBadTimeUnit = setRequestWithCurrentTimeAndContent(byteContent, BAD_AMOUNT_OF_TIME, TIME_UNIT.ordinal());
		methodProcessor.processMethod(new ByteArrayInputStream(requestWithBadTimeUnit.array()), PROTOCOL);
	}

}
