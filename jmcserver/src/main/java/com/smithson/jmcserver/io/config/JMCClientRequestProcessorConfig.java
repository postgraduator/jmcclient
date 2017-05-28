package com.smithson.jmcserver.io.config;

import com.smithson.jmcserver.io.ErrorMessageCreator;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.Response;

public interface JMCClientRequestProcessorConfig {
	JMCDataAccessor getJMCDataAccessor();
	RequestDataExtractor getRequestDataExtractor();
	Response getResponse();
	ErrorMessageCreator getErrorMessageCreator();
}
