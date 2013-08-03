package br.uff.ic.provmonitor.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jgit.util.StringUtils;

import br.uff.ic.provmonitor.connection.DatabaseType;
import br.uff.ic.provmonitor.log.LogMessages;
import br.uff.ic.provmonitor.log.ProvMonitorLevel;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.output.ProvMonitorOutputType;
import br.uff.ic.provmonitor.vcsmanager.VCSType;

/**
 * Properties of ProvMonitor System
 * */
public class ProvMonitorProperties {
	
	private DatabaseType dataBaseType;
	private String dataBaseConnection;
	private String dataBaseUser;
	private String dataBaseUserPass;
	private ProvMonitorLevel logMode;
	private VCSType vcsType;
	private String outputFile;
	private ProvMonitorOutputType outputType;
	private Properties provMonitorProps;
	private static ProvMonitorProperties myInstance;
	
	/**
	 * Private constructor as a singleton.
	 * */
	private ProvMonitorProperties (){
		this.loadProperties();
		this.loadPropertiesDefaultValues();
	}
	
	/**
	 * Public instance
	 * */
	public synchronized static ProvMonitorProperties getInstance(){
		if (myInstance == null){
			myInstance = new ProvMonitorProperties();
		}
		return myInstance;
	}
	
	public DatabaseType getDataBaseType() {
		return dataBaseType;
	}
	public void setDataBaseType(DatabaseType dataBaseType) {
		this.dataBaseType = dataBaseType;
	}

	public String getDataBaseConnection() {
		return dataBaseConnection;
	}
	public void setDataBaseConnection(String dataBaseConnection) {
		this.dataBaseConnection = dataBaseConnection;
	}
	public String getDataBaseUser() {
		return dataBaseUser;
	}
	public void setDataBaseUser(String dataBaseUser) {
		this.dataBaseUser = dataBaseUser;
	}
	public String getDataBaseUserPass() {
		return dataBaseUserPass;
	}
	public void setDataBaseUserPass(String dataBaseUserPass) {
		this.dataBaseUserPass = dataBaseUserPass;
	}
	public ProvMonitorLevel getLogMode() {
		return logMode;
	}
	public void setLogMode(ProvMonitorLevel logMode) {
		this.logMode = logMode;
	}
	public VCSType getVcsType() {
		return vcsType;
	}

	public void setVcsType(VCSType vcsType) {
		this.vcsType = vcsType;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	public ProvMonitorOutputType getOutputType() {
		return outputType;
	}

	public void setOutputType(ProvMonitorOutputType outputType) {
		this.outputType = outputType;
	}

	/**
	 * Save an example ProvMonitorProperties file with default values
	 * */
	public void generateDefaultPropertiesFile(){
		ProvMonitorProperties.getInstance();
		ProvMonitorProperties.getInstance().clearProperties();
		ProvMonitorProperties.getInstance().loadPropertiesDefaultValues();
		this.savaDefaultProperties();
	}
	
	private void loadProperties(){
		try {
			// create and load default properties
			Properties defaultProps = new Properties();
			FileInputStream in = new FileInputStream("provMonitor.properties");
			
			defaultProps.load(in);
			in.close();
	
			// create application properties with default
			provMonitorProps = new Properties(defaultProps);
			
			dataBaseType = DatabaseType.valueOfName(provMonitorProps.getProperty("dataBaseType"));
			dataBaseConnection = provMonitorProps.getProperty("dataBaseConnection");
			dataBaseUser = provMonitorProps.getProperty("dataBaseUser");
			dataBaseUserPass = provMonitorProps.getProperty("dataBaseUserPass");
			logMode = ProvMonitorLevel.valueOf(provMonitorProps.getProperty("logMode"));
			vcsType = VCSType.valueOfName(provMonitorProps.getProperty("cvsType"));
			outputFile = provMonitorProps.getProperty("outputFile");
			outputType = ProvMonitorOutputType.valueOfName(provMonitorProps.getProperty("outputType"));
			
			
			
		}catch(FileNotFoundException e){
			//Properties File not found. Starting with default options
			provMonitorProps = new Properties();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			ProvMonitorLogger.debug(ProvMonitorProperties.class.getName(), "loadProperties", e.getMessage());
			ProvMonitorLogger.warning(ProvMonitorProperties.class.getName(), "loadProperties", LogMessages.WARNING_PROPERTIES_FILE_NOT_FOUND_LOADING_DEFAULT_OPTIONS);
			
			Properties defaultProps = new Properties();
			provMonitorProps = new Properties(defaultProps);
		}
	}
	
	
	/**
	 * Clear all the properties values
	 * */
	private void clearProperties(){
		dataBaseType = null;
		dataBaseConnection = null;
		dataBaseUser = null;
		dataBaseUserPass = null;
		logMode = null;
		vcsType = null;
		outputFile = null;
		outputType = null;
	}
	
	/**
	 * Load the default values for the properties not setted on the properties files.
	 */
	private void loadPropertiesDefaultValues(){
		if (dataBaseType == null || StringUtils.isEmptyOrNull(dataBaseConnection) || StringUtils.isEmptyOrNull(dataBaseUser) || StringUtils.isEmptyOrNull(dataBaseUserPass)){
			dataBaseType = DatabaseType.JAVADB;
			dataBaseConnection = "";
			dataBaseUser = "";
			dataBaseUserPass = "";
			
		}
		if (logMode == null){
			logMode = ProvMonitorLevel.WARNING;
		}
		if (vcsType == null){
			vcsType = VCSType.GIT;
		}
		if (outputFile == null || StringUtils.isEmptyOrNull(outputFile)){
			outputFile = "";
			outputType = ProvMonitorOutputType.CONSOLE;
		}
		
	}
	
	private void savaDefaultProperties(){
		try {
			// create and load default properties
			Properties defaultProps = new Properties();
			
			// create application properties with default
			provMonitorProps = new Properties(defaultProps);
			
			provMonitorProps.setProperty("dataBaseType", dataBaseType.getName());
			provMonitorProps.setProperty("dataBaseConnection", dataBaseConnection);
			provMonitorProps.setProperty("dataBaseUser", dataBaseUser);
			provMonitorProps.setProperty("dataBaseUserPass", dataBaseUserPass);
			provMonitorProps.setProperty("logMode", logMode.getName());
			provMonitorProps.setProperty("cvsType", vcsType.getName());
			provMonitorProps.setProperty("outputFile", outputFile);
			provMonitorProps.setProperty("outputType", outputType.getCode());
			
			
			FileOutputStream out = new FileOutputStream("provMonitor.properties");
			provMonitorProps.store(out, "---Auto Generated Properties---");
			
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}