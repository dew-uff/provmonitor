package br.uff.ic.provmonitor.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import br.uff.ic.provmonitor.connection.DatabaseType;

/**
 * Properties of ProvMonitor System
 * */
public class ProvMonitorProperties {
	
	private DatabaseType dataBaseType;
	private String dataBaseConnection;
	private String dataBaseUser;
	private String dataBaseUserPass;
	private String logMode;
	private String cvsType;
	private String outputFile;
	private Properties provMonitorProps;
	private static ProvMonitorProperties myInstance;
	
	/**
	 * Private constructor as a singleton.
	 * */
	private ProvMonitorProperties (){
		this.loadProperties();
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
	public String getLogMode() {
		return logMode;
	}

	public void setLogMode(String logMode) {
		this.logMode = logMode;
	}

	public String getCvsType() {
		return cvsType;
	}
	public void setCvsType(String cvsType) {
		this.cvsType = cvsType;
	}
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
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
			
			dataBaseType = DatabaseType.valueOf(provMonitorProps.getProperty("dataBaseType"));
			dataBaseConnection = provMonitorProps.getProperty("dataBaseConnection");
			dataBaseUser = provMonitorProps.getProperty("dataBaseUser");
			dataBaseUserPass = provMonitorProps.getProperty("dataBaseUserPass");
			logMode = provMonitorProps.getProperty("logMode");
			cvsType = provMonitorProps.getProperty("cvsType");
			outputFile = provMonitorProps.getProperty("outputFile");
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void savaDefaultProperties(){
		try {
			// create and load default properties
			Properties defaultProps = new Properties();
			FileInputStream in = new FileInputStream("provMonitor.properties");
			
			defaultProps.load(in);
			in.close();
	
			// create application properties with default
			provMonitorProps = new Properties(defaultProps);
			
			provMonitorProps.setProperty("dataBaseType", dataBaseType.getName());
			provMonitorProps.setProperty("dataBaseConnection", dataBaseConnection);
			provMonitorProps.setProperty("dataBaseUser", dataBaseUser);
			provMonitorProps.setProperty("dataBaseUserPass", dataBaseUserPass);
			provMonitorProps.setProperty("logMode", logMode);
			provMonitorProps.setProperty("cvsType", cvsType);
			provMonitorProps.setProperty("outputFile", outputFile);
			
			
			FileOutputStream out = new FileOutputStream("provMonitor2.properties");
			provMonitorProps.store(out, "---No Comment---");
			
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Save an example ProvMonitorProperties file with default values
	 * */
	public void generateDefaultPropertiesFile(){
		ProvMonitorProperties.getInstance();
		this.savaDefaultProperties();
	}
	
}
