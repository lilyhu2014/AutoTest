package api.autotest.framework.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableData {
	private String name;
	private List<String> primaryKeyColumns;
	private Map<String, RowData> rows;
	
	public TableData() {}
	
	public TableData(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPrimaryKeyColumns() {
		return primaryKeyColumns;
	}

	public void setPrimaryKeyColumns(List<String> primaryKeyColumns) {
		this.primaryKeyColumns = primaryKeyColumns;
	}

	public Map<String, RowData> getRows() {
		if(rows == null) {
			rows = new HashMap<String, RowData>();
		}
		return rows;
	}

	public void setRows(Map<String, RowData> rows) {
		this.rows = rows;
	}
	
	
}
