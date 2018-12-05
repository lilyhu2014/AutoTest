package api.autotest.framework.db.dao;

import java.util.Objects;

public class ColumnData {
	private String columnName;
	private String columnValue;
	private String columnType;
	
	public ColumnData(String columnName, String columnValue, String columnType) {
		this.columnName = columnName;
		this.columnValue = columnValue;
		this.columnType = columnType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(columnName, columnValue);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof ColumnData)) {
			return false;
		}
		ColumnData obj1 = (ColumnData) obj;
		if("DELETED".equalsIgnoreCase(columnType)) {
			Double d1 = columnValue != null ? Double.parseDouble(columnValue) : null;
			Double d2 = obj1.getColumnValue() != null ? Double.parseDouble(obj1.getColumnValue()) : null;
			return Objects.equals(d1, d2);
		}
		return Objects.equals(this.getColumnValue(), ((ColumnData) obj).getColumnValue());
	}
	
	
}
