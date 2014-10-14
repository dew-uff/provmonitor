package br.uff.ic.provmonitor.exceptions;

import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;

public class VCSCheckOutConflictException extends VCSException {

	private static final long serialVersionUID = -674730758899420747L;

	public VCSCheckOutConflictException(String message, Throwable cause) {
		super(message, cause);
	}

}
