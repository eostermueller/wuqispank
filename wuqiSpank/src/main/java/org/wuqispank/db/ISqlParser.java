package org.wuqispank.db;

import java.util.List;

import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public interface ISqlParser {
	void parse(String sqlText) throws SqlParseException;
	void init();
	void setSqlModel(ISqlModel s);
	public abstract ISqlModel getSqlModel();
	void setRetryParseWithoutSelectList(boolean b);
}
