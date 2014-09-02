package org.wuqispank.model;

import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.wuqispank.WuqispankException;

public interface ISqlWrapperFactory {

	List<ITraceEvent> getList();
	
	int getMinNumEventsPerSql();

	void add(ITraceEvent iTraceEvent);

	ISqlWrapper createSqlWrapper(IRequestWrapper irw) throws WuqispankException;

}
