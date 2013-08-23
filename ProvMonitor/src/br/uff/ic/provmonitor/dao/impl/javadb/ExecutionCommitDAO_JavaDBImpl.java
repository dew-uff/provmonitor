package br.uff.ic.provmonitor.dao.impl.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ExecutionCommitDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ExecutionCommit;
import br.uff.ic.provmonitor.utils.DateUtils;

public class ExecutionCommitDAO_JavaDBImpl implements ExecutionCommitDAO {

	@Override
	public ExecutionCommit getById(String elementId, String elementPath) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try {
			PreparedStatement psGetById = conn.prepareStatement("SELECT * FROM EXECUTION_COMMIT WHERE ID_ELEMENT LIKE ? AND PATH LIKE ? ");
			psGetById.setString(1, elementId);
			String elementPath2 = elementPath.replaceAll("\\\\", "\\\\\\\\");
			psGetById.setString(2, elementPath2);
			
			
			ResultSet rs = psGetById.executeQuery();
			
			if (rs == null || !rs.next()){
				return null;
			}else{
				ExecutionCommit executionCommit = new ExecutionCommit();
				executionCommit.setCommitId(rs.getString("ID_COMMIT"));
				executionCommit.setElementId(rs.getString("ID_ELEMENT"));
				executionCommit.setElementPath(rs.getString("PATH"));
				executionCommit.setCommitTime(rs.getDate("END_TIME"));
				executionCommit.setStatus(rs.getString("STATUS"));
				
				return executionCommit;
			}
			
		} catch (SQLException | RuntimeException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		} 
	}

	@Override
	public void persist(ExecutionCommit executionCommit) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_COMMIT (ID_ELEMENT" +
																							",STATUS" +
																							",COMMIT_TIME" +
																							",PATH" +
																							",ID_COMMIT" +
																							") values (?,?,?,?,?)");
			psInsert.setString(1, executionCommit.getElementId());
			psInsert.setString(2, executionCommit.getStatus());
			psInsert.setDate(3, DateUtils.utilsDate2SqlDate(executionCommit.getCommitTime()));
			psInsert.setString(4, executionCommit.getElementPath());
			psInsert.setString(5, executionCommit.getCommitId());
			
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
	public void update(ExecutionCommit executionCommit) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("UPDATE EXECUTION_COMMIT SET ID_ELEMENT = ? " +
																						  ",STATUS = ? " +
																						  ",COMMIT_TIME = ? " +
																						  ",PATH = ? " +
																						  ",ID_COMMIT = ? " +
																" WHERE ID_ELEMENT = ? AND PATH = ? " );
			psInsert.setString(1, executionCommit.getElementId());
			psInsert.setString(2, executionCommit.getStatus());
			psInsert.setDate(3, DateUtils.utilsDate2SqlDate(executionCommit.getCommitTime()));
			psInsert.setString(4, executionCommit.getElementPath());
			psInsert.setString(5, executionCommit.getCommitId());
			psInsert.setString(6, executionCommit.getElementId());
			psInsert.setString(7, executionCommit.getElementPath());
			
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
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_COMMIT (ID_ELEMENT, STATUS, ID_COMMIT) values (?,?,?)");
			psInsert.setString(1,"TesteParam1");
			psInsert.setString(2,"TesteParam2");
			psInsert.setString(3,"TesteParam3");
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
				String createTableSQL="CREATE TABLE EXECUTION_COMMIT ( ID_ELEMENT VARCHAR(255) NOT NULL" +
																	", STATUS VARCHAR(255) NOT NULL" +
																	", COMMIT_TIME DATETIME" +
																	", PATH VARCHAR(255)" +
																	", ID_COMMIT VARCHAR(255) NOT NULL" +
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
