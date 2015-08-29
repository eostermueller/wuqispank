package org.wuqispank.test.level1.akiban;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.akiban.AkibanSqlParser;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class SimpleJoinTest {
	private static String TWO_TABLE_SELECT_SQL = "SELECT name, description, date from Event a, location b where a.location_id = b.loc_id and location_name = ?"; 
	//private static String THREE_TABLE_SELECT_SQL = "SELECT name, description, date, location_id, loc_id, location_name, a.event_type, c.event_type from Event a, location b, pricing c where a.location_id = b.loc_id and b.location_name = ? and a.event_type = c.event_type";
	private static String THREE_TABLE_SELECT_SQL_FULLY_QUALIFIED_JOIN_FIELDS = "SELECT name, description, date, location_id, loc_id, location_name, a.event_type, c.event_type from Event a, location b, pricing c where a.location_id = b.loc_id and a.event_type = c.event_type";
	private static String THREE_TABLE_SELECT_SQL_NO_FULLY_QUALIFIED_JOIN_FIELDS = "SELECT name, description, date, location_name from Event a, location b, pricing c where a.location_id = b.loc_id and a.event_type = c.event_type";

	//@Test
	public void canParseSqlTableNames() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(TWO_TABLE_SELECT_SQL);
		assertEquals("Count of SQL tables incorrect when parsing sql that joins two tables", 2, model.getTableCount());
		
		if( !"event".equals(model.getTable(0).getName().toLowerCase() ) 
				&& !"event".equals(model.getTable(1).getName().toLowerCase() ) ) {
			fail("Parser was unable to locate 'event' table in a SELECT");
		}

		if( !"location".equals(model.getTable(0).getName().toLowerCase() ) 
				&& !"location".equals(model.getTable(1).getName().toLowerCase() ) ) {
			fail("Parser was unable to locate 'location' table in a SELECT");
		}
	}
	@Test
	public void canParseThreeTableNames() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(THREE_TABLE_SELECT_SQL_FULLY_QUALIFIED_JOIN_FIELDS);
		assertEquals("Count of SQL tables incorrect when parsing sql that joins two tables", 3, model.getTableCount());
		
		if( !"event".equals(model.getTable(0).getName().toLowerCase() ) 
				&& !"event".equals(model.getTable(1).getName().toLowerCase() ) ) {
			fail("Parser was unable to locate 'event' table in a SELECT");
		}

		if( !"location".equals(model.getTable(0).getName().toLowerCase() ) 
				&& !"location".equals(model.getTable(1).getName().toLowerCase() ) ) {
			fail("Parser was unable to locate 'location' table in a SELECT");
		}
	}
	@Test
	public void canParseJoinExpressionsWithQualifiedJoinFields() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(THREE_TABLE_SELECT_SQL_FULLY_QUALIFIED_JOIN_FIELDS);
		
		boolean rc1 = validatePseudoJoin(getJoins(model), "event", "location_id", "location", "loc_id");//intentionally mismatched names
		assertTrue("Could not find the join for event and location tables", rc1);
		boolean rc2 = validatePseudoJoin(getJoins(model), "event", "event_type", "pricing", "event_type");
		assertTrue("Could not find the join for event and pricing tables", rc2);
	}
	@Test
	public void canParseJoinExpressionsWithoutHelpFromQualifiedJoinFields() throws SqlParseException {
		ISqlParser parser = new AkibanSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(THREE_TABLE_SELECT_SQL_NO_FULLY_QUALIFIED_JOIN_FIELDS);
		
		boolean rc1 = validatePseudoJoin(getJoins(model), "event", "location_id", "location", "loc_id");//intentionally mismatched names
		assertTrue("Could not find the join for event and location tables", rc1);
		boolean rc2 = validatePseudoJoin(getJoins(model), "event", "event_type", "pricing", "event_type");
		assertTrue("Could not find the join for event and pricing tables", rc2);
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
