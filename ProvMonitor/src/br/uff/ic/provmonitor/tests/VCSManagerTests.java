package br.uff.ic.provmonitor.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import br.uff.ic.provmonitor.exceptions.VCSException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.vcsmanager.VCSManager;
import br.uff.ic.provmonitor.vcsmanager.VCSManagerFactory;

public class VCSManagerTests {
	
	public static void main(String[] args) {
		
		try {
			//cloneParcialAndFullTest();
			
			//loneToExistingDirecgtoryTest();
			
			//Clone a branch using an intermediate workspace Test
			//cloneFromBranch();
			
			multipleBranchesClones();
			
		} finally {
			
		}
	}
	
	@SuppressWarnings("unused")
	private static void basicCVSFunctionalitiesTests(){
		//mainTestCode();
		//initializeTest("experimentId", "c:\\TesteProvMonitor\\CloneCheckOut\\files", "c:\\bda\\RepositorioCentral\\files");
		//String workspace = "C:\\Testes\\workspace\\Teste7\\";
		String workspace = "C:\\GitWorkspaceTest\\Teste2";
		//String centralRepository = "C:\\Testes\\CentralRepo\\Repo4";
		String centralRepository = "C:\\GitTeste\\Repo1";
		
		File workPath = new File (workspace);
		workPath.delete();
		
		File centralRepo = new File(centralRepository);
		if (!centralRepo.exists()){
			createCentralRepository(centralRepository);
		}
		
		initializeTest("experimentId", centralRepository, workspace);
		
		
		
		createFileContent(new String(workspace + "\\File1.html"));
		createFileContent(new String(workspace + "\\File2.html"));
		
		String message = "Teste de Commit intermediário";
		intermediateCommit(workspace, message);
	}
	
	private static void initializeTest(String experimentId, String sourceRepository, String workspacePath){
		try{
			//Record Timestamp
			Date timeStampInitExecute = Calendar.getInstance().getTime();
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(timeStampInitExecute);
			String experimentInstanceId = experimentId + nonce;
					
			VCSManager cvsManager = VCSManagerFactory.getInstance();
			cvsManager.cloneRepository(sourceRepository, workspacePath);
			
			//Repository branch
			cvsManager.createBranch(workspacePath, experimentInstanceId);
			
			//Repository checkOut
			//cvsManager.checkout(workspacePath, "trunk");
			
		}catch(VCSException e){
			e.printStackTrace();
		}
	}
	
