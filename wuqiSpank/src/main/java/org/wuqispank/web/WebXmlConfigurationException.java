package org.wuqispank.web;

public class WebXmlConfigurationException extends RuntimeException {

	public WebXmlConfigurationException(String invalidAgentPortNumber) {
		super(invalidAgentPortNumber);
	}

}
