package br.uff.ic.provmonitor.tests;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.business.scicumulus.SciCumulusBusinessHelper;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.VCSException;
import br.uff.ic.provmonitor.log.LogMessages;
import br.uff.ic.provmonitor.log.ProvMonitorLevel;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;
import br.uff.ic.provmonitor.utils.ExtendedContextUtils;
import br.uff.ic.provmonitor.vcsmanager.VCSManager;
import br.uff.ic.provmonitor.vcsmanager.VCSManagerFactory;

public class ProvMonitorTests {
	public static void main(String[] args) {

		//experimentWithTwoScicumulusActivitiesTest();
		
		//experimentScicumulusActivitiesBranchStrategies();
		experimentScicumulusActivitiesIntermediateWorkspaceBranchStrategies();
	
	}
	
	@SuppressWarnings("unused")
	private static void experimentWithTwoScicumulusActivitiesTest(){
		Date startDateTime = Calendar.getInstance().getTime();
		try {
			String experimentInstanceId = "ScicumulusTeste14";
			String centralRepository = "C:/Testes/CentralRepo/teste";
			String workspacePathBase = "C:/Testes/workspaces/WorkspaceScicumulus14";
			String workspacePath = workspacePathBase + "/input";
			String activity1InstanceId = "Activity1Instance1";
			String[] context = {experimentInstanceId,"root","Activity1Instance1"};
			String extendedContext = workspacePathBase + "/Activity1/1/";
			
			//Initializing Experiment
			initializeExperimentTest("Scicumulus", experimentInstanceId, centralRepository, workspacePath, startDateTime);
			
			//Starting Activity 1 - Instance 1
			Date activityStartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity1InstanceId, context, activityStartDateTime, workspacePath, extendedContext);
			createFileContent(extendedContext + "/file.html");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Changing file's content.
			changeFileContent(extendedContext + "/teste1.html");
			//Remove File
			deleteFile(extendedContext + "/Folder1/teste2.html");
			//Ending Activity 1 - Instance 1
			Date endActiviyDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity1InstanceId, context, activityStartDateTime, endActiviyDateTime, workspacePath, extendedContext);
			
			
			//Activity 2 - Instance 1
			String activity2InstanceId = "Activity2Instance1";
			String[] context2 = {experimentInstanceId,"root","Activity2Instance1"};
			String extendedContext2 = workspacePathBase + "/Activity2/1/";
			//Activity Start
			Date activity2StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity2InstanceId, context2, activity2StartDateTime, workspacePath, extendedContext2);
			createFileContent(extendedContext2 + "/file.html");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Changing file's content.
			changeFileContent(extendedContext2 + "/teste1.html");
			//Remove File
			//deleteFile(workspacePath + "/Folder1/teste2.html");
			//Ending Activity 1 - Instance 1
			Date endActiviy2DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity2InstanceId, context2, activity2StartDateTime, endActiviy2DateTime, workspacePath, extendedContext2);
			
			
			//Activity 3 - Instance 1
			String activity3InstanceId = "Activity3Instance1";
			String[] context3 = {experimentInstanceId,"root","Activity3Instance1"};
			String extendedContext3 = workspacePathBase + "/Activity3/1/";
			//Activity Start
			Date activity3StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity3InstanceId, context3, activity3StartDateTime, workspacePath, extendedContext3);
			createFileContent(extendedContext3 + "/file.html");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Changing file's content.
			changeFileContent(extendedContext3 + "/teste1.html");
			//Remove File
			//deleteFile(workspacePath + "/Folder1/teste2.html");
			//Ending Activity 1 - Instance 1
			Date endActiviy3DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity3InstanceId, context3, endActiviy3DateTime, endActiviy3DateTime, workspacePath, extendedContext3);
			
			//Finalizing Experiment
			RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspacePath, endActiviyDateTime);
			
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void experimentWithOneActivityTest(){
		Date startDateTime = Calendar.getInstance().getTime();
		//completeProcessTest();
		try {
			//String centralRepository = "C:/Testes/CentralRepo/exp_ProvMonitor";
			//String workspacePath = "C:/Testes/workspaces/SciCumulusWksp1";
			String centralRepository = "C:/Testes/CentralRepo/Repo1";
			String workspacePath = "C:/Testes/workspaces/WorkspaceExistente8";
			String activity1InstanceId = "Activity1Instance1";
			String[] context = {"root","Activity1Instance1"};
			//String extendedContext = "C:/Testes/Workspace/WorkspaceExistente/1/";
			
			//Initializing Experiment
			//String newWorkspacePath = util(workspacePath, context, extendedContext);
			initializeExperimentTest("Scicumulus", "ScicumulusTeste1", centralRepository, workspacePath, startDateTime);
			//Starting Activity 1 - Instance 1
			Date activityStartDateTime = Calendar.getInstance().getTime();
			
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity1InstanceId, context, activityStartDateTime, workspacePath);
			//RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity1InstanceId, context, activityStartDateTime, newWorkspacePath);
			createFileContent(workspacePath + "/file.html");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Changing file's content.
			changeFileContent(workspacePath + "/teste1.html");
			
			//Remove File
			deleteFile(workspacePath + "/Folder1/teste2.html");
			
			//Ending Activity 1 - Instance 1
			Date endActiviyDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity1InstanceId, context, activityStartDateTime, endActiviyDateTime, workspacePath);
			//Finalizing Experiment
			RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution("ScicumulusTeste1", centralRepository, workspacePath, endActiviyDateTime);
			
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static String util(String workspacePath, String[] context, String extendedContext){
		
		if (SciCumulusBusinessHelper.isSciCumulusExecution(extendedContext)){
			workspacePath = SciCumulusBusinessHelper.workspaceUpdate(workspacePath, extendedContext);
			ExtendedContextUtils exCUtil = new ExtendedContextUtils(extendedContext);
			String[] context2 = exCUtil.appendContext(context);
		}
		return workspacePath;
	}
	
	
	private static String initializeExperimentTest(String experimentId, String experimentInstanceId, String centralRepository, String workspacePath, Date startDateTime) throws ProvMonitorException{
		//String experimentInstanceId = "";
		
		try {
			experimentInstanceId = RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, experimentInstanceId, centralRepository, workspacePath);
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "initializeExperimentTest", "Exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return experimentInstanceId;
	}
	
	private static void createFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			exampleHtml.createNewFile();
			FileWriter out = new FileWriter(exampleHtml);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.write("<html>");
			out.write("<table>");
			out.write("<tr><td>line1</td><td>");
			out.write(nonce);
			out.write("</td><tr>");
			out.write("</table>");
			out.write("</html>");
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void changeFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			FileWriter out = new FileWriter(exampleHtml, true);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.append("<html>");
			out.append("<table>");
			out.append("<tr><td>line1</td><td>");
			out.append(nonce);
			out.append("</td><tr>");
			out.append("</table>");
			out.append("</html>");
			out.append("\n");
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void deleteFile(String filePath){
		try {
			Path path = Paths.get(filePath);
		    Files.delete(path);
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", filePath);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", filePath);
		} catch (IOException x) {
		    // File permission problems are caught here.
		    System.err.println(x);
		}
	}
	
	@SuppressWarnings("unused")
	private static void processTestWithLog1(){
		//Execution start time...
				Date provExecStartTime = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
				try{
					
					String centralRepository = "";
					String workspacePath = "";
					String activityInstanceId_1 = "ActivyInstanceId01";
					//String context = "";
					Date startDateTime = Calendar.getInstance().getTime();
					
					//Starting Log
					ProvMonitorLogger.config(ProvMonitorLevel.DEBUG);
					
					//Loading Properties
					ProvMonitorProperties prop = ProvMonitorProperties.getInstance();
					prop.getDataBaseType();
					
					//Starting execution
					ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(provExecStartTime)});
					ProvMonitorLogger.info(ProvMonitorTests.class.getName(), "Main", "Starting ProvMonitor tests...");
					
					//InitializeExperimentTest
					ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Calling initializeExperimentTest.");
					String experimentInstanceId = initializeExperimentTest("ExperimentTeste1", null, centralRepository, workspacePath, startDateTime);
					ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Return of initializeExperimentTest without erros. ExperimentInstanceId: " + experimentInstanceId + ". ");
					
					//Updating context
					//context = experimentInstanceId.concat("\\".concat(activityInstanceId_1));
					String [] context = {experimentInstanceId,activityInstanceId_1};
					
					//Activity Start
					Date activityStartDateTime = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(activityStartDateTime)});
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId_1, context, activityStartDateTime, workspacePath);
					Date activityStartDateTimeEndExec = Calendar.getInstance().getTime();
					Long diffTimeActvity1 = ((activityStartDateTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(activityStartDateTimeEndExec),diffTimeActvity1});
					
					//Changing files
					createFileContent(centralRepository + "\\File1.html");
					createFileContent(centralRepository + "\\File2.html");
					
					//Activity End
					Date endActiviyDateTime = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionEnding", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(endActiviyDateTime)});
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId_1, context, activityStartDateTime, endActiviyDateTime, workspacePath);
					Date endActiviyDateTimeEndExec = Calendar.getInstance().getTime();
					Long diffTimeActvity1End = ((activityStartDateTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionEnding", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(endActiviyDateTimeEndExec),diffTimeActvity1End});
					
					//Finalize Experiment
					Date endDateTime = Calendar.getInstance().getTime();
					RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspacePath, endDateTime);
					
				}catch(ProvMonitorException e){
					ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "main", "Error: " + e.getMessage());
				}finally{
					Date provExecEndTime = Calendar.getInstance().getTime();
					//Double diffTime = ((Long)((provExecEndTime.getTime() - provExecStartTime.getTime())/1000)).doubleValue();
					Long diffTime = ((provExecEndTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(provExecEndTime), diffTime});
				}
	}
	
	@SuppressWarnings("unused")
	private static void completeProcessTest(){
		//Execution start time...
				Date provExecStartTime = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
				try{
					
					String centralRepository = "C:\\Testes\\CentralRepo\\Repo1";
					String workspacePath = "C:\\Testes\\workspaces\\WorkspaceFullClone";
					String activityInstanceId_1 = "ActivyInstanceId01";
					//String context = "";
					Date startDateTime = Calendar.getInstance().getTime();
					
					
					//Loading Properties
					ProvMonitorProperties.getInstance().getLogMode();
					
					//Starting Log
					//ProvMonitorLogger.config(ProvMonitorLevel.DEBUG);
					ProvMonitorLogger.config(ProvMonitorProperties.getInstance().getLogMode());
					
					//Loading Properties
					ProvMonitorProperties prop = ProvMonitorProperties.getInstance();
					prop.getDataBaseType();
					
					//Starting execution
					ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(provExecStartTime)});
					ProvMonitorLogger.info(ProvMonitorTests.class.getName(), "Main", "Starting ProvMonitor tests...");
					
					//InitializeExperimentTest
					ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Calling initializeExperimentTest.");
					String experimentInstanceId = initializeExperimentTest("ExperimentTeste1", null, centralRepository, workspacePath, startDateTime);
					ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Return of initializeExperimentTest without erros. ExperimentInstanceId: " + experimentInstanceId + ". ");
					
					//Updating context
					//context = experimentInstanceId.concat("\\".concat(activityInstanceId_1));
					String [] context = {experimentInstanceId,activityInstanceId_1};
					
					//Activity Start
					Date activityStartDateTime = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(activityStartDateTime)});
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId_1, context, activityStartDateTime, workspacePath);
					Date activityStartDateTimeEndExec = Calendar.getInstance().getTime();
					Long diffTimeActvity1 = ((activityStartDateTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(activityStartDateTimeEndExec),diffTimeActvity1});
					
					//Changing files
					createFileContent(workspacePath + "\\File1.html");
					createFileContent(workspacePath + "\\File2.html");
					
					//Activity End
					Date endActiviyDateTime = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionEnding", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(endActiviyDateTime)});
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId_1, context, activityStartDateTime, endActiviyDateTime, workspacePath);
					Date endActiviyDateTimeEndExec = Calendar.getInstance().getTime();
					Long diffTimeActvity1End = ((activityStartDateTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionEnding", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(endActiviyDateTimeEndExec),diffTimeActvity1End});
					
					//Finalize Experiment
					Date endDateTime = Calendar.getInstance().getTime();
					RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspacePath, endDateTime);
					
				}catch(ProvMonitorException e){
					ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "main", "Error: " + e.getMessage());
				}finally{
					Date provExecEndTime = Calendar.getInstance().getTime();
					//Double diffTime = ((Long)((provExecEndTime.getTime() - provExecStartTime.getTime())/1000)).doubleValue();
					Long diffTime = ((provExecEndTime.getTime() - provExecStartTime.getTime())/1000);
					ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(provExecEndTime), diffTime});
				}
	}
	
	
	@SuppressWarnings("unused")
	private static void experimentScicumulusActivitiesBranchStrategies(){
		Date startDateTime = Calendar.getInstance().getTime();
		try {
			String experimentInstanceId = "ScicumulusTeste03";
			String centralRepository = "C:/Testes/SciCumulus/CentralRepo/ScicumulusTeste01";
			String workspacePathBase = "C:/Testes/SciCumulus/workspaces/Scicumulus/Trial3";
			
			//Workflow Input workspace
			String workspaceIntermediate = workspacePathBase + "/input";
			
			//String branchName1 = "Branch01";
			
			//Activities - Workflow Simulation
			//Mafft
			String activity1InstanceId = "Mafft";
			//ModelGenerator
			String activity2InstanceId = "ModelGenerator";
			//Readseq
			String activity3InstanceId = "Readseq";
			//Raxml
			String activity4InstanceId = "Raxml";
			
			
			String workspaceActivation1 = workspacePathBase + "/" + activity1InstanceId + "/1/input";
			String workspaceActivation2 = workspacePathBase + "/" + activity2InstanceId + "/1/input";
			String workspaceActivation3 = workspacePathBase + "/" + activity3InstanceId + "/1/input";
			String workspaceActivation4 = workspacePathBase + "/" + activity4InstanceId + "/1/input";
			
			String[] context = {experimentInstanceId,"root",activity1InstanceId};
			//String extendedContext = workspacePathBase + "/Activity1/1/";
			
			
			//Creating central repository
			String fileName1 = "file1.txt";
			String fileName2 = "file2.txt";
			Collection<String> filesNames = new ArrayList<String>();
			filesNames.add(fileName1);
			filesNames.add(fileName2);
			
			VCSManager vcsManager = VCSManagerFactory.getInstance();
			if (!vcsManager.isWorkspaceCreated(centralRepository)){
				vcsManager.createWorkspace(centralRepository);
				
				createFileContent(centralRepository + "/" + fileName1);
				createFileContent(centralRepository + "/" + fileName2);
				vcsManager.addAllFromPath(centralRepository);
				vcsManager.commit(centralRepository, "Initial Import");
			}
			
			//Initializing Experiment
			initializeExperimentTest("Scicumulus", experimentInstanceId, centralRepository, workspaceIntermediate, startDateTime);

			//////////////////////////////////
			//Activity 1 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			Date activityStartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity1InstanceId, context, activityStartDateTime, workspaceIntermediate, workspaceActivation1);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation1, filesNames);
			createFileContent(workspaceActivation1 + "/fileActivity1.txt");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation1 + "/Folder1/teste2.html");
			//Ending Activity 1 - Instance 1
			Date endActiviyDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity1InstanceId, context, activityStartDateTime, endActiviyDateTime, workspaceIntermediate, workspaceActivation1);
			//////////////////////////////////
			//Activity 1 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 2 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context2 = {context[0],context[1],activity2InstanceId};
			Date activity2StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity2InstanceId, context2, activity2StartDateTime, workspaceActivation1, workspaceActivation2);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation2, filesNames);
			createFileContent(workspaceActivation2 + "/fileActivity2.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy2DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity2InstanceId, context2, activity2StartDateTime, endActiviy2DateTime, workspaceIntermediate, workspaceActivation2);
			//////////////////////////////////
			//Activity 2 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 3 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context3 = {context[0],context[1],activity3InstanceId};
			Date activity3StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity3InstanceId, context3, activity3StartDateTime, workspaceActivation2, workspaceActivation3);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation3, filesNames);
			createFileContent(workspaceActivation3 + "/fileActivity3.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy3DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity3InstanceId, context3, activity3StartDateTime, endActiviy3DateTime, workspaceIntermediate, workspaceActivation3);
			//////////////////////////////////
			//Activity 3 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 4 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context4 = {context[0],context[1],activity4InstanceId};
			Date activity4StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity4InstanceId, context4, activity4StartDateTime, workspaceActivation3, workspaceActivation4);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation4, filesNames);
			//createFileContent(workspaceActivation3 + "/fileActivity3.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy4DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity4InstanceId, context4, activity4StartDateTime, endActiviy4DateTime, workspaceIntermediate, workspaceActivation4);
			//////////////////////////////////
			//Activity 4 - Instance 1 - End///
			//////////////////////////////////
			
			
			
			//Finalizing Experiment
			RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspaceIntermediate, endActiviyDateTime);
			
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//@SuppressWarnings("unused")
	private static void experimentScicumulusActivitiesIntermediateWorkspaceBranchStrategies(){
		Date startDateTime = Calendar.getInstance().getTime();
		try {
			Boolean firstIterationOfIntermediateWorkspace = true;
			String experimentInstanceId = "ScicumulusTeste100";
			String centralRepository = "C:/Testes/SciCumulus/CentralRepo/ScicumulusTeste100";
			String workspacePathBase = "C:/Testes/SciCumulus/workspaces/Scicumulus";
			
			//Workflow Input workspace
			String workspaceIntermediate = workspacePathBase + "/input";
			String workspacePathBaseTrial = workspacePathBase + "/Trial100";
			
			//String branchName1 = "Branch01";
			
			//Activities - Workflow Simulation
			//Mafft
			String activity1InstanceId = "Mafft";
			//ModelGenerator
			String activity2InstanceId = "ModelGenerator";
			//Readseq
			String activity3InstanceId = "Readseq";
			//Raxml
			String activity4InstanceId = "Raxml";
			
			
			String workspaceActivation1 = workspacePathBaseTrial + "/" + activity1InstanceId + "/1/input";
			String workspaceActivation2 = workspacePathBaseTrial + "/" + activity2InstanceId + "/1/input";
			String workspaceActivation3 = workspacePathBaseTrial + "/" + activity3InstanceId + "/1/input";
			String workspaceActivation4 = workspacePathBaseTrial + "/" + activity4InstanceId + "/1/input";
			
			String[] context = {experimentInstanceId,"root",activity1InstanceId};
			//String extendedContext = workspacePathBase + "/Activity1/1/";
			
			
			//Creating central repository
			String fileName1 = "file1.txt";
			String fileName2 = "file2.txt";
			Collection<String> filesNames = new ArrayList<String>();
			filesNames.add(fileName1);
			filesNames.add(fileName2);
			
			VCSManager vcsManager = VCSManagerFactory.getInstance();
			if (!vcsManager.isWorkspaceCreated(centralRepository)){
				vcsManager.createWorkspace(centralRepository);
				
				createFileContent(centralRepository + "/" + fileName1);
				createFileContent(centralRepository + "/" + fileName2);
				vcsManager.addAllFromPath(centralRepository);
				vcsManager.commit(centralRepository, "Initial Import");
			}
			
			//Initializing Experiment
			if (firstIterationOfIntermediateWorkspace){
				initializeExperimentTest("Scicumulus", experimentInstanceId, centralRepository, workspaceIntermediate, startDateTime);
			}

			//////////////////////////////////
			//Activity 1 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			Date activityStartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity1InstanceId, context, activityStartDateTime, workspaceIntermediate, workspaceActivation1, true);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation1, filesNames);
			createFileContent(workspaceActivation1 + "/fileActivity1.txt");
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation1 + "/Folder1/teste2.html");
			//Ending Activity 1 - Instance 1
			Date endActiviyDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity1InstanceId, context, activityStartDateTime, endActiviyDateTime, workspaceIntermediate, workspaceActivation1);
			//////////////////////////////////
			//Activity 1 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 2 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context2 = {context[0],context[1],activity2InstanceId};
			Date activity2StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity2InstanceId, context2, activity2StartDateTime, workspaceActivation1, workspaceActivation2);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation2, filesNames);
			createFileContent(workspaceActivation2 + "/fileActivity2.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy2DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity2InstanceId, context2, activity2StartDateTime, endActiviy2DateTime, workspaceIntermediate, workspaceActivation2);
			//////////////////////////////////
			//Activity 2 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 3 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context3 = {context[0],context[1],activity3InstanceId};
			Date activity3StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity3InstanceId, context3, activity3StartDateTime, workspaceActivation2, workspaceActivation3);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation3, filesNames);
			createFileContent(workspaceActivation3 + "/fileActivity3.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy3DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity3InstanceId, context3, activity3StartDateTime, endActiviy3DateTime, workspaceIntermediate, workspaceActivation3);
			//////////////////////////////////
			//Activity 3 - Instance 1 - End///
			//////////////////////////////////
			
			
			//////////////////////////////////
			//Activity 4 - Instance 1 - Init//
			//////////////////////////////////
			//Starting Activity 1 - Instance 1
			String[] context4 = {context[0],context[1],activity4InstanceId};
			Date activity4StartDateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activity4InstanceId, context4, activity4StartDateTime, workspaceActivation3, workspaceActivation4);
			//Execute Activity 1 - Instance 1
			executeActivation(workspaceActivation4, filesNames);
			//createFileContent(workspaceActivation3 + "/fileActivity3.txt");
			
			try {
			Thread.sleep(3000);
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//Remove File
			//deleteFile(workspaceActivation2 + "/fileActivity1.txt");
			//Ending Activity 2 - Instance 1
			Date endActiviy4DateTime = Calendar.getInstance().getTime();
			RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activity4InstanceId, context4, activity4StartDateTime, endActiviy4DateTime, workspaceIntermediate, workspaceActivation4);
			//////////////////////////////////
			//Activity 4 - Instance 1 - End///
			//////////////////////////////////
			
			
			
			//Finalizing Experiment
			RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspaceIntermediate, endActiviyDateTime);
			
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void executeActivation(String workspaceActivation, Collection<String> fileNames) throws VCSException{
		//Changes on Workspace
		for (String file : fileNames){
			changeFileContent(workspaceActivation + "/" + file);
			changeFileContent(workspaceActivation + "/" + file);	
		}
	}
}
