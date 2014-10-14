package br.uff.ic.provmonitor.exceptions;

/**
 * ProvMonitor base Exception
 */
public class ProvMonitorException extends Exception{
	private static final long serialVersionUID = -2990447650830960823L;

	public ProvMonitorException(String message){
		super(message);
	}
	
	public ProvMonitorException(String message, Exception e){
		super(message, e);
	}
	
	public ProvMonitorException(String message, Throwable cause){
		super(message, cause);
	}
}
