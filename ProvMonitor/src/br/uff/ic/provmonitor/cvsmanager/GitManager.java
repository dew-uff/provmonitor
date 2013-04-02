package br.uff.ic.provmonitor.cvsmanager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import br.uff.ic.provmonitor.exceptions.CVSException;

public class GitManager implements CVSManager {

	@Override
	public void createWorkspace(String workspace) throws CVSException {
		Repository repository;
		try {
			repository = getRepository(workspace);
			repository.create();
		} catch (IOException e) {
			throw new CVSException("Could not create repository: " + e.getMessage(), e.getCause());
		}
	}
	
	@Override
	public void cloneRepository(String sourceRepository, String workspacePath)
			throws CVSException {
		try {

			CloneCommand cCom = Git.cloneRepository();
			cCom.setURI(sourceRepository);
			File newPath = new File(workspacePath);
			cCom.setDirectory(newPath);			
		
			//First Try cloning everything
			cCom.setCloneAllBranches(true);
			
			cCom.call();
		} catch (GitAPIException e) {
			throw new CVSException("Could not clone repository: " + e.getMessage(), e.getCause());
		}
		
		
	}

	@Override
	public void createBranch(String workspace, String branchName)
			throws CVSException {
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
			throw new CVSException("Could not create branch: " + e.getMessage(), e.getCause());
		}
		
	}

	@Override
	public String commit(String workspacePath, String message)
			throws CVSException {
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
			throw new CVSException("Commit error: " + e.getMessage(), e.getCause());
		}
	}

	@Override
	public void addPathOrFile(String workspacePath, String pathOrFile)
			throws CVSException {
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	        add.addFilepattern(pathOrFile);
	        add.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new CVSException("Error stagging files or paths: " + e.getMessage(), e.getCause());
		}
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

	@Override
	public void checkout(String workspacePath, String branchName) throws CVSException {
		
		Repository repository;
		try {
			repository = getRepository(workspacePath);
			
			Git git = new Git(repository);
			
			CheckoutCommand co = git.checkout();
	        co.setName(branchName);
	        co.setCreateBranch(false);
	        co.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new CVSException(e.getMessage(), e.getCause());
		}
		
	}
	
	/**
	 * Return an instance of the Git repository to be manipulated
	 * */
	private Repository getRepository(String sourceURI) throws IOException{
		FileRepositoryBuilder frb = new FileRepositoryBuilder();
		Repository repository = frb.setGitDir(new File(sourceURI))
				  .readEnvironment() // scan environment GIT_* variables
				  .findGitDir() // scan up the file system tree
				  .build();
		
		return repository;
	}

	@Override
	public void addPathOrFile(String workspacePath,
			Collection<String> pathsOrFiles) throws CVSException {
		
		
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
			throw new CVSException(e.getMessage(), e.getCause());
		}
		
	}
	
	public void addAllFromPath(String workspacePath) throws CVSException{
		try {
			Repository repository = getRepository(workspacePath);
			Git git = new Git(repository);
			
			AddCommand add = git.add();
	
			add.addFilepattern(".");
	        
	        //add.addFilepattern(".txt");
	        
	        add.call();
	        
		} catch (IOException | GitAPIException e) {
			throw new CVSException(e.getMessage(), e.getCause());
		}
	}

}
