package br.uff.ic.provmonitor.exceptions.vcsexceptions;

/**
 * VCS Commit related exception
 */
public class VCSCommitException extends VCSWorkspaceException {

	private static final long serialVersionUID = 7119904491028434843L;

	public VCSCommitException(String message, Throwable cause) {
		super(message, cause);
	}

}