	private static void createCentralRepository(String centralRepository){
		try{
			VCSManager cvsManager = VCSManagerFactory.getInstance();
			cvsManager.createWorkspace(centralRepository);
			
			createFileContent(new String(centralRepository + "\\File1.html"));
			createFileContent(new String(centralRepository + "\\File2.html"));
			
			String message = "Commit Inicial";
			intermediateCommit(centralRepository, message);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void intermediateCommit(String workspacePath, String message){
		try{
			
			VCSManager cvsManager = VCSManagerFactory.getInstance();
			cvsManager.addAllFromPath(workspacePath);
			cvsManager.commit(workspacePath, message);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	private static void fileChangesGenerator(String fileName){
//		
//	}
	
	@SuppressWarnings("unused")
	private static void mainTestCode(){
		//String centralRepository = "C:\\Testes\\RepositorioCentral\\";
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(Calendar.getInstance().getTime());
		String workspacePath = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce + "\\";
		String workspaceGitPath = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce + "\\.git\\";
		String workspacePathClone = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce + "_clone\\";
		
		String file1Path = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce + "\\file1.txt";
		String file2Path = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce + "\\file2.txt";
		
		//String branchName = "Branch_" + nonce;
		
		//cloneTest(centralRepository, workspacePath);
		//mostBasicJGitTest(workspacePath);
		
		createWorkspaceTest(workspaceGitPath);
		checkOutTest(workspaceGitPath);
		
		createFileContent(file1Path);
		createFileContent(file2Path);
		
		//ArrayList<String> fileList = new ArrayList<String>();
		//fileList.add(file1Path);
		//fileList.add(file2Path);
		//String workspacePathPattern = "C:\\Testes\\TesteCVSManager\\Teste_" + nonce;
		//fileList.add(workspacePathPattern);
		//addByPatternsTest(workspaceGitPath,  fileList);
		addAllTest(workspaceGitPath);
		
		commitTest(workspaceGitPath, "commit inicial de teste");
		
		cloneTest(workspacePath, workspacePathClone);
	}
	
	private static void createWorkspaceTest(String workspacePath){
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		try{
			cvsManager.createWorkspace(workspacePath);
		}catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkOutTest(String workspacePath){
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		try{
			cvsManager.checkout(workspacePath, "head");
		}catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void addByPatternsTest(String workspacePath, Collection<String> filePathList){
		VCSManager cvsMan = VCSManagerFactory.getInstance();
		try {
			cvsMan.addPathOrFile(workspacePath, filePathList);
		} catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	private static void addAllTest(String workspacePath){
		VCSManager cvsMan = VCSManagerFactory.getInstance();
		try {
			cvsMan.addAllFromPath(workspacePath);
		} catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	private static void commitTest(String workspacePath, String message){
		VCSManager cvsMan = VCSManagerFactory.getInstance();
		try {
			cvsMan.commit(workspacePath, message);
		} catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	private static void cloneTest(String sourceUri, String destinationFolder){
		VCSManager cvsMan = VCSManagerFactory.getInstance();
		try {
			cvsMan.cloneRepository(sourceUri, destinationFolder);
		} catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests all process of cloning, branching and committing into the repository
	 * */
	@SuppressWarnings("unused")
	private static void FullTest1(){
		try {
			
			//String workspace = "C:\\Testes\\TesteInstrumentadoProvMonitor\\";
			String centralRepository = "C:\\Testes\\RepositorioCentral";
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			String branchName = "Branch_" + nonce;
			
			String workspacePath = "C:\\Testes\\Teste_" + nonce;
			new File(workspacePath).mkdirs();
			
			VCSManager cvs = VCSManagerFactory.getInstance();
			
			cvs.cloneRepository(centralRepository, workspacePath);
			cvs.createBranch(workspacePath, branchName);
			//cvs.checkout(branchName);
			
			String newFile = workspacePath + "\\Teste.txt";
			File file = new File(newFile);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
	        out.write("linha de teste");
	        out.close();
	        
	        cvs.commit(workspacePath + "\\","Commit de Teste: " + nonce);
			
			
		}catch(ProvMonitorException p){
			p.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void mostBasicJGitTest(String repositoryFolder){
		FileRepository repository;
		try {
			//creating repository
			repository = new FileRepositoryBuilder().setGitDir(new File(repositoryFolder)).build();
			repository.create();
			Git git = new Git(repository);
			Git.init().call();
			
			//adding files
			//RevWalk walk = new RevWalk(repository);
			//RevCommit commit = null;
			
			File exampleHtml = new File(repositoryFolder + "\\examplePage.html");
			exampleHtml.createNewFile();
			FileWriter out = new FileWriter(exampleHtml);
			out.write("<html>");
			out.write("<table>");
			out.write("</table>");
			out.write("</html>");
			out.close();
			git.add().addFilepattern(".").call();
			git.commit().setMessage("Simple html file.").call();
			
			
			//looking at the execution log
			Iterable<RevCommit> logs = git.log().call();
			Iterator<RevCommit> i = logs.iterator();
			 
//			while (i.hasNext()) {
//				commit = walk.parseCommit(i.next());
//				log.info(commit.getFullMessage());
//			}
//			 
//			assertThat(repository, is(notNullValue()));
			repository.close();
			
		} catch (IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void createFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			exampleHtml.createNewFile();
			FileWriter out = new FileWriter(exampleHtml);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.write("<html>");
			out.write("<table>");
			out.write("<tr><td>line1</td><td>");
			out.write(nonce);
			out.write("</td><tr>");
			out.write("</table>");
			out.write("</html>");
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unused")
	private static void cloneParcialAndFullTest() throws ProvMonitorException{
		
		String centralRepository = "C:\\Testes\\CentralRepo\\Repo1";
		String workspacePathFull = "C:\\Testes\\workspaces\\WorkspaceFullClone4";
		String workspacePath = "C:\\Testes\\workspaces\\WorkspaceParcialClone4";
		
		Collection<String> branchesToBeCloned = new ArrayList<String>();
		branchesToBeCloned.add("master");
		
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		//System.out.println("Cloning to: " + workspacePath);
		cvsManager.cloneRepository(centralRepository, workspacePathFull);
		cvsManager.cloneRepository(centralRepository, workspacePath, branchesToBeCloned);
	}
	
	@SuppressWarnings("unused")
	private static void cloneToExistingDirecgtoryTest() throws ProvMonitorException{
		//String centralRepository = "C:/Testes/CentralRepo/Repo1";
		String centralRepository = "C:/experimentos/exp_SciPhy"; 
		String workspacePath = "C:/Testes/workspaces/WorkspaceSciPhy1";
		VCSManager cvsManager = VCSManagerFactory.getInstance();
		//System.out.println("Cloning to: " + workspacePath);
		cvsManager.cloneRepository(centralRepository, workspacePath);
		cvsManager.addAllFromPath(workspacePath);
		String commiId = cvsManager.commit(workspacePath, "Teste de Commit em Workspace Pre-preenchido.");
		System.out.println("Commit ID: " + commiId);
		
	}
	
	@SuppressWarnings("unused")
	private static void cloneFromBranch() throws ProvMonitorException{
		
		String centralRepository = "C:/Testes/CentralRepo/Repo1"; 
		String workspaceBase = "C:/Testes/workspaces/WorkspaceMapReduce20";
		String workspacePath = workspaceBase + "/input";
		//String branchName = "Branch" + (new SimpleDateFormat("YYYYMMddHHmmssS")).format(Calendar.getInstance().getTime());
		String branchName = "Branch1";
		String experimentInstanceId = branchName;
		Collection<String> branchNames = new ArrayList<String>();
		branchNames.add(branchName);
		String fileName = "file.txt";
		String fileName2 = "file2.txt";
		
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		
		//Init
		//Repository branch
		vcsManager.createBranch(centralRepository, experimentInstanceId);
		//Repository checkOut
		vcsManager.checkout(centralRepository, experimentInstanceId);
		//Repository clone
		//vcsManager.cloneRepository(centralRepository, workspacePath, branchNames);
		vcsManager.cloneRepository(centralRepository, workspacePath);
		
		String workspaceInput;
		String activationWorkspace;
		String commitId;
		
		//ActivityStart
		workspaceInput = workspacePath;
		activationWorkspace = workspaceBase + "/Activity1/1/input";
		//vcsManager.cloneRepository(workspaceInput, activationWorkspace, branchNames);
		vcsManager.cloneRepository(workspaceInput, activationWorkspace);
		//vcsManager.checkout(activationWorkspace, experimentInstanceId);
		
		//Content change
		createFileContent(activationWorkspace + "/" + fileName);
		
		//ActivityEnd
		//Pushing back to the experiment root workspace
		vcsManager.addAllFromPath(activationWorkspace);
		commitId = vcsManager.commit(activationWorkspace, "TestCommitActivity1");
		System.out.println("Commit ID: " + commitId);
		vcsManager.pushBack(activationWorkspace, workspacePath);
		
		//ActivityStart
		workspaceInput = activationWorkspace;
		activationWorkspace = workspaceBase + "/Activity2/1/input";
		//vcsManager.cloneRepository(workspaceInput, activationWorkspace, branchNames);
		vcsManager.cloneRepository(workspaceInput, activationWorkspace);
		//vcsManager.checkout(activationWorkspace, experimentInstanceId);
		
		//Content change
		changeFileContent(activationWorkspace + "/" + fileName2);
		
		//ActivityEnd
		//Pushing back to the experiment root workspace
		vcsManager.addAllFromPath(activationWorkspace);
		commitId = vcsManager.commit(activationWorkspace, "TestCommitActivity2");
		System.out.println("Commit ID: " + commitId);
		vcsManager.pushBack(activationWorkspace, workspacePath);
		
		//End of Execution
		vcsManager.pushBack(workspacePath, centralRepository);
	}
	
	private static void changeFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			FileWriter out = new FileWriter(exampleHtml, true);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			//out.append("/n");
			out.append("<html>");
			out.append("<table>");
			out.append("<tr><td>line1</td><td>");
			out.append(nonce);
			out.append("</td><tr>");
			out.append("</table>");
			out.append("</html>");
			out.append("\n");
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void multipleBranchesClones(){
		try {
			String centralRepository = "C:/Testes/Repositorios/Repo01"; 
			
			String workspaceBase = "C:/Testes/workspaces/BranchWorkspaces/Workspace01/Branch01";
			String workspaceIntermediate = workspaceBase + "/input";
			String workspaceActivation1 = workspaceBase + "/activation1/input";
			String workspaceActivation2 = workspaceBase + "/activation2/input";
			String branchName1 = "Branch03";
			
			String workspaceBase2 = "C:/Testes/workspaces/BranchWorkspaces/Workspace01/Branch02";
			String workspaceIntermediate2 = workspaceBase2 + "/input";
			String workspace2Activation1 = workspaceBase2 + "/activation1/input";
			String workspace2Activation2 = workspaceBase2 + "/activation2/input";
			String branchName2 = "Branch04";
			
			Collection<String> branchNames = new ArrayList<String>();
			branchNames.add(branchName1);
			
			String fileName1 = "file1.txt";
			String fileName2 = "file2.txt";
			
			
			//Creating central repository
			VCSManager vcsManager = VCSManagerFactory.getInstance();
			if (!vcsManager.isWorkspaceCreated(centralRepository)){
				vcsManager.createWorkspace(centralRepository);
				createFileContent(centralRepository + "/" + fileName1);
				createFileContent(centralRepository + "/" + fileName2);
				vcsManager.addAllFromPath(centralRepository);
				vcsManager.commit(centralRepository, "Initial Import");
			}
			
			////////////////////////////// FIRST BRANCH - START ///////////////////////////////////
			//Creating branch and intermediate workspace
			vcsManager.createBranch(centralRepository, branchName1);
			vcsManager.checkout(centralRepository, branchName1);
			vcsManager.commit(centralRepository, "Created Branch: " + branchName1);
			vcsManager.cloneRepository(centralRepository, workspaceIntermediate);

			//First Activation
			createAndExecuteActivation("Activation1", workspaceIntermediate, workspaceActivation1);
			
			//Second Activation
			createAndExecuteActivation("Activation2", workspaceIntermediate, workspaceActivation2);
			
			//Final PushBack
			vcsManager.pushBack(workspaceIntermediate, centralRepository);
			////////////////////////////// FIRST BRANCH - END /////////////////////////////////////
			
			
			////////////////////////////// SECOND BRANCH - START ///////////////////////////////////
			
			//Creating branch and intermediate workspace
			vcsManager.createBranch(centralRepository, branchName2);
			vcsManager.checkout(centralRepository, branchName2);
			vcsManager.commit(centralRepository, "Created Branch: " + branchName2);
			vcsManager.cloneRepository(centralRepository, workspaceIntermediate2);
			
			//First Activation
			createAndExecuteActivation("Activation1", workspaceIntermediate2, workspace2Activation1);
			
			//Second Activation
			createAndExecuteActivation("Activation2", workspaceIntermediate2, workspace2Activation2);
			
			//Final PushBack
			vcsManager.pushBack(workspaceIntermediate2, centralRepository);
			////////////////////////////// SECOND BRANCH - END /////////////////////////////////////
			
			
			
		} catch (VCSException e) {
			e.printStackTrace();
		}
	}
	
	private static void createAndExecuteActivation(String activationName, String workspaceIntermediate , String workspaceActivation) throws VCSException{
		String fileName1 = "file1.txt";
		String fileName2 = "file2.txt";
		
		VCSManager vcsManager = VCSManagerFactory.getInstance();
		
		//Creating Workspace
		vcsManager.cloneRepository(workspaceIntermediate, workspaceActivation);
		
		//Changes on Workspace
		changeFileContent(workspaceActivation + "/" + fileName1);
		changeFileContent(workspaceActivation + "/" + fileName2);
		vcsManager.addAllFromPath(workspaceActivation);
		vcsManager.commit(workspaceActivation, activationName + " - Commit 1");
		changeFileContent(workspaceActivation + "/" + fileName1);
		changeFileContent(workspaceActivation + "/" + fileName2);
		
		//Commit Workspace's changes
		vcsManager.addAllFromPath(workspaceActivation);
		vcsManager.commit(workspaceActivation, activationName + " - Commit 2");
		
		//PushBack
		vcsManager.pushBack(workspaceActivation, workspaceIntermediate);
	}

}

