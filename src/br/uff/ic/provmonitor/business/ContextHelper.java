package br.uff.ic.provmonitor.business;

public class ContextHelper {
	/**
	 * Generate a branch name based upon the context
	 * @param context
	 * @return new Branch's name
	 */
	public static String getBranchNameFromContext(String[] context){
		StringBuilder branchName = new StringBuilder();
		for (String path: context){
			if (branchName.length()>0){
				branchName.append("_");
			}
			branchName.append(path);
		}
		return branchName.toString();
	}
	
	/**
	 * Generate the Element path based upon the context
	 * @param context
	 * @return new Element's path.
	 */
	public static String getElementPathFromContext(String[] context){
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("/");
			}
			elementPath.append(path);
		}
		return elementPath.toString();
	}
}
