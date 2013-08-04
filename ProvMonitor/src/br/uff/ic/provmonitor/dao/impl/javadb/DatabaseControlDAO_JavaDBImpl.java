package br.uff.ic.provmonitor.dao.impl.javadb;

import java.io.IOException;

import br.uff.ic.provmonitor.dao.ActivityInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactPortActivityInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactValueLocaltionDAO;
import br.uff.ic.provmonitor.dao.DatabaseControlDAO;
import br.uff.ic.provmonitor.dao.ExecutionFilesStatusDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.ProcessInstanceDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ServerDBException;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

public class DatabaseControlDAO_JavaDBImpl implements DatabaseControlDAO{

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
			
			//wait for the thread execution.
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
		
		//Verify and create if needed ExecutionFilesStatus Database Object
		ExecutionFilesStatusDAO executionFilesStatusDAO = daoFactory.getExecutionFileStatusDAO();
		if(!executionFilesStatusDAO.isTableCreated()){
			executionFilesStatusDAO.createTable();
		}
		
		//Verify and create if needed ProcessInstance Database Object
		ProcessInstanceDAO processInstanceDAO = daoFactory.getProcessInstanceDAO();
		if (!processInstanceDAO.isTableCreated()){
			processInstanceDAO.createTable();
		}
		
		//Verify and create if needed ActivityInstance Database Object
		ActivityInstanceDAO activityInstanceDAO = daoFactory.getActivityInstanceDAO();
		if (!activityInstanceDAO.isTableCreated()){
			activityInstanceDAO.createTable();
		}
		
		//Verify and create if needed ArtifactInstanceDAO Database Object
		ArtifactInstanceDAO artifactInstanceDAO = daoFactory.getArtifactInstanceDAO();
		if (!artifactInstanceDAO.isTableCreated()){
			artifactInstanceDAO.createTable();
		}
		
		//Verify and create if needed ArtifactValueLocaltionDAO Database Object
		ArtifactValueLocaltionDAO artifactValueLocaltionDAO = daoFactory.getArtifactValueLocaltionDAO();
		if (!artifactValueLocaltionDAO.isTableCreated()){
			artifactValueLocaltionDAO.createTable();
		}
		
		//Verify and create if needed ArtifactPortActivityInstanceDAO Database Object
		ArtifactPortActivityInstanceDAO artifactPortActivityInstanceDAO = daoFactory.getArtifactPortActivityInstanceDAO();
		if (!artifactPortActivityInstanceDAO.isTableCreated()){
			artifactPortActivityInstanceDAO.createTable();
		}
		
		//Verify and create if needed xxx Database Object
	}
}
