package com.smithson.jmcserver.io.impl;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.constant.Constants;
import com.smithson.jmcserver.io.constant.Status;

public class DefaultJMCDataAccessorTest {
	private final ByteBuffer ARBITRARY_BUFFER = ByteBuffer.wrap(new byte[]{1, 2, 10, 100, 50});
	private final ScheduledExecutorService clearOldElTheadPool = Mockito. mock(ScheduledExecutorService.class);
	@SuppressWarnings("unchecked")
	private final ConcurrentMap<ByteBuffer, ByteBuffer> cache = Mockito.mock(ConcurrentMap.class);
	
	private JMCDataAccessor dataAccesssor;
	private ByteBuffer key = Mockito.mock(ByteBuffer.class); 
	
	@Before
	public void setUp() throws Exception {
		dataAccesssor  = new DefaultJMCDataAccessor(cache, clearOldElTheadPool);
	}
	
	@Test
	public void clearMethodTest() {
		assertEquals(Status.CLEARED, dataAccesssor.clear());
	}
	
	@Test
	public void removeMethodTest() {
		Mockito.when(cache.remove(key)).thenReturn(null);
		assertEquals(Status.NOT_FOUND, dataAccesssor.remove(key));
		
		Mockito.when(cache.remove(key)).thenReturn(ByteBuffer.wrap(new byte[0]));
		assertEquals(Status.REMOVED, dataAccesssor.remove(key));
	}
	
	@Test
	public void getMethodTest() {
		Mockito.when(cache.containsKey(key)).thenReturn(false);
		assertEquals(Constants.EMPTY_BUFFER, dataAccesssor.get(key));
		
		Mockito.when(cache.containsKey(key)).thenReturn(true);
		Mockito.when(cache.get(key)).thenReturn(ARBITRARY_BUFFER);
		assertEquals(ARBITRARY_BUFFER, dataAccesssor.get(key));
	}

	@Test
	public void putMethodTest() {
		TimeUnit timeUnit = Mockito.mock(TimeUnit.class);
		Mockito.when(cache.replace(key, ARBITRARY_BUFFER)).thenReturn(ARBITRARY_BUFFER);
		assertEquals(Status.REPLACED, dataAccesssor.put(key, ARBITRARY_BUFFER, 0, timeUnit));
		
		Mockito.when(cache.replace(key, ARBITRARY_BUFFER)).thenReturn(null);
		assertEquals(Status.ADDED, dataAccesssor.put(key, ARBITRARY_BUFFER, 0, timeUnit));	
	}
	
	@Test
	public void scheduleRemoveTaskTest() {
		long ttl = 0;
		TimeUnit timeUnit = Mockito.mock(TimeUnit.class);
		dataAccesssor.put(key, ARBITRARY_BUFFER, ttl, timeUnit);
		Mockito.verify(clearOldElTheadPool, Mockito.never()).schedule(Mockito.any(Runnable.class), Mockito.eq(ttl), Mockito.any(TimeUnit.class));
		
		ttl = 1;	
		timeUnit = TimeUnit.MILLISECONDS;
		dataAccesssor.put(key, ARBITRARY_BUFFER, ttl, timeUnit);
		Mockito.verify(clearOldElTheadPool).schedule(Mockito.any(Runnable.class), Mockito.eq(ttl), Mockito.eq(timeUnit));
	}
	
	@Test(expected = NullPointerException.class)
	public void nullTimeUnitInPutMethodTest() {
		long ttl = 1;
		TimeUnit timeUnit = null;
		dataAccesssor.put(key, ARBITRARY_BUFFER, ttl, timeUnit);
	}

}
