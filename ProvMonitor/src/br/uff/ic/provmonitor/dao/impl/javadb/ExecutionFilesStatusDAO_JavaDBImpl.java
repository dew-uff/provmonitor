package br.uff.ic.provmonitor.dao.impl.javadb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collection;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ExecutionFilesStatusDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ExecutionFilesStatus;
import br.uff.ic.provmonitor.model.ExecutionStatus;
import br.uff.ic.provmonitor.utils.DateUtils;

public class ExecutionFilesStatusDAO_JavaDBImpl implements ExecutionFilesStatusDAO{

	@Override
	public ExecutionStatus getById(String elementId, String elementPath)
			throws ProvMonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(ExecutionFilesStatus executionFileStatus) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_FILE_STATUS (ID_ELEMENT" +
																								 ",ELEMENT_PATH" +
																								 ",FILE_PATH" +
																								 ",FILE_ACCESS_TYPE" +
																								 ",FILE_ACCESS_TIME" +
																								 ") values (?,?,?,?,?)");
			psInsert.setString(1, executionFileStatus.getElementId());
			psInsert.setString(2, executionFileStatus.getElementPath());
			psInsert.setString(3, executionFileStatus.getFilePath());
			psInsert.setString(4, executionFileStatus.getFiletAccessType());
			psInsert.setDate(5, DateUtils.utilsDate2SqlDate(executionFileStatus.getFileAccessDateTime()));
			
			psInsert.executeUpdate(); 
			
			conn.commit();
			
			
		}catch(SQLException e){
			//Schema object does not exist
			try{
				conn.rollback();
			}catch(SQLException ex){
			}
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
		
	}
	
	public void persit(Collection<ExecutionFilesStatus> filesCollection){
		for (ExecutionFilesStatus execFile: filesCollection){
			
		}
	}

	private void persist(ExecutionFilesStatus executionFileStatus, Connection conn) throws DatabaseException{
		try{
			
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_FILE_STATUS (ID_ELEMENT" +
																								 ",ELEMENT_PATH" +
																								 ",FILE_PATH" +
																								 ",FILE_ACCESS_TYPE" +
																								 ",FILE_ACCESS_TIME" +
																								 ") values (?,?,?,?,?)");
			psInsert.setString(1, executionFileStatus.getElementId());
			psInsert.setString(2, executionFileStatus.getElementPath());
			psInsert.setString(3, executionFileStatus.getFilePath());
			psInsert.setString(4, executionFileStatus.getFiletAccessType());
			psInsert.setDate(5, DateUtils.utilsDate2SqlDate(executionFileStatus.getFileAccessDateTime()));
			
			psInsert.executeUpdate(); 
			
			conn.commit();
			
			
		}catch(SQLException e){
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}
	@Override
	public void update(ExecutionFilesStatus executionFileStatus) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("UPDATE EXECUTION_FILE_STATUS SET ID_ELEMENT = ? " +
																							   ",ELEMENT_PATH = ? " +
																							   ",FILE_PATH = ? " +
																							   ",FILE_ACCESS_TYPE = ? " +
																							   ",FILE_ACCESS_TIME = ? " +
																" WHERE ID_ELEMENT = ? AND ELEMENT_PATH = ? " );
			psInsert.setString(1, executionFileStatus.getElementId());
			psInsert.setString(2, executionFileStatus.getElementPath());
			psInsert.setString(3, executionFileStatus.getFilePath());
			psInsert.setString(4, executionFileStatus.getFiletAccessType());
			psInsert.setDate(5, DateUtils.utilsDate2SqlDate(executionFileStatus.getFileAccessDateTime()));
			
			psInsert.setString(6, executionFileStatus.getElementId());
			psInsert.setString(7, executionFileStatus.getElementPath());
			
			psInsert.executeUpdate(); 
			
			conn.commit();
			
			
		}catch(SQLException e){
			//Schema object does not exist
			try{
				conn.rollback();
			}catch(SQLException ex){
			}
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
		
		
	}

	@Override
	public boolean isTableCreated() throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_FILE_STATUS (ID_ELEMENT, ELEMENT_PATH, FILE_PATH, FILE_ACCESS_TYPE, FILE_ACCESS_TIME) values (?,?,?,?,?)");
			psInsert.setString(1,"TesteParam1");
			psInsert.setString(2,"TesteParam2");
			psInsert.setString(3,"TesteParam3");
			psInsert.setString(4,"TesteParam4");
			psInsert.setDate(5, (Date) Calendar.getInstance().getTime());
			
			psInsert.executeUpdate(); 
			
			conn.rollback();
			
			return true;
			
		}catch(SQLException e){
			//Schema object does not exist
			try{
				conn.rollback();
			}catch(SQLException ex){
			}
			return false;
		}
	}

	@Override
	public void createTable() throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		Statement s = null;
		try{
			try{
				//Preparing statement
				s = conn.createStatement();
				//Transaction control
				conn.setAutoCommit(false);
				
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			if (s != null){
				String createTableSQL="CREATE TABLE EXECUTION_FILE_STATUS ( ID_ELEMENT VARCHAR(255) NOT NULL" +
																		 ", ELEMENT_PATH VARCHAR(255) NULL" +
																		 ", FILE_PATH VARCHAR(255)" +
																		 ", FILE_ACCESS_TYPE VARCHAR(255)" +
																		 ", FILE_ACCESS_TIME DATETIME" +
																		 ")";
				
				s.executeUpdate(createTableSQL);
				conn.commit();
			}
		}catch(SQLException e){
			try{
				conn.rollback();
			}catch(SQLException ex){
			}
			throw new DatabaseException(e.getMessage(), e.getCause());
		}
	}

}
