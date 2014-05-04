package org.wuqispank.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.wuqispank.model.DefaultSqlStatsObserver.TableStats;

public class CenterHeavyTableOrderMgr implements ITableOrderMgr {

	@Override
	public String[] reorderTables(ISqlStatsObserver sqlStats) {
		
		String[] oldOrder = getOldOrder(sqlStats);
		String[] newOrder = new String[oldOrder.length];
		
		int indexHead = 0;
		int indexTail = newOrder.length-1;
		int oldIndex = 0;
		
		while(oldIndex < newOrder.length) {
			
			newOrder[indexHead++] = oldOrder[oldIndex++];
			
			if (oldIndex >= newOrder.length)
				break;
			
			newOrder[indexTail--] = oldOrder[oldIndex++];
		}
		return newOrder;
	}

	@Override
	public String[] getOldOrder(ISqlStatsObserver sqlStats) {
		List<String> tableOrder = new ArrayList<String>();
		Iterator<TableStats> itr = sqlStats.getOrderedTables().values().iterator();
		while (itr.hasNext()) {
			tableOrder.add(itr.next().getTable().getName());
		}		
		String[] prototype={};
		return tableOrder.toArray(prototype);
	}

}
