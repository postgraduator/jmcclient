package com.smithson.jmcclient.io;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcclient.io.constant.Status;

public interface JMCClient extends AutoCloseable{
	Status put(String key, Object obj) throws IOException;
	Status put(String key, Object obj, Integer ttl, TimeUnit timeUnit) throws IOException;
	<T> T get(String key) throws IOException, ClassNotFoundException;
	Status remove(String key) throws IOException;
	Status clear() throws IOException;
}
