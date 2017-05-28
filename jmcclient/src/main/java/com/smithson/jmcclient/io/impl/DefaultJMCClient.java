package com.smithson.jmcclient.io.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.smithson.jmcclient.io.DataPreparing;
import com.smithson.jmcclient.io.JMCClient;
import com.smithson.jmcclient.io.Version;
import com.smithson.jmcclient.io.constant.JMCMethod;
import com.smithson.jmcclient.io.constant.ProtocolInfo;
import com.smithson.jmcclient.io.constant.Status;
import com.smithson.jmcclient.io.utils.DataUtils;
import com.smithson.jmcclient.io.utils.IOUtilsForJMC;
import com.smithson.jmcclient.io.utils.Validators;

class DefaultJMCClient implements JMCClient {
	private final Socket socket;
	private final DataPreparing<String> maker;

	public DefaultJMCClient(String serverName, int serverPort, DataPreparing<String> maker)
			throws UnknownHostException, IOException {
		super();
		socket = new Socket(serverName, serverPort);
		socket.setKeepAlive(true);
		this.maker = maker;
	}

	@Override
	public void close() throws IOException {
		socket.getOutputStream().write(maker.createRequestHead(JMCMethod.CLOSE_CONNECTION));
		socket.close();
	}

	protected byte[] preparePut(String key, Object obj, Integer ttl, TimeUnit timeUnit) throws IOException {
		byte[] head = maker.createRequestHead(JMCMethod.PUT);
		byte[] byteKey = maker.prepareKeyToRequest(key);
		byte[] time = maker.prepareTimeToRequest(ttl, timeUnit);
		byte[] content = maker.prepareObjectToRequest(obj);
		return DataUtils.mergeArrays(head, byteKey, time, content);
	}

	protected Status validateVersionAndGetStatus() throws IOException {
		byte[] head = IOUtilsForJMC.toByteArray(socket.getInputStream(), 2);
		Validators.supportedVersionValidator(ProtocolInfo.getProtocolInfoByVersion(new Version(head[0])),
				maker.getProtocolInfo());
		return Status.getStatusByID(head[1]);
	}

	@Override
	public Status put(String key, Object obj) throws IOException {
		return put(key, obj, 0, TimeUnit.MICROSECONDS);
	}

	@Override
	public Status put(String key, Object obj, Integer ttl, TimeUnit timeUnit) throws IOException {
		socket.getOutputStream().write(preparePut(key, obj, ttl, timeUnit));
		return validateVersionAndGetStatus();
	}

	protected Status sendHeadKey(String key, JMCMethod method) throws IOException {
		byte[] head = maker.createRequestHead(method);
		byte[] byteKey = maker.prepareKeyToRequest(key);
		socket.getOutputStream().write(DataUtils.mergeArrays(head, byteKey));
		return validateVersionAndGetStatus();
	}

	@Override
	public <T> T get(String key) throws IOException, ClassNotFoundException {
		Status statusOfRequestedObj = sendHeadKey(key, JMCMethod.GET);
		if (statusOfRequestedObj.equals(Status.NOT_FOUND)) {
			return null;
		} else {
			long conSize = DataUtils.getValueFromInput(socket.getInputStream(),
					(int) maker.getProtocolInfo().byteCountForContentSize());
			byte[] content = IOUtilsForJMC.toByteArray(socket.getInputStream(), conSize);
			Validators.dataIntegrityValidator(content.length, conSize,
					"Possible data corruprion was encountred when request GET was sent");
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(content));
			@SuppressWarnings("unchecked")
			T obj = (T) in.readObject();
			in.close();
			return obj;
		}
	}

	@Override
	public Status remove(String key) throws IOException {
		return sendHeadKey(key, JMCMethod.REMOVE);
	}

	@Override
	public Status clear() throws IOException {
		byte[] head = maker.createRequestHead(JMCMethod.CLEAR);
		socket.getOutputStream().write(head);
		return validateVersionAndGetStatus();
	}

}
