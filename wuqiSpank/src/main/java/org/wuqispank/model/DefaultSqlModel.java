package org.wuqispank.model;

import java.awt.image.ImageObserver;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Observer;

import org.headlessintrace.client.request.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;

public class DefaultSqlModel  implements ISqlModel, java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(DefaultSqlModel.class);
	private static final String TABLE_LIST_DELIMETER = ",";
	private static final Object TABLE_DELIMETER_FOR_KEY = "~";
	private int m_columnCount;
	private List<ITable> m_tables = new ArrayList<ITable>();
	private IRequest m_request;
	private List<IColumn> m_selectListColumns = new ArrayList<IColumn>();
	private List<IBinaryOperatorExpression> m_binaryOperatorExpressions = new ArrayList<IBinaryOperatorExpression>();
	private IModelObservationMgr m_observationMgr = null;
	private SqlType m_sqlType;
	private Throwable m_parseException;
	private Class sqlParser = null;
	private String key = null;
	

	private IModelObservationMgr getObservationMgr() {
		
		if (m_observationMgr==null) {
			m_observationMgr = new IModelObservationMgr() {
				
				@Override public void registerNewTableListener(Observer sqlStatsCounter) {}
				@Override public void registerNewSqlListener(ISqlStatsObserver sqlStatsCounter) {}
				@Override public void registerNewJoinListener(Observer sqlStatsCounter) {}
				@Override public void addNewTable(ITable table) {}
				@Override public void addNewSql() {}
				@Override public void addNewJoin(IBinaryOperatorExpression expression) {}
			};
		}
		return m_observationMgr;
	}

	@Override
	public String getKey() {
		if (this.key==null) {
			List<ITable> list = this.getSortedTables();
			StringBuilder sb = new StringBuilder();
			sb.append( getSqlType() ).append(".");
			int count = 0;
			
			for(ITable t : list) {
				if (count++>0) sb.append(TABLE_DELIMETER_FOR_KEY);
				sb.append(t.getName());
			}
			this.key = sb.toString();
		}
		return this.key;
		
	}
	@Override
	public void postProcess() {
		resolveColumnAliases();
	}
	
	@Override
	public ITable findTable(ITable criteria) {
		ITable matchingTable = null;
		for(ITable tab : this.getTables()) {
			if (tab.compareTo(criteria)==0) {
				matchingTable = tab;
				break;
			}
		}
		return matchingTable;
	}
	@Override
	public void addTable(ITable val) {
		
		ITable foundMatch = this.findTable(val);
		if (foundMatch==null) {
			getObservationMgr().addNewTable(val);
			getTables().add(val);
		}
	}
	@Override
	public ITable getTable(int val) {
		return getTables().get(val);
	}
	/**
	 * Start with list of columns (mostly) unassociated with any table.
	 * Then, if a column was assigned to a table alias (as in \"a.myColumn\" or \"foo.yourColumn\"), 
	 * then associate that column with the corresponding table.
	 */
    private void resolveColumnAliases() {
		for(IColumn c : getSelectListColumns()) {
			
			if (c.getTableAlias()!=null) {
				ITable t = findTableGivenAlias(c.getTableAlias());
				if (t!=null) {
					c.setTable(t);
					t.addColumn(c);
				}
			}
		}
		
		for(IBinaryOperatorExpression boe : getBinaryOperatorExpressions() ) {
			
			IColumn c = boe.getLeftColumn();
			ITable t = findTableGivenAlias(c.getTableAlias());
			if (t!=null) {
				c.setTable(t);
				t.addColumn(c);
			}
			
			c = boe.getRightColumn();
			if (c!=null) {
				t = findTableGivenAlias(c.getTableAlias());
				if (t!=null) {
					c.setTable(t);
					t.addColumn(c);
				}
			}
		}
	}

	@Override
	public List<IColumn> getSelectListColumns() {
		return m_selectListColumns;
	}

	private void setSelectListColumns(List<IColumn> columns) {
		this.m_selectListColumns = columns;
	}

	public int getTableCount() {
		return getTables().size();
	}

	public int getColumnCount() {
		return getSelectListColumns().size();
	}

	public void setColumnCount(int columnCount) {
		this.m_columnCount = columnCount;
	}
	@Override
	public String getHumanReadableTableNames() {
		StringBuffer sb = new StringBuffer();
		int count = 0;
		for(ITable table : getTables() ) {
			if (count++>0) sb.append(TABLE_LIST_DELIMETER);
			sb.append(table.getName() );
		}
		
		return sb.toString();
	}
	@Override
	public List<ITable> getTables() {
		return m_tables;
	}
	/**
	 * The parser hasn\"t done to work to associate the column with the table that owns it.
	 * At best, the parser provides the alias for the table.  
	 * \"a\" and \"b\", below, are table aliases.
	 * <PRE>
	 *   select a.PRODUCT_DESCR, b.PRODUCT_ID from PRODUCTS a, ORDERS b where .....
	 * </PRE>
	 *  
	 */
	@Override
	public void addSelectListColumn(String tableAlias, String columnName) {
		log.debug("Adding tab.col [" + tableAlias + "." + columnName + "]");
		IColumn c = DefaultFactory.getFactory().getColumn();
		c.setName(columnName);
		c.setTableAlias(tableAlias);
		getSelectListColumns().add(c);
	}
	@Override
	public IColumn addWhereClauseColumn(String tableAlias, String columnName) {
		log.debug("Adding where clause tab.col [" + tableAlias + "." + columnName + "]");
		
		IColumn rc = findSelectListColumn(columnName, tableAlias);
		if (rc == null) {
			rc = DefaultFactory.getFactory().getColumn();
			rc.setName(columnName);
			rc.setTableAlias(tableAlias);
			ITable t = null;
			t = findTableGivenAlias(tableAlias);
			if (t==null) {
				if (getTables().size()==1) {
					t = getTables().get(0);
				} 
//				else {
//					t = DefaultFactory.getFactory().getTable(null);//this.resolveColumnAliases() will be called later to find this table name.
//					t.setAlias(tableAlias); 
//				}
			}
			
			if (t!=null) {
				rc.setTable(t);
				t.addColumn(rc);
			}
		}
		if (!rc.validate())
			rc = null;
		return rc;
	}
	

	@Override
	public ITable findTableGivenAlias(String aliasCriteria) {
		if (aliasCriteria ==null) 
			return null;
		else
			aliasCriteria = aliasCriteria.toLowerCase();
		
		ITable rc = null;
		for(ITable t : getTables()) {
			if (aliasCriteria.equals( t.getName() )) {
				rc = t;
				break;
			}
			if (t==null || t.getAlias()==null || t.getAlias().equals(ISqlModel.NOT_SPECIFIED))
				continue;
			log.debug("Looking for table [" + aliasCriteria + "] comparing to table [" + t.getAlias() + "]");
			if (aliasCriteria.equals(t.getAlias().toLowerCase())) {
				rc = t;
				break;
			}
		}
		log.debug("findTable() returning [" + ( (rc==null) ? "<table not found>" : (rc.getName() + "] for alias [" + aliasCriteria + "]")) );
		return rc;
	}
	
	@Override
	public ITable findTable(String nameCriteria) {
		if (nameCriteria ==null) 
			return null;
		
		ITable rc = null;
		for(ITable t : getTables()) {
			if (t==null || t.getName().equals(ISqlModel.NOT_SPECIFIED) || t.getName()==null)
				continue;
			log.debug("Looking for table [" + nameCriteria + "] comparing to table [" + t.getName() + "]");
			if (nameCriteria.toLowerCase().equals(t.getName().toLowerCase())) {
				rc = t;
				break;
			}
		}
		log.debug("findTable() returning [" + ( (rc==null) ? "<table not found>" : rc.getName()) + "]");
		return rc;
	}

	@Override
	public void setTables(List<ITable> t) {
		m_tables = t;
	}
	private List<IBinaryOperatorExpression> getBinaryOperatorExpressions() {
		return m_binaryOperatorExpressions;
	}
	@Override
	public void addBinaryOperatorExpression(IBinaryOperatorExpression val) {
		getObservationMgr().addNewJoin(val);
		getBinaryOperatorExpressions().add(val);
	}
	@Override
	public IColumn findSelectListColumn(String columnNameCriteria, String tableAlias) {
		IColumn result = null;
		ITable table = this.findTableGivenAlias(tableAlias);
		if (table != null) {
			result = table.findColumn(columnNameCriteria);
			if (result==null) {
				result = findDetachedColumn(columnNameCriteria);
				if (result!=null) {
					result.setTable(table);
					table.addColumn(result);
				}	
			}
		}
		return result;
	}
	/**
	 * Find any columns in the "Select list" that haven\"t already been linked to a table.
	 * 
	 * @param columnNameCriteria
	 * @return
	 */
	private IColumn findDetachedColumn(String columnNameCriteria) {
		for(IColumn c : getSelectListColumns() ) {
			if (columnNameCriteria.toLowerCase().equals(c.getName())
					&& c.isDetached()) {
				return c;
			}
		}
		return null;
	}
	@Override
	public Iterator<IBinaryOperatorExpression> getBinaryOperatorExpressionsIterator() {
		return getBinaryOperatorExpressions().iterator();
	}

	@Override
	public void setObservationMgr(IModelObservationMgr observationMgr) {
		m_observationMgr = observationMgr;
	}

	@Override
	public boolean matchesTablesOf(ISqlModel targetModel) {
		
		if (this.getTableCount() != targetModel.getTableCount())
			return false;
		
		for(ITable table : getTables() ) {
			if (!targetModel.tableExists(table))
				return false;
		}
		return true;
	}

	@Override
	public boolean tableExists(ITable criteria) {
		for(ITable table : getTables() ) {
			if (table.equivalent(criteria))
				return true;
		}
		return false;
	}
	private boolean tableExistsInJoin(ITable criteria) {
		boolean ynFound = false;
		for(IBinaryOperatorExpression join : this.getBinaryOperatorExpressions()) {
			
			if (join.getLeftColumn()!=null
					&& join.getLeftColumn().getTable()!=null) {
				if (join
						.getLeftColumn()
						.getTable()
						.getName()
						.equals(criteria.getName())) {
					ynFound = true;
					break;
				}
				
			}
			if (join.getRightColumn()!=null
					&& join.getRightColumn().getTable()!=null) {
				if (join
						.getRightColumn()
						.getTable()
						.getName()
						.equals(criteria.getName())) {
					ynFound = true;
					break;
				}
			}
		}
		return ynFound;
	}

	@Override
	public ITable[] getTablesWithoutJoins() {
		List<ITable> tablesWithoutJoins = new ArrayList<ITable>();
		
		for(ITable table : getTables() ) {
			if (!tableExistsInJoin(table))
				tablesWithoutJoins.add(table);
		}
		ITable[] prototype = {};
		return tablesWithoutJoins.toArray(prototype);
		
	}

	@Override
	public SqlType getSqlType() {
		return m_sqlType;
	}
	@Override
	public void setSqlType(SqlType val) {
		m_sqlType = val;
	}

	@Override
	public boolean isParsedSuccessfully() {
		return this.getParseException()==null;
	}

	@Override
	public void setParseException(Throwable val) {
		m_parseException = val;
	}
	@Override
	public Throwable getParseException() {
		return m_parseException;
	}

	@Override
	public void setParser(Class clazz) {
		this.sqlParser = clazz;
	}

	@Override
	public Class getParser() {
		return this.sqlParser;
	}

	@Override
	public List<ITable> toTableList(String[] ary) {
		ArrayList<ITable> tables = new ArrayList<ITable>();
		for(String s : ary) {
			tables.add(DefaultFactory.getFactory().getTable(s) );
		}
		return tables;
	}

	@Override
	public List<ITable> getSortedTables() {
		List<ITable> list = this.getTables();
		Collections.sort(list);
		return list;
	}
	

}

