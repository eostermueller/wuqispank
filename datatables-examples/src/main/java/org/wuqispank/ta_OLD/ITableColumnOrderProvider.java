package org.wuqispank.ta_OLD;

import org.wuqispank.model.ITable;

public interface ITableColumnOrderProvider {
	int getPhysisicalColumnOrder(ITable table);
}
