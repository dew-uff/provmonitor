package br.uff.ic.provmonitor.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

/**
 * 
 * @author Vitor
 *
 */
public class ProvMonitorOutputManager {
	private static ProvMonitorOutputManager myInstance;
	private ProvMonitorOutputType instanceType;
	private String outputFilePath;
	private StringBuilder outputMessage;
	
	private ProvMonitorOutputManager(){
		this.outputMessage = new StringBuilder();
		ProvMonitorOutputType outType = ProvMonitorProperties.getInstance().getOutputType();
		if (outType != null){
			this.instanceType = outType; 
			
		String outPath = ProvMonitorProperties.getInstance().getOutputFile();
		if (outPath == null || !(outPath.length() > 0)){
			outPath = "provMonitor.output";
		}
		this.outputFilePath = outPath;
		}
	}
	
	public synchronized static ProvMonitorOutputManager getInstance(){
		if (myInstance == null){
			myInstance = new ProvMonitorOutputManager();
		}
		return myInstance;
	}
	
	/**
	 * Append a message line in the output.
	 * @param message String - Message line to be appended in output. 
	 */
	public void appendMessageLine(String message){
		getInstance().outputMessage.append("\n")
								   .append(message);
	}
	
	/**
	 * Append a message in the output.
	 * @param message String - Message to be appended in output.
	 */
	public void appendMenssage(String message){
		getInstance().outputMessage.append(message);
	}
	
	/**
	 * Flush the output.
	 */
	public void flush() throws ProvMonitorException{
		switch (getInstance().instanceType){
		case CONSOLE:
			System.out.println(getInstance().outputMessage.toString());
			break;
		case FILE:
			File outPutFile = new File(getInstance().outputFilePath);
			try {
				outPutFile.createNewFile();
				FileWriter out = new FileWriter(outPutFile);
				
				out.write(getInstance().outputMessage.toString());
				out.close();
				
			} catch (IOException e) {
				throw new ProvMonitorException(e.getMessage(), e.getCause());
			}
			break;
		}
	}
}
