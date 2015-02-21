package br.uff.ic.provmonitor.tests;

import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSWorkspaceCreationException;
import br.uff.ic.provmonitor.vcsmanager.CLIGitManager;

public class CLIGitManagerTests {
	public final static String VALID_WORKSPACE="C:/Testes/Repos/Central1";
	//public final static String VALID_WORKSPACE="C:/Testes/Workspaces/newWorkspace2";
	public final static String INVALID_WORKSPACE="C:/Testes";
	public final static String EXISTENT_WORKSPACE="C:/Testes/Workspaces/newWorkspace1";
	public final static String NEW_WORKSPACE="C:/Testes/Workspaces/newWorkspace22";
	
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
			System.out.println("isWorkspaceCreatedTest: Error with VALID_WORKSPACE");
			return false;
		}
		String invalidWorkspace = INVALID_WORKSPACE;
		if (vcs.isWorkspaceCreated(invalidWorkspace)){
			System.out.println("isWorkspaceCreatedTest: Error with INVALID_WORKSPACE");
			return false;
		}
		
		return true;
	}
	
	
	//TODO: Deal with non existent workspace.
	//TODO: Workaround when the workspace path does not exists.
	public static boolean isNewWorkspaceCreationTestOk() throws VCSException{
		CLIGitManager vcs = new CLIGitManager();
		boolean creationOk = false;
		boolean creationErrorOk = true;
		try{
			vcs.createWorkspace(NEW_WORKSPACE);
			creationOk = true;
		}catch(VCSWorkspaceCreationException e){
//			System.out.println("isNewWorkspaceCreationTestOk: Error with NEW_WORKSPACE. ");
//			e.printStackTrace();
			creationOk = false;
		}
		try{
			vcs.createWorkspace(VALID_WORKSPACE);
			creationErrorOk = false;
			System.out.println("isNewWorkspaceCreationTestOk: Error with VALID_WORKSPACE. ");
		}catch(VCSWorkspaceCreationException e){
//			System.out.println("isNewWorkspaceCreationTestOk StackTrace: ");
//			e.printStackTrace();
			creationErrorOk = true;
		}
		
		return creationOk && creationErrorOk;
	}
}
