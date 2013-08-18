package br.uff.ic.provmonitor.business.scicumulus;


/**
 * Business Helper for SciCumulus paramterers extensions.
 * 
 * */
public class SciCumulusBusinessHelper {
	
	/**
	 * Workspace update for the SciCumulus particularities
	 * */
	public static String workspaceUpdate(String workspacePath, String sciCumulusExtendedContext){
		StringBuilder sb = new StringBuilder();
		sb.append(workspacePath);
		String[] parsedScContext = sciCumulusExtendedContext.split("/");
		
		if (parsedScContext != null && parsedScContext.length > 0){
			sb.append("/");
			sb.append(parsedScContext[parsedScContext.length - 1]);
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Returns if it is a SciCumulus execution through sciCumulusExtendedContext.
	 * @param sciCumulusExtendedContext - String - SciCumulus extended context parameter used to deal with with the SciCumulus particularities.
	 * */
	public static Boolean isSciCumulusExecution (String sciCumulusExtendedContext){
		return (sciCumulusExtendedContext != null && sciCumulusExtendedContext.length() > 0);
	}
}
