package com.smithson.jmcserver.io.impl;

import com.smithson.jmcserver.io.ErrorMessageCreator;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.Response;
import com.smithson.jmcserver.io.config.JMCClientRequestProcessorConfig;

class DefaultJMCClientRequestProcessorConfig implements JMCClientRequestProcessorConfig {
	private final JMCDataAccessor accessor;
	private final RequestDataExtractor  req;
	private final Response res;
	private final ErrorMessageCreator errorMessageCreator;
	
	public DefaultJMCClientRequestProcessorConfig(JMCDataAccessor accessor, RequestDataExtractor req, Response res, ErrorMessageCreator errorMessageCreator) {
		super();
		this.accessor = accessor;
		this.req = req;
		this.res = res;
		this.errorMessageCreator = errorMessageCreator;
	}

	@Override
	public JMCDataAccessor getJMCDataAccessor() {
		return accessor;
	}

	@Override
	public RequestDataExtractor getRequestDataExtractor() {
		return req;
	}

	@Override
	public Response getResponse() {
		return res;
	}

	@Override
	public ErrorMessageCreator getErrorMessageCreator() {
		return errorMessageCreator;
	}

}
