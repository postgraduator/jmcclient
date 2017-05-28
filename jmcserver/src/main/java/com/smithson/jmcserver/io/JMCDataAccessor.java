package com.smithson.jmcserver.io;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcserver.io.constant.Status;

public interface JMCDataAccessor {
	Status put(ByteBuffer key, ByteBuffer content, long ttl, TimeUnit timeUnit);
	ByteBuffer get(ByteBuffer key);
	Status remove(ByteBuffer key);
	Status clear();
}
