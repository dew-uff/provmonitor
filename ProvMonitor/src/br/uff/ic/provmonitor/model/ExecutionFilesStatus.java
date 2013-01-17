package br.uff.ic.provmonitor.model;

import org.apache.derby.client.am.DateTime;

public class ExecutionFilesStatus {
	private String elementId;
	private String elementPath;
	private String elementAccessType;
	private DateTime elementAccessDateTime;
	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementPath() {
		return elementPath;
	}
	public void setElementPath(String elementPath) {
		this.elementPath = elementPath;
	}
	public String getElementAccessType() {
		return elementAccessType;
	}
	public void setElementAccessType(String elementAccessType) {
		this.elementAccessType = elementAccessType;
	}
	public DateTime getElementAccessDateTime() {
		return elementAccessDateTime;
	}
	public void setElementAccessDateTime(DateTime elementAccessDateTime) {
		this.elementAccessDateTime = elementAccessDateTime;
	}
	
	
}
