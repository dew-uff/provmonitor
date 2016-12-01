package br.uff.ic.provmonitor.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jgit.util.StringUtils;

import br.uff.ic.provmonitor.benchmark.Benchmark;
import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.vcsexceptions.VCSException;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.properties.ProvMonitorProperties;
import br.uff.ic.provmonitor.utils.WorkspaceUtils;
import br.uff.ic.provmonitor.vcsmanager.VCSManager;
import br.uff.ic.provmonitor.vcsmanager.VCSManagerFactory;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspaceAccessReader;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspacePathStatus;

public class ProvMonitorBenchmarks {
	
	public static final int AT_SHORT_TERM = 1;
	public static final int AT_SHORT_TERM_MANY_FILES = 2;
	public static final int AT_LONG_TERM = 10;
	public static final int AT_LONG_TERM_MANY_FILES = 11;
	
	public static final int AT_NON_INSTRUMENTED = 100;
	
	public static void main(String[] args) {
		runBanchmark();
	}
	
	public void runBachmarkFromProperties(){
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		System.out.println("Benchmark starting...");
		System.out.println("");
		System.out.println("");
		
		String rootExperimentBaseId = ProvMonitorProperties.getInstance().getBenchmarkRootExperimentId();
		String rootCentralRepository = ProvMonitorProperties.getInstance().getBenchmarkRootCentralRepository();
		String rootWorkspacePathBase = ProvMonitorProperties.getInstance().getBenchmarkRootWorkspacePathBase();
		String banchMarkSetup = ProvMonitorProperties.getInstance().getBenchmarkSetup();
		
		StringTokenizer st = new StringTokenizer(banchMarkSetup, ";");
		Integer experimentCount = 1;
		while (st.hasMoreTokens()){
			String setupConfig = st.nextToken();
			
			if (!StringUtils.isEmptyOrNull(setupConfig)){
				
				String experimentId = rootExperimentBaseId + experimentCount;
				String experimentIdNP = "NP_" + rootExperimentBaseId + experimentCount;
						
				StringTokenizer stExp = new StringTokenizer(setupConfig, ",");
				
				Integer numTrials = null;
				Integer numActivities = null;
				Integer numFiles = null;
				Integer numFileLines = null;
				
				if (stExp.hasMoreTokens()){
					numTrials = Integer.parseInt(stExp.nextToken());
				}
				if (stExp.hasMoreTokens()){
					numActivities = Integer.parseInt(stExp.nextToken());
				}
				if (stExp.hasMoreTokens()){
					numFiles = Integer.parseInt(stExp.nextToken());
				}
				if (stExp.hasMoreTokens()){
					numFileLines = Integer.parseInt(stExp.nextToken());
				}
				
				for (int i = 1; i <= numTrials; i++){
					try{
						
						//Without ProvMonitor
						String experimentInstanceIdNP = experimentIdNP + "_instance" + i;
						String centralRepositoryNP = rootCentralRepository + "/" + experimentIdNP + "/" + experimentInstanceIdNP;
						String workspacePathBaseNP = rootWorkspacePathBase + "/" + experimentIdNP + "/" + experimentInstanceIdNP;
						
						System.out.println("Starting Benchmark " + experimentInstanceIdNP + " without ProvMonitor" + " - " + sf.format(Calendar.getInstance().getTime()));
						
						List<Integer> activitiesTypesWithoutProvMonitor = benchMarkSetup_NonInstrumented(experimentInstanceIdNP, centralRepositoryNP, workspacePathBaseNP, numActivities, numFiles, numFileLines);
						benchMarkExecution(experimentIdNP, experimentInstanceIdNP, centralRepositoryNP, workspacePathBaseNP, activitiesTypesWithoutProvMonitor);
						
						System.out.println("... Ending Benchmark " + experimentInstanceIdNP + " without ProvMonitor" + " - " + sf.format(Calendar.getInstance().getTime()));
						
						System.out.println("");
						
						//With ProvMonitor
						String experimentInstanceId = experimentId + "_instance" + i;
						String centralRepository = rootCentralRepository + "/" + experimentId + "/" + experimentInstanceId;
						String workspacePathBase = rootWorkspacePathBase + "/" + experimentId + "/" + experimentInstanceId;
						
						System.out.println("Starting Benchmark " + experimentInstanceId + " using ProvMonitor" + " - " + sf.format(Calendar.getInstance().getTime()));
						
						List<Integer> activitiesTypes = benchMarkSetup(experimentInstanceId, centralRepository, workspacePathBase, numActivities, numFiles, numFileLines);
						benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
						
						System.out.println("... Ending Benchmark " + experimentInstanceId + " using ProvMonitor" + " - " + sf.format(Calendar.getInstance().getTime()));
						System.out.println("");
						System.out.println("");
						
					}catch(ProvMonitorException | IOException e){
						e.printStackTrace();
					}
					
				}
				
			}
			
			experimentCount++;
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("... Benchmark end.");
		
	}

	public static void runBanchmark(){
		
//		String experimentId = "BenchMark1_FewActivitiesFewFiles";
//		String experimentInstanceId = "BenchMark1_FewActivitiesFewFiles_Instance1";
//		String centralRepository = "C:/Testes/CentralRepositories/BenchMark1/CentralRepoFewFiles";
//		String workspacePathBase = "C:/Testes/workspaces/BenchMarks/FewActivitiesFewFiles";
		
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		
		/**
		 * Using ProVmonitor
		 */
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark6_300Activities200Files";
			String experimentInstanceId = "BenchMark6_200Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark6/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/300Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark6 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 300, 200);
				
				//List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark6 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark7_400Activities200Files";
			String experimentInstanceId = "BenchMark7_400Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark7/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/400Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark7 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 400, 200);
				
				//List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark7 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark8_100Activities200Files";
			String experimentInstanceId = "BenchMark8_100Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark8/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/100Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark8 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 100, 200);
				
				//List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark8 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		/*************************************
		 ******* Without using ProvMonitor
		 *************************************/
		
		
		for (int i=1;i<=3;i++){
			
			String experimentId = "BenchMark101_10Activities200Files";
			String experimentInstanceId = "BenchMark101_10Activities200Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark101/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchaMark101_10Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark101 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 10, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 10, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark101 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			
			String experimentId = "BenchMark102_4Activities200Files";
			String experimentInstanceId = "BenchMark102_4Activities200Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark102/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark102_4Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark102 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 4, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 4, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark102 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			
			String experimentId = "BenchMark103_4Activities4Files";
			String experimentInstanceId = "BenchMark103_4Activities4Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark103/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark103_4Activities4Files"+i;
			
			try{
				System.out.println("Starting Benchmark103 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 4, 4);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 4, 4);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark103 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			
			String experimentId = "BenchMark104_10Activities4Files";
			String experimentInstanceId = "BenchMark104_10Activities4Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark104/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark104_10Activities4Files"+i;
			
			try{
				System.out.println("Starting Benchmark104 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 10, 4);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 10, 4);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark104 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark105_200Activities200Files";
			String experimentInstanceId = "BenchMark105_200Activities200Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark105/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark105_200Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark105 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 200, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 200, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark105 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark106_300Activities200Files";
			String experimentInstanceId = "BenchMark106_200Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark106/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark106_300Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark106 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 300, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 300, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark106 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark107_400Activities200Files";
			String experimentInstanceId = "BenchMark107_400Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark107/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark107_400Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark107 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 400, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 400, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark107 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
		for (int i=1;i<=3;i++){
			String experimentId = "BenchMark108_100Activities200Files";
			String experimentInstanceId = "BenchMark108_100Activities300Files_Instance" + i;
			String centralRepository = "C:/Testes/CentralRepositories/BenchMark108/CentralRepoFewFiles"+i;
			String workspacePathBase = "C:/Testes/workspaces/BenchMarks/BenchMark108_100Activities200Files"+i;
			
			try{
				System.out.println("Starting Benchmark108 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
				
				//List<Integer> activitiesTypes = benchMarkSetup_FewActivitiesFewFiles(experimentInstanceId, centralRepository, workspacePathBase, 100, 200);
				
				List<Integer> activitiesTypes = benchMarkSetup_NonInstrumented(experimentInstanceId, centralRepository, workspacePathBase, 100, 200);
			
				benchMarkExecution(experimentId, experimentInstanceId, centralRepository, workspacePathBase, activitiesTypes);
			
				System.out.println("... Ending Benchmark108 Trial " + i + " - " + sf.format(Calendar.getInstance().getTime()));
			}catch(ProvMonitorException | IOException e){
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @param activitiesTypes
	 */
//	private static void execute(String experimentInstanceId, String centralRepository, String workspacePathBase, List<Integer> activitiesTypes){
//		
//	}
	
	/**
	 * Setup the benchmark with properties to match an execution with few activities with few files
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @return
	 * @throws IOException 
	 * @throws VCSException 
	 */
	private static List<Integer> benchMarkSetup(String experimentInstanceId, String centralRepository, String workspacePathBase, Integer totalActivities, Integer numberOfFiles, Integer numOfLines) throws VCSException, IOException{
		createInputData(true, centralRepository, numberOfFiles, numOfLines);
		List<Integer> activitiesTypes = new ArrayList<Integer>();
		for (int i = 0; i < totalActivities; i++){
			activitiesTypes.add(ProvMonitorBenchmarks.AT_SHORT_TERM);
		}
		return activitiesTypes;
	}
	
	/**
	 * Setup the benchmark with properties to match an execution with few activities with few files
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @return
	 * @throws IOException 
	 * @throws VCSException 
	 */
	private static List<Integer> benchMarkSetup_FewActivitiesFewFiles(String experimentInstanceId, String centralRepository, String workspacePathBase, Integer totalActivities, Integer numberOfFiles) throws VCSException, IOException{
		createInputData(true, centralRepository, numberOfFiles, 2000);
		List<Integer> activitiesTypes = new ArrayList<Integer>();
		for (int i = 0; i < totalActivities; i++){
			activitiesTypes.add(ProvMonitorBenchmarks.AT_SHORT_TERM);
		}
		return activitiesTypes;
	}
	
	/**
	 * Setup the benchmark with properties to match an execution with few activities with few files
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @return
	 * @throws IOException 
	 * @throws VCSException 
	 */
	
	private static List<Integer> benchMarkSetup_NonInstrumented(String experimentInstanceId, String centralRepository, String workspacePathBase, Integer totalActivities, Integer numberOfFiles, Integer numLines) throws VCSException, IOException{
		createInputData(false, centralRepository, numberOfFiles, numLines);
		List<Integer> activitiesTypes = new ArrayList<Integer>();
		for (int i = 0; i < totalActivities; i++){
			activitiesTypes.add(ProvMonitorBenchmarks.AT_NON_INSTRUMENTED);
		}
		return activitiesTypes;
	}
	
	/**
	 * Setup the benchmark with properties to match an execution with few activities with few files
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @return
	 * @throws IOException 
	 * @throws VCSException 
	 */
	
	private static List<Integer> benchMarkSetup_NonInstrumented(String experimentInstanceId, String centralRepository, String workspacePathBase, Integer totalActivities, Integer numberOfFiles) throws VCSException, IOException{
		createInputData(false, centralRepository, numberOfFiles, 2000);
		List<Integer> activitiesTypes = new ArrayList<Integer>();
		for (int i = 0; i < totalActivities; i++){
			activitiesTypes.add(ProvMonitorBenchmarks.AT_NON_INSTRUMENTED);
		}
		return activitiesTypes;
	}
	
	/**
	 * Creates the input data file to be used by the benchmarks
	 * @param usesVCS
	 * @param repositoryPath
	 * @param inputFilesNumber
	 * @param filesNumberOfLines
	 * @throws IOException 
	 * @throws VCSException 
	 */
	private static void createInputData(Boolean usesVCS, String repositoryPath, Integer inputFilesNumber, Integer filesNumberOfLines) throws IOException, VCSException{
		
		//Creating central repository
		Collection<String> filesNames = new ArrayList<String>();
		for (int i=0;i<inputFilesNumber;i++){
			filesNames.add(new String("file" + String.valueOf(i) + ".txt"));
		}
		
		VCSManager vcsManager = VCSManagerFactory.getInstance();

		if (!vcsManager.isWorkspaceCreated(repositoryPath)){
			if (usesVCS){ 
				vcsManager.createWorkspace(repositoryPath);
			}else{
				File dirPath = new File(repositoryPath);
				//Verify if path exists to avoid error
				if (!dirPath.exists() || !dirPath.isDirectory()){
					//If path does not exists create it.
					if(!dirPath.mkdirs()){
						throw new IOException("Workspace path does not exists and it was not possible create the path.");
					}
				}
			}
			
			for (String fileName : filesNames) {
				createFileContent(repositoryPath + "/" + fileName, filesNumberOfLines);
			}
			
			if (usesVCS){ 
				vcsManager.addAllFromPath(repositoryPath);
				vcsManager.commit(repositoryPath, "Initial Import");
			}
		}
		
	}
	
	/**
	 * Creates data file random content;
	 * @param filePath
	 * @param numberOfLines
	 * @throws IOException 
	 */
	private static void createFileContent(String filePath, Integer numberOfLines) throws IOException{
		File exampleHtml = new File(filePath);
		
		exampleHtml.createNewFile();
		FileWriter out = new FileWriter(exampleHtml);
		
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		
		for (int i=0;i<numberOfLines;i++){
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.write(nonce);
			out.write(System.getProperty("line.separator"));
			
		}
		
		out.close();
			
	}
	
	/**
	 * Change the content of the informed file with and random line at the end of file.
	 * @param filePath
	 */
	private static void changeFileContent(String filePath){
		File exampleHtml = new File(filePath);
		try {
			FileWriter fr = new FileWriter(exampleHtml, true);
			BufferedWriter out = new BufferedWriter(fr);
			
			SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
			String nonce = sf.format(Calendar.getInstance().getTime());
			
			out.append(nonce);
			out.close();
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes the changes in files. The basic representation of a basic activity that writes at the informed files.
	 * @param workspaceActivation
	 * @param fileNames
	 * @throws VCSException
	 */
	private static void executeActivation(String workspaceActivation, Collection<String> fileNames) throws VCSException{
		//Changes on Workspace
		for (String file : fileNames){
			changeFileContent(workspaceActivation + "/" + file);
			//changeFileContent(workspaceActivation + "/" + file);	
		}
	}
	
	/**
	 * ProvMonitors Experiment Setup to initialize an experiment execution.
	 * @param experimentId
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePath
	 * @param startDateTime
	 * @return The Experiment Instance ID.
	 * @throws ProvMonitorException
	 */
	private static String initializeExperimentTest(String experimentId, String experimentInstanceId, String centralRepository, String workspacePath, Date startDateTime) throws ProvMonitorException{
		try {
			experimentInstanceId = RetrospectiveProvenanceBusinessServices.initializeExperimentExecution(experimentId, experimentInstanceId, centralRepository, workspacePath);
		} catch (ProvMonitorException e) {
			ProvMonitorLogger.fatal(ProvMonitorTests.class.getName(), "initializeExperimentTest", "Exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return experimentInstanceId;
	}
	
	/**
	 * The Benchmark execution with the configured set of activities types
	 * @param experimentId
	 * @param experimentInstanceId
	 * @param centralRepository
	 * @param workspacePathBase
	 * @param activitiesTypes
	 * @throws ProvMonitorException
	 * @throws IOException
	 */
	private static void benchMarkExecution(String experimentId, String experimentInstanceId, String centralRepository, String workspacePathBase, List<Integer> activitiesTypes) throws ProvMonitorException, IOException{
		
		Benchmark benchmark = new Benchmark(experimentId,experimentInstanceId);
		Date startTime;
		Date endTime;
		
		String[] contextBase = {experimentInstanceId,"root"};
		
		String workspacePathBaseTrial = workspacePathBase + "/" + experimentInstanceId;
		String workspaceIntermediate = workspacePathBaseTrial + "/input";
		
		String activityInstanceBaseName="activity";
		Integer activityCount = 0;
		Boolean firstIterationOfIntermediateWorkspace = true;
		
		Date startDateTime = Calendar.getInstance().getTime();
		
		Boolean experimentProvMonitorInitialized = false;
		
		for (Integer activityType : activitiesTypes){
			String activityInstanceId = activityInstanceBaseName + activityCount++;
			String workspaceActivation = workspacePathBaseTrial + "/" + activityInstanceId + "/"+experimentInstanceId+"/input";
			
			//Initializing Experiment
			if (firstIterationOfIntermediateWorkspace){
				if(!activityType.equals(ProvMonitorBenchmarks.AT_NON_INSTRUMENTED)){
					initializeExperimentTest(experimentId, experimentInstanceId, centralRepository, workspaceIntermediate, startDateTime);
					experimentProvMonitorInitialized = true;
				}else{
					WorkspaceUtils.copyDirectory(centralRepository, workspaceIntermediate);
				}
				firstIterationOfIntermediateWorkspace = false;
			}
			
			String[] context = {contextBase[0],contextBase[1],activityInstanceId};
			
			startTime = Calendar.getInstance().getTime();
			
			switch (activityType){
				case ProvMonitorBenchmarks.AT_SHORT_TERM:
				case ProvMonitorBenchmarks.AT_SHORT_TERM_MANY_FILES:
				case ProvMonitorBenchmarks.AT_LONG_TERM:
				case ProvMonitorBenchmarks.AT_LONG_TERM_MANY_FILES:
					//executeActivity();
					shortTermActivityExecution(activityInstanceId, workspaceIntermediate, workspaceActivation, context);
					break;
				case ProvMonitorBenchmarks.AT_NON_INSTRUMENTED:
					nonInstrumentedActivityExecution(activityInstanceId, workspaceIntermediate, workspaceActivation, context);
					break;
			}
			
			endTime = Calendar.getInstance().getTime();
			
			benchmark.saveMarkup(startTime, endTime, activityInstanceId);
			
			//The next activity intermediate workspace is the last activity workspace.
			workspaceIntermediate = workspaceActivation;
		}
		
		Date endActiviyDateTime = Calendar.getInstance().getTime();
		//Finalizing Experiment
		if (experimentProvMonitorInitialized){
			RetrospectiveProvenanceBusinessServices.FinalizeExperimentExecution(experimentInstanceId, centralRepository, workspaceIntermediate, endActiviyDateTime);
		}
	}
	
	/**
	 * Execution of a ShorTerm activity
	 * @param activityInstanceId
	 * @param workspaceIntermediate
	 * @param workspaceActivation
	 * @param context
	 * @throws ProvMonitorException
	 * @throws IOException
	 */
	private static void shortTermActivityExecution(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context) throws ProvMonitorException, IOException{
		List<String> filesNames = new ArrayList<String>();
		filesNames = getFilesNames(workspaceIntermediate);
		activityExecution(activityInstanceId, workspaceIntermediate, workspaceActivation, context, filesNames);
	}
	
	/**
	 * 
	 * @param rootPath
	 * @return
	 * @throws IOException
	 */
	private static List<String> getFilesNames(String rootPath) throws IOException{
		//Date startDate = new Date("01/01/1900 00:00:00");
		//Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readAccessedPathStatusAndStatusTime(Paths.get(rootPath), startDate, true);
		Collection<WorkspacePathStatus>accessedFiles = WorkspaceAccessReader.readPathFilesLasReadTime(Paths.get(rootPath), true);
		
		
		List<String> filesNames = new ArrayList<String>();
		for (WorkspacePathStatus wkps : accessedFiles){
			String pathName = wkps.getPathName();
			String fileName = pathName.replace(rootPath, "");
			if (fileName != null && fileName.length() >0){
				boolean isNameBeginsCleaned = false;
				while (!isNameBeginsCleaned){
					if (fileName.charAt(0) == '/' ){
						fileName = fileName.substring(1, fileName.length());
					}else{
						isNameBeginsCleaned = true;
					}
				}
			}
			
			filesNames.add(fileName);
		}
		
		return filesNames;
	}
	
	/**
	 * 
	 * @param activityInstanceId
	 * @param workspaceIntermediate
	 * @param workspaceActivation
	 * @param context
	 * @param filesNames
	 * @throws ProvMonitorException
	 */
	private static void activityExecution(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context, List<String> filesNames) throws ProvMonitorException{
		Date activityStartDateTime = Calendar.getInstance().getTime();
		RetrospectiveProvenanceBusinessServices.notifyActivityExecutionStartup(activityInstanceId, context, activityStartDateTime, workspaceIntermediate, workspaceActivation);
		//Execute Activity 1 - Instance 1
		executeActivation(workspaceActivation, filesNames);
		//createFileContent(workspaceActivation + "/fileActivity1.txt", 1);
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		//Remove File
		//deleteFile(workspaceActivation1 + "/Folder1/teste2.html");
		//Ending Activity 1 - Instance 1
		
		activityStartDateTime = null;
		Date endActiviyDateTime = Calendar.getInstance().getTime();
		RetrospectiveProvenanceBusinessServices.notifyActivityExecutionEnding(activityInstanceId, context, activityStartDateTime, endActiviyDateTime, workspaceIntermediate, workspaceActivation);
	}
	
	/**
	 * 
	 * @param activityInstanceId
	 * @param workspaceIntermediate
	 * @param workspaceActivation
	 * @param context
	 * @throws ProvMonitorException
	 * @throws IOException
	 */
	private static void nonInstrumentedActivityExecution(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context) throws ProvMonitorException, IOException{
		//Copy Input Files from workspaceIntermediate to workspaceActivation
		WorkspaceUtils.copyDirectory(workspaceIntermediate, workspaceActivation);
				
		List<String> filesNames = new ArrayList<String>();
		filesNames = getFilesNames(workspaceIntermediate);
		activityExecutionWithoutProvMonitor(activityInstanceId, workspaceIntermediate, workspaceActivation, context, filesNames);
	}
	
	/**
	 * 
	 * @param activityInstanceId
	 * @param workspaceIntermediate
	 * @param workspaceActivation
	 * @param context
	 * @param filesNames
	 * @throws ProvMonitorException
	 * @throws IOException 
	 */
	private static void activityExecutionWithoutProvMonitor(String activityInstanceId, String workspaceIntermediate, String workspaceActivation, String[] context, List<String> filesNames) throws ProvMonitorException, IOException{
		//Execute Activity 1 - Instance 1
		
		executeActivation(workspaceActivation, filesNames);
		//createFileContent(workspaceActivation + "/fileActivity1.txt", 1);
	}
}
