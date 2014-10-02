package br.uff.ic.provmonitor.tests;

import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSWorkspaceCreationException;
import br.uff.ic.provmonitor.vcsmanager.CLIGitManager;

public class CLIGitManagerTests {
	public final static String VALID_WORKSPACE="C:/Testes/Central1";
	public final static String INVALID_WORKSPACE="C:/Testes";
	public final static String EXISTENT_WORKSPACE="C:/Testes/manualTests/newWorkspace1";
	public final static String NEW_WORKSPACE="C:/Testes/manualTests/newWorkspace3";
	
	public static void main(String[] args) {
		try {
			//Tests messages buffer
			StringBuilder reprovedTests = new StringBuilder();
			
			//Workspace identification test
			Boolean isWorkspaceCreatedOk = isWorkspaceCreatedTest();
			if (!isWorkspaceCreatedOk){
				reprovedTests.append("isWorkapceCreatedOk: REPROVED.")
						     .append(System.getProperty("line.separator"));
			}
			
			if (!isNewWorkspaceCreationTestOk()){
				reprovedTests.append("isNewWorkspaceCreationOk: REPROVED.")
							 .append(System.getProperty("line.separator"));
			}
			
			
			//Tests verification
			if (!(reprovedTests.length() > 0)){
				System.out.println("All tests ok!");
			}else{
				System.out.println("TESTS RESULTS:");
				System.out.println(reprovedTests.toString());
			}
			
		} catch (VCSException e) {
			e.printStackTrace();
		}finally{
			
		}
	}
	
	public static boolean isWorkspaceCreatedTest() throws VCSException{
		
		CLIGitManager vcs = new CLIGitManager();
		String validWorkspace = VALID_WORKSPACE;
		if (!vcs.isWorkspaceCreated(validWorkspace)){
			return false;
		}
		String invalidWorkspace = INVALID_WORKSPACE;
		if (vcs.isWorkspaceCreated(invalidWorkspace)){
			return false;
		}
		
		return true;
	}
	
	
	//TODO: Tratar quando workspace não existir
	public static boolean isNewWorkspaceCreationTestOk() throws VCSException{
		CLIGitManager vcs = new CLIGitManager();
		boolean creationOk = false;
		boolean creationErrorOk = true;
		try{
			vcs.createWorkspace(NEW_WORKSPACE);
			creationOk = true;
		}catch(VCSWorkspaceCreationException e){
			creationOk = false;
		}
		try{
			vcs.createWorkspace(VALID_WORKSPACE);
			creationErrorOk = false;
		}catch(VCSWorkspaceCreationException e){
			creationErrorOk = true;
		}
		
		return creationOk && creationErrorOk;
	}
}
