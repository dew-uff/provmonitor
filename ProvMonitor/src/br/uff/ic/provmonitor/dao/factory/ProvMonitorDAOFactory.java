package br.uff.ic.provmonitor.dao.factory;

import br.uff.ic.provmonitor.dao.ActivityInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactPortActivityInstanceDAO;
import br.uff.ic.provmonitor.dao.ArtifactValueLocaltionDAO;
import br.uff.ic.provmonitor.dao.DatabaseControlDAO;
import br.uff.ic.provmonitor.dao.ExecutionFilesStatusDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.ProcessInstanceDAO;
import br.uff.ic.provmonitor.dao.impl.javadb.ActivityInstanceDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ArtifactInstanceDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ArtifactPortActivityInstanceDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ArtifactValueLocaltionDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.DatabaseControlDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ExecutionFilesStatusDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ExecutionStatusDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.javadb.ProcessInstanceDAO_JavaDBImpl;
import br.uff.ic.provmonitor.dao.impl.postegres.DatabaseControlDAO_PostegresImpl;
import br.uff.ic.provmonitor.dao.impl.postegres.ExecutionFilesStatusDAO_PostegresImpl;
import br.uff.ic.provmonitor.dao.impl.postegres.ExecutionStatusDAO_PostegresImpl;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

/**
 * ProvMonitor Database access objects Factory. Responsible for return the right instance of database access objects implementation. <br />
 * <br />
 * <b>Currently Supports: </b> <br />
 * <code>
 * - JavaDB (Apache Derby) <br />
 * - ProvManager WebService <br />
 * </code>
 * */
public class ProvMonitorDAOFactory {
	
	/**
	 * Get the Activity Instance DAO Implementation
	 * @return ActivityInstanceDAO
	 * */
	public ActivityInstanceDAO getActivityInstanceDAO(){
		return new ActivityInstanceDAO_JavaDBImpl();
	}
	
	/**
	 * Get the Artifact Instance DAO Implementation
	 * @return ArtifactInstanceDAO
	 * */
	public ArtifactInstanceDAO getArtifactInstanceDAO(){
		return new ArtifactInstanceDAO_JavaDBImpl();
	}
	
	/**
	 * Get the Execution Status DAO Implementation
	 * @return ExecutionStatusDAO
	 * */
	public ExecutionStatusDAO getExecutionStatusDAO(){
		switch(ProvMonitorProperties.getInstance().getDataBaseType()){
			case POSTGRES:
				return new ExecutionStatusDAO_PostegresImpl();
			case MYSQL:
			case JAVADB:
			default:
				return new ExecutionStatusDAO_JavaDBImpl();
		}
	}
	
	public ExecutionFilesStatusDAO getExecutionFileStatusDAO(){
		switch(ProvMonitorProperties.getInstance().getDataBaseType()){
			case POSTGRES:
				return new ExecutionFilesStatusDAO_PostegresImpl();
			case MYSQL:
			case JAVADB:
			default:
				return new ExecutionFilesStatusDAO_JavaDBImpl();
		}
	}
	
	/**
	 * Get the Process Instance DAO Implementation
	 * @return ProcessInstanceDAO
	 * */
	public ProcessInstanceDAO getProcessInstanceDAO(){
		return new ProcessInstanceDAO_JavaDBImpl();
	}
	
	/**
	 * Get the Database control DAO Implementation
	 * @return DatabaseControlDAO
	 * */
	public DatabaseControlDAO getDatabaseControlDAO(){
		switch(ProvMonitorProperties.getInstance().getDataBaseType()){
			case POSTGRES:
				return new DatabaseControlDAO_PostegresImpl();
			case MYSQL:
			case JAVADB:
			default:
				return new DatabaseControlDAO_JavaDBImpl();
		}
		
	}
	
	/**
	 * Get the Database control DAO Implementation
	 * @return ArtifactValueLocaltionDAO
	 * */
	public ArtifactValueLocaltionDAO getArtifactValueLocaltionDAO(){
		return new ArtifactValueLocaltionDAO_JavaDBImpl();
	}
	
	/**
	 * Get the Database control DAO Implementation
	 * @return ArtifactPortActivityInstanceDAO
	 * */
	public ArtifactPortActivityInstanceDAO getArtifactPortActivityInstanceDAO(){
		return new ArtifactPortActivityInstanceDAO_JavaDBImpl();
	}
	
}
