package br.uff.ic.provmonitor.vcsmanager;

import java.util.Collection;

import br.uff.ic.provmonitor.exceptions.CVSException;

/**
 * CVS Manager Interface. Make it independent of implementation.
 */
public interface VCSManager {
	
	public void createWorkspace(String workspace) throws CVSException;
	public void cloneRepository(String sourceRepository, String workspacePath) throws CVSException;
	public void cloneRepository(String sourceRepository, String workspacePath, Collection<String> cloneOnlyBranches) throws CVSException;
	public void createBranch(String workspace, String branchName) throws CVSException;
	/**
	 * @return String Commit ID - Identification of the commit.
	 * */
	public String commit(String workspacePath, String message) throws CVSException;
	public void addAllFromPath(String workspacePath) throws CVSException;
	public void addPathOrFile(String workspacePath, String pathOrFile) throws CVSException;
	public void addPathOrFile(String workspacePath, Collection<String> pathsOrFiles) throws CVSException;
	public String update(String workspacePath) throws CVSException;
	public void pushBack(String workspacePath, String repositoryPath) throws CVSException;
	public void checkout(String workspacePath, String branchName) throws CVSException;
	
}
