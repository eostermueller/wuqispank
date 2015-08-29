package org.wuqispank.db.jsqlparser;

import java.util.List;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.update.Update;

import org.wuqispank.DefaultFactory;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.model.UniqueTableList;

class UpdateExtractor implements ParsedDataExtractor {
	private Update update;
	private ISqlModel model;

	UpdateExtractor(Update updateStmt,ISqlModel m) {
		this.update = updateStmt;
		this.model = m;
	}
	
	public ISqlModel getSqlModel() {
		return this.model;
	}

	@Override
	public void extract() throws SqlParseException {
		UniqueTableList tables = new UniqueTableList();
		
		SqlPartsFinder sqlPartsFinder = new SqlPartsFinder();
		List<Table> tableList = sqlPartsFinder.getTableList(this.update);
		for(Table jT : tableList) {
			ITable t = DefaultFactory.getFactory().getTable(jT.getName());
			Alias a = jT.getAlias();
			if (jT.getAlias()!=null && jT.getAlias().getName()!=null)
				t.setAlias(jT.getAlias().getName());
			t.setSchema(jT.getSchemaName());
			tables.addTable(t);
		}
		for (ITable t : tables.getTables()) {
			this.getSqlModel().addTable(t);
		}
		for(Column c : sqlPartsFinder.getColumns()) {
			this.getSqlModel().addSelectListColumn(
					c.getTable().getName(),
					c.getColumnName() );
					 
		}
	}
}
