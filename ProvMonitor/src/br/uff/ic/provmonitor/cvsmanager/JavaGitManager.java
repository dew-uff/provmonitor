package br.uff.ic.provmonitor.cvsmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import br.uff.ic.provmonitor.exceptions.CVSException;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.WorkingTree;
import edu.nyu.cs.javagit.api.commands.GitCommitResponse;

/**
 * CVS Manager Implementation for Git CVS
 */
public class JavaGitManager implements CVSManager{

	@Override
	public void createWorkspace(String workspace) throws CVSException {
		// TODO Auto-generated method stub
		//FileRepositoryBuilder frb = new FileRepositoryBuilder();
	}

	@Override
	public void cloneRepository(String sourceRepository, String workspacePath)
			throws CVSException {
		String command = "git.exe clone " + sourceRepository + " " + workspacePath;
		try{
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
			   System.out.println(line);
			}
		}catch(IOException e){
			throw new CVSException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void createBranch(String workspace, String branchName)
			throws CVSException {
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
			throw new CVSException(e.getMessage(), e.getCause());
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
	public String commit(String workspacePath, String message) throws CVSException {
		try{
			File workingTreePath = new File(workspacePath);
			//WorkingTree wt = WorkingTree.getInstance(workingTreePath);
	
			// Add modified content and commit
			//GitAddResponse gar = wt.add();
			
			WorkingTree wt2 = WorkingTree.getInstance(workingTreePath);
			wt2.addAndCommitAll(message);
			GitCommitResponse gcr = wt2.commitAll(message);
			Ref ref = gcr.getCommitShortHashName();
			String shortName = ref.getName();
			
			return shortName;
			
		}catch(JavaGitException e){
			throw new CVSException(e.getMessage(), e.getCause());
		}
		catch(IOException e){
			throw new CVSException(e.getMessage(), e.getCause());
		}
	}
	
	
	@Override
	public void addPathOrFile(String workspacePath, String pathOrFile)
			throws CVSException {
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public String update(String workspacePath) throws CVSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushBack(String workspacePath, String repositoryPath)
			throws CVSException {
		// TODO Auto-generated method stub
		
	}

	public void checkout(String workspacePath, String branchName)
			throws CVSException { 
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
			throw new CVSException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void addPathOrFile(String workspacePath,
			Collection<String> pathsOrFiles) throws CVSException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAllFromPath(String workspacePath) throws CVSException {
		// TODO Auto-generated method stub
		
	}
}
