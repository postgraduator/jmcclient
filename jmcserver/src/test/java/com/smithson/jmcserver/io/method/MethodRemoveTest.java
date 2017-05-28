package com.smithson.jmcserver.io.method;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.utils.DataUtils;

public class MethodRemoveTest {

	private final ProtocolInfo PROTOCOL = ProtocolInfo.VERSION_1_0;
	private final JMCMethod EXPECTED_METHOD = JMCMethod.REMOVE; 
	private final String KEY = "The arbitrary key";
	private final MethodProcessor methodProcessor =  new MethodRemove();
	private ByteBuffer requestBuffer;
	private ByteBuffer responseBuffer;
	private JMCDataAccessor accessor = Mockito.mock(JMCDataAccessor.class);
	private InputStream in;
	
	
	@Before
	public void setUpResponse() {
		responseBuffer = ByteBuffer.wrap(new byte[2]);
		responseBuffer.mark();
		responseBuffer.put(PROTOCOL.getVersion().getFullVersion());
		responseBuffer.put(Status.REMOVED.getStatusID());
		responseBuffer.reset();
	}
	
	@Before
	public void setUpRequest() {
		byte[] key = KEY.getBytes();
		byte[] keySize = DataUtils.valToByteArray(key.length, PROTOCOL.byteCountForKey());
		byte[] fullKeyBuffer = DataUtils.mergeArrays(keySize, key);
		requestBuffer = ByteBuffer.wrap(fullKeyBuffer);
		in = new ByteArrayInputStream(requestBuffer.array());
	}
	
	@Test
	public void removeRequestTest() throws IOException {
		InvokedRequestData data = methodProcessor.processMethod(in, PROTOCOL);
		assertEquals(PROTOCOL, data.getProtocolInfo());
		assertEquals(EXPECTED_METHOD, data.getMethod());
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		assertEquals(key, data.getKey());
	}
	
	@Test
	public void contentExistsTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.remove(key)).thenReturn(Status.REMOVED);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.REMOVED, response.getStatusOfRequestProcessing());
		assertEquals(responseBuffer, response.getPreparedByteBuffer());
	}
	
	@Test
	public void contentDoesNotExistTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.remove(key)).thenReturn(Status.NOT_FOUND);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.NOT_FOUND, response.getStatusOfRequestProcessing());
		responseBuffer.put(1, Status.NOT_FOUND.getStatusID());
		assertEquals(responseBuffer, response.getPreparedByteBuffer());
	}

}
