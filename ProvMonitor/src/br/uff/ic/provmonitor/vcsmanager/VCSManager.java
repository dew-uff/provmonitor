package br.uff.ic.provmonitor.vcsmanager;

import java.util.Collection;

import br.uff.ic.provmonitor.exceptions.VCSException;

/**
 * CVS Manager Interface. Make it independent of implementation.
 */
public interface VCSManager {
	
	public void createWorkspace(String workspace) throws VCSException;
	public void cloneRepository(String sourceRepository, String workspacePath) throws VCSException;
	public void cloneRepository(String sourceRepository, String workspacePath, Collection<String> cloneOnlyBranches) throws VCSException;
	public void createBranch(String workspace, String branchName) throws VCSException;
	/**
	 * @return String Commit ID - Identification of the commit.
	 * */
	public String commit(String workspacePath, String message) throws VCSException;
	public void addAllFromPath(String workspacePath) throws VCSException;
	public void addPathOrFile(String workspacePath, String pathOrFile) throws VCSException;
	public void addPathOrFile(String workspacePath, Collection<String> pathsOrFiles) throws VCSException;
	public String update(String workspacePath) throws VCSException;
	public void pushBack(String workspacePath, String repositoryPath) throws VCSException;
	public void checkout(String workspacePath, String branchName) throws VCSException;
	
}
