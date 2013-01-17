package br.uff.ic.provmonitor.exceptions;


/**
 * Embedded database server related exception
 * */
public class ServerDBException extends DatabaseException{

	private static final long serialVersionUID = 8693538344607733889L;

	public ServerDBException(String message) {
		super(message);
	}

	public ServerDBException(String message, Exception e) {
		super(message, e);
	}
}
