package org.wuqispank.jdbc;

import org.headlessintrace.jdbc.IJdbcProvider;
import org.wuqispank.web.IConfig;

public class WebXmlConfigurableProvider implements IJdbcProvider {

	private IConfig m_config;

	IConfig getConfig() {
		return m_config;
	}
	public void setConfig(IConfig val) {
		m_config = val;
	}
	@Override
	public String[] getStatementPackageAndClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getConnectionPackageAndClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

}
