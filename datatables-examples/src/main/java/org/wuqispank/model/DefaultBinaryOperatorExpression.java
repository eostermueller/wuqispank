package org.wuqispank.model;

public class DefaultBinaryOperatorExpression implements
		IBinaryOperatorExpression {

	private IColumn m_rightColumn;
	private IColumn m_leftColumn;
	private static String NULL_PLACEHOLDER = "<WS_NULL>";

	@Override
	public IColumn getLeftColumn() {
		return m_leftColumn;
	}
	@Override
	public void setLeftColumn(IColumn val) {
		m_leftColumn = val;
	}
	@Override
	public void setRightColumn(IColumn val) {
		m_rightColumn = val;
	}

	@Override
	public IColumn getRightColumn() {
		return m_rightColumn;
	}

	/**
	 * The caller is asking, "Is the given tab.col combination in either the right or left of this expression?  If so, pls return that column"
	 */
	@Override
	public IColumn find(String columnCriteria, String tableCriteria) {

		
		if (getLeftColumn()!=null)
			if (getLeftColumn().equals(columnCriteria, tableCriteria))
				return getLeftColumn();

		if (getRightColumn()!=null)
			if (getRightColumn().equals(columnCriteria, tableCriteria))
				return getRightColumn();
			
		return null;
	}

}
