package com.smithson.jmcclient.io.constant;

import java.util.HashMap;
import java.util.Map;

public enum Status {
	UNKNOWN(-1), NO_ERROR(0), ADDED(1), REPLACED(2), FOUND(3), REMOVED(4), CLEARED(5), CLOSED(50), ERROR(
			100), NOT_SUPPORTED_PROTOCOL(101), NOT_FOUND(102), COMMAND_NOT_SUPPORTED(103), POSSIBLE_DATA_CORRUPTION(
					104), WRONG_SIZE_VALUE(105), BAD_TIME_UNIT(106), BAD_VERSION_VALUE(107), CONNECTION_REFUSED(108), NEGATIVE_TIME(109);
	private byte id;

	private final static Map<Byte, Status> idStatusMap = new HashMap<>();
	static {
		for (Status status : Status.values()) {
			idStatusMap.put(status.getStatusID(), status);
		}
	}

	private Status(int id) {
		this.id = (byte) id;
	}

	public byte getStatusID() {
		return (byte) id;
	}

	public static Status getStatusByID(byte id) {
		Status status = idStatusMap.get(id);
		return status == null ? UNKNOWN : status;
	}
}
