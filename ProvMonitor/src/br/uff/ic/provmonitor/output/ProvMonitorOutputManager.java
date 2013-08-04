package br.uff.ic.provmonitor.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;

/**
 * Singleton output manager of ProvMonitor. 
 * <br />The purpose is to make possible to redirect output messages to a chosen output channel/stream.
 * <br /><br />
 * <b>To see current supported output options:</b> {@link ProvMonitorOutputType}
 * 
 * @see ProvMonitorOutputType
 * 
 * @author Vitor C. Neves - vcneves@ic.uff.br
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
	
	/**
	 * Get the ProvMonitorOutputManager instance to be used.
	 * @return ProvMonitorOutputManager instance.
	 */
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
		if (getInstance().outputMessage.length() > 0){
			getInstance().outputMessage.append("\n");
		}
		appendMenssage(message);
	}
	
	/**
	 * Append a message in the output.
	 * @param message String - Message to be appended in output.
	 */
	public void appendMenssage(String message){
		getInstance().outputMessage.append(message);
	}
	
	/**
	 * Flush the output to the selected type output.
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
