package org.wuqispank.db.akiban;


import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuqispank.DefaultFactory;
import org.wuqispank.WuqispankException;
import org.wuqispank.db.ISqlParser;
import org.wuqispank.db.SqlParseException;
import org.wuqispank.model.IBinaryOperatorExpression;
import org.wuqispank.model.IColumn;
import org.wuqispank.model.ISqlModel;
import org.wuqispank.model.ITable;
import org.wuqispank.model.SqlType;

import com.akiban.sql.StandardException;
import com.akiban.sql.parser.BinaryOperatorNode;
import com.akiban.sql.parser.ColumnReference;
import com.akiban.sql.parser.CursorNode;
import com.akiban.sql.parser.FromBaseTable;
import com.akiban.sql.parser.FromList;
import com.akiban.sql.parser.FromTable;
import com.akiban.sql.parser.JoinNode;
import com.akiban.sql.parser.NodeTypes;
import com.akiban.sql.parser.QueryTreeNode;
import com.akiban.sql.parser.SQLParser;
import com.akiban.sql.parser.SQLParserContext.IdentifierCase;
import com.akiban.sql.parser.SelectNode;
import com.akiban.sql.parser.StatementNode;
import com.akiban.sql.parser.TableName;
import com.akiban.sql.parser.ValueNode;
import com.akiban.sql.parser.Visitable;
import com.akiban.sql.parser.Visitor;




/**
 * @stolenFrom: https://gist.github.com/nathanlws/5797500
 * @author erikostermueller
 *
 */
public class AkibanSqlParser implements ISqlParser {
	private static final Logger log = LoggerFactory.getLogger(AkibanSqlParser.class);
	private boolean ynUpdateOrDelete = false;
	private ISqlModel m_sqlModel;
	private boolean retryParseWithoutSelectList;

