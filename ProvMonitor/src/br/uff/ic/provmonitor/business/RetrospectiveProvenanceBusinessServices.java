package br.uff.ic.provmonitor.business;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.util.StringUtils;

import br.uff.ic.provmonitor.dao.ArtifactInstanceDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.ConnectionException;
import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ServerDBException;
import br.uff.ic.provmonitor.exceptions.VCSCheckOutConflictException;
import br.uff.ic.provmonitor.exceptions.VCSException;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.model.ArtifactInstance;
import br.uff.ic.provmonitor.model.ExecutionCommit;
import br.uff.ic.provmonitor.model.ExecutionFilesStatus;
import br.uff.ic.provmonitor.model.ExecutionStatus;
import br.uff.ic.provmonitor.output.ProvMonitorOutputManager;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;
import br.uff.ic.provmonitor.vcsmanager.VCSManager;
import br.uff.ic.provmonitor.vcsmanager.VCSManagerFactory;
import br.uff.ic.provmonitor.workspaceWatcher.PathAccessType;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspaceAccessReader;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspacePathStatus;

/**
 * ProvMonitor retrospective business services.
 * 
 * Class responsible to manage the retrospective provenance information gathering and register.
 * 
 * @author Vitor C. Neves - vcneves@ic.uff.br
 *
 */
public class RetrospectiveProvenanceBusinessServices {
		
	/**
	 * Experiment execution initialization.
	 * @param experimentId Experiment identifier.
	 * @throws ProvMonitorException ProvMonitor base exception if problems occurs.
	 * <br /><br />
	 * <b>Update - Instead Use:</b> public static String initializeExperimentExecution(String experimentId, String experimentInstanceId, String sourceRepository, String workspacePath) throws ProvMonitorException.
	 *
	 */
	@Deprecated
	public static void initializeExperimentExecution(String experimentId) throws ProvMonitorException{
		
		//Record Timestamp
		Date timeStampInitExecute = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(timeStampInitExecute);
		String experimentInstanceId = experimentId + nonce;
		
		//Printing Generated Values
		ProvMonitorOutputManager.appendMessageLine("ExperimentInstanceId: " + experimentInstanceId);
		ProvMonitorOutputManager.appendMessageLine("BranchName: " + experimentInstanceId);
		
		//Initialize DB
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbInitialize();
		
		//Repository clone
		
		//Repository Branch
		
		//System.out.println("initializeExperimentExecution end execution.");
	}
	/**
	 * Experiment execution initialization.
	 * <br /><br /><strong>Description:</strong>
	 * Responsible to clone central repository, prepare the workspace, prepare the needed infrastructure (database tables, embedded database services initialization, etc.) and when applied generate experiment instance id.
	 *  
	 * @param experimentId Experiment identifier.
	 * @param experimentInstanceId Experiment execution/trial identifier. Optional parameter. If Null or Empty is informed, this will be auto generated and returned at the end of the method execution.
	 * @param sourceRepository Central repository URI path. Repository from workspace will be cloned.
	 * @param workspacePath Experiment workspace URI path.
	 * 
	 * @return ExperimentInstanceId Auto generated when the parameter is null or empty, otherwise the same inputed value.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 * @throws ServerDBException Database server related problems.
	 */
	public static String initializeExperimentExecution(String experimentId, String experimentInstanceId, String sourceRepository, String workspacePath) throws ProvMonitorException{
		
		//If experimentInstanceId is not informed, generate it.
		if (StringUtils.isEmptyOrNull(experimentInstanceId)){
			experimentInstanceId = ProvMonitorBusinessHelper.generateExperimentInstanceId(experimentId);
		}

		//Printing Generated/received/considered values
		ProvMonitorOutputManager.appendMessageLine("ExperimentInstanceId: " + experimentInstanceId);
		ProvMonitorOutputManager.appendMessageLine("BranchName: " + experimentInstanceId);
		ProvMonitorOutputManager.appendMessageLine("Workspace: " + workspacePath);
		ProvMonitorOutputManager.appendMessageLine("CentralRepository: " + sourceRepository);
		
		//Initialize DB
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbInitialize();
		
		//Workspace preparation
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		//System.out.println("Cloning to: " + workspacePath);
		
		//Verify if Workspace already exists. If not, clone it.
		
		//Checking out/Creating canonical workspace
		try {
			vcsManager.checkout(sourceRepository, ProvMonitorProperties.getInstance().getCanonicalBranchName());
		} catch(VCSCheckOutConflictException e){
			vcsManager.commit(sourceRepository, "Resolving checkout conflicts.");
			vcsManager.checkout(sourceRepository, ProvMonitorProperties.getInstance().getCanonicalBranchName());
		} catch(VCSException e){
			vcsManager.createBranch(sourceRepository, ProvMonitorProperties.getInstance().getCanonicalBranchName());
			vcsManager.checkout(sourceRepository, ProvMonitorProperties.getInstance().getCanonicalBranchName());
			vcsManager.commit(sourceRepository, "Creating canonical branch for trial: " + ProvMonitorProperties.getInstance().getCanonicalBranchName());
		}
		
		
		//Repository branch
		vcsManager.createBranch(sourceRepository, experimentInstanceId);
		//Repository checkOut
		vcsManager.checkout(sourceRepository, experimentInstanceId);
		//Repository commit new branch
		vcsManager.commit(sourceRepository, "Creating branch for trial: " + experimentInstanceId);
		
		//Repository clone
		//cvsManager.cloneRepository(sourceRepository, workspacePath);
		List<String> branches2Clone = new ArrayList<String>();
		branches2Clone.add(ProvMonitorProperties.getInstance().getCanonicalBranchName());
		branches2Clone.add(experimentInstanceId);
		vcsManager.cloneRepository(sourceRepository, workspacePath, branches2Clone);
		
		//Repository branch
		//cvsManager.createBranch(workspacePath, experimentInstanceId);
		
		//Repository checkOut
		//cvsManager.checkout(workspacePath, experimentInstanceId);
		
		//System.out.println("initializeExperimentExecution end execution.");
		return experimentInstanceId;
	}
	
