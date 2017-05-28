package com.smithson.jmcserver.io.constant;

import static com.smithson.jmcserver.io.constant.Constants.IMPL_METHODS_V_1_0;
import static com.smithson.jmcserver.io.constant.Constants.NO_METHOD;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smithson.jmcserver.io.Version;
import com.smithson.jmcserver.io.method.MethodProcessor;

public enum ProtocolInfo {
	VERSION_1_0(new Version((byte) 1, (byte) 0), Byte.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
			IMPL_METHODS_V_1_0), 
	UNSUPORTED(null, 0, 0, 0, Collections.<MethodProcessor>emptyList());
	private final Version ver;
	private final Integer maxKeySize;
	private final Long maxContentSize;
	private final Long maxTimePeriod;
	private final List<MethodProcessor> supportedMethods;
	private final Map<JMCMethod, MethodProcessor> methodMap;

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

	private ProtocolInfo(Version ver, int maxKeySize, long maxTimePeriod, long maxContentSize, List<MethodProcessor> supportedMethods) {
		this.ver = ver;
		this.maxKeySize = maxKeySize;
		this.maxTimePeriod = maxTimePeriod;
		this.maxContentSize = maxContentSize;
		this.supportedMethods = supportedMethods;
		this.methodMap = new HashMap<>();
		for(MethodProcessor processor: supportedMethods) {
			methodMap.put(processor.method, processor);
		}
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

	public List<MethodProcessor> getSupportedJMCMethods() {
		return supportedMethods;
	}
	
	public MethodProcessor getMethodProcessor(JMCMethod method) {
		MethodProcessor processor = methodMap.get(method);
		return processor != null? processor: NO_METHOD;
	}
}
