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
import java.util.Calendar;
import java.util.Date;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.business.scicumulus.SciCumulusBusinessHelper;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.log.LogMessages;
import br.uff.ic.provmonitor.log.ProvMonitorLevel;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;
import br.uff.ic.provmonitor.utils.ExtendedContextUtils;

public class ProvMonitorTests {
	public static void main(String[] args) {

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
}
