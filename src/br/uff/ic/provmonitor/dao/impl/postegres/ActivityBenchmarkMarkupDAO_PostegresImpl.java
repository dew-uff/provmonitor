package br.uff.ic.provmonitor.dao.impl.postegres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import br.uff.ic.provmonitor.connection.ConnectionManager;
import br.uff.ic.provmonitor.dao.ActivityBenchmarkMarkupDAO;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ActivityBenchmarkMarkup;
import br.uff.ic.provmonitor.utils.DateUtils;

public class ActivityBenchmarkMarkupDAO_PostegresImpl implements ActivityBenchmarkMarkupDAO {

	@Override
	public ActivityBenchmarkMarkup getById(String activityInstanceId) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try {
			PreparedStatement psGetById = conn.prepareStatement("SELECT * FROM ACTIVITY_BENCHMARK_MARKUP WHERE ACTIVITY_INSTANCE_ID LIKE ? ");
			psGetById.setString(1, activityInstanceId);
			
			
			ResultSet rs = psGetById.executeQuery();
			
			if (rs == null || !rs.next()){
				return null;
			}else{
				ActivityBenchmarkMarkup activityBenchmarkMarkup = new ActivityBenchmarkMarkup();
				activityBenchmarkMarkup.setExperimentId(rs.getString("EXPERIMENT_ID"));
				activityBenchmarkMarkup.setExperimentInstanceId(rs.getString("EXPERIMENT_INSTANCE_ID"));
				activityBenchmarkMarkup.setActivityInstanceId(rs.getString("ACTIVITY_INSTANCE_ID"));
				activityBenchmarkMarkup.setStartTime(rs.getTimestamp("START_TIME"));
				activityBenchmarkMarkup.setEndTime(rs.getTimestamp("END_TIME"));
				activityBenchmarkMarkup.setElementPath(rs.getString("ELEMENT_PATH"));
				
				
				return activityBenchmarkMarkup;
			}
			
			
		} catch (SQLException | RuntimeException e) {
			throw new DatabaseException(e.getMessage(), e.getCause());
		} 
	}

	@Override
	public void persist(ActivityBenchmarkMarkup activityBenchmarkMarkup) throws ProvMonitorException{
	
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO ACTIVITY_BENCHMARK_MARKUP (EXPERIMENT_ID" +
																							",EXPERIMENT_INSTANCE_ID" +
																							",ACTIVITY_INSTANCE_ID" +
																							",START_TIME" +
																							",END_TIME" +
																							",ELEMENT_PATH" +
																							") values (?,?,?,?,?,?)");
			psInsert.setString(1, activityBenchmarkMarkup.getExperimentId());
			psInsert.setString(2, activityBenchmarkMarkup.getExperimentInstanceId());
			psInsert.setString(3, activityBenchmarkMarkup.getActivityInstanceId());
			psInsert.setTimestamp(4, DateUtils.utilsDate2SqlTimeStamp(activityBenchmarkMarkup.getStartTime()));
			psInsert.setTimestamp(5, DateUtils.utilsDate2SqlTimeStamp(activityBenchmarkMarkup.getEndTime()));
	
			
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
	public void update(ActivityBenchmarkMarkup activityBenchmarkMarkup) throws ProvMonitorException {
		Connection conn = ConnectionManager.getInstance().getConnection();
		try{
			try{
				//Transaction control
				conn.setAutoCommit(false);
			}catch(SQLException e){
				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
			}
			
			//Verifying schema
			
			
			PreparedStatement psInsert = conn.prepareStatement("UPDATE ACTIVITY_BENCHMARK_MARKUP SET EXPERIMENT_ID = ? " +
																						  ",EXPERIMENT_INSTANCE_ID = ? " +
																						  ",ACTIVITY_INSTANCE_ID = ? " +
																						  ",START_TIME = ? " +
																						  ",END_TIME = ? " +
																						  ",ELEMENT_PATH = ? " +
																" WHERE ACTIVITY_INSTANCE_ID = ? " );
			psInsert.setString(1, activityBenchmarkMarkup.getExperimentId());
			psInsert.setString(2, activityBenchmarkMarkup.getExperimentInstanceId());
			psInsert.setString(3, activityBenchmarkMarkup.getActivityInstanceId());
			psInsert.setTimestamp(4, DateUtils.utilsDate2SqlTimeStamp(activityBenchmarkMarkup.getStartTime()));
			psInsert.setTimestamp(5, DateUtils.utilsDate2SqlTimeStamp(activityBenchmarkMarkup.getEndTime()));
			psInsert.setString(6, activityBenchmarkMarkup.getElementPath());
			psInsert.setString(7, activityBenchmarkMarkup.getActivityInstanceId());
			
			
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
			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO ACTIVITY_BENCHMARK_MARKUP (ACTIVITY_INSTANCE_ID) values (?)");
			psInsert.setString(1,"TesteParam1");
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
				String createTableSQL="CREATE TABLE ACTIVITY_BENCHMARK_MARKUP ( EXPERIMENT_ID VARCHAR(255) NULL" +
																	", EXPERIMENT_INSTANCE_ID VARCHAR(255) NULL" +
																	", ACTIVITY_INSTANCE_ID VARCHAR(255) NOT NULL" +
																	", START_TIME TIMESTAMP" +
																	", END_TIME TIMESTAMP" +
																	", ELEMENT_PATH VARCHAR(255)" +
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
