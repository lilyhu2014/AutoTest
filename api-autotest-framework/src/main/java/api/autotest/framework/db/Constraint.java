package api.autotest.framework.db;

public class Constraint {
	private String operator;
	private String colName;
	private String colValue;
	private String destination;
	private String source;
	private String rowCount;
	private String ignoreValue;
	private boolean hasIgnoreValue;
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColValue() {
		return colValue;
	}
	public void setColValue(String colValue) {
		this.colValue = colValue;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRowCount() {
		return rowCount;
	}
	public void setRowCount(String rowCount) {
		this.rowCount = rowCount;
	}
	public String getIgnoreValue() {
		return ignoreValue;
	}
	public void setIgnoreValue(String ignoreValue) {
		this.ignoreValue = ignoreValue;
	}
	public boolean isHasIgnoreValue() {
		return hasIgnoreValue;
	}
	public void setHasIgnoreValue(boolean hasIgnoreValue) {
		this.hasIgnoreValue = hasIgnoreValue;
	}
}
