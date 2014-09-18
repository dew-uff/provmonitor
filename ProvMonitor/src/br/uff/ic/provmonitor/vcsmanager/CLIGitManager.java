package br.uff.ic.provmonitor.vcsmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Set;

import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSAddFilesException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSCommitException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSStatusException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSWorkspaceCreationException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSWorkspaceInitException;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

public class CLIGitManager implements VCSManager {
	private static final String FILE_ADDED_FLAG="add";
	private static final String FILE_CREATED_FLAG="A";
	private static final String FILE_MODIFIED_FLAG="M";
	private static final String FILE_REMOVED_FLAG="D";

	@Override
	public boolean isWorkspaceCreated(String workspace) throws VCSException {
		StringBuilder sb = new StringBuilder();
		sb.append("git status ");
		
		String command = sb.toString();
		try{
			//Process proc = Runtime.getRuntime().exec(command);
			File dirPath = new File(workspace);
			Process proc = Runtime.getRuntime().exec(command, null, dirPath);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			boolean error = false;
			boolean success = false;
			if (brError.ready()){
				error = true;
			}
			if (br.ready()){
				success = true;
			}
			
			//String line;
			//while ((line = br.readLine()) != null) {
			//   System.out.println(line);
			//}
			//while ((line = brError.readLine()) != null) {
			//	   System.out.println(line);
			//}
			
			return (!error && success);
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
		//return false;
		//return !error;
	}

	@Override
	@SuppressWarnings("unused")
	public void createWorkspace(String workspace) throws VCSException {
		try{
			//Workspace initialization
			workspaceInit(workspace);
			
			//Adding all files to the new workspace
			VCSWorkspaceMetaData vcsAddMD = addAllFromPath(workspace);
			
			//Retrieving workspace status
			VCSWorkspaceMetaData vcsStatusMD = getStatus(workspace);
			
			//Initial commit for workspace creation
			VCSWorkspaceMetaData vcsCommitMD = commit(workspace, "Workspace creation.");
		}catch(VCSWorkspaceInitException e){
			throw new VCSWorkspaceCreationException(e.getMessage(), e);
		}catch(VCSAddFilesException e){
			throw new VCSWorkspaceCreationException(e.getMessage(), e);
		}
	}
	
	private void workspaceInit(String workspace) throws VCSException{
		StringBuilder sb = new StringBuilder();
		sb.append("git init ");
		
		String command = sb.toString();
		try{
			//Process proc = Runtime.getRuntime().exec(command);
			File dirPath = new File(workspace);
			Process proc = Runtime.getRuntime().exec(command, null, dirPath);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line;
			if (brError.ready()){
				StringBuilder errorMessage = new StringBuilder();
				while ((line = brError.readLine()) != null) {
				   System.out.println(line);
				   errorMessage.append(line);
				}
				throw new VCSWorkspaceInitException(errorMessage.toString(), null);
			}
			if (br.ready()){
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
			
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void cloneRepository(String sourceRepository, String workspacePath)
			throws VCSException {
		cloneRepository(sourceRepository, workspacePath, null);

	}

	@Override
	public void cloneRepository(String sourceRepository, String workspacePath,
			Collection<String> cloneOnlyBranches) throws VCSException {
		
		switch (ProvMonitorProperties.getInstance().getGitCloneOptions()){
		case DEFAULT:
			//cloneRepositoryWithTemp(sourceRepository, workspacePath);
			cloneRepositoryWithoutTempThroughGitCLI(sourceRepository, workspacePath, false, false);
			break;
		case SHARE:
			cloneRepositoryWithoutTempThroughGitCLI(sourceRepository, workspacePath, true, false);
			break;
		case HARDLINK:
			cloneRepositoryWithoutTempThroughGitCLI(sourceRepository, workspacePath, false, true);
			break;
		case SHARE_HARDLINK:
			cloneRepositoryWithoutTempThroughGitCLI(sourceRepository, workspacePath, true, true);
			break;
		}

	}

	@Override
	public void createBranch(String workspace, String branchName)
			throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void pushBack(String workspacePath, String repositoryPath)
			throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void fetchWithRepository(String workspacePath, String repositoryPath)
			throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	public VCSWorkspaceMetaData addAllFromPath(String workspacePath) throws VCSException {
		StringBuilder sb = new StringBuilder();
		sb.append("git add -A -v");
		VCSWorkspaceMetaData vcsWkspMd = new VCSWorkspaceMetaData();
		
		String command = sb.toString();
		try{
			//Process proc = Runtime.getRuntime().exec(command);
			File dirPath = new File(workspacePath);
			Process proc = Runtime.getRuntime().exec(command, null, dirPath);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line;
			if (brError.ready()){
				StringBuilder errorMessage = new StringBuilder();
				while ((line = brError.readLine()) != null) {
				   System.out.println(line);
				   errorMessage.append(line);
				}
				throw new VCSAddFilesException(errorMessage.toString(), null);
			}
			if (br.ready()){
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					//StringTokenizer st = new StringTokenizer(line," ");
					String [] files = line.split(" ");
					if (files != null && files.length > 1){
						if (FILE_ADDED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getCreated().add(fileName);
						}
					}
				}
			}
			
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
		
		return vcsWkspMd;
	}

	@Override
	public VCSWorkspaceMetaData getStatus(String workspacePath)	throws VCSException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("git status --porcelain");
		VCSWorkspaceMetaData vcsWkspMd = new VCSWorkspaceMetaData();
		
		String command = sb.toString();
		try{
			//Process proc = Runtime.getRuntime().exec(command);
			File dirPath = new File(workspacePath);
			Process proc = Runtime.getRuntime().exec(command, null, dirPath);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line;
			if (brError.ready()){
				StringBuilder errorMessage = new StringBuilder();
				while ((line = brError.readLine()) != null) {
				   System.out.println(line);
				   errorMessage.append(line);
				}
				throw new VCSStatusException(errorMessage.toString(), null);
			}
			if (br.ready()){
				while ((line = br.readLine()) != null) {
					System.out.println(line);
					//StringTokenizer st = new StringTokenizer(line," ");
					String [] files = line.split(" ");
					if (files != null && files.length > 1){
						if (FILE_CREATED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getCreated().add(fileName);
						}
						else if (FILE_MODIFIED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getChanged().add(fileName);
						}
						else if (FILE_REMOVED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getRemoved().add(fileName);
						}
					}
				}
			}
			
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
		
		return vcsWkspMd;
	}

	@Override
	public void addPathOrFile(String workspacePath, String pathOrFile)
			throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPathOrFile(String workspacePath,
			Collection<String> pathsOrFiles) throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkout(String workspacePath, String branchName)
			throws VCSException {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public String update(String workspacePath) throws VCSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VCSWorkspaceMetaData commit(String workspacePath, String message) throws VCSException {

		StringBuilder sb = new StringBuilder();
		sb.append("git commit -a -m \"")
		  .append(message)
		  .append("\"");
		String command = sb.toString();
		sb.append(" --porcelain");
		String commandPorcelain = sb.toString();
		
		VCSWorkspaceMetaData vcsWkspMd = new VCSWorkspaceMetaData();
		try{
			File dirPathPorcelain = new File(workspacePath);
			Process procProcelain = Runtime.getRuntime().exec(commandPorcelain, null, dirPathPorcelain);
			
			BufferedReader brPorcelain = new BufferedReader(new InputStreamReader(procProcelain.getInputStream()));
			BufferedReader brErrorPorcelain = new BufferedReader(new InputStreamReader(procProcelain.getErrorStream()));
			String linePorcelain;
			if (brErrorPorcelain.ready()){
				StringBuilder errorMessage = new StringBuilder();
				while ((linePorcelain = brErrorPorcelain.readLine()) != null) {
				   System.out.println(linePorcelain);
				   errorMessage.append(linePorcelain);
				}
				throw new VCSStatusException(errorMessage.toString(), null);
			}
			if (brPorcelain.ready()){
				while ((linePorcelain = brPorcelain.readLine()) != null) {
					System.out.println(linePorcelain);
					//StringTokenizer st = new StringTokenizer(line," ");
					String [] files = linePorcelain.split(" ");
					if (files != null && files.length > 1){
						if (FILE_CREATED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getCreated().add(fileName);
						}
						else if (FILE_MODIFIED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getChanged().add(fileName);
						}
						else if (FILE_REMOVED_FLAG.equalsIgnoreCase(files[0])){
							String fileName = files[1];
							vcsWkspMd.getRemoved().add(fileName);
						}
					}
				}
			}
			
			File dirPath = new File(workspacePath);
			Process proc = Runtime.getRuntime().exec(command, null, dirPath);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			BufferedReader brError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			String line;
			if (brError.ready()){
				StringBuilder errorMessage = new StringBuilder();
				while ((line = brError.readLine()) != null) {
				   System.out.println(line);
				   errorMessage.append(line);
				}
				throw new VCSCommitException(errorMessage.toString(), null);
			}
			if (br.ready()){
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
			
		}catch(IOException e){
			throw new VCSException(e.getMessage(), e.getCause());
		}
		
		return vcsWkspMd;
	}

	@Override
	public Set<String> removeAllFromPath(String workspacePath)
			throws VCSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getRemovedFiles(String workspacePath)
			throws VCSException {
		// TODO Auto-generated method stub
		return null;
	}

	private void cloneRepositoryWithoutTempThroughGitCLI(String sourceRepository, String workspacePath, Boolean share, Boolean hardlinks) throws VCSException{
		StringBuilder sb = new StringBuilder();
		sb.append("git clone ");
		if (share)
			sb.append(" --share ");
		if (hardlinks)
			sb.append(" --hardlinks");
		
		sb.append(sourceRepository)
		  .append(" ")
		  .append(workspacePath);
		
		String command = sb.toString();
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
}