	@SuppressWarnings("static-access")
	@Override
	public void parse(String sqlText) throws SqlParseException {
		if (this.getSqlModel()==null) {
			throw new SqlParseException(DefaultFactory.getFactory().getMessages().missingSqlModel(sqlText));
		}
		this.getSqlModel().setParser(this.getClass());
		init();
		if (sqlText==null || "".equals(sqlText.trim())) {
			throw new SqlParseException(DefaultFactory.getFactory().getMessages().getMissingSqlText() );
		}
		
	    CustomSQLParser parser = new CustomSQLParser();
	    //SQLParser parser = new SQLParser();
	    
        ColumnNamePrinter printer = new ColumnNamePrinter();
//July1        parser.setIdentifierCase(SQLParserContext.IdentifierCase.PRESERVE);
        StatementNode stmt;
		try {
			stmt = parser.parseStatement(sqlText);
			stmt.accept(new ColumnNamePrinter());
			log.debug("Result type [" + stmt.getClass().getName() + "] ["  + stmt.statementToString() + "]");
			this.getSqlModel().setSqlType( SqlType.valueOf( stmt.statementToString() ) );
			if (stmt instanceof CursorNode) {
				CursorNode cursorNode = (CursorNode)stmt;
				 SelectNode sn = (SelectNode)cursorNode.getResultSetNode(); 
				 //System.out.println("From");
				 if (sn !=null) {
					 ValueNode whereClause = sn.getWhereClause();
					 if (whereClause!=null) {
						 log.debug("WHERE CLAUSE table name [" + whereClause.getTableName() + "]");
						 log.debug("WHERE CLAUSE is Constant Expr [" + whereClause.isConstantExpression() + "]");
						 log.debug("WHERE CLAUSE getUserData() [" + whereClause.getUserData() + "]");
					 }
					 
				 }
			}
			getSqlModel().postProcess();
		} catch ( RuntimeException e) {
			
			SqlParseException spe = new SqlParseException(e);
			spe.setSql(sqlText);
			getSqlModel().setParseException(spe);
			throw spe;
		} catch (StandardException e) {
			
			SqlParseException spe = new SqlParseException(e);
			spe.setSql(sqlText);
			getSqlModel().setParseException(spe);
			throw spe;
		}

		if (log.isDebugEnabled()) {
			log.debug("Sql statement [" + sqlText + "]");
			stmt.treePrint();		
		}
		
    }
	public class ColumnNamePrinter implements Visitor {
        @Override
        public Visitable visit(Visitable visitable) {
            QueryTreeNode node = (QueryTreeNode)visitable;
            String text = "<undef>";
            if (node!=null) {
            	if (node.getUserData() !=null) {
            		text = node.getUserData().toString();
            	}
            }
            
            //System.out.println("#!# Found node type [" +  node.getNodeType() + "] user data[" + text + "]");
    		//ITable myTable = DefaultFactory.getFactory().getTable();
            ITable myTable = null;
            switch(node.getNodeType()) {
            	case NodeTypes.FROM_LIST:
            		log.debug("FROM_LIST type [" + node.getClass().getName() + "]");
            		FromList fromList = (FromList)node;
            		//if (ynUpdateOrDelete) {
            		if (false) {
                		for(Iterator<FromTable> it = fromList.iterator();it.hasNext();) {
                			FromTable fromTable = (FromTable)it.next();
                			try {
                    			log.debug("From FROM_TABLE getCorrelationName [" + fromTable.getCorrelationName() + "]");
    							log.debug("From FROM_TABLE getExposedName     [" + fromTable.getExposedName() + "]");
    	            			log.debug("From FROM_TABLE getOrigTableName   [" + fromTable.getOrigTableName() + "]");
    	            			log.debug("From FROM_TABLE getTableName       [" + fromTable.getTableName() + "]");
    	            			myTable = DefaultFactory.getFactory().getTable(fromTable.getTableName().toString());
    	            			
    	                		myTable.setAlias(fromTable.getCorrelationName());
    	                		getSqlModel().addTable(myTable);
    						} catch (StandardException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
                		}
            			
            		}
            		//fromList.get(index)
//            		TableName tableName = (TableName)node;
//            		myTable.setName(tableName.getTableName());
//            		log.debug("What does this table name look like.  Does it contain the schema?  The alias? [" + tableName.getFullTableName() + "]");
//            		getTables().add(myTable);
            		
            		break;
            	case NodeTypes.DELETE_NODE:
            		ynUpdateOrDelete = true;
            		break;
            		//if (fromList.getParserContext().
            	case NodeTypes.UPDATE_NODE:
            		ynUpdateOrDelete = true;
            		break;
            	case NodeTypes.COLUMN_REFERENCE:
                    ColumnReference ref = (ColumnReference)node;
                    //System.out.println("  ~*" + ref.getColumnName());
                    if (ref.getTableNameNode()!=null) {
                        //System.out.println("  ~!" + ref.getTableNameNode().getFullTableName());
                        //System.out.println("  ~#" + ref.getTableNameNode().getTableName());
                    } else {
                    	//System.out.println("Found null table");
                    }
                    //System.out.println("  ~%" + ref.getSQLColumnName());
                    //System.out.println("  ~&" + ref.getTableName());
                    
                    getSqlModel().addSelectListColumn(ref.getTableName(), ref.getColumnName());
            		break;
            	case NodeTypes.FROM_BASE_TABLE:
            		//if (!ynUpdateOrDelete) {//gotta skip this for updates/deletes, because Akiban tells us about the table name twice for some unknown  (and probably very good) reasons.
                		FromBaseTable fromBaseTable = (FromBaseTable)node;
                		try {
                			TableName unkTableName = fromBaseTable.getOrigTableName();
                			myTable = DefaultFactory.getFactory().getTable(unkTableName.getTableName());
                    		//myTable.setName(unkTableName.getTableName());
                		} catch (Exception e) {
                			
                		}
                		myTable.setAlias(fromBaseTable.getCorrelationName());
                		getSqlModel().addTable(myTable);
            		//}
            		break;
            	case NodeTypes.TABLE_NAME:
            		TableName unk2TableName = (TableName)node;
            		myTable = DefaultFactory.getFactory().getTable(unk2TableName.getTableName());
            		//myTable.setName(unk2TableName.getTableName());
            		log.debug("What does this table name look like.  Does it contain the schema?  The alias? [" + unk2TableName.getFullTableName() + "]");
            		getSqlModel().addTable(myTable);
            		break;
            	case NodeTypes.SELECT_NODE:
            		SelectNode selectNode = (SelectNode)node;
            		ValueNode valueNode = selectNode.getWhereClause();
            		if (valueNode!=null) {
                		log.debug("SELECT_NODE with where clause table name[" + valueNode.getTableName() + "]");		
            		}
            		break;
            	case NodeTypes.JOIN_NODE:
            	case NodeTypes.HALF_OUTER_JOIN_NODE:
            	case NodeTypes.FULL_OUTER_JOIN_NODE:
            		JoinNode joinNode = (JoinNode)node;
            		log.debug("JOIN [" + joinNode.toString() + "]");
            		break;
            	case NodeTypes.BINARY_EQUALS_OPERATOR_NODE:
            		if (node instanceof BinaryOperatorNode) {
            			BinaryOperatorNode binaryOperatorNode = (BinaryOperatorNode)node;
            			ValueNode left = binaryOperatorNode.getLeftOperand();
            			ValueNode right = binaryOperatorNode.getRightOperand();
            			IBinaryOperatorExpression expr = DefaultFactory.getFactory().getBinaryOperatorExpression();

            			IColumn leftColumn = getSqlModel().addWhereClauseColumn(
        						binaryOperatorNode.getLeftOperand().getTableName(), 
        						binaryOperatorNode.getLeftOperand().getColumnName());
            			expr.setLeftColumn( leftColumn );

               			IColumn rightColumn = getSqlModel().addWhereClauseColumn(
               					binaryOperatorNode.getRightOperand().getTableName(), 
               					binaryOperatorNode.getRightOperand().getColumnName());
               			expr.setRightColumn( rightColumn );
            			//log.debug("Probably need to add more code to get details of a literal value in the SQL statement. Could not find right column [" + binaryOperatorNode.getRightOperand().getColumnName() + "] in table [" + binaryOperatorNode.getRightOperand().getTableName() + "] ");
            			
            			getSqlModel().addBinaryOperatorExpression(expr);
            			log.debug("BINARY_EQUALS_OPERATOR_NODE Left [" + binaryOperatorNode.getLeftOperand().getTableName() + "." + binaryOperatorNode.getLeftOperand().getColumnName() + "]");
            			log.debug("BINARY_EQUALS_OPERATOR_NODE Right [" + binaryOperatorNode.getRightOperand().getTableName() + "." + binaryOperatorNode.getRightOperand().getColumnName() + "]");
            			
            		} else {
            			log.debug("Found BINARY_EQUALS_OPERATOR_NODE that is not BinaryOperatorNode [" + node.getClass().getName() + "]");
            		}
            	default:
            		//not needed/supported yet.
            		break;
            }
            return visitable;
        }

		@Override
		public boolean skipChildren(Visitable arg0) throws StandardException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean stopTraversal() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean visitChildrenFirst(Visitable arg0) {
			// TODO Auto-generated method stub
			return false;
		}
 

//		private List<ITable> getTables() {
//			return getSqlModel().getTables();
//		}


    }
 
    public class CustomSQLParser extends SQLParser {
        private IdentifierCase identCase = IdentifierCase.PRESERVE;
 
        public void setIdentifierCase(IdentifierCase newCase) {
            identCase = newCase;
        }
 
        public IdentifierCase getIdentifierCase() {
            return identCase;
        }
    }

	@Override
	public void init() {
		ynUpdateOrDelete = false;
	}

	@Override
	public void setSqlModel(ISqlModel s) {
		m_sqlModel = s;
	}
	@Override
	public ISqlModel getSqlModel() {
		return m_sqlModel;
	}

	@Override
	public void setRetryParseWithoutSelectList(boolean b) {
		// TODO Auto-generated method stub
		
	}
 
}
