package br.uff.ic.provmonitor.vcsmanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import br.uff.ic.provmonitor.exceptions.VCSException;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.output.ProvMonitorOutputManager;

/**
 * 
 * CVS Manager Implementation for Git CVS.
 * <br /><p>
 * <b>Technology:</b> Uses jGit API.</p>
 * 
 * */
public class GitManager implements VCSManager {

	@Override
	public void createWorkspace(String workspace) throws VCSException {
		Repository repository;
		try {
			repository = getRepository(workspace);
			repository.create();
		} catch (IOException e) {
			throw new VCSException("Could not create repository: " + e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * TODO: Implement File Visitors on different external classes.
	 * TODO: Identify how to exclude temporary workspace. 
	 */
	@Override
	public void cloneRepository(String sourceRepository, String workspacePath) throws VCSException {
		try {

			try{
				//Trying to clone to an empty directory.
				this.cloneRepositoryToEmptyFolder(sourceRepository, workspacePath);
			}catch (VCSException ge){
				try {
					//If it fails, try to clone to an existing repository.
					
					//Creating temporary workspace.
					SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmss");
					String nonce = sf.format(Calendar.getInstance().getTime());
					
					String workspacePathTmp;
					
					String[] wps = workspacePath.split("/");
					if (wps != null && wps.length > 1){
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < (wps.length - 1); i++){
							if (sb.length() > 0){
								sb.append("/");
							}
							sb.append(wps[i]);
						}
						sb.append("/" + wps[(wps.length - 1)] + "_tmp" + nonce);
						workspacePathTmp = sb.toString();
					}else{
						throw new IOException("Invalid workspace path");
					}
					
					this.cloneRepositoryToEmptyFolder(sourceRepository, workspacePathTmp);
					
					//Moving from temp folder to base workspace
					final Path origin = Paths.get(workspacePathTmp);
					final Path destiny = Paths.get(workspacePath);
					//Files.copy(origin, destiny.resolve(origin.getFileName()), StandardCopyOption.REPLACE_EXISTING);
					
					
					//Coping Temp repository
					Files.walkFileTree(origin, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,
							new SimpleFileVisitor<Path>() {
					       		@Override
					       		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
					       				throws IOException
					       		{
					       			Path targetdir = destiny.resolve(origin.relativize(dir));
					       			try {
					       				Files.copy(dir, targetdir);
					       			} catch (FileAlreadyExistsException e) {
					       				if (!Files.isDirectory(targetdir))
					       					throw e;
					       			}
					       			return FileVisitResult.CONTINUE;
					       		}
					       		@Override
					       		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
					       			Files.copy(file, destiny.resolve(origin.relativize(file)));
					       			return FileVisitResult.CONTINUE;
					       		}
					});

					//Removing Temp repository
					try{
						Files.walkFileTree(origin, new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
								if (!file.getFileSystem().isReadOnly()){
									Files.deleteIfExists(file);
								}
								return FileVisitResult.CONTINUE;
					        }
					        @Override
					        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException{
					        	if (e == null) {
					        		if (!dir.getFileSystem().isReadOnly()){
					        			Files.deleteIfExists(dir);
					        		}
					                return FileVisitResult.CONTINUE;
					            } else {
					            	// directory iteration failed
					                throw e;
					            }
					        }
						});
					}catch(Exception e){
						ProvMonitorLogger.warning("Temporary repository not removed: " + workspacePathTmp);
						ProvMonitorOutputManager.appendMenssage("Warning: Temporary repository not removed: " + workspacePathTmp);
					}
					
					//Reseting workspace
					try{
						Repository rep = getRepository(workspacePath);
						Git git = new Git(rep);
						ResetCommand rc = git.reset();
						rc.setRef(Constants.HEAD);
						rc.call();
					}catch(Throwable t){
						//throw new VCSException(t.getMessage(), t.getCause());
						ProvMonitorLogger.warning("Could not reset workspace's repositopry. Error: " + t.getMessage());
						ProvMonitorOutputManager.appendMenssage("Warning: Could not reset workspace's repositopry. Error: " + t.getMessage());
					}
					
				}catch (IOException e) {
					throw new VCSException(ge.getMessage() + e.getMessage(), e.getCause());
				}
			}
		} catch (VCSException e) {
			throw new VCSException("Could not clone repository: " + e.getMessage(), e.getCause());
		}
	}
	
	private void cloneRepositoryToEmptyFolder(String sourceRepository, String workspacePath) throws VCSException {
		try {

			CloneCommand cCom = Git.cloneRepository();
			
			cCom.setBare(false);
			
			cCom.setURI(sourceRepository);
			File newPath = new File(workspacePath);
			cCom.setDirectory(newPath);	
			
		
			//First Try cloning everything
			cCom.setCloneAllBranches(true);
			
			//Cloning only Trunk
//			cCom.setCloneAllBranches(false);
//			
//			Collection<String> branchCollection = new ArrayList<String>();
//			branchCollection.add("master");
//			cCom.setBranchesToClone(branchCollection);
			
			cCom.call();
			
		} catch (Throwable e) {
			throw new VCSException(e.getLocalizedMessage(), e.getCause());
			//throw new VCSException("Could not clone repository: " + e.getMessage(), e.getCause());
		}
	}
	
	/**
	 * Clone only the specified branches. Or Full clone when branches collection is null.
	 * 
	 * @param sourceRepository - String - Source Repository to be cloned.
	 * @param workspacePath - String - Destiny workspace path.
	 * @param cloneOnlyBranches - Collection< String > - Branches to be cloned. Null for Full clone.
	 * 
	 * */
	public void cloneRepository(String sourceRepository, String workspacePath, Collection<String> cloneOnlyBranches) throws VCSException{
		try {
		Boolean fullClone = cloneOnlyBranches==null?true:false;
		
		CloneCommand cCom = Git.cloneRepository();
		cCom.setBare(false);
		
		cCom.setURI(sourceRepository);
		File newPath = new File(workspacePath);
		cCom.setDirectory(newPath);			
		
		if (fullClone){
			//Cloning everything
			cCom.setCloneAllBranches(true);
		}else{
			//Cloning only branches
			cCom.setCloneAllBranches(false);
			cCom.setBranchesToClone(cloneOnlyBranches);
			cCom.setCloneSubmodules(false);
		} 
		
		cCom.call();
		
		} catch (GitAPIException e) {
			throw new VCSException("Could not clone repository: " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public void createBranch(String workspace, String branchName)
			throws VCSException {
		Repository repository;
		try {
			repository = getRepository(workspace);
			Git git = new Git(repository);
//			git.branchCreate().setName(branchName).call();
			
			CheckoutCommand co = git.checkout();
	        co.setName(branchName);
	        co.setCreateBranch(true);
	        co.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new VCSException("Could not create branch: " + e.getMessage(), e.getCause());
		}
		
	}

	@Override
	public String commit(String workspacePath, String message)
			throws VCSException {
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			CommitCommand commit = git.commit();
	        RevCommit revision;
	        
			revision = commit.setMessage(message).call();
			String revisionId = revision.getId().getName();
			
			//System.out.println(revision.getShortMessage());
			
			return revisionId;
        
		} catch (GitAPIException | IOException e) {
			throw new VCSException("Commit error: " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public void addPathOrFile(String workspacePath, String pathOrFile)
			throws VCSException {
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	        add.addFilepattern(pathOrFile);
	        add.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new VCSException("Error stagging files or paths: " + e.getMessage(), e.getCause());
		}
	}

	
	
	@Override
	public String update(String workspacePath) throws VCSException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushBack(String workspacePath, String repositoryPath) throws VCSException {
		try{
			
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			PushCommand pc = git.push();
			
			pc.setRemote(repositoryPath);
			
			pc.call();
		
		}catch (IOException | GitAPIException  e){
			throw new VCSException("Error pushing back repository to central repository: " + e.getMessage(), e.getCause());
		}
		
		
	}

	@Override
	public void checkout(String workspacePath, String branchName) throws VCSException {
		
		Repository repository;
		try {
			repository = getRepository(workspacePath);
			
			Git git = new Git(repository);
			
			CheckoutCommand co = git.checkout();
	        co.setName(branchName);
	        co.setCreateBranch(false);
	        co.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new VCSException(e.getMessage(), e.getCause());
		}
		
	}
	
	/**
	 * Return an instance of the Git repository to be manipulated
	 * */
	private Repository getRepository(String sourceURI) throws IOException{
		FileRepositoryBuilder frb = new FileRepositoryBuilder();
		String sourceRepositoryURI = new String(sourceURI);
		
		ProvMonitorLogger.debug("GitManager", "getRepository", "Verifying OS files and paths separator.");
		if (sourceRepositoryURI.contains("\\")){
			sourceRepositoryURI = sourceRepositoryURI.concat("\\.git");
			ProvMonitorLogger.debug("GitManager", "getRepository", "Using \\.git");
		}else{
			sourceRepositoryURI = sourceRepositoryURI.concat("/.git");
			ProvMonitorLogger.debug("GitManager", "getRepository", "Using /.git");
		}
		Repository repository = frb.setGitDir(new File(sourceRepositoryURI))
				  .readEnvironment() // scan environment GIT_* variables
				  .findGitDir() // scan up the file system tree
				  .build();
		ProvMonitorLogger.debug("GitManager", "getRepository", "SourceURI: " + sourceRepositoryURI);
		return repository;
	}

	@Override
	public void addPathOrFile(String workspacePath,
			Collection<String> pathsOrFiles) throws VCSException {
		
		
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	
	        for (String path : pathsOrFiles) {
	            add.addFilepattern(path);
	        }
	        
	        //add.addFilepattern(".txt");
	        
	        add.call();
			
		} catch (IOException | GitAPIException e) {
			throw new VCSException(e.getMessage(), e.getCause());
		}
		
	}
	
	public void addAllFromPath(String workspacePath) throws VCSException{
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	
			add.addFilepattern(".");
	        
	        //add.addFilepattern(".txt");
	        
			add.call();

			//StatusCommand sc = git.status();
			//Status status = sc.call();
			//status.getAdded();
			
	        //DirCache dir = add.call();
	        
	        //dir.getEntryCount();
	        
	        
		} catch (IOException | GitAPIException e) {
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}
	
	public Set<String> removeAllFromPath(String workspacePath) throws VCSException{
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	
			add.addFilepattern(".");
			add.setUpdate(true);
	        
	        //add.addFilepattern(".txt");
	        
			add.call();

			StatusCommand sc = git.status();
			Status status = sc.call();
			return status.getRemoved();
			
	        //DirCache dir = add.call();
	        
	        //dir.getEntryCount();
	        
	        
		} catch (IOException | GitAPIException e) {
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}
	
	public Set<String> getRemovedFiles(String workspacePath) throws VCSException{
		try {	
			Repository repo = getRepository(workspacePath);
			Git git = new Git(repo);
			
			StatusCommand sc = git.status();
			Status status;
			
			status = sc.call();
			return status.getRemoved();
			
		} catch (NoWorkTreeException | GitAPIException | IOException e) {
			throw new VCSException(e.getMessage(), e.getCause());
		}
	}
	
	@SuppressWarnings("unused")
	public void getStatus(String workspacePath){
		try {	
			Repository repo = getRepository(workspacePath);
			Git git = new Git(repo);
			
			StatusCommand sc = git.status();
			Status status;
			
			status = sc.call();
			Set<String> untracked = status.getUntracked();
			Set<String> added = status.getAdded();
			Set<String> modified = status.getModified();
			Set<String> changed = status.getChanged();
			
		} catch (NoWorkTreeException | GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
