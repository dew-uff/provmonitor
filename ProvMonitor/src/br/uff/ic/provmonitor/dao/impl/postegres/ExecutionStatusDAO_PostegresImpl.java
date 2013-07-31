  package br.uff.ic.provmonitor.dao.impl.postegres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ExecutionStatus;
import br.uff.ic.provmonitor.utils.DateUtils;

public class ExecutionStatusDAO_PostegresImpl implements ExecutionStatusDAO{

	@Override
	public ExecutionStatus getById(String elementId, String elementPath) throws ProvMonitorException{
		Connection conn = ConnectionManager.getInstance().getConnection();
		try {
			PreparedStatement psGetById = conn.prepareStatement("SELECT * FROM EXECUTION_STATUS WHERE ID_ELEMENT LIKE ? AND PATH LIKE ? ");
			psGetById.setString(1, elementId);
			//psGetById.setString(2, elementPath);
			String elementPath2 = elementPath.replaceAll("\\\\", "\\\\\\\\");
			psGetById.setString(2, elementPath2);
			
			
			ResultSet rs = psGetById.executeQuery();
			
			if (rs == null || !rs.next()){
				return null;
			}else{
				ExecutionStatus executionStatus = new ExecutionStatus();
				executionStatus.setCommitId(rs.getString("ID_COMMIT"));
				executionStatus.setElementId(rs.getString("ID_ELEMENT"));
				executionStatus.setElementPath(rs.getString("PATH"));
				executionStatus.setElementType(rs.getString("TYPE_ELEMENT"));
				executionStatus.setEndTime(rs.getDate("END_TIME"));
				executionStatus.setPerformers(rs.getString("PERFORMERS"));
				executionStatus.setStartTime(rs.getDate("START_TIME"));
				executionStatus.setStatus(rs.getString("STATUS"));
				
				return executionStatus;
			}
			
			
		} catch (SQLException | RuntimeException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		} 
	}

	@Override
	public void persist(ExecutionStatus executionStatus)  throws ProvMonitorException{
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_STATUS (ID_ELEMENT" +
																							",TYPE_ELEMENT" +
																							",STATUS" +
																							",START_TIME" +
																							",END_TIME" +
																							",PATH" +
																							",PERFORMERS" +
																							",ID_COMMIT" +
																							") values (?,?,?,?,?,?,?,?)");
			psInsert.setString(1, executionStatus.getElementId());
			psInsert.setString(2, executionStatus.getElementType());
			psInsert.setString(3, executionStatus.getStatus());
			psInsert.setDate(4, DateUtils.utilsDate2SqlDate(executionStatus.getStartTime()));
			psInsert.setDate(5, DateUtils.utilsDate2SqlDate(executionStatus.getEndTime()));
			psInsert.setString(6, executionStatus.getElementPath());
			psInsert.setString(7, executionStatus.getPerformers());
			psInsert.setString(8, executionStatus.getCommitId());
			
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
	public void update(ExecutionStatus executionStatus) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("UPDATE EXECUTION_STATUS SET ID_ELEMENT = ? " +
																						  ",TYPE_ELEMENT = ? " +
																						  ",STATUS = ? " +
																						  ",START_TIME = ? " +
																						  ",END_TIME = ? " +
																						  ",PATH = ? " +
																						  ",PERFORMERS = ? " +
																						  ",ID_COMMIT = ? " +
																" WHERE ID_ELEMENT = ? AND PATH = ? " );
			psInsert.setString(1, executionStatus.getElementId());
			psInsert.setString(2, executionStatus.getElementType());
			psInsert.setString(3, executionStatus.getStatus());
			psInsert.setDate(4, DateUtils.utilsDate2SqlDate(executionStatus.getStartTime()));
			psInsert.setDate(5, DateUtils.utilsDate2SqlDate(executionStatus.getEndTime()));
			psInsert.setString(6, executionStatus.getElementPath());
			psInsert.setString(7, executionStatus.getPerformers());
			psInsert.setString(8, executionStatus.getCommitId());
			psInsert.setString(9, executionStatus.getElementId());
			psInsert.setString(10, executionStatus.getElementPath());
			
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
	
	/**
	 * Verify table existence in database. <br />
	 * @return <code><b>true</b></code>  - If table exists. <br />
	 * 		   <code><b>false</b></code> - If table does not exist.
	 * @throws ProvMonitorException If table could not be created. <br />
	 * 
	 */
	@Override
	public boolean isTableCreated()  throws ProvMonitorException{
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO EXECUTION_STATUS (ID_ELEMENT, TYPE_ELEMENT, STATUS) values (?,?,?)");
			psInsert.setString(1,"TesteParam1");
			psInsert.setString(2,"TesteParam2");
			psInsert.setString(3,"TesteParam3");
			psInsert.executeUpdate(); 
			
			
//			//Testando RollBack e Controle manual de transação			
//			String selectOne = "SELECT * FROM EXECUTION_STATUS";
//			Statement s = conn.createStatement();
//			ResultSet rs1 = s.executeQuery(selectOne);
//			while (rs1.next())
//			{
//			      System.out.println("SelectOne: " + rs1.getString(1));
//			}
			
			conn.rollback();
			
			
//			String selectTwo = "SELECT * FROM EXECUTION_STATUS";
//			s = conn.createStatement();
//			ResultSet rs2 = s.executeQuery(selectTwo);
//			while (rs2 != null && rs2.next())
//			{
//			      System.out.println("SelectTwo: " + rs2.getString(1));
//			}
			
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

	/**
	 * Create table in database. <br />
	 * @throws ProvMonitorException If table could not be created. <br />
	 * @throws DatabaseException <code>Database related problems.</code><br />
	 * 		<ul>
	 * 			<li>ConnectionException - Connection problems related.</li>
	 * 			<li>DatabaseException - Problems with the table creation itself.</li>
	 * 		</ul>
	 * */
	@Override
	public void createTable() throws ProvMonitorException{
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
				String createTableSQL="CREATE TABLE EXECUTION_STATUS ( ID_ELEMENT VARCHAR(255) NOT NULL" +
																	", TYPE_ELEMENT VARCHAR(255) NOT NULL" +
																	", STATUS VARCHAR(255) NOT NULL" +
																	", START_TIME TIMESTAMP" +
																	", END_TIME TIMESTAMP" +
																	", PATH VARCHAR(255)" +
																	", PERFORMERS VARCHAR(255)" +
																	", ID_COMMIT VARCHAR(255)" +
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
