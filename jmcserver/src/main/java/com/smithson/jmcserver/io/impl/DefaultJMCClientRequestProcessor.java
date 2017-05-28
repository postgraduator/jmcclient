package com.smithson.jmcserver.io.impl;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smithson.jmcserver.io.InvokedRequestData;
import com.smithson.jmcserver.io.JMCClientRequestProcessor;
import com.smithson.jmcserver.io.JMCDataAccessor;
import com.smithson.jmcserver.io.RequestDataExtractor;
import com.smithson.jmcserver.io.Response;
import com.smithson.jmcserver.io.config.JMCClientRequestProcessorConfig;
import com.smithson.jmcserver.io.constant.JMCMethod;
import com.smithson.jmcserver.io.constant.ProtocolInfo;
import com.smithson.jmcserver.io.constant.Status;
import com.smithson.jmcserver.io.exception.JMCServerException;

class DefaultJMCClientRequestProcessor implements JMCClientRequestProcessor {
	private static final Logger ACCESS_LOGGER = LoggerFactory.getLogger("ACCESS_LOG");
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultJMCClientRequestProcessor.class);

	private final JMCClientRequestProcessorConfig config;
	private boolean keepConnection;

	public DefaultJMCClientRequestProcessor(JMCClientRequestProcessorConfig config) {
		super();
		this.config = config;
	}

	@Override
	public void run() {
		try {
			do {
				try {
					service();
				} catch (JMCServerException e) {
					exceptionHandler(e);
					break;
				}
			} while (keepConnection);
		} catch (IOException e) {
			throw new JMCServerException("Connection error", Status.ERROR);
		} finally {
			try {
				destroy();
			} catch (IOException e) {
				LOGGER.error("IO error was encountered during client request proceesor closing.");
			}
		}

	}

	@Override
	public void service() throws IOException {
		RequestDataExtractor req = config.getRequestDataExtractor();
		Response res = config.getResponse();
		InvokedRequestData clientReqData = req.extract();
		JMCDataAccessor accessor = config.getJMCDataAccessor();
		keepConnection = !clientReqData.getMethod().equals(JMCMethod.CLOSE_CONNECTION);
		Status resStatus = res.writeResponse(clientReqData, accessor, clientReqData.getProtocolInfo());
		ACCESS_LOGGER.info("Request: protocol {}, method {}. Response status {}.", clientReqData.getProtocolInfo(),
				clientReqData.getMethod(), resStatus);
	}

	@Override
	public void destroy() throws IOException {
		config.getRequestDataExtractor().getInputStream().close();
		config.getResponse().getOutputStream().flush();
		config.getResponse().getOutputStream().close();
	}

	protected void exceptionHandler(JMCServerException error) throws IOException {
		ByteBuffer errorMessage = config.getErrorMessageCreator().createErrorMessage(ProtocolInfo.VERSION_1_0, error.getStatus());
		LOGGER.warn("The last request can not be processed. Response about error with status {} was sent. {}",
				error.getStatus(), error.getMessage(), error);
		config.getResponse().getOutputStream().write(errorMessage.array());
		keepConnection = false;
	}

}
