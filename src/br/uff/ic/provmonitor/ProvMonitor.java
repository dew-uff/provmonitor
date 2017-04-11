package br.uff.ic.provmonitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.jar.Manifest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import br.uff.ic.provmonitor.business.ProvMonitorBusinessHelper;
import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.business.scicumulus.SciCumulusBusinessHelper;
import br.uff.ic.provmonitor.enums.MethodType;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ValidateException;
import br.uff.ic.provmonitor.log.LogMessages;
import br.uff.ic.provmonitor.log.ProvMonitorLevel;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.output.ProvMonitorOutputManager;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;
import br.uff.ic.provmonitor.tests.ProvMonitorTests;
import br.uff.ic.provmonitor.utils.DateUtils;
import br.uff.ic.provmonitor.validator.OptionsValidator;

public class ProvMonitor {

	private static void optionsInitialize(Options options){
		// add m option - Method Type to be invoked on the BusinessServices
		options.addOption("m", true, "method type");
		
		options.addOption("ei", 	true, "experimentId");
		options.addOption("eii", 	true, "experimentInstanceId");
		options.addOption("aii", 	true, "activityInstanceId");
		options.addOption("pii", 	true, "processInstanceId");
		options.addOption("di", 	true, "decisionPointId");
		options.addOption("ov", 	true, "optionValue");
		options.addOption("ai",		true, "artifactId");
		options.addOption("av", 	true, "artifactValue");
		options.addOption("hUrl",	true, "hostURL");
		options.addOption("hPath", 	true, "hostLocalPath");
		options.addOption("stDt", 	true, "sartDateTime");
		options.addOption("edDt", 	true, "endDateTime");
		options.addOption("cR", 	true, "centralRepository");
		options.addOption("wp", 	true, "workspacePath");
		options.addOption("sceContext", true, "sciCumulusExtendedContext");
		//options.addOption("",true,"");
		options.addOption("context",true,"context");
		options.addOption("ver", false, "version");
		options.addOption("propertiesGen", false, "propertiesDefaultValuesGeneration");
		options.addOption("firstActivity",false,"firstActivity");
	}
	
	/**
	 * @param args
	 * @return 
	 */
	public static void main(String[] args) {
		//Execution start time...
		Date provExecStartTime = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY HH:mm:ss:SSS");
		try {
			
			// CLI construction - create Options object
			Options options = new Options();
			//Initializing the Options of the CLI
			optionsInitialize(options);
			
			//Reading parameters from this program args
			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args);
			
			//Only prints ProvMonitorVersion into the Default Output
			if (cmd.hasOption("ver")){
				printProvMonitorVersion();
				return;
			}
			if (cmd.hasOption("propertiesGen")){
				ProvMonitorProperties.getInstance().generateDefaultPropertiesFile();
				return;
			}
			Boolean firstActivity = false;
			if (cmd.hasOption("firstActivity")){
				firstActivity = true;
			}
			
			//Starting Log
			ProvMonitorLogger.config(ProvMonitorLevel.DEBUG);
			ProvMonitorLogger.measure(ProvMonitor.class.getName(), "Main", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(provExecStartTime)});
			
			//Validate options in the CommandLine
			OptionsValidator.validate(cmd);
			
			//Verify the type of method to be invoked by the BusinessServices
			Integer methodId = Integer.parseInt(cmd.getOptionValue("m"));
			
