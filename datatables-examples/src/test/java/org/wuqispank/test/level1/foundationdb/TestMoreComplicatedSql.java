package org.wuqispank.test.level1.foundationdb;

import static org.junit.Assert.*;

import org.junit.Test;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.foundationdb.FoundationDBSqlParser;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class TestMoreComplicatedSql {

	@Test
	public void test() throws SqlParseException {
		String sql = "select acbsloanmi0_.J_MBSI as J1_16_0_, acbsloanmi0_.J_MBTI as J2_16_0_, acbsloanmi0_.J_DUEC as J3_16_0_, acbsloanmi0_.J_MCCC as J4_16_0_, acbsloanmi0_.J_MCBC as J5_16_0_, acbsloanmi0_.J_EGFI as J6_16_0_ from BSDTADLS.J_CLMI acbsloanmi0_ where acbsloanmi0_.J_MBSI='A1' and acbsloanmi0_.J_MBTI='000000177'";
		ISqlParser parser = new FoundationDBSqlParser();
		ISqlModel model = org.wuqispank.DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(sql);
		assertEquals("Count of SQL tables incorrect when parsing the simplest SELECT SQL statement ever", 1, model.getTableCount() );

		//BSDTADLS.J_CLMI acbsloanmi0_
		ITable shouldBe_J_CLMI = model.getTable(0);
		assertEquals("SQL table name incorrectly parsed for simplest SELECT SQL statement ever", "J_CLMI".toLowerCase(), shouldBe_J_CLMI.getName() );
	}

}
