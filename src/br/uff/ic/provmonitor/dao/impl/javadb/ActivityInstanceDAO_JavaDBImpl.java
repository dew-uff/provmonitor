package br.uff.ic.provmonitor.dao.impl.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ActivityInstanceDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ActivityInstance;

public class ActivityInstanceDAO_JavaDBImpl implements ActivityInstanceDAO{

	@Override
	public ActivityInstance getById(String activityId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(ActivityInstance activityInstance) {
		// TODO Auto-generated method stub
		
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
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO ACTIVITY_INSTANCE (INSTANCE_ID, ACTIVITY_INSTANCE_ID) values (?,?)");
			psInsert.setString(1,"TesteParam1");
			psInsert.setString(2,"TesteParam2");
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
				String createTableSQL="CREATE TABLE ACTIVITY_INSTANCE (INSTANCE_ID VARCHAR(255) NOT NULL" +
																	", ACTIVITY_INSTANCE_ID VARCHAR(255) NOT NULL" +
																	", ACTIVITY_ID VARCHAR(255)" +
																	", NAME VARCHAR(255)" +
																	", PRIMARY KEY (INSTANCE_ID)" +
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
