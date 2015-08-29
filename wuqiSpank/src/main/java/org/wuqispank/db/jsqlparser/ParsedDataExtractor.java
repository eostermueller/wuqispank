package org.wuqispank.db.jsqlparser;

import java.util.List;

import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

interface ParsedDataExtractor {
	void extract() throws SqlParseException;
	/**
	 * return a unique list of tables.
	 * @return
	 */
	ISqlModel getSqlModel();
}
