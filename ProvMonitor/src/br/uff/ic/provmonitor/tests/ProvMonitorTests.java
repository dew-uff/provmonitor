package br.uff.ic.provmonitor.tests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.log.LogMessages;
import br.uff.ic.provmonitor.log.ProvMonitorLevel;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;

public class ProvMonitorTests {
	public static void main(String[] args) {
		//Execution start time...
		Date provExecStartTime = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
		try{
			
			String centralRepository = "";
			String workspacePath = "";
			Date startDateTime = Calendar.getInstance().getTime();
			
			//Starting Log
			ProvMonitorLogger.config(ProvMonitorLevel.DEBUG);
			ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.START_EXECUTION_TIME, new Object[]{sdf.format(provExecStartTime)});
			
			ProvMonitorLogger.info(ProvMonitorTests.class.getName(), "Main", "Starting ProvMonitor tests...");
			
			ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Calling initializeExperimentTest.");
			//InitializeExperimentTest
			String experimentInstanceId = initializeExperimentTest("ExperimentTeste1", centralRepository, workspacePath, startDateTime);
			ProvMonitorLogger.debug(ProvMonitorTests.class.getName(), "Main", "Return of initializeExperimentTest without erros. ExperimentInstanceId: " + experimentInstanceId + ". ");	
			
			
		}catch(ProvMonitorException e){
			ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "main", "Error: " + e.getMessage());
		}finally{
			Date provExecEndTime = Calendar.getInstance().getTime();
			//Double diffTime = ((Long)((provExecEndTime.getTime() - provExecStartTime.getTime())/1000)).doubleValue();
			Long diffTime = ((provExecEndTime.getTime() - provExecStartTime.getTime())/1000);
			ProvMonitorLogger.measure(ProvMonitorTests.class.getName(), "Main", LogMessages.END_EXECUTION_TIME_WITH_DIFF, new Object[]{sdf.format(provExecEndTime), diffTime});
		}
		
	}
	
	
	private static String initializeExperimentTest(String experimentId, String centralRepository, String workspacePath, Date startDateTime) throws ProvMonitorException{
		String experimentInstanceId = "";
		
		try {
			experimentInstanceId = RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, centralRepository, workspacePath);
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "initializeExperimentTest", "Exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return experimentInstanceId;
	}
}
