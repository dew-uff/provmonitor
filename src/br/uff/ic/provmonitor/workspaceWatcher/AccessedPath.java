package br.uff.ic.provmonitor.workspaceWatcher;

import java.util.Date;

/***
 * Represents the Entry reported by the WorkspaceAccessReader
 * */
public class AccessedPath {
	
	public String accessedPathName;
	public Date accessedDateTime;
	
	/***
	 * Public Main Constructor
	 * */
	public AccessedPath (String accessedPathName, Date accessedDateTime){
		this.accessedPathName = accessedPathName;
		this.accessedDateTime = accessedDateTime;
	}
	
	public String getAccessedPathName() {
		return accessedPathName;
	}
	public void setAccessedPathName(String accessedPathName) {
		this.accessedPathName = accessedPathName;
	}
	public Date getAccessedDateTime() {
		return accessedDateTime;
	}
	public void setAccessedDateTime(Date accessedDateTime) {
		this.accessedDateTime = accessedDateTime;
	}
	
	
}
