package org.wuqispank.model;

public class DefaultBaseTable implements IBaseTable {
	private String m_name = ISqlModel.NOT_SPECIFIED;

	public DefaultBaseTable() {
		super();
	}

	@Override
	public String getName() {
		return m_name.toLowerCase().trim();
	}

	@Override
	public void setName(String name) {
		this.m_name = name;
	}

	@Override
	public boolean equivalent(ITable val) {
		if (this.getName().equals(val.getName()))
			return true;
		else 
			return false;
	}

}