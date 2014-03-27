package org.wuqispank.web;


public class AuthenticatedWebPage extends BasePage {

	/**
	 * Contruct
	 */
	public AuthenticatedWebPage()
	{

	}


	/**
	 * Get downcast session object
	 * 
	 * @return The session
	 */
	public WebSession getWebSession()
	{
		return (WebSession)getSession();
	}

}
