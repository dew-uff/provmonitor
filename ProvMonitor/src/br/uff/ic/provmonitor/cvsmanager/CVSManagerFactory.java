package br.uff.ic.provmonitor.cvsmanager;

/**
 * 
 * Factory for the CVS Managers Implementations
 *
 */
public class CVSManagerFactory {
	public static CVSManager getInstance(){
		//return new JavaGitManager();
		return new GitManager();
	}
}
