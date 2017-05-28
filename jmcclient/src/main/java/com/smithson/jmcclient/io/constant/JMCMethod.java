package com.smithson.jmcclient.io.constant;

import java.util.HashMap;
import java.util.Map;

public enum JMCMethod {
	PUT(0), GET(1), REMOVE(2), CLEAR(3), NO_METHOD(-1), CLOSE_CONNECTION(100);
	private int id;

	private JMCMethod(int id) {
		this.id = id;
	}

	private static Map<Integer, JMCMethod> idMethodMap = new HashMap<>();
	static {
		for (JMCMethod method : JMCMethod.values()) {
			idMethodMap.put(Integer.valueOf(method.getMethodID()), method);
		}
	}

	public static JMCMethod getMethodByID(int id) {
		JMCMethod method = idMethodMap.get(id);
		return method != null ? method : NO_METHOD;
	}

	public int getMethodID() {
		return id;
	}
}
