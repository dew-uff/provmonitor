package br.uff.ic.provmonitor.exceptions;


/**
 * Local Database related exception
 * */
public class DatabaseException extends ProvMonitorException{

	private static final long serialVersionUID = 1072372661568169430L;
	private String sqlState;

	public String getSqlState() {
		return sqlState;
	}

	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Exception e) {
		super(message, e);
	}
	
	public DatabaseException(String message, Throwable cause){
		super(message, cause);
	}

	public DatabaseException(String message, Throwable cause, String sqlState){
		super(message, cause);
		this.sqlState = sqlState;
	}
}
