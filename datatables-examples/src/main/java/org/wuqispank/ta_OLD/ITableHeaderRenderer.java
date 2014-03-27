package org.wuqispank.ta_OLD;

import java.io.IOException;
import java.io.Writer;

import org.wuqispank.model.ITable;
import org.wuqispank.tablecount_DEPRECATED.MXGraphContext;

public interface ITableHeaderRenderer extends IMarkupElement {


	void setParentId_2(int i);

	int getParentId_1();

	void setParentId_1(int i);

	void setLeftToRightPosition(int i);


	void setTable(ITable table);

	ITableHeaderConfiguration getTableHeaderConfiguration();

	void setTableHeaderConfiguration(ITableHeaderConfiguration val);

	MXGraphContext getGraphContext();

	void setGraphContext(MXGraphContext mxGraphContext);


}
