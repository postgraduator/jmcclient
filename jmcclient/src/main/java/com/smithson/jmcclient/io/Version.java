package com.smithson.jmcclient.io;

import com.smithson.jmcclient.io.exception.BadVersionException;

public class Version {
	private final byte mainVer;
	private final byte subVer;

	public Version(byte ver) {
		if (ver <= 0) {
			throw new BadVersionException(
					"The full version value must be more than zero (the passed value " + ver + ").");
		}
		this.mainVer = (byte) ((int) ver >> 4);
		this.subVer = (byte) (ver - ((int) mainVer << 4));
	}

	public Version(byte mainVer, byte subVer) {
		if (mainVer < 0 || subVer < 0 || (mainVer == 0 && subVer == 0)) {
			throw new BadVersionException(
					"The main or sub version value must be more than zero (the passed values: main= " + mainVer + "sub="
							+ subVer + ").");
		}
		this.mainVer = mainVer;
		this.subVer = subVer;

	}

	public byte getMainVer() {
		return mainVer;
	}

	public byte getSubVer() {
		return subVer;
	}

	public byte getFullVersion() {
		return (byte) (((int) mainVer << 4) + this.subVer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mainVer;
		result = prime * result + subVer;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (mainVer != other.mainVer)
			return false;
		if (subVer != other.subVer)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("version %s.%s", mainVer, subVer);
	}
}
