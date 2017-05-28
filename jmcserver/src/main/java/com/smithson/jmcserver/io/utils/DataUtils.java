package com.smithson.jmcserver.io.utils;

import java.io.IOException;
import java.io.InputStream;

public class DataUtils {
	private DataUtils() {}
	public static byte[] mergeArrays(byte[]...array) {
		int size = 0;
		for (int i = 0; i < array.length; i++) {
			size += array[i].length;
		}
		
		byte[] result  = new byte[size];
		int destPos = 0;
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, result, destPos, array[i].length);
			destPos += array[i].length;
		}
		
		return result;		
	}
	
	public static Object[] mergeArrays(Object[]...array) {
		int size = 0;
		for (int i = 0; i < array.length; i++) {
			size += array[i].length;
		}
		
		Object[] result  = new Object[size];
		int destPos = 0;
		for (int i = 0; i < array.length; i++) {
			System.arraycopy(array[i], 0, result, destPos, array[i].length);
			destPos += array[i].length;
		}
		
		return result;		
	}
	
	public static byte[] valToByteArray(long val, byte countOfBytes) {
		byte[] result = new byte[countOfBytes];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (val >>> (8 * i)); 
		}
		return result;
	}
	
	public static long getValueFromInput(InputStream in, int countOfBytes) throws IOException {
		byte[] buffer = new byte[countOfBytes];
		int readBytes = in.read(buffer);
		long result;
		if (readBytes == countOfBytes) {
			result = 0;
			for (int i = 0; i < buffer.length; i++) {
				if(buffer[i] >= 0) {
					result += buffer[i] << (i * 8);
				} else {
					result += (256 + buffer[i]) << (i * 8); 
				}
			}
		} else {
			result = 0;
		}

		return result;
	}
}
