package br.uff.ic.provmonitor.utils;



/**
 * Util class responsible to  extract the activity instance id and the activity instance activation id from an extended context string.
 * 
 * */
public class ExtendedContextUtils {
	
	private String extendedContextString;
	private String activityInstanceId;
	private String activityInstanceActivationId;
	
	
	public ExtendedContextUtils (String extendedContextURL){
		
		this.extendedContextString = extendedContextURL;
		
		String [] parsedString = extendedContextURL.split("\\");
		Integer parsedLength = parsedString.length;
		if (parsedLength > 2){
			this.activityInstanceActivationId = parsedString[parsedLength - 1];
			this.activityInstanceActivationId = parsedString[parsedLength - 2];
		}
	}
	
	public String getExtendedContextString() {
		return extendedContextString;
	}
	
	public String getActivityInstanceId() {
		return activityInstanceId;
	}
	public String getActivityInstanceActivationId() {
		return activityInstanceActivationId;
	}
	
	public String[] appendContext(String[] context){
		Integer contextLen = context.length;
		String[] context2 = new String[contextLen + 1];
		
		if (contextLen > 0){
			Integer i = 0;
			for (String path: context){
				context2[i++] = path;
			}
		}
		
		context2[contextLen] = this.activityInstanceActivationId;
		return context2;
	}
	
	//	public void setExtendedContextString(String extendedContextString) {
	//		this.extendedContextString = extendedContextString;
	//	}
	//
	//	public void setActivityInstanceId(String activityInstanceId) {
	//		this.activityInstanceId = activityInstanceId;
	//	}
	//
	//	public void setActivityInstanceActivationId(String activityInstanceActivationId) {
	//		this.activityInstanceActivationId = activityInstanceActivationId;
	//	}
	
}
