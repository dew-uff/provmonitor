package br.uff.ic.provmonitor.workspaceWatcher;

import java.util.Date;

/**
 * Represents different access types on the paths in the workspace. 
 */
public class WorkspacePathStatus {
	public String pathName;
	public PathAccessType pathStatusType;
	public Date statusDateTime;
	
	/**
	 * Getters & Setters
	 */
	
	public WorkspacePathStatus (String pathName, PathAccessType pathStatusType, Date statusDateTime){
		this.pathName = pathName;
		this.pathStatusType = pathStatusType; 
		this.statusDateTime = statusDateTime;
	}
	
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	public PathAccessType getPathStatusType() {
		return pathStatusType;
	}

	public void setPathStatusType(PathAccessType pathStatusType) {
		this.pathStatusType = pathStatusType;
	}
	public Date getStatusDateTime() {
		return statusDateTime;
	}
	public void setStatusDateTime(Date statusDateTime) {
		this.statusDateTime = statusDateTime;
	}
	
	
}
