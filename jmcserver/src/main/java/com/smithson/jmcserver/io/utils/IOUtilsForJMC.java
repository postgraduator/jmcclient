package com.smithson.jmcserver.io.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;

public final class IOUtilsForJMC {

	private IOUtilsForJMC() {}
	public final static int BUFFER_SIZE = 1024 * 4;
	private static int newBufferSizeCalc(int oldLength) {
		long newBufferedDataLength = 2L * oldLength;
		int bufferedSize = Integer.MAX_VALUE;
		if(newBufferedDataLength < Integer.MAX_VALUE) {
			bufferedSize = (int) newBufferedDataLength;
		}
		return bufferedSize;
	}
	private static byte[] expandBuffer(final long readBytes, byte[] bufferedData) {
		if(readBytes > Integer.MAX_VALUE) {
			throw new BufferOverflowException();
		} else if (readBytes > bufferedData.length) {
			int bufferedSize = newBufferSizeCalc(bufferedData.length);
			byte temp[] = bufferedData;
			bufferedData = new byte[bufferedSize]; 
			System.arraycopy(temp, 0, bufferedData, 0, temp.length);
		}
		return bufferedData;
	}
	public static byte[] toByteArray(InputStream in, long countOfBytes) throws IOException {
		byte[] buffer;
		byte[] bufferedData;
		if (countOfBytes < BUFFER_SIZE) {
			buffer = new byte[(int) countOfBytes];
			bufferedData = new byte[(int) countOfBytes];
		} else {
			buffer = new byte[BUFFER_SIZE];
			bufferedData = new byte[BUFFER_SIZE];	
		}
		long readBytes = 0;
		int count = 0;
		while(countOfBytes > 0 && (count = in.read(buffer)) != -1) {
			readBytes += count;
			countOfBytes -= readBytes;
			try {
				bufferedData = expandBuffer(readBytes, bufferedData);
			} catch (BufferOverflowException e) {
				throw new IOException(e);
			}
			System.arraycopy(buffer, 0, bufferedData, (int)readBytes - count, count);
		}
		byte[] temp = bufferedData;
		bufferedData = new byte[(int)readBytes];
		System.arraycopy(temp, 0, bufferedData, 0, (int)readBytes);
		return bufferedData;
	}

}
