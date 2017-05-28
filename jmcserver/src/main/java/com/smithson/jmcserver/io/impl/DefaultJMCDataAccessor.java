package com.smithson.jmcserver.io.impl;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.constant.Constants;
import com.smithson.jmcserver.io.constant.Status;

class DefaultJMCDataAccessor implements JMCDataAccessor {
	private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOG");
	private final ConcurrentMap<ByteBuffer, ByteBuffer> cache;
	private final ScheduledExecutorService clearOldElThreadPool;
	
	public DefaultJMCDataAccessor(ConcurrentMap<ByteBuffer, ByteBuffer> cache, ScheduledExecutorService clearOldElThreadPool) {
		super();
		this.cache = cache;
		this.clearOldElThreadPool = clearOldElThreadPool;
	}

	private void submitTempContentRemoveTask(ByteBuffer key, long ttl, TimeUnit timeUnit) {
		if (ttl > 0) {
			Objects.requireNonNull(timeUnit, "The passed time unit is null.");
			clearOldElThreadPool.schedule(new Runnable() {
				@Override
				public void run() {
					cache.remove(key);
				}
			}, ttl, timeUnit);
			ACCESS_LOGGER.info("The added content will be removed in {}, {}", ttl,
					timeUnit);
		}
	}
	
	@Override
	public Status put(final ByteBuffer key, final ByteBuffer content, long ttl, TimeUnit timeUnit) {
		submitTempContentRemoveTask(key, ttl, timeUnit);
		if(cache.replace(key, content) == null) {
			cache.putIfAbsent(key, content);
			return Status.ADDED;
		} else {
			return Status.REPLACED;
		}
	}

	@Override
	public ByteBuffer get(ByteBuffer key) {
		ByteBuffer content = cache.get(key);
		if(content != null) {
			return content;
		}
		return Constants.EMPTY_BUFFER;
	}

	@Override
	public Status remove(ByteBuffer key) {
		ByteBuffer removed = cache.remove(key); 
		if( removed != null) {			
			return Status.REMOVED;
		}
		return Status.NOT_FOUND;
	}

	@Override
	public Status clear() {
		cache.clear();
		return Status.CLEARED;
	}

	

}
