package org.wuqispank.model;

public interface IBinaryOperatorExpression {
	IColumn getLeftColumn();
	IColumn getRightColumn();
	IColumn find(String column1, String table1);
	void setLeftColumn(IColumn val);
	void setRightColumn(IColumn val);
	boolean isLiteralComparison();
	void setLiteralComparison(boolean val);
}
