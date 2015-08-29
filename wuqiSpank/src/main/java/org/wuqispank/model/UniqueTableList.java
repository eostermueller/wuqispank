package org.wuqispank.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class UniqueTableList {
	Map<String, ITable> map = new Hashtable<String,ITable>();
	public void addTable(ITable table) {
		this.map.put(table.getName(),  table);
	}
	
	public List<ITable> getTables() {
		List<ITable> tables = new ArrayList<ITable>();
		tables.addAll(map.values());
			
		return tables;
	}
}
