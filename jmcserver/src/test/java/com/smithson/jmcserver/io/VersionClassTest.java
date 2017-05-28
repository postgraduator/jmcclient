package com.smithson.jmcserver.io;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.smithson.jmcserver.io.Version;
import com.smithson.jmcserver.io.exception.BadVersionException;


@RunWith(Enclosed.class)
public class VersionClassTest {

	@RunWith(Parameterized.class)
	public static class VersionTest {
		private final byte fullVer;
		private final byte mainVer;
		private final byte subVer;

		public VersionTest(byte fullVer, byte mainVer, byte subVer) {
			this.fullVer = fullVer;
			this.mainVer = mainVer;
			this.subVer = subVer;
		}

		@Test
		public void versionTest() {
			Version verFull = new Version(fullVer);
			Version verMain = new Version(mainVer, subVer);
			assertEquals(fullVer, verFull.getFullVersion());
			assertEquals(mainVer, verFull.getMainVer());
			assertEquals(subVer, verFull.getSubVer());
			assertEquals(verFull, verMain);
		}

		@Parameterized.Parameters
		public static List<Byte[]> getParameters() {
			return Arrays.asList(new Byte[][] { new Byte[] { 17, 1, 1 }, new Byte[] { 37, 2, 5 } });
		}
	}
	
	@RunWith(Parameterized.class)
	public static class ExceptionTest {
		private byte ver;
		private byte mainVer;
		private byte subVer;
		public ExceptionTest(byte ver, byte mainVer, byte subVer) {
			this.ver = ver;
			this.mainVer = mainVer;
			this.subVer = subVer;
		}
		
		@Test(expected = BadVersionException.class)
		public void fullVersionExceptionTest() {
			new Version(ver);
		}
		
		@Test(expected = BadVersionException.class)
		public void versionExceptionTest() {
			new Version(mainVer, subVer);
		}
		
		@Parameterized.Parameters
		public static List<Object[]> getParameters() {
			return Arrays.asList(new Object[][] {
				new Byte[] {0, 0, 0},
				new Byte[] {-1, -1, 0},
				new Byte[] {-2, 0, -1},
				new Byte[] {-2, -1, -1}
			});
		}
	}
}
