package org.wuqispank.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;


public class DefaultTable extends DefaultBaseTable implements ITable, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3406840702165705332L;

	private static final Logger log = LoggerFactory.getLogger(DefaultTable.class);
	
	private List<IColumn> m_whereClauseColumns = new ArrayList<IColumn>();
	private String m_schema = ISqlModel.NOT_SPECIFIED;
	private String m_alias = ISqlModel.NOT_SPECIFIED;
	private boolean m_shouldBeCached = false;
	private boolean m_isGrowthTable = false;
	
	
	public DefaultTable(String tableName) {
		setName(tableName);
	}
	public DefaultTable() {
	}

	@Override
	public String getAlias() {
		if (m_alias!=null)
			return m_alias.toLowerCase().trim();
		else 
			return null;
	}

	@Override
	public void setAlias(String alias) {
		this.m_alias = alias;
	}

	@Override
	public String getSchema() {
		return m_schema;
	}

	@Override
	public void setSchema(String schema) {
		this.m_schema = schema;
	}

	public String toString() {
		return getName();
	}

//	@Override
//	public void addColumn(IColumn c) {
//		IColumn c = findColumn(columnName);
//		if (c==null) {
//			c = DefaultFactory.getFactory().getColumn();
//			c.setName(columnName);
//			getColumns().add(c);
//		}
//	}

	
	private List<IColumn> getWhereClauseColumns() {
		return m_whereClauseColumns;
	}
	@Override
	public int getWhereClauseColumnCount() {
		return getWhereClauseColumns().size();
	}
	@Override
	public IColumn getColumn(int val) {
		return getWhereClauseColumns().get(val);
	}
	@Override
	public void addColumn(IColumn val) {
		getWhereClauseColumns().add(val);
	}

	@Override
	public IColumn findColumn(String nameCriteria) {
		IColumn rc = null;
		for(IColumn c : getWhereClauseColumns()) {
			log.debug("Looking for column [" + nameCriteria + "] in table [" + getName() + "] comparing to column [" + c.getName() + "]");
			if (c.getName()!=null && nameCriteria.toLowerCase().equals(c.getName().toLowerCase())) {
				rc = c;
				break;
			}
		}
		log.debug("findColumn() returning [" + ( (rc==null) ? "<column not found>" : rc.getName()) + "]");
		return rc;
	}
	@Override
	public boolean shouldBeCached() {
		return this.m_shouldBeCached;
	}
	@Override
	public void setShouldBeCached(boolean val) {
		this.m_shouldBeCached = val;
	}
	@Override
	public void setGrowthTable(boolean val) {
		this.m_isGrowthTable = val;
	}
	@Override
	public boolean isGrowthTable() {
		return m_isGrowthTable;
	}
	@Override
	public int compareTo(ITable o) {
		return this.getName().compareToIgnoreCase(o.getName());
	}
	
}
