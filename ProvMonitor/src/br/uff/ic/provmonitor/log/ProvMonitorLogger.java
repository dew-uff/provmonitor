package br.uff.ic.provmonitor.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Responsible for register ProvMonitor execution Log. Static usage.
 * 
 * <br /><br />
 * <b>Objective: </b> Turn ProvMonitor code more agnostic of Logger implementation (Log4J or Util.Logger), i.e., easy refactory/adaptation/evoltuion.
 * 
 * <br /><br />
 * <b>DOCUMENTATION:</b><br />
 * Precedence of Levels is based on Java.util.Log and Log4J.
 * <br /><br />
 * <b>ProvMonitorLog:</b> DEBUG < MEASURING < INFO < WARNING < FATAL
 * 
 * <br />
 * <b>Java.Util.Log:</b> FINEST < FINER < FINE < CONFIG < INFO < WARNING < SEVERE < OFF 
 * 
 * <br />
 * <b>Log4J: </b> DEBUG < INFO < WARN < ERROR < FATAL
 * 
 * <br /><br />
 * <b>ProvMonitorLog Level Explanation:</b> <br />
 * <ul>
 * <li>DEBUG: FINER - Debug information.</li>
 * <li>MEASURING: CONFIG - for metrics.</li>
 * <li>INFO: Milestones.</li>
 * <li>WARNING: Possible problems or recoverable errors.</li>
 * <li>FATAL: fatal errors.</li>
 * </ul>
 * 
 * */
public class ProvMonitorLogger {
	private static Logger logger = Logger.getLogger(ProvMonitorLogger.class.getName());
	
	/**
	 * Private constructor. Guarantee static usage.
	 * */
	private ProvMonitorLogger(){
		super();
	}
	
	/**
	 * Setting config information
	 * 
	 * @param Level - level of the logger
	 */
	public static void config(ProvMonitorLevel level){
		logger.setLevel(level);

		//Preparing Log File
		try {
			
			//Default file handler
			FileHandler fileHandler = new FileHandler("provmonitor.log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
			
			//Measure file handler
			if (level.intValue() <= ProvMonitorLevel.MEASURE.intValue()){
				FileHandler measureFileHandler = new FileHandler("provmonitorMeasure.log", true);
				measureFileHandler.setFormatter(new SimpleFormatter());
				measureFileHandler.setLevel(ProvMonitorLevel.MEASURE);
				
				logger.addHandler(measureFileHandler);
			}
			
			//Debug file handler
			if (level.intValue() <= ProvMonitorLevel.DEBUG.intValue()){
				FileHandler debugFileHandler = new FileHandler("provmonitorDebug.log", true);
				debugFileHandler.setFormatter(new SimpleFormatter());
				debugFileHandler.setLevel(ProvMonitorLevel.DEBUG);
				logger.addHandler(debugFileHandler);
			}
			
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Register log info.
	 * 
	 * @param level <code>Level</code> - level of the log message
	 * @param msg <code>String</code> - log message
	 */
	public static void log (Level level, String msg){
		logger.log(level, msg);
	}
	
	/**
	 * Register log info.
	 * 
	 * @param level - level of the log message.
	 * @param msg - log message.
	 * @param params - parameters of the log message.
	 */
	public static void log(Level level, String msg, Object[] params){
		logger.log(level, msg, params);
	}
	
	/**
	 * Register log info.
	 * 
	 * @param level - level of the log message.
	 * @param sourceClass - Source Class that generated the log message.
	 * @param sourceMethod - Source Method that generated the log message.
	 * @param msg - log message.
	 */
	public static void log(Level level, String sourceClass, String sourceMethod, String msg){
		logger.logp(level, sourceClass, sourceMethod, msg);
	}
	
	/**
	 * Register log info.
	 * 
	 * @param level - level of the log message.
	 * @param sourceClass - Source Class that generated the log message.
	 * @param sourceMethod - Source Method that generated the log message.
	 * @param msg - log message.
	 * @param params - parameters of the log message.
	 */
	public static void log(Level level, String sourceClass, String sourceMethod, String msg, Object[] params){
		logger.logp(level, sourceClass, sourceMethod, msg, params);
	}
	
	/**
	 * Register Fatal error message logs.
	 * @param msg - log message.
	 * 
	 */
	public static void fatal (String msg){
		logger.log(ProvMonitorLevel.FATAL, msg);
	}
	
	/**
	 * Register fatal errors message logs.
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void fatal (String sourceClass, String sourceMethod, String msg){
		logger.logp(ProvMonitorLevel.FATAL,sourceClass, sourceMethod, msg);
	}
	
	/**
	 * Register warning message logs.
	 * @param msg
	 */
	public static void warning (String msg){
		//logger.warning(msg);
		logger.log(ProvMonitorLevel.WARNING, msg);
	}
	
	/**
	 * Register warning message logs.
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void warning(String sourceClass, String sourceMethod, String msg){
		//logger.logp(Level.WARNING,sourceClass, sourceMethod, msg);
		logger.logp(ProvMonitorLevel.WARNING,sourceClass, sourceMethod, msg);
	}
	
	/**
	 * Register info message logs. Register execution milestones.
	 * @param msg
	 */
	public static void info(String msg){
		//logger.log(Level.INFO, msg);
		logger.log(ProvMonitorLevel.INFO, msg);
	}
	
	/**
	 * Register info message logs. Register execution milestones.
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void info(String sourceClass, String sourceMethod, String msg){
		//logger.logp(Level.INFO,sourceClass, sourceMethod, msg);
		logger.logp(ProvMonitorLevel.INFO,sourceClass, sourceMethod, msg);
	}
	
	/**
	 * Register measurements message logs for metrics informations.
	 * @param msg
	 */
	public static void measure(String msg){
		//logger.log(Level.CONFIG, msg);
		logger.log(ProvMonitorLevel.MEASURE, msg);
	}
	
	/**
	 * Register measurements message logs for metrics informations.
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void measure(String sourceClass, String sourceMethod, String msg){
		//logger.logp(Level.CONFIG,sourceClass, sourceMethod, msg);
		logger.logp(ProvMonitorLevel.MEASURE,sourceClass, sourceMethod, msg);
	}
	
	public static void measure(String sourceClass, String sourceMethod, String msg, Object[] params){
		//logger.logp(Level.CONFIG,sourceClass, sourceMethod, msg);
		logger.logp(ProvMonitorLevel.MEASURE,sourceClass, sourceMethod, msg, params);
	}
	
	/**
	 * Register debug message logs.
	 * @param msg
	 */
	public static void debug(String msg){
		logger.log(ProvMonitorLevel.DEBUG, msg);
	}
	
	/**
	 * Register debug message logs.
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void debug (String sourceClass, String sourceMethod, String msg){
		//logger.logp(Level.FINER,sourceClass, sourceMethod, msg);
		logger.logp(ProvMonitorLevel.DEBUG,sourceClass, sourceMethod, msg);
	}
}
