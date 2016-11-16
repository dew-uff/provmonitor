package br.uff.ic.provmonitor.dao.impl.postegres;

import java.io.IOException;

import br.uff.ic.provmonitor.dao.ActivityBenchmarkMarkupDAO;
import br.uff.ic.provmonitor.dao.DatabaseControlDAO;
import br.uff.ic.provmonitor.dao.ExecutionCommitDAO;
import br.uff.ic.provmonitor.dao.ExecutionFilesStatusDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ServerDBException;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

public class DatabaseControlDAO_PostegresImpl implements DatabaseControlDAO{

	/**
	 * Execute all initializing steps needed for the database implementation. <br />
	 * <ul>
	 * 		<li><b>Step1: </b> Start embedded DB server.</li>
	 * 		<li><b>Step2: </b> Create schema if needed</li>
	 * </ul>
	 * @throws ProvMonitorExcepion If some irrecoverable exception occurs.<br />
	 * @throws DatabaseException <code>Database related problems.</code><br />
	 * 		<ul>
	 * 			<li><b>ConnectionException: </b><code>Database connection problems.</code><br /></li>
	 * 			<li><b>ServerDBException: </b><code>Database server related problems.</code><br /></li>
	 * 		</ul>
	 * */
	@Override
	public void dbInitialize() throws ProvMonitorException{
		
			//Server must start only when using JavaDB Implementation
			if (ProvMonitorProperties.getInstance().getDataBaseType() == null || DatabaseControlDAO.DATABASE_TYPE_JAVADB.equals(ProvMonitorProperties.getInstance().getDataBaseType())){
				//Start JavaDB Server
				serverDBStart();
			}
			//Verify existence and create schema objects if needed
			verifyAndCreateSchemaObjects();
			
			//verifyCreateSchemaTest();
	}

	/**
	 * Execute all finalizing steps needed for the database implementation
	 * 	<b>Step: </b> Stop embedded DB server
	 * @throws DatabaseException
	 * @throws ServerDBException 
	 * */
	@Override
	public void dbFinalize() throws DatabaseException, ServerDBException{
		try{
			//Shut down server only when using JavaDB Implementation
			if (ProvMonitorProperties.getInstance().getDataBaseType() == null || DatabaseControlDAO.DATABASE_TYPE_JAVADB.equals(ProvMonitorProperties.getInstance().getDataBaseType())){
				//Stop JavaDB Server
				serverDBStop();
			}
			
		}catch(ServerDBException e){
			throw e;
		}catch(Exception e){
			throw new DatabaseException(e.getMessage(), e);
		}
		
	}
	
	/**
	 * Start JavaDB Server
	 * */
	private void serverDBStart() throws ServerDBException{
		String setCP = "setNetworkServerCP.bat";
	    String startServer = "startNetworkServer.bat";
	    try {
			Runtime.getRuntime().exec(setCP);
			//Process serverStartProcess = Runtime.getRuntime().exec(startServer);
			//serverStartProcess.waitFor();
			ProcessBuilder pb = new ProcessBuilder(startServer);
			
			//pb.redirectOutput(Redirect.INHERIT);
			//pb.redirectError(Redirect.INHERIT);
			
			pb.start();
			//Process p = pb.start();
			//p.waitFor(); - As a .bat command it waits forever for the end of execution.
			//Thread.currentThread();
			Thread.sleep(3000);

			//Print text to console
//			OutputStream outputStream = serverStartProcess.getOutputStream();
//            PrintStream printStream = new PrintStream(outputStream);
//            printStream.println();
//            printStream.flush();
//            printStream.close();
			
            //Java 7 Solution to print outputstream
            //ProcessBuilder pb = new ProcessBuilder("yourcommand");
            //pb.redirectOutput(Redirect.INHERIT);
            //pb.redirectError(Redirect.INHERIT);
            //Process p = pb.start();
            
            //Verify error
			//if (p.exitValue() != 0 ){
			//	throw new ServerDBException("Database Server Java DB fail to load.");
			//}
			
		} catch (IOException e) {
			throw new ServerDBException(e.getMessage(), e);
		} catch (InterruptedException e){
			throw new ServerDBException(e.getMessage(), e);
		}
	}

