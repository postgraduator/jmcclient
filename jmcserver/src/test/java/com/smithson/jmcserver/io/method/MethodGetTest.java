package com.smithson.jmcserver.io.method;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.PreparedResponse;
import com.smithson.jmcserver.io.constant.Constants;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.utils.DataUtils;

public class MethodGetTest {

	private final ProtocolInfo PROTOCOL = ProtocolInfo.VERSION_1_0;
	private final JMCMethod EXPECTED_METHOD = JMCMethod.GET; 
	private final String KEY = "The arbitrary key";
	private final Object CONTENT = "The arbitrary content";
	
	private ByteBuffer content;
	private final MethodProcessor methodProcessor =  new MethodGet();
	private ByteBuffer requestBuffer;
	private ByteBuffer responseBuffer;
	private ByteBuffer notFoundResponseBuffer;
	private JMCDataAccessor accessor = Mockito.mock(JMCDataAccessor.class);
	private InputStream in;
	
	
	@Before
	public void setUpResponse() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		ObjectOutputStream outObj = new ObjectOutputStream(out);
		outObj.writeObject(CONTENT);
		byte[] content = out.toByteArray();
		this.content = ByteBuffer.wrap(content);
		byte[] conSize = DataUtils.valToByteArray(content.length, PROTOCOL.byteCountForContentSize());
		
		responseBuffer = ByteBuffer.wrap(new byte[2 + conSize.length + content.length]);
		responseBuffer.mark();
		responseBuffer.put(PROTOCOL.getVersion().getFullVersion());
		responseBuffer.put(Status.FOUND.getStatusID());
		responseBuffer.put(conSize);
		responseBuffer.put(content);
		responseBuffer.reset();
		
		notFoundResponseBuffer = ByteBuffer.wrap(new byte[2]);
		notFoundResponseBuffer.mark();
		notFoundResponseBuffer.put(PROTOCOL.getVersion().getFullVersion());
		notFoundResponseBuffer.put(Status.NOT_FOUND.getStatusID());
		notFoundResponseBuffer.reset();
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
	public void getRequestTest() throws IOException {
		InvokedRequestData data = methodProcessor.processMethod(in, PROTOCOL);
		assertEquals(PROTOCOL, data.getProtocolInfo());
		assertEquals(EXPECTED_METHOD, data.getMethod());
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		assertEquals(key, data.getKey());
	}
	
	@Test
	public void contentExistsTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.get(key)).thenReturn(content);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.FOUND, response.getStatusOfRequestProcessing());
		assertEquals(responseBuffer, response.getPreparedByteBuffer());
	}
	
	@Test
	public void contentDoesNotExistTest() throws IOException {
		ByteBuffer key = ByteBuffer.wrap(KEY.getBytes());
		Mockito.when(accessor.get(key)).thenReturn(Constants.EMPTY_BUFFER);
		PreparedResponse response = methodProcessor.doMethod(methodProcessor.processMethod(in, PROTOCOL), accessor);
		assertEquals(Status.NOT_FOUND, response.getStatusOfRequestProcessing());		
		assertEquals(notFoundResponseBuffer, response.getPreparedByteBuffer());
	}

}
