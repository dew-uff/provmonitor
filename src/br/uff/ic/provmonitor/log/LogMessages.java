package br.uff.ic.provmonitor.log;

public class LogMessages {
	/**
	 * PARAM: Start dateTime.
	 */
	public static final String START_EXECUTION_TIME = "STARTING PROVMONITOR: {0}.";
	/**
	 * PARAMS: End dateTime. ExecutuionTime in seconds.
	 */
	public static final String END_EXECUTION_TIME_WITH_DIFF = "ENDING PROVMONITOR: {0}. EXEC_TIME(s): {1}.";
	
	/**
	 * PARAM: Start dateTime.
	 */
	public static final String START_METHOD_EXECUTION_TIME = "STARTING METHOD: {0}.";
	/**
	 * PARAMS: End dateTime. ExecutuionTime in seconds.
	 */
	public static final String END_METHOD_EXECUTION_TIME_WITH_DIFF = "ENDING METHOD: {0}. EXEC_TIME(s): {1}.";
	
	
	//FATAL ERROR Messages
	public static final String FATAL_ERROR_OUTPUT_NOT_FLUSHED = "The output could not be flushed. Error trace: {0}";
	
	
	//Warning Messages
	public static final String WARNING_PROPERTIES_FILE_NOT_FOUND_LOADING_DEFAULT_OPTIONS = "The properties file was not found. Loading default values.";
}
