package br.uff.ic.provmonitor;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.enums.MethodType;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ValidateException;
import br.uff.ic.provmonitor.utils.DateUtils;
import br.uff.ic.provmonitor.validator.OptionsValidator;

public class ProvMonitor {

	//private Options options = new Options();
	
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
		//options.addOption("",true,"");
		
		options.addOption("context",true,"context");
	}
	
	/**
	 * @param args
	 * @return 
	 */
	public static void main(String[] args) {
		try {
			//System.out.println("rodou!");
			//System.out.println("ProvMonitor: StartExecution....");
			
			
			//Getting ProvMonitor execution start time, to be used in cases that time was not informed by parameter.
			//Date provMonitorExecutionStartTime = Calendar.getInstance().getTime();
			
			// CLI construction
			// create Options object
			Options options = new Options();
			//Initializing the Options of the CLI
			optionsInitialize(options);
			
			//Reading parameters
			CommandLineParser parser = new PosixParser();
			CommandLine cmd = parser.parse(options, args);
			
			//Validate options in the CommandLine
			OptionsValidator.validate(cmd);
			
			//Verify the type of method to be invoked by the BusinessServices
			Integer methodId = Integer.parseInt(cmd.getOptionValue("m"));
			
			switch (MethodType.valueOf(methodId)){
			case INITIALIZE_EXPERIMENT_EXECUTION:
				{
					//System.out.println("Initializing experiment execution...");
					//Validate options
					//OptionsValidator.validate(cmd);
					//Getting parameters
					String experimentId = cmd.getOptionValue("ei");
					String workspacePath = cmd.getOptionValue("wp");
					String centralRepository = cmd.getOptionValue("cR");
					
					//System.out.println("Workspace: " + workspacePath);
					//System.out.println("Central Repository: " + centralRepository);
					
					//Invoking BusinessServices
					//OutputStream outputStream = Thread.currentThread().get
					
					//RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId);
					
					RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, centralRepository, workspacePath);
				}
				break;
			case FINALIZE_EXPERIMENT_EXECUTION:
				{
					//Reading parameters
					String experimentInstanceId = cmd.getOptionValue("eii");
					String centralRepository = cmd.getOptionValue("cR");
					String endDate = cmd.getOptionValue("edDt");
					Date endDateTime = DateUtils.dateParse(endDate);
					
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, endDateTime);
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_ENDING:
				{
					//Getting parameters
					String activityInstanceId = cmd.getOptionValue("aii");
					String[] context = cmd.getOptionValues("context");
					String startDate = cmd.getOptionValue("stDt");
					Date starDateTime = DateUtils.dateParse(startDate);
					String endDate = cmd.getOptionValue("edDt");
					Date endDateTime = DateUtils.dateParse(endDate);
					String workspacePath = cmd.getOptionValue("wp");
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId, context, starDateTime, endDateTime, workspacePath);
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_STARTUP:
				{
					//Getting parameters
					String activityInstanceId = cmd.getOptionValue("aii");
					String[] context = cmd.getOptionValues("context");
					String startDate = cmd.getOptionValue("stDt");
					Date starDateTime = DateUtils.dateParse(startDate);
					String workspacePath = cmd.getOptionValue("wp");
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId, context, starDateTime, workspacePath);
				}
				break;
			case NOTIFY_DECISION_POINT_ENDING:
				{
					//Getting parameters
					String decisionPointId = cmd.getOptionValue("di");
					String optionValue = cmd.getOptionValue("ov");
					String[] context = cmd.getOptionValues("context");
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyDecisionPointEnding(decisionPointId, optionValue, context);
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
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyProcessExecutionEnding(processInstanceId, context, starDateTime, endDateTime, workspacePath);
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
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.notifyProcessExecutionStartup(processInstanceId, context, starDateTime, workspacePath);
				}
				break;
			case PUBLISH_ARTIFACT_VALUE_LOCATION:
				{
					//Getting parameters
					String artifactId = cmd.getOptionValue("ai");
					String[] context = cmd.getOptionValues("context");
					String hostURL = cmd.getOptionValue("hUrl");
					String hostLocalPath = cmd.getOptionValue("hPath");
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.publishArtifactValueLocation(artifactId, context, hostURL, hostLocalPath);
				}
				break;
			case SET_ARTIFACT_VALUE:
				{
					//Getting parameters
					String artifactId = cmd.getOptionValue("ai");
					String value = cmd.getOptionValue("av");
					String[] context = cmd.getOptionValues("context");
					//Invoking BusinessServices
					RetrospectiveProvenanceBusinessServices.setArtifactValue(artifactId, context, value);
				}
				break;
			}
		}catch (org.apache.commons.cli.ParseException e){
			System.out.print("Parameter parse error: ");
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}catch (ValidateException e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}catch (ProvMonitorException pe){
			System.out.println(pe.getMessage());
		}catch (Exception e){
			e.printStackTrace();
		}//finally{
			//System.out.flush();
			//System.out.println("ProvMonitor: End Execution....");
		//}
		
	}

}
