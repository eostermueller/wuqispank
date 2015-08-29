package org.wuqispank.db.jsqlparser;

import java.util.List;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;

import org.wuqispank.DefaultFactory;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.model.UniqueTableList;

public class InsertExtractor implements ParsedDataExtractor {

	private ISqlModel model;
	private Insert insert;

	public InsertExtractor(Insert stmt, ISqlModel m) {
		this.insert = stmt;
		this.model = m;
	}
	@Override
	public void extract() throws SqlParseException {
		UniqueTableList uniqueTables = new UniqueTableList();
		
		SqlPartsFinder sqlPartsFinder = new SqlPartsFinder();
		List<Table> jTableList = sqlPartsFinder.getTableList(this.insert);
		for(Table jT : jTableList) {
			ITable t = DefaultFactory.getFactory().getTable(jT.getName());
			if (jT.getAlias()!=null && jT.getAlias().getName()!=null)
				t.setAlias(jT.getAlias().getName());
			t.setSchema(jT.getSchemaName());
			uniqueTables.addTable(t);
		}
		for (ITable t : uniqueTables.getTables()) {
			this.getSqlModel().addTable(t);
		}
		
	}
	@Override
	public ISqlModel getSqlModel() {
		return this.model;
	}


}
