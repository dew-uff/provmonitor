package br.uff.ic.provmonitor.exceptions.vcsexceptions;

/**
 * 
 * Workspace creation exception when trying to create a workspace on a path that already has an initialized workspace.
 *
 */
public class VCSWorkspaceAlreadyExistsException extends VCSWorkspaceCreationException {
	
	private static final long serialVersionUID = 542280759571655891L;

	public VCSWorkspaceAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
