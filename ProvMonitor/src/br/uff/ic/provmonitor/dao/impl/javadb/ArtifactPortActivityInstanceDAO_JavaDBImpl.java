package br.uff.ic.provmonitor.dao.impl.javadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ArtifactPortActivityInstanceDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;

public class ArtifactPortActivityInstanceDAO_JavaDBImpl implements ArtifactPortActivityInstanceDAO {

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
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO ARTIFACT_PORT_ACTIVITY_INSTANCE (ACTIVITY_INSTANCE, ARTIFACT, PORT) values (?,?,?)");
			psInsert.setString(1,"TesteParam1");
			psInsert.setString(2,"TesteParam2");
			psInsert.setString(3,"TesteParam3");
			psInsert.executeUpdate(); 
			conn.rollback();
			
			//String selectOne = "SELECT * FROM EXECUTION_STATUS";
			//PreparedStatement s = conn.prepareStatement(selectOne);
			//ResultSet rs1 = s.executeQuery(selectOne);
			//ResultSet rs1 = s.executeQuery();
			
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
//				String createTableSQL="CREATE TABLE ARTIFACT_PORT_ACTIVITY_INSTANCE (ACTIVITY_INSTANCE VARCHAR(255) NOT NULL" +
//																				  ", ARTIFACT VARCHAR(255) NOT NULL" +
//																				  ", PORT VARCHAR(255) NOT NULL" +
//																				  ", FOREIGN KEY (ACTIVITY_INSTANCE) REFERENCES ACTIVITY_INSTANCE (INSTANCE_ID)" +
//																				  ")";
				
				String createTableSQL="CREATE TABLE ARTIFACT_PORT_ACTIVITY_INSTANCE (ACTIVITY_INSTANCE VARCHAR(255) NOT NULL" +
						  														  ", ARTIFACT VARCHAR(255) NOT NULL" +
						  														  ", PORT VARCHAR(255) NOT NULL" +
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
