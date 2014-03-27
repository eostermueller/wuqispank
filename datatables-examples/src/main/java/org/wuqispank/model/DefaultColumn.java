package org.wuqispank.model;


public class DefaultColumn implements IColumn, java.io.Serializable {
	private String m_name = ISqlModel.NOT_SPECIFIED;
	private String m_tableAlias = ISqlModel.NOT_SPECIFIED;
	
	@Override
	public boolean isDetached() {
		return getTable()==null; 
	}
	private ITable m_table = null;
	@Override
	public ITable getTable() {
		return m_table;
	}
	@Override
	public void setTable(ITable table) {
		this.m_table = table;
	}
	@Override
	public String getTableAlias() {
		return m_tableAlias;
	}
	@Override
	public void setTableAlias(String tableAlias) {
		this.m_tableAlias = tableAlias;
	}
	
	@Override
	public String getName() {
		return m_name.toLowerCase().trim();
	}
	@Override
	public void setName(String val) {
		m_name = val;
	}
	@Override
	public boolean equals(String columnCriteria, String tableNameCriteria) {
		boolean rc = false;
		if (
					(columnCriteria==null && getName()==null)
				|| 	columnCriteria.toLowerCase().trim().equals(getName())
			)
			{
				if (
						(tableNameCriteria==null && getTable()==null)
						|| tableNameCriteria.toLowerCase().trim().equals(getTable().getName())
						) {
							rc = true;
						}
			}
		return rc;
	}
}
