package br.uff.ic.provmonitor.exceptions.vcsexceptions;

/**
 * VCS workspace creation error exception 
 */
public class VCSWorkspaceCreationException extends VCSWorkspaceException{

	private static final long serialVersionUID = -7882166548911218507L;

	public VCSWorkspaceCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}
