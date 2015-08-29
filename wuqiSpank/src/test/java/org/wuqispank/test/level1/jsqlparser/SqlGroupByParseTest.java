package org.wuqispank.test.level1.jsqlparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.headlessintrace.client.model.ITraceEvent;
import org.junit.Test;
import org.wuqispank.DefaultFactory;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.db.jsqlparser.JSqlParser;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;

public class SqlGroupByParseTest {
	
	//This link shows my INNER JOIN alias is ok:  http://www.sqlbook.com/SQL/INNER-JOIN-37.aspx
	private static String SQL_WITH_AGGREGATION = "select sum(this_.J_BGA_) as y0_, sum(this_.J_BGB_) as y1_," + 
			" sum(this_.J_BGC_) as y2_, sum(this_.J_BGD_) as y3_, this_.J_BFBI as y4_, this_.J_BFMC as y5_ " + 
			"from BSDTADLS.J_CALM this_ inner join BSDTADLS.J_CAMS fac1_ on this_.J_BFAI=fac1_.J_BAAI " + 
			"and this_.J_BFCI=fac1_.J_BACI inner join BSDTADLS.J_CAMI limit3_ on fac1_.J_BAAI=limit3_.J_BAAI " + 
			"and fac1_.J_BACI=limit3_.J_BACI inner join BSDTADLS.J_CAPF parms2_ on fac1_.J_BAAI=parms2_.J_IELC " + 
			"where this_.J_BFBI in ('00001356') and limit3_.J_BCAC='A' and fac1_.J_CRAI='          ' " + 
			"and fac1_.J_CRCC=this_.J_CQJC and parms2_.J_REXI=this_.J_BFDI " + 
			"and this_.J_BFEI='00' and this_.J_BFFC='00' and this_.J_BFGI=this_.J_BFBI group by this_.J_BFBI, this_.J_BFMC"; 

	@Test
	public void canParseJoinTables() throws SqlParseException {
		ISqlParser parser = new JSqlParser();
		ISqlModel model = DefaultFactory.getFactory().getSqlModel();
		parser.setSqlModel(model);
		parser.parse(SQL_WITH_AGGREGATION);
		assertEquals("Count of SQL tables incorrect when parsing sql that joins two tables", 4, model.getTableCount());
		
//		validatePseudoJoin(getJoins(model), 
//				"tab1", "col1", "tab2", "col2");
		
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BFAI","J_CALM","J_BAAI","J_CAMS"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BFCI","J_CALM" ,"J_BACI","J_CAMS"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BAAI","J_CAMS","J_BAAI","J_CAMS"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BACI","J_CAMS","J_BACI","J_CAMS"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BAAI","J_CAMS","J_IELC","J_CAPF"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_CRCC","J_CAMS","J_CQJC","J_CALM"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_REXI","J_CAPF","J_BFDI","J_CALM"));
		assertTrue("Couldn't find join",validatePseudoJoin(getJoins(model),"J_BFGI","J_CALM","J_BFBI","J_CALM"));
		assertTrue("Couldn't find test for literal",validateLeftSide(getJoins(model),"J_BCAC","J_CAMI"));
		assertTrue("Couldn't find test for literal",validateLeftSide(getJoins(model),"J_CRAI","J_CAMS"));
		assertTrue("Couldn't find test for literal",validateLeftSide(getJoins(model),"J_BFEI","J_CALM"));
		assertTrue("Couldn't find test for literal",validateLeftSide(getJoins(model),"J_BFFC","J_CALM"));
		
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
				String column1, 
				String table1,
				String column2, 
				String table2) {

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
	private boolean validateLeftSide(
			List<IBinaryOperatorExpression> list, 
				String column1, 
				String table1) {

		for(IBinaryOperatorExpression expr : list) {
			IColumn left = expr.getLeftColumn();
			
			
			
			if (
					   expr.find(column1,table1) !=null
				) {
				assertNull("The right side should have been null, because it is a literal instead of a column",expr.getRightColumn());
				
				return true;
			}
		}
		
		return false;
	}
}