	/**
	 * Stops JavaDB Server
	 * */
	private void serverDBStop() throws ServerDBException{
		String setCP = "setNetworkServerCP.bat";
		String stopServer = "stopNetworkServer.bat";
	    try {
			Runtime.getRuntime().exec(setCP);
			Runtime.getRuntime().exec(stopServer);
		} catch (IOException e) {
			throw new ServerDBException(e.getMessage(), e);
		}
	} 
	
	/**
	 * Verify schema objects and create the objects if needed. <br />
	 * @throws ProvMonitorExcepion If some irrecoverable exception occurs.<br />
	 * @throws DatabaseException <code>Database related problems.</code><br />
	 * 		<ul><li><b>ConnectionException: </b><code>Database connection problems.</code><br /></li>
	 * 		<li><b>ServerDBException: </b><code>Database server related problems.</code><br /></li></ul>
	 * */
	private void verifyAndCreateSchemaObjects() throws ProvMonitorException{
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();

		//Verify and create if needed ExecutionStatus Database Object
		ExecutionStatusDAO executionStatusDAO = daoFactory.getExecutionStatusDAO();
		if (!executionStatusDAO.isTableCreated()){
			executionStatusDAO.createTable();
		}
		
		//Verify and create if needed ExecutionStatus Database Object
		ExecutionCommitDAO executionCommitDAO = daoFactory.getExecutionCommitDAO();
		if (!executionCommitDAO.isTableCreated()){
			executionCommitDAO.createTable();
		}
		
		//Verify and create if needed ExecutionFilesStatus Database Object
		ExecutionFilesStatusDAO executionFilesStatusDAO = daoFactory.getExecutionFileStatusDAO();
		if(!executionFilesStatusDAO.isTableCreated()){
			executionFilesStatusDAO.createTable();
		}
		
		//Verify and create if needed ActivityBenchmarkMarkupDAO Database Object
		ActivityBenchmarkMarkupDAO activityBenchmarkMarkupDAO = daoFactory.getActivityBenchmarkMarkupDAO();
		if (!activityBenchmarkMarkupDAO.isTableCreated()){
			activityBenchmarkMarkupDAO.createTable();
		}
		
		//Verify and create if needed ProcessInstance Database Object
//		ProcessInstanceDAO processInstanceDAO = daoFactory.getProcessInstanceDAO();
//		if (!processInstanceDAO.isTableCreated()){
//			processInstanceDAO.createTable();
//		}
//		
//		//Verify and create if needed ActivityInstance Database Object
//		ActivityInstanceDAO activityInstanceDAO = daoFactory.getActivityInstanceDAO();
//		if (!activityInstanceDAO.isTableCreated()){
//			activityInstanceDAO.createTable();
//		}
//		
//		//Verify and create if needed ArtifactInstanceDAO Database Object
//		ArtifactInstanceDAO artifactInstanceDAO = daoFactory.getArtifactInstanceDAO();
//		if (!artifactInstanceDAO.isTableCreated()){
//			artifactInstanceDAO.createTable();
//		}
//		
//		//Verify and create if needed ArtifactValueLocaltionDAO Database Object
//		ArtifactValueLocaltionDAO artifactValueLocaltionDAO = daoFactory.getArtifactValueLocaltionDAO();
//		if (!artifactValueLocaltionDAO.isTableCreated()){
//			artifactValueLocaltionDAO.createTable();
//		}
//		
//		//Verify and create if needed ArtifactPortActivityInstanceDAO Database Object
//		ArtifactPortActivityInstanceDAO artifactPortActivityInstanceDAO = daoFactory.getArtifactPortActivityInstanceDAO();
//		if (!artifactPortActivityInstanceDAO.isTableCreated()){
//			artifactPortActivityInstanceDAO.createTable();
//		}
		
		//Verify and create if needed xxx Database Object
	}
	
//	/**
//	 * Verify schema objects and create the objects if needed. <br />
//	 * @throws ProvMonitorExcepion If some irrecoverable exception occurs.<br />
//	 * @throws DatabaseException <code>Database related problems.</code><br />
//	 * 		<ul><li><b>ConnectionException: </b><code>Database connection problems.</code><br /></li>
//	 * 		<li><b>ServerDBException: </b><code>Database server related problems.</code><br /></li></ul>
//	 * */
//	private void verifyCreateSchemaObjects() throws ProvMonitorException{
//		Connection conn = ConnectionManager.getInstance().getConnection();
//		boolean createObjects = false;
//		try{
//			
//			try{
//				//Preparing statement
//				//Statement s = conn.createStatement();
//
//				//Transaction control
//				conn.setAutoCommit(false);
//				
//				//s.executeQuery("");
//				
//			}catch(SQLException e){
//				throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
//			}
//			
//			//Verifying schema
//			PreparedStatement psInsert = conn.prepareStatement("INSERT INTO WISH_LIST(WISH_ITEM) values (?)");
//			psInsert.setString(1,"Teste");
//			psInsert.executeUpdate(); 
//			conn.rollback();
//			
//			
//		}catch(SQLException e){
//			//Schema object does not exist
//			try{
//				conn.rollback();
//			}catch(SQLException ex){
//			}
//			
//			//Create schema objects
//			createObjects = true;
////			createSchemaObjects();
//		}
//		
//		if (createObjects){
//			try{
//				Statement s = null;
//				try{
//					//Preparing statement
//					s = conn.createStatement();
//
//					//Transaction control
//					conn.setAutoCommit(false);
//					
//					//s.executeQuery("");
//					
//				}catch(SQLException e){
//					throw new ConnectionException(e.getMessage(), e.getCause(), e.getSQLState());
//				}
//				if (s != null){
//					String createExecutionStatusSQL = "CREATE TABLE EXECUTION_STATUS(id_element int unsigned not null, type_element int unsigned not null, status smallint unsigned not null, start_time bigint unsigned, end_time bigint unsigned, path varchar(255), performers varchar(255));";
//					s.executeQuery(createExecutionStatusSQL);
//				}
//			}catch(Exception e){
//				//TODO: Tratar adequadamente
//			}
//		}
//	}
	
//	private void createSchemaObjects() throws ProvMonitorException{
//		//Preparing statement
//		//Statement s = conn.createStatement();
//	}
	
//	private void verifyCreateSchemaTest() throws ConnectionException, SQLException{
//		Connection conn = ConnectionManager.getInstance().getConnection();
//		String createString = "CREATE TABLE WISH_LIST  "
//		        +  "(WISH_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY " 
//		        +  "   CONSTRAINT WISH_PK PRIMARY KEY, " 
//		        +  " ENTRY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
//		        +  " WISH_ITEM VARCHAR(32) NOT NULL) " ;
//		
//		Statement s = conn.createStatement();
//        // Call utility method to check if table exists.
//        //      Create the table if needed
//        //if (! WwdUtils.wwdChk4Table(conn))
////        {  
////             System.out.println (" . . . . creating table WISH_LIST");
//             s.execute(createString);
////         }
//        //  Prepare the insert statement to use 
//       PreparedStatement psInsert = conn.prepareStatement("insert into WISH_LIST(WISH_ITEM) values (?)");
//       psInsert.setString(1,"Teste");
//       psInsert.executeUpdate(); 
//		
//       
//       //   Select all records in the WISH_LIST table
//       ResultSet myWishes = s.executeQuery("select ENTRY_DATE, WISH_ITEM from WISH_LIST order by ENTRY_DATE");
//
//       //  Loop through the ResultSet and print the data 
//       String printLine = "  __________________________________________________";
//       System.out.println(printLine);
//       while (myWishes.next()){
//    	   System.out.println("On " + myWishes.getTimestamp(1) + " I wished for " + myWishes.getString(2));
//       }
//       System.out.println(printLine);
//       //  Close the resultSet 
//       myWishes.close();
//       
//       // Release the resources (clean up )
//       psInsert.close();
//       s.close();
//       conn.close();
//	}

}
