package org.wuqispank.db.jsqlparser;

import java.util.List;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import org.wuqispank.DefaultFactory;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.model.UniqueTableList;

class SelectExtractor implements ParsedDataExtractor {
	private Select select;
	private ISqlModel model;

	SelectExtractor(Select s, ISqlModel m) {
		this.select = s;
		this.model = m;
	}

	@Override
	public void extract() throws SqlParseException {
		UniqueTableList uniqueTables = new UniqueTableList();
		
		SqlPartsFinder sqlPartsFinder = new SqlPartsFinder();
		List<Table> jTableList = sqlPartsFinder.getTableList(this.select);
		for(Table jT : jTableList) {
			ITable t = DefaultFactory.getFactory().getTable(jT.getName());
			if (jT.getAlias()!=null && jT.getAlias().getName()!=null)
				t.setAlias(jT.getAlias().getName());
			t.setSchema(jT.getSchemaName());
			uniqueTables.addTable(t);
		}
		
		PlainSelect ps = (PlainSelect)select.getSelectBody();
		if (ps.getJoins()!=null) {
	        for( Join join : ps.getJoins()) {
	        	FromItem fi = join.getRightItem();
	        	if (fi instanceof Table) {
	        		Table jT = (Table)fi;
		        	ITable t = DefaultFactory.getFactory().getTable(jT.getName());
		        	if (fi.getAlias()!=null && fi.getAlias().getName() !=null)
		        		t.setAlias(fi.getAlias().getName());
		        	t.setSchema(jT.getSchemaName());
		        	this.traverseExpressionForJoins( join.getOnExpression() );
		        	uniqueTables.addTable( t );
	        	}
	        }
		}
		for (ITable t : uniqueTables.getTables()) {
			this.getSqlModel().addTable(t);
		}
		List<SelectItem> selectItems =  ps.getSelectItems();
		for(SelectItem si : selectItems) {
			if (si instanceof SelectExpressionItem) {
				SelectExpressionItem sei = (SelectExpressionItem)si;
				if (sei.getExpression() != null && sei.getExpression() instanceof Column) {
					Column c = (Column)sei.getExpression();
					this.getSqlModel().addSelectListColumn(
							c.getTable().getName(),
							c.getColumnName() );
				}
			}
		}
		
		for(Column c : sqlPartsFinder.getColumns()) {
			this.getSqlModel().addSelectListColumn(
					c.getTable().getName(),
					c.getColumnName() );
		}
		
        this.traverseExpressionForJoins( ps.getWhere() );
	}
	private void traverseExpressionForJoins(Expression e) throws SqlParseException {
		
		if(e instanceof BinaryExpression) {
			
			BinaryExpression jBinaryExpression = (BinaryExpression)e;
			if ( jBinaryExpression.getLeftExpression() instanceof Column ) {
//				 && jBinaryExpression.getRightExpression() instanceof Column) {

						IBinaryOperatorExpression boe = DefaultFactory.getFactory().getBinaryOperatorExpression();
						IColumn left = DefaultFactory.getFactory().getColumn();
						Expression ex = jBinaryExpression.getLeftExpression();
						if (ex instanceof Column) {
							Column jLeft = (Column)ex;
            				left = getSqlModel().addWhereClauseColumn(
            						jLeft.getTable().getName(), //actually, JSqlParser returns the alias here, not the table name. 
            						jLeft.getColumnName()); 
							
//							left.setName(jLeft.getColumnName());
//							ITable leftTable = DefaultFactory.getFactory().getTable(jLeft.getTable().getName());
//							left.setTable(leftTable);
    						boe.setLeftColumn(left);
						} else {
							throw new SqlParseException("Probably need to add more code here to learn more about this model");
						}
						IColumn right = DefaultFactory.getFactory().getColumn();
						ex = jBinaryExpression.getRightExpression();
						if (ex instanceof Column) {
							Column jRight = (Column)ex;
            				right = getSqlModel().addWhereClauseColumn(
            						jRight.getTable().getName(), //actually, JSqlParser returns the alias here, not the table name. 
            						jRight.getColumnName()); 
//							left.setName(jRight.getColumnName());
//							ITable rightTable = DefaultFactory.getFactory().getTable(jRight.getTable().getName());
//							right.setTable(rightTable);
    						boe.setRightColumn(right);
						} 
						//getting rid of the following allows "Were myCol = 'X'
//						else {
//							throw new SqlParseException("Probably need to add more code here to learn more about this model");
//						}
						this.getSqlModel().addBinaryOperatorExpression(boe);
					 
				 } else if (e instanceof BinaryExpression) {
						BinaryExpression be = (BinaryExpression)e;
						this.traverseExpressionForJoins(((BinaryExpression) e).getLeftExpression());
						this.traverseExpressionForJoins(((BinaryExpression) e).getRightExpression());
				} 
			} 
		}

	@Override
	public ISqlModel getSqlModel() {
		return this.model;
	}
}