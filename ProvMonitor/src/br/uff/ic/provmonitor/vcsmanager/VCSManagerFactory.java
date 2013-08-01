package br.uff.ic.provmonitor.vcsmanager;

import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

/**
 * 
 * Factory for the CVS Managers Implementations <br />
 * <b>Default: </b> GitManager
 * @see GitManager
 * @see JavaGitManager
 *
 */
public class VCSManagerFactory {
	public static VCSManager getInstance(){
		switch(ProvMonitorProperties.getInstance().getVcsType()){
		case GIT_COMAND_LINE:
			return new JavaGitManager();
		case GIT:
		default:
			return new GitManager();
		}
		
	}
}
