package com.smithson.jmcserver.io;

import java.io.IOException;
import java.io.InputStream;

public interface RequestDataExtractor {
	InvokedRequestData extract() throws IOException;
	InputStream getInputStream();
}
