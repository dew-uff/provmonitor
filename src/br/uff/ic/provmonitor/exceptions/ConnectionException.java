package br.uff.ic.provmonitor.exceptions;

/**
 * Local Database connection related exception
 * */
public class ConnectionException extends DatabaseException{

	private static final long serialVersionUID = -1821201566507740863L;

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(String message, Exception e) {
		super(message, e);
	}
	
	public ConnectionException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ConnectionException(String message, Throwable cause, String sqlState){
		super(message, cause, sqlState);
	}
}
