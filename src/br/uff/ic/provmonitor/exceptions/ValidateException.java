package br.uff.ic.provmonitor.exceptions;

/**
 * Validation Exception
 */
public class ValidateException extends ProvMonitorException {
	private static final long serialVersionUID = -1244334761169051507L;

	public ValidateException(String message){
		super(message);
	}
}
