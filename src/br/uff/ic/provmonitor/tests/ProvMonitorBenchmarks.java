package br.uff.ic.provmonitor.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.vcsmanager.VCSManager;
import br.uff.ic.provmonitor.vcsmanager.VCSManagerFactory;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspaceAccessReader;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspacePathStatus;

public class ProvMonitorBenchmarks {
	
	public static final int AT_SHORT_TERM = 1;
	public static final int AT_SHORT_TERM_MANY_FILES = 2;
	public static final int AT_LONG_TERM = 10;
	public static final int AT_LONG_TERM_MANY_FILES = 11;
	
	public static void main(String[] args) {
		runBanchmark();
	}

	public static void runBanchmark(){
		
		String experimentId = "BenchMark1_FewActivitiesFewFiles";
		String experimentInstanceId = "BenchMark1_FewActivitiesFewFiles_Instance1";
		String centralRepository = "C:/Testes/CentralRepositories/BenchMark1/CentralRepoFewFiles";
		String workspacePathBase = "C:/Testes/workspaces/BenchMarks/FewActivitiesFewFiles";
		
		try{
		
			List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase);
		
			benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
		
		}catch(ProvMonitorException | IOException e){
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @param activitiesTypes
	 */
//	private static void execute(String experimentInstanceId, String centralRepository, String workspacePathBase, List<Integer> activitiesTypes){
//		
//	}
	
	/**
	 * Setup the benchmark with properties to match an execution with few activities with few files
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @return
	 * @throws IOException 
	 * @throws VCSException 
	 */
	private static List<Integer> benchMarkSetup_FewActivitiesFewFiles(String experimentInstanceId, String centralRepository, String workspacePathBase) throws VCSException, IOException{
		Integer totalActivities = 4;
		createInputData(true, centralRepository, 1, 10);
		List<Integer> activitiesTypes = new ArrayList<Integer>();
		for (int i = 0; i < totalActivities; i++){
			activitiesTypes.add(ProvMonitorBenchmarks.AT_SHORT_TERM);
		}
		return activitiesTypes;
	}
	
	/**
	 * Creates the input data file to be used by the benchmarks
	 * @param usesVCS
	 * @param repositoryPath
	 * @param inputFilesNumber
	 * @param filesNumberOfLines
	 * @throws IOException 
	 * @throws VCSException 
	 */
	private static void createInputData(Boolean usesVCS, String repositoryPath, Integer inputFilesNumber, Integer filesNumberOfLines) throws IOException, VCSException{
		
		//Creating central repository
		Collection<String> filesNames = new ArrayList<String>();
		for (int i=0;i<inputFilesNumber;i++){
			filesNames.add(new String("file" + String.valueOf(i) + ".txt"));
		}
		
		VCSManager vcsManager = VCSManagerFactory.getInstance();

		if (!vcsManager.isWorkspaceCreated(repositoryPath)){
			if (usesVCS){ 
				vcsManager.createWorkspace(repositoryPath);
			}
			
			for (String fileName : filesNames) {
				createFileContent(repositoryPath + "/" + fileName, filesNumberOfLines);
			}
			
			if (usesVCS){ 
				vcsManager.addAllFromPath(repositoryPath);
				vcsManager.commit(repositoryPath, "Initial Import");
			}
		}
		
	}
	
	/**
	 * Creates data file random content;
	 * @param filePath
	 * @param numberOfLines
	 * @throws IOException 
	 */
	private static void createFileContent(String filePath, Integer numberOfLines) throws IOException{
		File exampleHtml = new File(filePath);
		
		exampleHtml.createNewFile();
		FileWriter out = new FileWriter(exampleHtml);
		
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		
		for (int i=0;i<numberOfLines;i++){
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.write(nonce);
			out.write(System.getProperty("line.separator"));
			
		}
		
		out.close();
			
	}
	
	/**
	 * Change the content of the informed file with and random line at the end of file.
	 * @param filePath
	 */
	private static void changeFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			FileWriter fr = new FileWriter(exampleHtml, true);
			BufferedWriter out = new BufferedWriter(fr);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.append(nonce);
			out.close();
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes the changes in files. The basic representation of a basic activity that writes at the informed files.
	 * @param workspaceActivation
	 * @param fileNames
	 * @throws VCSException
	 */
	private static void executeActivation(String workspaceActivation, Collection<String> fileNames) throws VCSException{
		//Changes on Workspace
		for (String file : fileNames){
			changeFileContent(workspaceActivation + "/" + file);
			//changeFileContent(workspaceActivation + "/" + file);	
		}
	}
	
	/**
	 * ProvMonitors Experiment Setup to initialize an experiment execution.
	 * @param experimentId
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePath
	 * @param startDateTime
	 * @return The Experiment Instance ID.
	 * @throws ProvMonitorException
	 */
	private static String initializeExperimentTest(String experimentId, String experimentInstanceId, String centralRepository, String workspacePath, Date startDateTime) throws ProvMonitorException{
		try {
			experimentInstanceId = RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, experimentInstanceId, centralRepository, workspacePath);
		} catch (ProvMonitorException e) {
			ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "initializeExperimentTest", "Exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return experimentInstanceId;
	}
	
	/**
	 * The Benchmark execution with the configured set of activities types
	 * @param experimentId
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @param activitiesTypes
	 * @throws ProvMonitorException
	 * @throws IOException
	 */
	private static void benchMarkExecution(String experimentId, String experimentInstanceId, String centralRepository, String workspacePathBase, List<Integer> activitiesTypes) throws ProvMonitorException, IOException{
		
		String[] contextBase = {experimentInstanceId,"root"};
		
		String workspacePathBaseTrial = workspacePathBase + "/" + experimentInstanceId;
		String workspaceIntermediate = workspacePathBaseTrial + "/input";
		
		String activityInstanceBaseName="activity";
		Integer activityCount = 0;
		Boolean firstIterationOfIntermediateWorkspace = true;
		
		Date startDateTime = Calendar.getInstance().getTime();
		
		for (Integer activityType : activitiesTypes){
			String activityInstanceId = activityInstanceBaseName + activityCount++;
			String workspaceActivation = workspacePathBaseTrial + "/" + activityInstanceId + "/"+experimentInstanceId+"/input";
			
			//Initializing Experiment
			if (firstIterationOfIntermediateWorkspace){
				initializeExperimentTest(experimentId, experimentInstanceId, centralRepository, workspaceIntermediate, startDateTime);
				firstIterationOfIntermediateWorkspace = false;
			}
			
			String[] context = {contextBase[0],contextBase[1],activityInstanceId};
			
			switch (activityType){
				case ProvMonitorBenchmarks.AT_SHORT_TERM:
				case ProvMonitorBenchmarks.AT_SHORT_TERM_MANY_FILES:
				case ProvMonitorBenchmarks.AT_LONG_TERM:
				case ProvMonitorBenchmarks.AT_LONG_TERM_MANY_FILES:
					//executeActivity();
					shortTermActivityExecution(activityInstanceId, workspaceIntermediate, workspaceActivation, context);
					break;
			}
			
			//The next activity intermediate workspace is the last activity workspace.
			workspaceIntermediate = workspaceActivation;
		}
		
		Date endActiviyDateTime = Calendar.getInstance().getTime();
		//Finalizing Experiment
		RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspaceIntermediate, endActiviyDateTime);
	}
	
