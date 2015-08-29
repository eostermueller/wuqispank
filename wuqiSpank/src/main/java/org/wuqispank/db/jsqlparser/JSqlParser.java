package org.wuqispank.db.jsqlparser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class JSqlParser implements ISqlParser {

	private ISqlModel sqlModel;

	@Override
	public void parse(String sqlText) throws SqlParseException {

		if (this.getSqlModel()==null) {
			throw new SqlParseException(DefaultFactory.getFactory().getMessages().missingSqlModel(sqlText));
		}
		
		try {
			Statement statement = CCJSqlParserUtil.parse(sqlText);
			ParsedDataExtractor pde = null;
			if (statement instanceof Select) {
				pde = new SelectExtractor( (Select)statement, this.getSqlModel());
			} else if (statement instanceof Update) {
				pde = new UpdateExtractor( (Update)statement, this.getSqlModel());
			} else if (statement instanceof Insert) {
				pde = new InsertExtractor( (Insert)statement, this.getSqlModel());
			} else if (statement instanceof Delete) {
				pde = new DeleteExtractor( (Delete)statement, this.getSqlModel());
			} else {
				throw new SqlParseException("The Replace statement is not yet supported.");
			}
			pde.extract();
			getSqlModel().postProcess();
			
			
		} catch (JSQLParserException jpe) {
			getSqlModel().setParseException(jpe);
			
			throw new SqlParseException(sqlText,jpe.getCause());
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSqlModel(ISqlModel s) {
		this.sqlModel = s;

	}

	@Override
	public ISqlModel getSqlModel() {
		return this.sqlModel;
	}

	@Override
	public void setRetryParseWithoutSelectList(boolean b) {
		// TODO Auto-generated method stub

	}

}

