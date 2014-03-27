package org.wuqispank.model;

import java.util.Observer;

/**
 * Registers listeners for update events.
 * Provides methods (addNewTable and addNewJoin) that increment 1 time for every invocation.
 * @author erikostermueller
 *
 */
public class DefaultModelObservationMgr implements IModelObservationMgr {

	private NewTableObservable m_newTableObservable = new NewTableObservable();
	private NewJoinObservable m_newJoinObservable = new NewJoinObservable();
	private NewSqlObservable m_newSqlObservable = new NewSqlObservable();
	public void registerNewTableListener(Observer sqlStatsCounter) {
		m_newTableObservable.addObserver(sqlStatsCounter);
	}
	@Override
	public void registerNewSqlListener(ISqlStatsObserver sqlStatsCounter) {
		m_newSqlObservable.addObserver(sqlStatsCounter);
	}
	@Override
	public void addNewTable(ITable table) {
		m_newTableObservable.update(table);
	}
	@Override
	public void addNewJoin(IBinaryOperatorExpression expression) {
		m_newJoinObservable.update(expression);
	}
	@Override
	public void addNewSql() {
		m_newSqlObservable.newSql();
	}

	public void registerNewJoinListener(Observer sqlStatsCounter) {
		m_newJoinObservable.addObserver(sqlStatsCounter);
	}
	class NewTableObservable extends java.util.Observable {
		public void update(ITable table) {
			this.setChanged();
			this.notifyObservers(table);
		}
	}
	class NewJoinObservable extends java.util.Observable {
		public void update(IBinaryOperatorExpression joinExpression) {
			this.setChanged();
			this.notifyObservers(joinExpression);
		}
	}
	class NewSqlObservable extends java.util.Observable {
		public void newSql() {
			this.setChanged();
			this.notifyObservers();
		}
	}
}