	/**
	 * Execution of a ShorTerm activity
	 * @param activityInstanceId
	 * @param workspaceIntermediate
	 * @param workspaceActivation
	 * @param context
	 * @throws ProvMonitorException
	 * @throws IOException
	 */
	private static void shortTermActivityExecution(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context) throws ProvMonitorException, IOException{
		List<String> filesNames = new ArrayList<String>();
		filesNames = getFilesNames(workspaceIntermediate);
		activityExecution(activityInstanceId, workspaceIntermediate, workspaceActivation, context, filesNames);
	}
	
	private static List<String> getFilesNames(String rootPath) throws IOException{
		//Date startDate = new Date("01/01/1900 00:00:00");
		//Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readAccessedPathStatusAndStatusTime(Paths.get(rootPath), startDate, true);
		Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readPathFilesLasReadTime(Paths.get(rootPath), true);
		
		
		List<String> filesNames = new ArrayList<String>();
		for (WorkspacePathStatus wkps : accessedFiles){
			String pathName = wkps.getPathName();
			String fileName = pathName.replace(rootPath, "");
			if (fileName != null && fileName.length() >0){
				boolean isNameBeginsCleaned = false;
				while (!isNameBeginsCleaned){
					if (fileName.charAt(0) == '/' ){
						fileName = fileName.substring(1, fileName.length());
					}else{
						isNameBeginsCleaned = true;
					}
				}
			}
			
			filesNames.add(fileName);
		}
		
		return filesNames;
	}
	
	private static void activityExecution(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context, List<String> filesNames) throws ProvMonitorException{
		Date activityStartDateTime = Calendar.getInstance().getTime();
		RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId, context, activityStartDateTime, workspaceIntermediate, workspaceActivation);
		//Execute Activity 1 - Instance 1
		executeActivation(workspaceActivation, filesNames);
		//createFileContent(workspaceActivation + "/fileActivity1.txt", 1);
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//Remove File
		//deleteFile(workspaceActivation1 + "/Folder1/teste2.html");
		//Ending Activity 1 - Instance 1
		
		activityStartDateTime = null;
		Date endActiviyDateTime = Calendar.getInstance().getTime();
		RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId, context, activityStartDateTime, endActiviyDateTime, workspaceIntermediate, workspaceActivation);
	}
}
