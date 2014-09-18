package br.uff.ic.provmonitor.exceptions.vcsexceptions;

import br.uff.ic.provmonitor.exceptions.ProvMonitorException;

public class VCSException extends ProvMonitorException{

	private static final long serialVersionUID = 676040515416931158L;

	public VCSException(String message, Throwable cause) {
		super(message, cause);
	}

}
