package org.wuqispank.test.level1.akiban;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.akiban.AkibanSqlParser;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class SimpleUpdateTest {

	@Test
	public void canParseSqlTableNamesInUpdate() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse("update SEQUENCE set NEXTID = ? where NAME = ?");
		assertEquals("Count of SQL tables incorrect when parsing UPDATE sql with a single table", 1, model.getTableCount());
		
		if( !"sequence".equals(model.getTable(0).getName().toLowerCase() )  ) {
			fail("Parser was unable to locate 'sequence' table in an UPDATE");
		}
		for(Iterator<IBinaryOperatorExpression> itr = model.getBinaryOperatorExpressionsIterator(); itr.hasNext();) {
			IBinaryOperatorExpression joinExpression = (IBinaryOperatorExpression)itr.next();
			Object leftTableMarker = null;
			Object rightTableMarker = null;
			
			IColumn colLeft = joinExpression.getLeftColumn();
			if (colLeft==null)
				joinExpression.setLiteralComparison(true);
			else if (colLeft.getTable()==null) {
				fail("must not be null");
			}
		}
	}
	
	private List<IBinaryOperatorExpression> getJoins(ISqlModel model) {
		
		List myJoins = new ArrayList<IBinaryOperatorExpression>();
		for(Iterator<IBinaryOperatorExpression> itr = model.getBinaryOperatorExpressionsIterator(); itr.hasNext();) {
			IBinaryOperatorExpression foo = (IBinaryOperatorExpression)itr.next();
			myJoins.add(foo);
		}
		return myJoins;
	}
	private boolean validatePseudoJoin(
			List<IBinaryOperatorExpression> list, 
				String table1, 
				String column1,
				String table2, 
				String column2) {

		for(IBinaryOperatorExpression expr : list) {
			IColumn left = expr.getLeftColumn();
			IColumn right = expr.getRightColumn();
			
			if (
					   expr.find(column1,table1) !=null
					&& expr.find(column2,table2) !=null
				)
				return true;
		}
		
		return false;
	}
}
