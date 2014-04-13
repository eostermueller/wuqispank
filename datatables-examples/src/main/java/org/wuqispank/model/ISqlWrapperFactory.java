package org.wuqispank.model;

import java.util.List;

import org.intrace.client.model.ITraceEvent;
import org.wuqispank.WuqispankException;

public interface ISqlWrapperFactory {

	List<ITraceEvent> getList();
	
	int getNumEventsPerSql();

	void add(ITraceEvent iTraceEvent);

	ISqlWrapper createSqlWrapper(IRequestWrapper irw) throws WuqispankException;

}
