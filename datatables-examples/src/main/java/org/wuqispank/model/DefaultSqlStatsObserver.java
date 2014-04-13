package org.wuqispank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSqlStatsObserver implements ISqlStatsObserver, java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(DefaultSqlStatsObserver.class);

	private Map<String, TableStats> m_tableStats = new Hashtable<String, TableStats>();
	
	private TableStats getTableStats(ITable table) {
		return m_tableStats.get(table.getName() );
	}
	private void putTableStats(ITable table, TableStats tableStats) {
		m_tableStats.put(table.getName(), tableStats );
	}
	@Override
	public int getTableCount(ITable tableCriteria) {
		TableStats tableStats = getTableStats(tableCriteria);
		int rc = 0;
		if (tableStats != null)
			rc = tableStats.tableCount;
		return rc;
	}
	@Override
	public double getAverageJoinsPerSql(ITable tableCriteria) {
		TableStats tableStats = getTableStats(tableCriteria);
		double rc = 0;
		if (tableStats != null)
			rc = tableStats.getAverageJoinsPerSql();
		return rc;
	}
	public int getJoinCount(ITable table) {
		return getTableStats(table).m_totalJoinCount;
	}
	

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DefaultModelObservationMgr.NewTableObservable ) {
			if (arg instanceof ITable) {
				incrementNewTableCount((ITable)arg);
			} else {
				log.warn("Not prepared to handle arg=[" + arg.getClass().getName() + "] and Observable=DefaultModelObservationMgr.NewTableObservable.  This will lead to skewed TableStats and messed up TA graphs.");
			}
		} else if (o instanceof DefaultModelObservationMgr.NewJoinObservable ) {
			if (arg instanceof IBinaryOperatorExpression) {
				IBinaryOperatorExpression expr = (IBinaryOperatorExpression)arg;
				incrementNewJoinCount( expr.getLeftColumn() );
				incrementNewJoinCount( expr.getRightColumn() );
			} else {
				log.warn("Not prepared to handle arg=[" + arg.getClass().getName() + "] and Observable=DefaultModelObservationMgr.NewJoinObservable.  This will lead to skewed TableStats and messed up TA graphs.");
			}
			
		} else if (o instanceof DefaultModelObservationMgr.NewSqlObservable ) {
			Iterator<TableStats> itr = m_tableStats.values().iterator();
			while(itr.hasNext()) {
				TableStats tableStats = itr.next();
				tableStats.newSqlStatement();
			}
		}
		
	}
	private void incrementNewTableCount(ITable table) {
		TableStats tableStats = getTableStats(table);
		if (tableStats==null) {
			tableStats = new TableStats(table);
			putTableStats(table, tableStats);
		}
		tableStats.tableCount++;
	}
	private void incrementNewJoinCount(IColumn column) {
		if (column!=null) {
			ITable table = column.getTable();
			if (table!=null) {
				TableStats tableStats = getTableStats(table);
				if (tableStats==null) {
					tableStats = new TableStats(table);
					putTableStats(table, tableStats);
				}
				//tableStats.m_totalJoinCount++;
				tableStats.incrementJoinCount();
			}
		}
	}
	/**
	 * Can iterate through the return in order, where tables with lowest join averages are first in the list.
	 * Tables with highest join averages are last in the list.
	 * @stolen from here:  http://stackoverflow.com/questions/1448369/how-to-sort-a-treemap-based-on-its-values
	 */
	@Override
	public Map<String, TableStats> getOrderedTables()
    {
        List<Entry<String, TableStats>> list = new LinkedList<Entry<String, TableStats>>(m_tableStats.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, TableStats>>()
        {
			@Override
			public int compare(Entry<String, TableStats> o1,
					Entry<String, TableStats> o2) {
				
				TableStats tableStats1 = o1.getValue();
				TableStats tableStats2 = o2.getValue();
				int compare = Double.compare(
						tableStats1.getAverageJoinsPerSql(),
						tableStats2.getAverageJoinsPerSql()
						);
				//Doc says there can't be any ties in a comparator, so the table name should be the decider.
				if (compare==0) {
					compare = tableStats1.getTable().getName().compareTo(tableStats2.getTable().getName());
					//compare *= -1;
				}
				return compare;
			}
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, TableStats> sortedMap = new LinkedHashMap<String, TableStats>();
        for (Entry<String, TableStats> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }	
	public class TableStats implements java.io.Serializable {
		private double m_averageJoinsPerSql = -1;
		
		public double getAverageJoinsPerSql() {
			if (m_averageJoinsPerSql == -1) 
				m_averageJoinsPerSql = calculateAverageJoinsPerSql();
			
			return m_averageJoinsPerSql;
		}
		public TableStats(ITable table) {
			m_table = table;
		}
		private double calculateAverageJoinsPerSql() {
		    if (m_listJoinsPerSql == null || m_listJoinsPerSql.isEmpty()) {
		        return 0;
		    }

		    double sum = 0;
		    for (Integer joinsPerSql : m_listJoinsPerSql) {
		        sum += joinsPerSql;
		    }

		    return (double)sum / m_listJoinsPerSql.size();
		}		/**
		 * will be used to calculate the average number of times this table was joined to, over all SQL in this request.
		 */
		public void newSqlStatement() {
			if (m_singlSqlJoinCount!=0)
				m_listJoinsPerSql.add(m_singlSqlJoinCount);
			m_singlSqlJoinCount = 0;
		}
		public int getTotalJoinCount() {
			return m_totalJoinCount;
		}
		List<Integer> m_listJoinsPerSql = new ArrayList();
		/**
		 * Number of SQL statements that referenced this table.
		 */
		public int tableCount = 0;
		private int m_singlSqlJoinCount = 0;
		
		public void incrementJoinCount() {
			m_totalJoinCount++;
			m_singlSqlJoinCount++;
		}
		/**
		 * Total number of times that this table was referenced in join clauses.
		 */
		private int m_totalJoinCount = 0;
		private ITable m_table;

		public String toString() {
			return this.tableCount + "|" + this.m_totalJoinCount;
		}

		public ITable getTable() {
			return m_table;
		}
	}

}
