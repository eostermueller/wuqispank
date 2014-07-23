package org.wuqispank.web.tableaccesstimeline;

import org.wuqispank.WuqispankException;
import org.wuqispank.model.ISqlWrapper;

public interface IRowGroup {

	void add(ISqlWrapper sql);

	int count();

	void render(int count) throws WuqispankException;

}
