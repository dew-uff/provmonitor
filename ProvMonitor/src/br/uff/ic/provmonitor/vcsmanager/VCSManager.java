package br.uff.ic.provmonitor.vcsmanager;

import java.util.Collection;
import java.util.Set;

import br.uff.ic.provmonitor.exceptions.VCSException;

/**
 * CVS Manager Interface. Make it independent of implementation.
 */
public interface VCSManager {
	
	public boolean isWorkspaceCreated(String workspace) throws VCSException;
	
	/**
	 * Create a workspace in the specified path.
	 * @param workspace - path of the workspace to be created.
	 * @throws VCSException - If any exception occurs.
	 */
	public void createWorkspace(String workspace) throws VCSException;
	
	/**
	 * Clone all branches from a source repository to a local repository and create local workspace.
	 * @param sourceRepository - source repository to clone from.
	 * @param workspacePath - destiny path of the workspace where the source will be cloned.
	 * @throws VCSException - If any exception occurs.
	 */
	public void cloneRepository(String sourceRepository, String workspacePath) throws VCSException;
	
	/**
	 * Clone the specified branches from a source repository to a local repository and create local workspace.
	 * @param sourceRepository - source repository to clone from.
	 * @param workspacePath - destiny path of the workspace where the source will be cloned.
	 * @param cloneOnlyBranches - <code>Collection< String ></code> Branches to be cloned.
	 * @throws VCSException - If any exception occurs.
	 */
	public void cloneRepository(String sourceRepository, String workspacePath, Collection<String> cloneOnlyBranches) throws VCSException;
	
	/**
	 * Create a new branch into the specified workspace.
	 * @param workspace - workspace target of the new branch.
	 * @param branchName - name of the new branch to be created.
	 * @throws VCSException - If any exception occurs.
	 */
	public void createBranch(String workspace, String branchName) throws VCSException;
	
	/**
	 * Push back local changes to the remote repository.
	 * @param workspacePath - local path, source of changes to be pushed back.
	 * @param repositoryPath - destiny repository to receive the local changes.
	 * @throws VCSException - If any exception occurs.
	 */
	public void pushBack(String workspacePath, String repositoryPath) throws VCSException;
	
	/**
	 * Add all untracked files into control.
	 * @param workspacePath - Observed workspace.
	 * @throws VCSException - If any exception occurs.
	 */
	public void addAllFromPath(String workspacePath) throws VCSException;
	
	/**
	 * Add specified untracked file into control.
	 * @param workspacePath - Observed workspace.
	 * @param pathOrFile - path to be added into control.
	 * @throws VCSException - If any exception occurs.
	 */
	public void addPathOrFile(String workspacePath, String pathOrFile) throws VCSException;
	
	/**
	 * Add specified untracked collection of files into control.
	 * @param workspacePath - Observed workspace.
	 * @param pathsOrFiles - Collection of paths to be added into control.
	 * @throws VCSException - If any exception occurs.
	 */
	public void addPathOrFile(String workspacePath, Collection<String> pathsOrFiles) throws VCSException;
	
	/**
	 * Checkout the specified branch into the informed workspace.
	 * @param workspacePath - path of the observed workspace.
	 * @param branchName - branch to be checked out.
	 * @throws VCSException - If any exception occurs.
	 */
	public void checkout(String workspacePath, String branchName) throws VCSException;
	
	/**
	 * Not implemented
	 * @param workspacePath
	 * @return
	 * @throws VCSException
	 */
	@Deprecated
	public String update(String workspacePath) throws VCSException;
	
	/**
	 * Commit changes in the workspace.
	 * 
	 * @param workspacePath - path of the observed workspace.
	 * @param message - commit message.
	 * @return String Commit ID - Identification of the commit.
	 * @throws VCSException - If any exception occurs.
	 */
	public String commit(String workspacePath, String message) throws VCSException;
	
	/**
	 * Commit the removed files, removing it from repository.
	 * 
	 * @param workspacePath - path of the observed workspace.
	 * @return Set<String> - Collection of removed paths.
	 * @throws VCSException - If any exception occurs.
	 */
	public Set<String> removeAllFromPath(String workspacePath) throws VCSException;
	
	/**
	 * Return Set of paths removed from the workspace
	 * @param workspacePath - path of the observed workspace.
	 * @return Set<String> - paths of removed files.
	 * @throws VCSException - If any exception occurs.
	 */
	public Set<String> getRemovedFiles(String workspacePath) throws VCSException;
	
	
}
