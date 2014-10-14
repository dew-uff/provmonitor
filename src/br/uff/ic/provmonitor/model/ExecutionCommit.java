package br.uff.ic.provmonitor.model;

import java.util.Date;

public class ExecutionCommit {
	private String elementId;
	private String status;
	private Date commitTime;
	private String elementPath;
	private String commitId;
	
	/**
	 * Getters & Setters
	 */
	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCommitTime() {
		return commitTime;
	}
	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}
	public String getElementPath() {
		return elementPath;
	}
	public void setElementPath(String elementPath) {
		this.elementPath = elementPath;
	}
	public String getCommitId() {
		return commitId;
	}
	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}
}
