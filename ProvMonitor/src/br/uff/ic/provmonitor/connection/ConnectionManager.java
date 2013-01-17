package br.uff.ic.provmonitor.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.uff.ic.provmonitor.exceptions.ConnectionException;

public class ConnectionManager {
	private static ConnectionManager myInstance = null;
	private Connection conn = null;
	//private DatabaseType databaseType = DatabaseType.JAVADB;
	private DatabaseType databaseType = DatabaseType.MYSQL;
	private String dbName = "ProvMonitorTesteDB1";		
	private String user = "provmonitor";
	private String passwd = "provmonitor";
	
	private ConnectionManager () throws ConnectionException{

	    // define the connection to be used 
		switch (databaseType){
		case JAVADB:
			conn = getJavaDBConnectionURL();
			break;
		case MYSQL:
			conn = getMySQLConnectionURL();
			break;
		default:
		    if (conn == null){
		    	throw new ConnectionException("[ConnectionManager]: Connection could not be estabilished.");
		    }
		}
	}
	
	private Connection getJavaDBConnectionURL() throws ConnectionException{
		try{
			// define the driver to use 
		    String driver = "org.apache.derby.jdbc.ClientDriver";
		    
		    //Testing for driver
		    Class.forName(driver);
		    
		    // define the Derby connection URL to use 
		    String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";create=true";
		    
		    //Opening Connection
		    //conn = DriverManager.getConnection(connectionURL);
		    //return conn;
		    return DriverManager.getConnection(connectionURL);
		    
		}catch(ClassNotFoundException e){
	    	throw new ConnectionException(e.getMessage(), e);
	    }catch(SQLException e){
	    	throw new ConnectionException(e.getMessage(), e);
	    }
	}
	
	private Connection getMySQLConnectionURL() throws ConnectionException{
		try{
			// define the driver to use 
		    String driver = "com.mysql.jdbc.Driver";
		    
		    //Testing for driver
		    Class.forName(driver);
		    
		    // define the Derby connection URL to use 
		    String connectionURL = new String("jdbc:mysql://localhost:3306/") + dbName;
		    
		    //Class.forName(driver).newInstance();
		    
		    //Opening Connection
		    //return DriverManager.getConnection(connectionURL);
			return DriverManager.getConnection(connectionURL, user, passwd);
			
		}catch(ClassNotFoundException e){
	    	throw new ConnectionException(e.getMessage(), e);
	    }catch(SQLException e){
	    	throw new ConnectionException(e.getMessage(), e);
	    }
		
	}
	
	/**
	 * @return ConnectionManager instance
	 * */
	public synchronized static ConnectionManager getInstance() throws ConnectionException{
		if (myInstance == null){
			myInstance = new ConnectionManager();
		}
		return myInstance;
	}
	
	/**
	 * @return java.sql.Connection
	 * */
	public Connection getConnection() throws ConnectionException{
		if (conn == null){
			getInstance();
		}
		return conn;
	}
	
	public void endConnection() throws ConnectionException{
		try{
			if (conn != null){
				if (!conn.isClosed()){
					conn.close();
				}
			}
		}catch(SQLException e){
			throw new ConnectionException(e.getMessage(), e);
		}
		
	}
}
