package api.autotest.framework.db.dao;

import java.util.HashMap;
import java.util.Map;

public class RowData {
	public enum Status {
		ADDED, DELETED, EXISTING
	}
	
	private Status status = Status.EXISTING;
	private String primaryKey;
	private Map<String, ColumnData> columns;
	
	public RowData() {}
	
	public RowData(Map<String, ColumnData> columns) {
		this.columns = columns;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Map<String, ColumnData> getColumns() {
		if(columns == null) {
			columns = new HashMap<String, ColumnData>();
		}
		return columns;
	}

	public void setColumns(Map<String, ColumnData> columns) {
		this.columns = columns;
	}

	@Override
	public int hashCode() {
		return columns != null ? columns.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof RowData)) {
			return false;
		}
		RowData obj1 = (RowData) obj;
		int h1 = this.hashCode();
		int h2 = obj1.hashCode();
		return h1 == h2;
	}
}
