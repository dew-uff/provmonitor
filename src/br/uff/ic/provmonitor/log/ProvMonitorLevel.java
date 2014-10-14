package br.uff.ic.provmonitor.log;

import java.util.logging.Level;

/**
 * 
 * @author Vitor
 * //DEBUG < MEASURING < INFO < WARNING < FATAL
 *
 */
public class ProvMonitorLevel extends Level{

	private static final long serialVersionUID = 3153639910914486338L;
	
	
	/**
	 * DEBUG level is a level for gathering execution metrics. This level is initialized to 400.
	 */
	public static final ProvMonitorLevel DEBUG = new ProvMonitorLevel("DEBUG", 400);
	/**
	 * MEASURE level is a level for gathering execution metrics. This level is initialized to 700.
	 */
	public static final ProvMonitorLevel MEASURE = new ProvMonitorLevel("MEASURE", 700);
	/**
	 * INFO level is a level for gathering execution metrics. This level is initialized to 800.
	 */
	public static final ProvMonitorLevel INFO = new ProvMonitorLevel("INFO", 800);
	/**
	 * WARNING level is a level for gathering execution metrics. This level is initialized to 900.
	 */
	public static final ProvMonitorLevel WARNING = new ProvMonitorLevel("WARNING", 900);
	/**
	 * FATAL level is a level for gathering execution metrics. This level is initialized to 1000.
	 */
	public static final ProvMonitorLevel FATAL = new ProvMonitorLevel("FATAL", 1000);

	public static ProvMonitorLevel valueOf(String name){
		switch (name){
		case "DEBUG":
			return ProvMonitorLevel.DEBUG;
		case "MEASURE":
			return ProvMonitorLevel.MEASURE;
		case "INFO":
			return ProvMonitorLevel.INFO;
		case "WARNING":
			return ProvMonitorLevel.WARNING;
		case "FATAL":
			return ProvMonitorLevel.FATAL;
		}
		return null;
	}
	
	protected ProvMonitorLevel(String name, int value) {
		super(name, value);
	}

}
