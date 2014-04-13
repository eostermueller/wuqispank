package org.wuqispank.web.tablecount;

import org.wuqispank.WuqispankException;
import org.wuqispank.model.ISqlWrapper;

public interface IRow {
	void setGraphContext(GraphContext val);
	GraphContext ctx();
	void setSqlWrapper(ISqlWrapper val);
	ISqlWrapper getSqlWrapper();
	void render(int count) throws WuqispankException;
	Object getRowLane();
	void setRowLane(Object val);

}
