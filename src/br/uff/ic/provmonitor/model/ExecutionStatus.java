package br.uff.ic.provmonitor.model;

import java.util.Date;

/**
 * Represents the execution info of elements
 * */
public class ExecutionStatus {
	
	private String elementId;
	private String elementType;
	private String status;
	private Date startTime;
	private Date endTime;
	private String elementPath;
	private String performers;
	private String commitId;
	
	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getElementPath() {
		return elementPath;
	}
	public void setElementPath(String elementPath) {
		this.elementPath = elementPath;
	}
	public String getPerformers() {
		return performers;
	}
	public void setPerformers(String performers) {
		this.performers = performers;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
	
}