			switch (MethodType.valueOf(methodId)){
			case INITIALIZE_EXPERIMENT_EXECUTION:
				{
					//Getting parameters
					String experimentId = cmd.getOptionValue("ei");
					String workspacePath = cmd.getOptionValue("wp");
					String centralRepository = cmd.getOptionValue("cR");
					String experimentInstanceId = cmd.getOptionValue("eii");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "initializeExperimentExecution", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, experimentInstanceId, centralRepository, workspacePath);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "initializeExperimentExecution", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			case FINALIZE_EXPERIMENT_EXECUTION:
				{
					//Reading parameters
					String experimentInstanceId = cmd.getOptionValue("eii");
					String centralRepository = cmd.getOptionValue("cR");
					String workspacePath = cmd.getOptionValue("wp");
					String endDate = cmd.getOptionValue("edDt");
					Date endDateTime = DateUtils.dateParse(endDate);
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspacePath, endDateTime);
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_STARTUP:
				{
					//Getting parameters
					String activityInstanceId = cmd.getOptionValue("aii");
					String[] context = cmd.getOptionValues("context");
					Date startDateTime = provExecStartTime;
					
					String workspacePath = cmd.getOptionValue("wp");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					String extendedContext = cmd.getOptionValue("sceContext");
					if (SciCumulusBusinessHelper.isSciCumulusExecution(extendedContext)){
						RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId, context, startDateTime, workspacePath, extendedContext, firstActivity);
					}else{
						//Invoking BusinessServices
						RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId, context, startDateTime, workspacePath);
					}
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyActivityExecutionStartup", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
					
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_ENDING:
				{
					//Getting parameters
					String activityInstanceId = cmd.getOptionValue("aii");
					String[] context = cmd.getOptionValues("context");
					Date startDateTime = null;
					
					Date endDateTime = provExecStartTime;
					
					String workspacePath = cmd.getOptionValue("wp");
					
					String extendedContext = cmd.getOptionValue("sceContext");
					if (SciCumulusBusinessHelper.isSciCumulusExecution(extendedContext)){
						RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId, context, startDateTime, endDateTime, workspacePath, extendedContext);
					}else{
						//Invoking BusinessServices
						RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId, context, startDateTime, endDateTime, workspacePath);
					}
				}
				break;
			case NOTIFY_DECISION_POINT_ENDING:
				{
					//Getting parameters
					String decisionPointId = cmd.getOptionValue("di");
					String optionValue = cmd.getOptionValue("ov");
					String[] context = cmd.getOptionValues("context");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyDecisionPointEnding", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyDecisionPointEnding(decisionPointId, optionValue, context);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyDecisionPointEnding", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			case NOTIFY_PROCESS_EXECUTION_STARTUP:
				{
					//Getting parameters
					String processInstanceId = cmd.getOptionValue("pii");
					String[] context = cmd.getOptionValues("context");
					String startDate = cmd.getOptionValue("stDt");
					Date starDateTime = DateUtils.dateParse(startDate);
					String workspacePath = cmd.getOptionValue("wp");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyProcessExecutionStartup", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyProcessExecutionStartup(processInstanceId, context, starDateTime, workspacePath);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyProcessExecutionStartup", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			case NOTIFY_PROCESS_EXECUTION_ENDING:
				{
					//Getting parameters
					String processInstanceId = cmd.getOptionValue("pii");
					String[] context = cmd.getOptionValues("context");
					String startDate = cmd.getOptionValue("stDt");
					Date starDateTime = DateUtils.dateParse(startDate);
					String endDate = cmd.getOptionValue("edDt");
					Date endDateTime = DateUtils.dateParse(endDate);
					String workspacePath = cmd.getOptionValue("wp");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyProcessExecutionEnding", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyProcessExecutionEnding(processInstanceId, context, starDateTime, endDateTime, workspacePath);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "notifyProcessExecutionEnding", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			case PUBLISH_ARTIFACT_VALUE_LOCATION:
				{
					//Getting parameters
					String artifactId = cmd.getOptionValue("ai");
					String[] context = cmd.getOptionValues("context");
					String hostURL = cmd.getOptionValue("hUrl");
					String hostLocalPath = cmd.getOptionValue("hPath");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "publishArtifactValueLocation", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.publishArtifactValueLocation(artifactId, context, hostURL, hostLocalPath);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "publishArtifactValueLocation", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			case SET_ARTIFACT_VALUE:
				{
					//Getting parameters
					String artifactId = cmd.getOptionValue("ai");
					String value = cmd.getOptionValue("av");
					String[] context = cmd.getOptionValues("context");
					
					Date methodInit = Calendar.getInstance().getTime();
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "setArtifactValue", LogMessages.START_METHOD_EXECUTION_TIME, new Object[]{sdf.format(methodInit)});
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.setArtifactValue(artifactId, context, value);
					
					Date methodEnd = Calendar.getInstance().getTime();
					Long methodDiffTime = ((methodEnd.getTime() - methodInit.getTime())/1000);
					ProvMonitorLogger.measure(RetrospectiveProvenanceBusinessServices.class.getName(), "setArtifactValue", LogMessages.END_METHOD_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(methodEnd), methodDiffTime});
				}
				break;
			}
		}catch (org.apache.commons.cli.ParseException e){
			System.out.print("Parameter parse error: ");
			System.out.println(e.getMessage());
			//e.printStackTrace();
			ProvMonitorLogger.fatal(ProvMonitor.class.getName(), "Main", "ParseException: " + e.getMessage());
		}catch (ValidateException e){
			System.out.println(e.getMessage());
			ProvMonitorLogger.fatal(ProvMonitor.class.getName(), "Main", "Validade parameters exception: " + e.getMessage());
			//e.printStackTrace();
		}catch (ProvMonitorException pe){
			System.out.println(pe.getMessage());
			ProvMonitorLogger.fatal(ProvMonitor.class.getName(), "Main", "ProvMonitor execution excetption: " + pe.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			ProvMonitorLogger.fatal(ProvMonitor.class.getName(), "Main", "Generic non treated exception: " + e.getMessage());
		}finally{
			Date provExecEndTime = Calendar.getInstance().getTime();
			Long diffTime = ((provExecEndTime.getTime() - provExecStartTime.getTime())/1000);
			ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(provExecEndTime), diffTime});
			try {
				ProvMonitorOutputManager.getInstance().flush();
			} catch (ProvMonitorException e) {
				ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "Main", LogMessages.FATAL_ERROR_OUTPUT_NOT_FLUSHED, new Object[]{e.getMessage()});
			}
		}
		
	}
	
	/**
	 * Puts ProvMonitor version into ProvMonitorOutputManager. <br />
	 * @see ProvMonitorOutputManager
	 * 
	 */
	private static void printProvMonitorVersion() {
		try {
			Manifest manifest = new Manifest(ClassLoader.getSystemResourceAsStream("MANIFEST.MF"));
			String versionValue = manifest.getMainAttributes().getValue("ProvMonior-Version");
			ProvMonitorOutputManager.appendMessageLine("ProvMonitor Version: " + versionValue);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
