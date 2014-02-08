package br.uff.ic.provmonitor.vcsmanager;

import java.util.Set;

/**
 * Container for workspace informations after workspace changes.
 *
 */
public class VCSWorkspaceMetaData {
	
	private String commidId;
	private Set<String> removed;
	private Set<String> created;
	private Set<String> changed;
	private Set<String> modified;
	
	
	/**
	 * Getters & Setters
	 */
	
	public String getCommidId() {
		return commidId;
	}
	public void setCommidId(String commidId) {
		this.commidId = commidId;
	}
	public Set<String> getRemoved() {
		return removed;
	}
	public void setRemoved(Set<String> removed) {
		this.removed = removed;
	}
	public Set<String> getCreated() {
		return created;
	}
	public void setCreated(Set<String> created) {
		this.created = created;
	}
	public Set<String> getChanged() {
		return changed;
	}
	public void setChanged(Set<String> changed) {
		this.changed = changed;
	}
	public Set<String> getModified() {
		return modified;
	}
	public void setModified(Set<String> modified) {
		this.modified = modified;
	}
	
	
}
