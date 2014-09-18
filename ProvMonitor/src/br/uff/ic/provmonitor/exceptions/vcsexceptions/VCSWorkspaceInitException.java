package br.uff.ic.provmonitor.exceptions.vcsexceptions;

/**
 * Workspace initialization related exception
 */
public class VCSWorkspaceInitException extends VCSWorkspaceCreationException {
	private static final long serialVersionUID = 7789919674993807388L;

	public VCSWorkspaceInitException(String message, Throwable cause) {
		super(message, cause);
	}

}
