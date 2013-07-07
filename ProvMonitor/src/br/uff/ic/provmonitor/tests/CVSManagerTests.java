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

import br.uff.ic.provmonitor.cvsmanager.CVSManager;
import br.uff.ic.provmonitor.cvsmanager.CVSManagerFactory;
import br.uff.ic.provmonitor.exceptions.CVSException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;

public class CVSManagerTests {
	public static void main(String[] args) {
		//basicCVSFunctionalitiesTests();
		try {
			cloneParcialAndFullTest();
		} catch (ProvMonitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					
			CVSManager cvsManager = CVSManagerFactory.getInstance();
			cvsManager.cloneRepository(sourceRepository, workspacePath);
			
			//Repository branch
			cvsManager.createBranch(workspacePath, experimentInstanceId);
			
			//Repository checkOut
			//cvsManager.checkout(workspacePath, "trunk");
			
		}catch(CVSException e){
			e.printStackTrace();
		}
	}
	
	private static void createCentralRepository(String centralRepository){
		try{
			CVSManager cvsManager = CVSManagerFactory.getInstance();
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
			
			CVSManager cvsManager = CVSManagerFactory.getInstance();
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
		CVSManager cvsManager = CVSManagerFactory.getInstance();
		try{
			cvsManager.createWorkspace(workspacePath);
		}catch (CVSException e) {
			e.printStackTrace();
		}
	}
	
	private static void checkOutTest(String workspacePath){
		CVSManager cvsManager = CVSManagerFactory.getInstance();
		try{
			cvsManager.checkout(workspacePath, "head");
		}catch (CVSException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void addByPatternsTest(String workspacePath, Collection<String> filePathList){
		CVSManager cvsMan = CVSManagerFactory.getInstance();
		try {
			cvsMan.addPathOrFile(workspacePath, filePathList);
		} catch (CVSException e) {
			e.printStackTrace();
		}
	}
	
	private static void addAllTest(String workspacePath){
		CVSManager cvsMan = CVSManagerFactory.getInstance();
		try {
			cvsMan.addAllFromPath(workspacePath);
		} catch (CVSException e) {
			e.printStackTrace();
		}
	}
	
	private static void commitTest(String workspacePath, String message){
		CVSManager cvsMan = CVSManagerFactory.getInstance();
		try {
			cvsMan.commit(workspacePath, message);
		} catch (CVSException e) {
			e.printStackTrace();
		}
	}
	
	private static void cloneTest(String sourceUri, String destinationFolder){
		CVSManager cvsMan = CVSManagerFactory.getInstance();
		try {
			cvsMan.cloneRepository(sourceUri, destinationFolder);
		} catch (CVSException e) {
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
			
			CVSManager cvs = CVSManagerFactory.getInstance();
			
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
	
	
	
	private static void cloneParcialAndFullTest() throws ProvMonitorException{
		
		String centralRepository = "C:\\Testes\\CentralRepo\\Repo1";
		String workspacePathFull = "C:\\Testes\\workspaces\\WorkspaceFullClone4";
		String workspacePath = "C:\\Testes\\workspaces\\WorkspaceParcialClone4";
		
		Collection<String> branchesToBeCloned = new ArrayList<String>();
		branchesToBeCloned.add("master");
		
		CVSManager cvsManager = CVSManagerFactory.getInstance();
		//System.out.println("Cloning to: " + workspacePath);
		cvsManager.cloneRepository(centralRepository, workspacePathFull);
		cvsManager.cloneRepository(centralRepository, workspacePath, branchesToBeCloned);
	}
	

}
