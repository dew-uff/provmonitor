package br.uff.ic.provmonitor.model;

import java.util.Date;

public class ExecutionFilesStatus {
	
	public static final String TYPE_READ = "READ";
	public static final String TYPE_CREATE = "CREATE";
	public static final String TYPE_REMOVE = "REMOVE";
	public static final String TYPE_CHANGE = "CHANGE";
	
	private String elementId;
	private String elementPath;
	private String filePath;
	private String filetAccessType;
	private Date fileAccessDateTime;
	
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFiletAccessType() {
		return filetAccessType;
	}
	public void setFiletAccessType(String filetAccessType) {
		this.filetAccessType = filetAccessType;
	}
	public Date getFileAccessDateTime() {
		return fileAccessDateTime;
	}
	public void setFileAccessDateTime(Date fileAccessDateTime) {
		this.fileAccessDateTime = fileAccessDateTime;
	}

	
}
