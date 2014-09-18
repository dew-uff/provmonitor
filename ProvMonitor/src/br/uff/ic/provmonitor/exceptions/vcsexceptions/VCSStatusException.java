package br.uff.ic.provmonitor.exceptions.vcsexceptions;

/**
 * VCS workspace status command related exception 
 */
public class VCSStatusException extends VCSWorkspaceException {

	private static final long serialVersionUID = -8151494725712521144L;

	public VCSStatusException(String message, Throwable cause) {
		super(message, cause);
	}

}
