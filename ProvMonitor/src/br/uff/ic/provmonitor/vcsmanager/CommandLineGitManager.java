package br.uff.ic.provmonitor.vcsmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Set;

import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;

/**
 * CVS Manager Implementation for Git CVS.
 * <br /><p>
 * <b>Technology:</b> using command line git commands.</p>
 */
public class CommandLineGitManager implements VCSManager{

	@Override
	public void createWorkspace(String workspace) throws VCSException {
		//FileRepositoryBuilder frb = new FileRepositoryBuilder();
	}

	public boolean isWorkspaceCreated(String workspace) throws VCSException{
		return false;
	}
	
	@Override
	public void cloneRepository(String sourceRepository, String workspacePath)
			throws VCSException {
		String command = "git.exe clone " + sourceRepository + " " + workspacePath;
		try{
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
			   System.out.println(line);
			}
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public void cloneRepository(String sourceRepository, String workspacePath,
			Collection<String> cloneOnlyBranches) throws VCSException {
	}

	@Override
	public void createBranch(String workspace, String branchName)
			throws VCSException {
		StringBuilder gitBrancBuilder = new StringBuilder();
		gitBrancBuilder.append("Git.exe branch ")
					   .append(workspace)
					   .append(" " + branchName);
		try{
			Process proc = Runtime.getRuntime().exec(gitBrancBuilder.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
			   System.out.println(line);
			}
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		} 
	}

//	@Override
//	public String commit(String workspacePath, String message) throws CVSException {
//		
//		StringBuilder gitStagging = new StringBuilder();
//		gitStagging.append(workspacePath + "\\Git add -A ");
//		
//		StringBuilder gitCommitCommand = new StringBuilder();
//		gitCommitCommand.append(workspacePath + "\\Git.exe commit -m " + message );
//		try{
//			Process stagProc = Runtime.getRuntime().exec(gitStagging.toString());
//			
//			Process proc = Runtime.getRuntime().exec(gitCommitCommand.toString());
//			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//			String line;
//			while ((line = br.readLine()) != null) {
//			   System.out.println(line);
//			}
//		}catch(IOException e){
//			throw new CVSException(e.getMessage(), e.getCause());
//		}
//		return null;
//	}

	@Override
	public VCSWorkspaceMetaData commit(String workspacePath, String message) throws VCSException {
		return new VCSWorkspaceMetaData();
	}
//	public String commit(String workspacePath, String message) throws VCSException {
//		try{
//			File workingTreePath = new File(workspacePath);
//			//WorkingTree wt = WorkingTree.getInstance(workingTreePath);
//	
//			// Add modified content and commit
//			//GitAddResponse gar = wt.add();
//			
//			WorkingTree wt2 = WorkingTree.getInstance(workingTreePath);
//			wt2.addAndCommitAll(message);
//			GitCommitResponse gcr = wt2.commitAll(message);
//			Ref ref = gcr.getCommitShortHashName();
//			String shortName = ref.getName();
//			
//			return shortName;
//			
//		}catch(JavaGitException e){
//			throw new VCSException(e.getMessage(), e.getCause());
//		}
//		catch(IOException e){
//			throw new VCSException(e.getMessage(), e.getCause());
//		}
//	}
	
	
	@Override
	public void addPathOrFile(String workspacePath, String pathOrFile)
			throws VCSException {
		//return null;
	}

	@Override
	public String update(String workspacePath) throws VCSException {
		return null;
	}

	@Override
	public void pushBack(String workspacePath, String repositoryPath)
			throws VCSException {
	}
	
	public void fetchWithRepository(String workspacePath, String repositoryPath) throws VCSException{
		
	}

	public void checkout(String workspacePath, String branchName)
			throws VCSException { 
		StringBuilder gitBrancBuilder = new StringBuilder();
		gitBrancBuilder.append("Git.exe checkout ")
					   .append(branchName);
		try{
			Process proc = Runtime.getRuntime().exec(gitBrancBuilder.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
			   System.out.println(line);
			}
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void addPathOrFile(String workspacePath,
			Collection<String> pathsOrFiles) throws VCSException {
		
	}

	@Override
	public VCSWorkspaceMetaData addAllFromPath(String workspacePath) throws VCSException {
		return new VCSWorkspaceMetaData();
	}
	
	/**
	 * Return Set of paths removed from the workspace
	 * @param workspacePath - path of the observed workspace.
	 * @return Set<String> - paths of removed files.
	 * @throws VCSException - If one of NoWorkTreeException | GitAPIException | IOException occurs.
	 */
	public Set<String> getRemovedFiles(String workspacePath) throws VCSException{
		return null;
	}

	public Set<String> removeAllFromPath(String workspacePath) throws VCSException{
		return null;
	}

	@Override
	public VCSWorkspaceMetaData getStatus(String workspacePath)
			throws VCSException {
		return null;
	}
}