	/**
	 * Experiment execution finalization. 
	 * <br /><br /><strong>Description:</strong>
	 * Responsible to register the end of the experiment execution, push back workspace to the central repository, and shut down the used infrastructure.
	 * 
	 * @param experimentInstanceId Experiment execution/trial identifier.
	 * @param sourceRepository Central repository URI path. Repository from workspace will be cloned.
	 * @param workspacePath Experiment workspace URI path.
	 * @param endDateTime Experiment end date time execution.
	 * 
	 * @return ExperimentInstanceId - Auto generated when the parameter is null or empty, otherwise the same inputed value.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws VCSException Version control system related problems.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 * @throws ServerDBException Database server related problems.
	 */
	public static void FinalizeExperimentExecution(String experimentInstanceId, String sourceRepository, String workspacePath, Date endDateTime) throws ProvMonitorException{
		//Stop DB infra
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbFinalize();
		
		//Pushback Repository
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		vcsManager.pushBack(workspacePath, sourceRepository);
		
	}
	
	/**
	 * Notify startup of a simple activity instance execution.
	 * <br /><br />
	 * <b>Update - Instead Use:</b> <br />
	 * public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspacePath) throws ProvMonitorException.
	 */
	@Deprecated
	public static boolean notifyActivityExecutionStartup(String activityInstanceId, String[] context){
		//Record Timestamp
		return false;
	}

	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspaceInput, String activationWorkspace) throws ProvMonitorException{
		notifyActivityExecutionStartup(activityInstanceId, context, activityStartDateTime, workspaceInput, activationWorkspace, false);
	}
	
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspaceInput, String activationWorkspace, Boolean firstActivity) throws ProvMonitorException{
		switch (ProvMonitorProperties.getInstance().getBranchStrategy()){
			case TRIAL:
				notifyActivityExecutionStartupBranchByTrial(activityInstanceId, context, activityStartDateTime, workspaceInput, activationWorkspace, firstActivity);
				break;
			case ACTIVITY:
				notifyActivityExecutionStartupBranchByActivity(activityInstanceId, context, activityStartDateTime, workspaceInput, activationWorkspace, firstActivity);
				break;
		}
	}
	
	public static void notifyActivityExecutionStartupBranchByTrial(String activityInstanceId, String[] context, Date activityStartDateTime, String workspaceInput, String activationWorkspace, Boolean firstActivity) throws ProvMonitorException{
		//Prepare ActivityObject to be persisted
		ExecutionStatus elementExecStatus = getNewExecStatus(activityInstanceId, context, activityStartDateTime, null);
		
		//Activity start clone
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		
		//First activity Branching
		if (firstActivity){ //CREATE OR CHECK OUT CANONICAL BRANCH
			
			try{
				//Try to checkOut - First activity of first trial may have the branch already created by ExperimentInit
				vcsManager.checkout(workspaceInput, context[0]);
			}catch(VCSException e1){
				//Checking out/Creating canonical workspace
				try {
					vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
				} catch(VCSCheckOutConflictException e){
					vcsManager.commit(workspaceInput, "Resolving checkout conflicts.");
					vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
				} catch(VCSException e){
					vcsManager.createBranch(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
					vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
					vcsManager.commit(workspaceInput, "Creating canonical branch for trial: " + ProvMonitorProperties.getInstance().getCanonicalBranchName());
				}
				
				//Repository branch
				vcsManager.createBranch(workspaceInput, context[0]);
				//Repository checkOut
				vcsManager.checkout(workspaceInput, context[0]);
				//Repository commit new branch
				vcsManager.commit(workspaceInput, "Creating branch for trial: " + context[0]);
			}
			
			
			
		}//else{ //DO NORMAL EXECUTION.
		
			//vcsManager.checkout(workspaceInput, context[0]);
			vcsManager.cloneRepository(workspaceInput, activationWorkspace);
		
		//}
		
		
		//TODO: Implement transaction control and atomicity for multivalued attributes.
		
		//Persisting
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		//factory.getActivityInstanceDAO().persist(activityInstance);
		factory.getExecutionStatusDAO().persist(elementExecStatus);
		
	}
	
	public static void notifyActivityExecutionStartupBranchByActivity(String activityInstanceId, String[] context, Date activityStartDateTime, String workspaceInput, String activationWorkspace, Boolean firstActivity) throws ProvMonitorException{
		//Prepare ActivityObject to be persisted
		ExecutionStatus elementExecStatus = getNewExecStatus(activityInstanceId, context, activityStartDateTime, null);
		
		//Activity start clone
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		
		//First activity Branching
		if (firstActivity){ //CREATE OR CHECK OUT CANONICAL BRANCH
			
			//Checking out/Creating canonical workspace
			try {
				vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
			} catch(VCSCheckOutConflictException e){
				vcsManager.commit(workspaceInput, "Resolving checkout conflicts.");
				vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
			} catch(VCSException e){
				vcsManager.createBranch(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
				vcsManager.checkout(workspaceInput, ProvMonitorProperties.getInstance().getCanonicalBranchName());
				vcsManager.commit(workspaceInput, "Creating canonical branch for trial: " + ProvMonitorProperties.getInstance().getCanonicalBranchName());
			}
			
			//Repository branch
			vcsManager.createBranch(workspaceInput, context[0]);
			//Repository checkOut
			vcsManager.checkout(workspaceInput, context[0]);
			//Repository commit new branch
			vcsManager.commit(workspaceInput, "Creating branch for trial: " + context[0]);
			
		}else{ //DO NORMAL EXECUTION.
		
			//BranchName
			String branchName = ContextHelper.getBranchNameFromContext(context);
			//Repository branch
			vcsManager.createBranch(workspaceInput, branchName);
			//Repository checkOut
			vcsManager.checkout(workspaceInput, branchName);
			//Repository commit new branch
			vcsManager.commit(workspaceInput, "Creating branch for activity: " + activityInstanceId + ". branch name: " + branchName);
			
			//vcsManager.checkout(workspaceInput, context[0]);
			vcsManager.cloneRepository(workspaceInput, activationWorkspace);
		
		}
		
		
		//TODO: Implement transaction control and atomicity for multivalued attributes.
		
		//Persisting
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		//factory.getActivityInstanceDAO().persist(activityInstance);
		factory.getExecutionStatusDAO().persist(elementExecStatus);
	}
	
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context, Date activityStartDateTime, Date endActiviyDateTime, String workspaceRoot, String activationWorkspace) throws ProvMonitorException{
		//Mounting context
		//Prepare ActivityObject to be persisted
		ExecutionStatus elementExecStatus = getNewExecStatus(activityInstanceId, context, activityStartDateTime, endActiviyDateTime);
		
		//Record Timestamp
		
		//Verify accessed files
		Collection<ExecutionFilesStatus> execFiles = getAccessedFiles(elementExecStatus, activationWorkspace);
						
						
		//Commit changed files
		StringBuilder message = new StringBuilder();
		message.append("ActivityInstanceId:")
			   .append(activityInstanceId)
			   .append("; context:")
			   .append(elementExecStatus.getElementPath())
			   .append("; EndActivityCommit");
		
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		//((GitManager)cvsManager).getStatus(workspacePath);
		vcsManager.addAllFromPath(activationWorkspace);
		Set<String> removed = vcsManager.removeAllFromPath(activationWorkspace);
		String commitId = vcsManager.commit(activationWorkspace, message.toString());
				
		
		//Recover executionStatus element
		ProvMonitorOutputManager.appendMessageLine("Starting ActivityExecutionEnding Method...");
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		ExecutionStatusDAO execStatusDAO = factory.getExecutionStatusDAO();
		ProvMonitorOutputManager.appendMessageLine("Getting activity by id: " + activityInstanceId);
		ExecutionStatus elemExecutionStatus = execStatusDAO.getById(activityInstanceId, elementExecStatus.getElementPath());
		
		if (elemExecutionStatus == null){
			throw new ProvMonitorException("Element: " + activityInstanceId + " not found exception. Activity could not be finished if it was not started." );
		}
		
		//update execution element
		ProvMonitorOutputManager.appendMessageLine("Updating Activity properties: End DateTime....");
		elemExecutionStatus.setEndTime(endActiviyDateTime);
		
		ProvMonitorOutputManager.appendMessageLine("Updating Activity properties: Status....");
		elemExecutionStatus.setStatus("ended");
		
		//Recording Commit ID
		//elemExecutionStatus.setCommitId(commitId);
		ExecutionCommit execCommit = new ExecutionCommit();
		execCommit.setCommitId(commitId);
		execCommit.setCommitTime(endActiviyDateTime);
		execCommit.setStatus("ActivityEnd");
		execCommit.setElementId(elemExecutionStatus.getElementId());
		execCommit.setElementPath(elemExecutionStatus.getElementPath());
		
		//Joining all files changes to be persisted.
		Collection<ExecutionFilesStatus> removedFiles = getRemovedFiles(removed, elementExecStatus, activationWorkspace);
		execFiles.addAll(removedFiles);
		
		//persist updated element
		ProvMonitorOutputManager.appendMessageLine("Persisting Activity....");
		execStatusDAO.update(elemExecutionStatus);
		
		factory.getExecutionCommitDAO().persist(execCommit);
		
		ProvMonitorOutputManager.appendMessageLine("Activity Persisted.");
		
		//Persist accessed files
		for (ExecutionFilesStatus executionFileStatus: execFiles){
			factory.getExecutionFileStatusDAO().persist(executionFileStatus);
		}
		
		//Pushing back to the experiment root workspace
		vcsManager.pushBack(activationWorkspace, workspaceRoot);
	}
	
	private static ExecutionStatus getNewExecStatus(String activityInstanceId, String[] context,Date activityStartDateTime, Date activityEndDateTime){
		ExecutionStatus elementExecStatus = new ExecutionStatus();
		elementExecStatus.setElementId(activityInstanceId);
		elementExecStatus.setElementType("activity");
		elementExecStatus.setStatus("starting");
		//Mounting context
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("/");
			}
			elementPath.append(path);
		}
		
		elementExecStatus.setElementPath(elementPath.toString());
		//Record Timestamp
		elementExecStatus.setStartTime(activityStartDateTime);
		if (activityEndDateTime != null){
			elementExecStatus.setEndTime(activityEndDateTime);
		}
		
		return elementExecStatus;
	}
	
	private static Collection<ExecutionFilesStatus> getAccessedFiles(ExecutionStatus elementExecStatus, String workspacePath){
		//Reading accessedFiles
		ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
		try{
			//Collection<AccessedPath> accessedFiles = WorkspaceAccessReader.readAccessedPathsAndAccessTime(Paths.get(workspacePath), startActivityDateTime, true);
			Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readWorkspacePathStatusAndStatusTime(Paths.get(workspacePath), elementExecStatus.getStartTime(), true);
			if (accessedFiles != null && !accessedFiles.isEmpty()){
				for (WorkspacePathStatus acFile: accessedFiles){
					
					ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
					execFileStatus.setFileAccessDateTime(acFile.getStatusDateTime());
					execFileStatus.setFilePath(acFile.getPathName().replaceFirst("/", ""));
					execFileStatus.setElementId(elementExecStatus.getElementId());
					execFileStatus.setElementPath(elementExecStatus.getElementPath());
					execFileStatus.setFiletAccessType(acFile.getPathStatusType().name());
					//execFileStatus.setFiletAccessType(ExecutionFilesStatus.TYPE_READ);
					
					execFiles.add(execFileStatus);
					
				}
			}
		}catch(Exception e){
			ProvMonitorLogger.warning("RetrospectiveProvenanceBusinessServices", "notifyActivityExecutionStartup", e.getMessage());
			ProvMonitorOutputManager.appendMenssage("WARNING: RetrospectiveProvenanceBusinessServices: notifyActivityExecutionStartup" + e.getMessage());
		}
		
		return execFiles;
	}
	
	private static Collection<ExecutionFilesStatus> getRemovedFiles(Set<String> removed, ExecutionStatus elementExecStatus, String workspacePath){
		//Registering removed files
		ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
		if (removed != null && !removed.isEmpty()){
			for (String removedPath: removed){
				ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
				execFileStatus.setFileAccessDateTime(elementExecStatus.getStartTime());
				execFileStatus.setFilePath(workspacePath.concat("/".concat(removedPath)));
				execFileStatus.setElementId(elementExecStatus.getElementId());
				execFileStatus.setElementPath(elementExecStatus.getElementPath());
				execFileStatus.setFiletAccessType(PathAccessType.REMOVE.name());
				
				execFiles.add(execFileStatus);
			}
		}
		return execFiles;
	}
	
	/**
	 * Notify startup of a simple activity instance execution.
	 *
	 * @param activityInstanceId Activity instance identifier. 
	 * @param context Sequence of identifiers that defines a simple activity instance location in the experiment. (path, i.e.: Sub workflows that wraps the simple activity instance) 
	 * @param activityStartDateTime Activity instance date time of startup execution.
	 * @param workspacePath Experiment workspace path.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws VCSException Version control system related problems.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspacePath) throws ProvMonitorException{
		newNotifyActivityExecutionStartup(activityInstanceId, context, activityStartDateTime, workspacePath);
	}
	
	/**
	 * Notify startup of a composite activity instance execution.
	 * <br /><br />
	 * <b>Update - Instead Use:</b> <br />
	 * public static void notifyProcessExecutionStartup(String processInstanceId, String[] context, Date processStartDateTime, String workspacePath) throws ProvMonitorException.
	 */
	@Deprecated
	public static boolean notifyProcessExecutionStartup(String processInstanceId, String[] context){
		return false;
	}
	
	/**
	 * Notify startup of a composite activity instance execution.
	 *
	 * @param processInstanceId Process (composite activity) instance identifier. 
	 * @param context Sequence of identifiers that defines a process instance location in the experiment. (path, i.e.: Sub workflows that wraps the simple activity instance) 
	 * @param processStartDateTime Process instance date time of startup execution.
	 * @param workspacePath Experiment workspace path.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws VCSException Version control system related problems.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	public static void notifyProcessExecutionStartup(String processInstanceId, String[] context, Date processStartDateTime, String workspacePath) throws ProvMonitorException{
		notifyActivityExecutionStartup(processInstanceId, context, processStartDateTime, workspacePath);
	}
	
	/**
	 * Notify ending of a simple activity instance execution.
	 * <br /><br />
	 * <b>Update - Instead Use:</b> <br />
	 * public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context, Date startActivityDateTime, Date endActiviyDateTime, String workspacePath) throws ProvMonitorException.
	 */
	@Deprecated
	public static boolean notifyActivityExecutionEnding(String activityInstanceId, String[] context){
		return false;
	}
	
	/**
	 * Notify ending of a simple activity instance execution.
	 *
	 * @param activityInstanceId Activity instance identifier. 
	 * @param context Sequence of identifiers that defines a simple activity instance location in the experiment. (path, i.e.: Sub workflows that wraps the simple activity instance) 
	 * @param activityStartDateTime Activity instance date time of startup execution.
	 * @param endActiviyDateTime Activity instance date time of ending execution.
	 * @param workspacePath Experiment workspace path.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws VCSException Version control system related problems.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context, Date startActivityDateTime, Date endActiviyDateTime, String workspacePath) throws ProvMonitorException{
		//Mounting context
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("/");
			}
			elementPath.append(path);
		}
		
		//Record Timestamp
		
		//Verify accessed files
		ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
		try{
			//Collection<AccessedPath> accessedFiles = WorkspaceAccessReader.readAccessedPathsAndAccessTime(Paths.get(workspacePath), startActivityDateTime, true);
			Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readWorkspacePathStatusAndStatusTime(Paths.get(workspacePath), startActivityDateTime, true);
			if (accessedFiles != null && !accessedFiles.isEmpty()){
				for (WorkspacePathStatus acFile: accessedFiles){
					
					ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
					execFileStatus.setFileAccessDateTime(acFile.getStatusDateTime());
					execFileStatus.setFilePath(acFile.getPathName().replaceFirst("/", ""));
					execFileStatus.setElementId(activityInstanceId);
					execFileStatus.setElementPath(elementPath.toString());
					execFileStatus.setFiletAccessType(acFile.getPathStatusType().name());
					//execFileStatus.setFiletAccessType(ExecutionFilesStatus.TYPE_READ);
					
					execFiles.add(execFileStatus);
					
				}
			}
		}catch(IOException e){
			throw new ProvMonitorException(e.getMessage(), e.getCause());
		}
				
				
		//Commit changed files
		StringBuilder message = new StringBuilder();
		message.append("ActivityInstanceId:")
			   .append(activityInstanceId)
			   .append("; context:")
			   .append(elementPath.toString())
			   .append("; EndActivityCommit");
		
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		//((GitManager)cvsManager).getStatus(workspacePath);
		cvsManager.addAllFromPath(workspacePath);
		Set<String> removed = cvsManager.removeAllFromPath(workspacePath);
		String commitId = cvsManager.commit(workspacePath, message.toString());
				
		
		//Recover executionStatus element
		ProvMonitorOutputManager.appendMessageLine("Starting ActivityExecutionEnding Method...");
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		ExecutionStatusDAO execStatusDAO = factory.getExecutionStatusDAO();
		ProvMonitorOutputManager.appendMessageLine("Getting activity by id: " + activityInstanceId);
		ExecutionStatus elemExecutionStatus = execStatusDAO.getById(activityInstanceId, elementPath.toString());
		
		if (elemExecutionStatus == null){
			throw new ProvMonitorException("Element: " + activityInstanceId + " not found exception. Activity could not be finished if it was not started." );
		}
		
		//update execution element
		ProvMonitorOutputManager.appendMessageLine("Updating Activity properties: End DateTime....");
		elemExecutionStatus.setEndTime(endActiviyDateTime);
		
		ProvMonitorOutputManager.appendMessageLine("Updating Activity properties: Status....");
		elemExecutionStatus.setStatus("ended");
		
		//Recording Commit ID
		//elemExecutionStatus.setCommitId(commitId);
		ExecutionCommit execCommit = new ExecutionCommit();
		execCommit.setCommitId(commitId);
		execCommit.setCommitTime(endActiviyDateTime);
		execCommit.setStatus("ActivityEnd");
		execCommit.setElementId(elemExecutionStatus.getElementId());
		execCommit.setElementPath(elemExecutionStatus.getElementPath());
		
		//Registering removed files
		if (removed != null && !removed.isEmpty()){
			for (String removedPath: removed){
				ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
				execFileStatus.setFileAccessDateTime(endActiviyDateTime);
				execFileStatus.setFilePath(workspacePath.concat("/".concat(removedPath)));
				execFileStatus.setElementId(activityInstanceId);
				execFileStatus.setElementPath(elementPath.toString());
				execFileStatus.setFiletAccessType(PathAccessType.REMOVE.name());
				
				execFiles.add(execFileStatus);
			}
		}
		
		//persist updated element
		ProvMonitorOutputManager.appendMessageLine("Persisting Activity....");
		execStatusDAO.update(elemExecutionStatus);
		
		factory.getExecutionCommitDAO().persist(execCommit);
		
		ProvMonitorOutputManager.appendMessageLine("Activity Persisted.");
		
		//Persist accessed files
		for (ExecutionFilesStatus executionFileStatus: execFiles){
			factory.getExecutionFileStatusDAO().persist(executionFileStatus);
		}
		
	}
	
	/**
	 * Notify ending of a Process (composite activity) instance execution.
	 *
	 * @param processInstanceId Process (composite activity) instance identifier. 
	 * @param context Sequence of identifiers that defines a composite activity instance location in the experiment. (path, i.e.: Sub workflows that wraps the composite activity instance) 
	 * @param startProcessDateTime Process instance date time of startup execution.
	 * @param endProcessDateTime Process instance date time of ending execution.
	 * @param workspacePath Experiment workspace path.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws VCSException Version control system related problems.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	public static void notifyProcessExecutionEnding(String processInstanceId, String[] context, Date startProcessDateTime, Date endProcessDateTime, String workspacePath) throws ProvMonitorException{
		//Record Timestamp
		//Verify accessed files
		//Commit changed files
		notifyActivityExecutionEnding(processInstanceId, context, startProcessDateTime, endProcessDateTime, workspacePath);
	}
	
	/**
	 * Notify ending of a Process (composite activity) instance execution.
	 * <br /><br />
	 * <b>Update - Instead Use:</b> <br />
	 * public static void notifyProcessExecutionEnding(String processInstanceId, String[] context, Date startProcessDateTime, Date endProcessDateTime, String workspacePath) throws ProvMonitorException.
	 */
	@Deprecated
	public static boolean notifyProcessExecutionEnding(String processInstanceId, String[] context){
		//Record Timestamp
		//Verify accessed files
		//Commit changed files
		return false;
	}
	
	/**
	 * <strong>NOT YET IMPLEMENTED</strong><br /><br />
	 * Notify a decision point execution. 
	 * @param decisionPointId Decision point identifier.
	 * @param optionValue Option value used on the decision.
	 * @param context Sequence of identifiers that defines a decision point location in the experiment. (path, i.e.: Sub workflows that wraps the decision point)
	 * @return if the operation was successful.
	 * 
	 */
	public static boolean notifyDecisionPointEnding(String decisionPointId, String optionValue, String[] context){
		return false;
	}

	/**
	 * Publish artifact values. 
	 * @param artifactId Artifact identifier.
	 * @param context context Sequence of identifiers that defines an artifact location in the experiment.
	 * @param value Artifact value.
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	//public boolean setArtifactValue(String artifactId, String[] context, String value) throws CharonException{
		//return repository.getCharon().getCharonAPI().setArtifactValue(artifactId, context, value);
	//}
	public static void setArtifactValue(String artifactId, String[] context, String value) throws ProvMonitorException{
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("/");
			}
			elementPath.append(path);
		}
		
		//Preparing artifact Objetct to be persisted
		ArtifactInstance artifactInstance = new ArtifactInstance();
		artifactInstance.setArtifactId(artifactId);
		artifactInstance.setArtifactValue(value);
		artifactInstance.setArtifactPath(elementPath.toString());
		
		ArtifactInstanceDAO artifactValueDAO = new ProvMonitorDAOFactory().getArtifactInstanceDAO();
		artifactValueDAO.persist(artifactInstance);
		
	}
	
	
	/**
	 * Publish an artifact value location.
	 * 
	 * @param artifactId Artifact identifier.
	 * @param context context Sequence of identifiers that defines an artifact location in the experiment.
	 * @param value Artifact value.
	 * @param hostURL URL of artifact location host.
	 * @param hostLocalPath Path of the artifact location inside the host.
	 * 
	 * @throws ProvMonitorException ProvMonitor base exception if some irrecoverable or runtime exception occurs.
	 * @throws DatabaseException Database related problems.
	 * @throws ConnectionException Database connection problems.
	 */
	//public boolean publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath) throws CharonException{
		//return repository.getCharon().getCharonAPI().publishArtifactValueLocation(artifactId, context, hostURL, hostLocalPath);
	//}
	public static void publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath){
		
	}
	
	@SuppressWarnings("unused")
	private static void oldNotifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspacePath) throws ProvMonitorException{
		try{
			//Prepare ActivityObject to be persisted
			ExecutionStatus elementExecStatus = new ExecutionStatus();
			elementExecStatus.setElementId(activityInstanceId);
			elementExecStatus.setElementType("activity");
			elementExecStatus.setStatus("starting");
			//Mounting context
			StringBuilder elementPath = new StringBuilder();
			for (String path: context){
				if (elementPath.length()>0){
					elementPath.append("/");
				}
				elementPath.append(path);
			}
			
			elementExecStatus.setElementPath(elementPath.toString());
			//Record Timestamp
			elementExecStatus.setStartTime(activityStartDateTime);
			
			//ActivityInstance activity = new ActivityInstance();
			//activity.setActivityInstanceId(activityInstanceId);
			//activity.set
			
			
			//Reading accessedFiles
			ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
			try{
				//Collection<AccessedPath> accessedFiles = WorkspaceAccessReader.readAccessedPathsAndAccessTime(Paths.get(workspacePath), startActivityDateTime, true);
				Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readWorkspacePathStatusAndStatusTime(Paths.get(workspacePath), activityStartDateTime, true);
				if (accessedFiles != null && !accessedFiles.isEmpty()){
					for (WorkspacePathStatus acFile: accessedFiles){
						
						ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
						execFileStatus.setFileAccessDateTime(acFile.getStatusDateTime());
						execFileStatus.setFilePath(acFile.getPathName().replaceFirst("/", ""));
						execFileStatus.setElementId(activityInstanceId);
						execFileStatus.setElementPath(elementPath.toString());
						execFileStatus.setFiletAccessType(acFile.getPathStatusType().name());
						//execFileStatus.setFiletAccessType(ExecutionFilesStatus.TYPE_READ);
						
						execFiles.add(execFileStatus);
						
					}
				}
			}catch(Exception e){
				ProvMonitorLogger.warning("RetrospectiveProvenanceBusinessServices", "notifyActivityExecutionStartup", e.getMessage());
				ProvMonitorOutputManager.appendMenssage("WARNING: RetrospectiveProvenanceBusinessServices: notifyActivityExecutionStartup" + e.getMessage());
			}
			
			
			//Activity start commit
			StringBuilder message = new StringBuilder();
			message.append("ActivityInstanceId:")
				   .append(activityInstanceId)
				   .append("; context:")
				   .append(elementPath.toString())
				   .append("; StartActivityCommit");
			
			VCSManager cvsManager = VCSManagerFactory.getInstance();
			//((GitManager)cvsManager).getStatus(workspacePath);
			//Set<String> removed = cvsManager.getRemovedFiles(workspacePath);
			cvsManager.addAllFromPath(workspacePath);
			Set<String> removed = cvsManager.removeAllFromPath(workspacePath);
			String commitId = cvsManager.commit(workspacePath, message.toString());
			
			//Registering removed files
			if (removed != null && !removed.isEmpty()){
				for (String removedPath: removed){
					ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
					execFileStatus.setFileAccessDateTime(activityStartDateTime);
					execFileStatus.setFilePath(workspacePath.concat("/".concat(removedPath)));
					execFileStatus.setElementId(activityInstanceId);
					execFileStatus.setElementPath(elementPath.toString());
					execFileStatus.setFiletAccessType(PathAccessType.REMOVE.name());
					
					execFiles.add(execFileStatus);
				}
			}
			
			//Recording Commit ID
			//elementExecStatus.setCommitId(commitId);
			ExecutionCommit execCommit = new ExecutionCommit();
			execCommit.setCommitId(commitId);
			execCommit.setCommitTime(activityStartDateTime);
			execCommit.setStatus("ActivityStart");
			execCommit.setElementId(elementExecStatus.getElementId());
			execCommit.setElementPath(elementExecStatus.getElementPath());
			
			//TODO: Implementar controle de transação e atomicidade para os casos de atributos multivalorados
			
			ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
			
			//factory.getActivityInstanceDAO().persist(activityInstance);
			factory.getExecutionStatusDAO().persist(elementExecStatus);
			factory.getExecutionCommitDAO().persist(execCommit);
			
			//Persist acessed files
			for (ExecutionFilesStatus executionFileStatus: execFiles){
				factory.getExecutionFileStatusDAO().persist(executionFileStatus);
			}
			
			
		}catch(Exception e){
			throw new ProvMonitorException(e.getMessage(), e.getCause());
		}
		
	}
	
	public static void newNotifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspacePath) throws ProvMonitorException{
		//Prepare ActivityObject to be persisted
		ExecutionStatus elementExecStatus = getNewExecStatus(activityInstanceId, context, activityStartDateTime, null);
		
		//Reading accessedFiles
		Collection<ExecutionFilesStatus> execFiles = getAccessedFiles(elementExecStatus, workspacePath);
		
		//Activity start commit
		StringBuilder message = new StringBuilder();
		message.append("ActivityInstanceId:")
			   .append(activityInstanceId)
			   .append("; context:")
			   .append(elementExecStatus.getElementPath())
			   .append("; StartActivityCommit");
		
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		
		cvsManager.addAllFromPath(workspacePath);
		//Identifying removed files
		Set<String> removed = cvsManager.removeAllFromPath(workspacePath);
		//Committing changes
		String commitId = cvsManager.commit(workspacePath, message.toString());
		
		//Joining all files changes to be persisted.
		Collection<ExecutionFilesStatus> removedFiles = getRemovedFiles(removed, elementExecStatus, workspacePath);
		execFiles.addAll(removedFiles);
		
		//Recording Commit ID
		//elementExecStatus.setCommitId(commitId);
		ExecutionCommit execCommit = new ExecutionCommit();
		execCommit.setCommitId(commitId);
		execCommit.setCommitTime(activityStartDateTime);
		execCommit.setStatus("ActivityStart");
		execCommit.setElementId(elementExecStatus.getElementId());
		execCommit.setElementPath(elementExecStatus.getElementPath());
		
		
		//TODO: Implement transaction control and atomicity for multivalued attributes.
		
		//Persisting
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		//factory.getActivityInstanceDAO().persist(activityInstance);
		factory.getExecutionStatusDAO().persist(elementExecStatus);
		factory.getExecutionCommitDAO().persist(execCommit);
		
		//Persisting accessed files
		for (ExecutionFilesStatus executionFileStatus: execFiles){
			factory.getExecutionFileStatusDAO().persist(executionFileStatus);
		}
		
	}
	
}
