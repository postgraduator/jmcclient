package com.smithson.jmcclient.io.constant;

import static com.smithson.jmcclient.io.constant.Constants.IMPL_METHODS_V_1_0;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smithson.jmcclient.io.Version;

public enum ProtocolInfo {
	VERSION_1_0(new Version((byte) 1, (byte) 0), Byte.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
			IMPL_METHODS_V_1_0), 
	UNSUPORTED(null, 0, 0, 0, Collections.<JMCMethod>emptyList());
	private final Version ver;
	private final Integer maxKeySize;
	private final Long maxContentSize;
	private final Long maxTimePeriod;
	private final List<JMCMethod> supportedMethods;


	private final static Map<Version, ProtocolInfo> VERSION_PROTOCOL_MAP = new HashMap<Version, ProtocolInfo>();

	static {
		for (ProtocolInfo protocol : ProtocolInfo.values()) {
			VERSION_PROTOCOL_MAP.put(protocol.getVersion(), protocol);
		}
	}

	public static ProtocolInfo getProtocolInfoByVersion(Version ver) {
		ProtocolInfo protocol = VERSION_PROTOCOL_MAP.get(ver);
		return protocol != null ? protocol : UNSUPORTED;
	}

	private ProtocolInfo(Version ver, int maxKeySize, long maxTimePeriod, long maxContentSize, List<JMCMethod> supportedMethods) {
		this.ver = ver;
		this.maxKeySize = maxKeySize;
		this.maxTimePeriod = maxTimePeriod;
		this.maxContentSize = maxContentSize;
		this.supportedMethods = supportedMethods;
	}

	public Version getVersion() {
		return ver;
	}

	public Integer getMaxKeySize() {
		return maxKeySize;
	};
	
	private Byte countOfByteCalculator(long value) {
		long maxStoredVal = value;
		byte count = 0;
		do {
			++count;
		} while((maxStoredVal >>> (8 * count)) > 0);
		return count;
	}
	
	public Byte byteCountForKey() {
		return countOfByteCalculator(getMaxKeySize());
	}

	public Long getMaxContentSize() {
		return maxContentSize;
	};
	
	public Byte byteCountForContentSize() {
		return countOfByteCalculator(getMaxContentSize());
	}
	
	public Byte byteCountForTimePeriod() {
		return countOfByteCalculator(getMaxTimePeriod());
	}

	public long getMaxTimePeriod() {
		return maxTimePeriod;
	}

	public List<JMCMethod> getSupportedJMCMethods() {
		return supportedMethods;
	}
}
