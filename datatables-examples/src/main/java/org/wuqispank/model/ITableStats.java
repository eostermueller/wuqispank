package org.wuqispank.model;

public interface ITableStats extends IBaseTable {
	public void incrementReference();
	public void incrementWhereClauseReference();
	
	/**
	 * This answers the question, "In how many sql statements was this table referenced?"
	 * Very important number to highight to the end users.
	 * Tables with the highest numbers here need most refactoring.
	 * @return
	 */
	public int getReferenceCount();
	
	/**
	 * For graph readability, tables with most connections to other tables
	 * must be located close to the vertical center of the graph, allowing the many
	 * connections to radiate out evenly to the left and the right.
	 * Tables with high "Where Clause Reference Count" will be located towards this vertical center.
	 *  Tables with low "Where Clause Reference Count" will be located further towards the left/right of the screen.
	 * @return
	 */
	public int getWhereClauseReferenceCount();
}
