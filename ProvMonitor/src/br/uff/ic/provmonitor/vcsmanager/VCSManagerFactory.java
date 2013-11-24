package br.uff.ic.provmonitor.vcsmanager;

import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

/**
 * 
 * Factory for the CVS Managers Implementations <br />
 * <b>Default: </b> GitManager
 * @see JGitManager
 * @see CommandLineGitManager
 *
 */
public class VCSManagerFactory {
	public static VCSManager getInstance(){
		switch(ProvMonitorProperties.getInstance().getVcsType()){
		case GIT_COMAND_LINE:
			return new CommandLineGitManager();
		case GIT:
		default:
			return new JGitManager();
		}
		
	}
}
