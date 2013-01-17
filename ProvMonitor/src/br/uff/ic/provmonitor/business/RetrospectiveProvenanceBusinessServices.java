package br.uff.ic.provmonitor.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.uff.ic.provmonitor.dao.ArtifactInstanceDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ArtifactInstance;
import br.uff.ic.provmonitor.model.ExecutionStatus;

//TODO: Translate comments and Javadocs
public class RetrospectiveProvenanceBusinessServices {
		
	//private Repository repository;
	
	//public RetrospectiveProvenanceServices() {
		//repository = Repository.getInstance();
	//}
	
	/**
	 * Inicializa uma nova execução de um experimento.
	 * @param experimentId Identificador do experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public String initializeExperimentExecution(String experimentId) throws CharonException{
		//return repository.getCharon().getCharonAPI().initializeExperimentExecution(experimentId);
	//}
	public static void initializeExperimentExecution(String experimentId) throws ProvMonitorException{
		//System.out.println("initializeExperimentExecution start execution...");
		
		//Record Timestamp
		Date timeStampInitExecute = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(timeStampInitExecute);
		String experimentInstanceId = experimentId + nonce;
		
		//Printing Generated Values
		System.out.println("ExperimentInstanceId: " + experimentInstanceId);
		System.out.println("BranchName: Branch_" + experimentInstanceId);
		
		//Initialize DB
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbInitialize();
		
		//Repository clone
		
		
		//System.out.println("initializeExperimentExecution end execution.");
	}
	
	public static void FinalizeExperimentExecution(String experimentInstanceId, String centralRepository, Date endDateTime) throws ProvMonitorException{
		//Stop DB infra
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbFinalize();
		
		//Pushback Repository
		
	}
	
	/**
	 * Notifica o início de execução de uma instância de uma atividade simples.
	 * @param activityInstanceId Identificador da instância da atividade simples que foi inicializada.
	 * @param context Sequência de indentificadores que define a localização da atividade simples no experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean notifyActivityExecutionStartup(String activityInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyActivityExecutionStartup(activityInstanceId, context);
	//}
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context){
		//Record Timestamp
		
	}
	
	/**
	 * Notifica o início de execução de uma instância de uma atividade simples.
	 * @param activityInstanceId Identificador da instância da atividade simples que foi inicializada.
	 * @param context Sequência de indentificadores que define a localização da atividade simples no experimento.
	 * @param activityStartDateTime
	 * @return resposta de sucesso de operação (true|false).'
	 * @throws ProvMonitorException
	 */
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime) throws ProvMonitorException{
		//Prepare ActivityObject to be persisted
		ExecutionStatus elementExecStatus = new ExecutionStatus();
		elementExecStatus.setElementId(activityInstanceId);
		elementExecStatus.setElementType("activity");
		elementExecStatus.setStatus("starting");
		//Mounting context
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("\\");
			}
			elementPath.append(path);
		}
		
		elementExecStatus.setElementPath(elementPath.toString());
		elementExecStatus.setStartTime(activityStartDateTime);
		
		//ActivityInstance activity = new ActivityInstance();
		//activity.setActivityInstanceId(activityInstanceId);
		//activity.set
		
		//Activity start commit
		
		//Record Timestamp
		ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
		//factory.getActivityInstanceDAO().persist(activityInstance);
		factory.getExecutionStatusDAO().persist(elementExecStatus);
		
	}
	
	/**
	 * Notifica o início de execução de uma instância de uma atividade composta.
	 * @param processInstanceId Identificador da instância da atividade composta que foi inicializada.
	 * @param context Sequência de indentificadores que define a localização da atividade composta no experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean notifyProcessExecutionStartup(String processInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyProcessExecutionStartup(processInstanceId, context);
	//}
	public static void notifyProcessExecutionStartup(String processInstanceId, String[] context){
	}
	
	/**
	 * Notifica o início de execução de uma instância de uma atividade composta.
	 * @param processInstanceId Identificador da instância da atividade composta que foi inicializada.
	 * @param context Sequência de indentificadores que define a localização da atividade composta no experimento.
	 * @param processStartDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyProcessExecutionStartup(String processInstanceId, String[] context, Date processStartDateTime) throws ProvMonitorException{
		notifyActivityExecutionStartup(processInstanceId, context, processStartDateTime);
	}
	
	/**
	 * Notifica o fim de execução de uma instância de uma atividade simples.
	 * @param activityInstanceId Identificador da instância da atividade simples que foi finalizada.
	 * @param context Sequência de indentificadores que define a localização da atividade simples no experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean notifyActivityExecutionEnding(String activityInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyActivityExecutionEnding(activityInstanceId, context);
	//}
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context){
		//Record Timestamp
		
		//Verify accessed files
		
		//Commit changed files
	}
	
	/**
	 * Notifica o fim de execução de uma instância de uma atividade simples.
	 * @param activityInstanceId Identificador da instância da atividade simples que foi finalizada.
	 * @param context Sequência de indentificadores que define a localização da atividade simples no experimento.
	 * @param startActivityDateTime
	 * @param endActiviyDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context, Date startActivityDateTime, Date endActiviyDateTime) throws ProvMonitorException{
		//Record Timestamp
		
		//Verify accessed files
		
		//Commit changed files
		
		//Recover executionStatus element
		System.out.println("Starting ActivityExecutionEnding Method...");
		ExecutionStatusDAO execStatusDAO = new ProvMonitorDAOFactory().getExecutionStatusDAO();
		System.out.println("Getting activity by id: " + activityInstanceId);
		ExecutionStatus elemExecutionStatus = execStatusDAO.getById(activityInstanceId);
		
		if (elemExecutionStatus == null){
			throw new ProvMonitorException("Element: " + activityInstanceId + " not found exception. Activity could not be finished if it was not started." );
		}
		
		//update execution element
		System.out.println("Updating Activity properties: End DateTime....");
		elemExecutionStatus.setEndTime(endActiviyDateTime);
		
		System.out.println("Updating Activity properties: Status....");
		elemExecutionStatus.setStatus("ended");
		
		//persist updated element
		System.out.println("Persisting Activity....");
		execStatusDAO.update(elemExecutionStatus);
		System.out.println("Activity Persisted.");
		
	}
	
	/**
	 * Notifica o fim de execução de uma instância de uma atividade composta.
	 * @param processInstanceId Identificador da instância da atividade composta que foi finalizada.
	 * @param context Sequência de indentificadores que define a localização da atividade composta no experimento.
	 * @param startProcessDateTime
	 * @param endProcessDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyProcessExecutionEnding(String processInstanceId, String[] context, Date startProcessDateTime, Date endProcessDateTime) throws ProvMonitorException{
		//Record Timestamp
		//Verify accessed files
		//Commit changed files
		notifyActivityExecutionEnding(processInstanceId, context, startProcessDateTime, endProcessDateTime);
	}
	
	/**
	 * Notifica o fim de execução de uma instância de uma atividade composta.
	 * @param processInstanceId Identificador da instância da atividade composta que foi finalizada.
	 * @param context Sequência de indentificadores que define a localização da atividade composta no experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean notifyProcessExecutionEnding(String processInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyProcessExecutionEnding(processInstanceId, context);
	//}
	public static void notifyProcessExecutionEnding(String processInstanceId, String[] context){
		//Record Timestamp
		
		//Verify accessed files
		
		//Commit changed files
	}
	
	/**
	 * Notifica o fim de execução de um ponto de decisão.
	 * @param decisionPointId Identificador do ponto de decisão que foi finalizado.
	 * @param optionValue Opção selecionada para o ponto de decisão.
	 * @param context Sequência de indentificadores que define a localização do ponto de decisão no experimento.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean notifyDecisionPointEnding(String decisionPointId, String optionValue, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyDecisionPointEnding(decisionPointId, optionValue, context);
	//}
	public static void notifyDecisionPointEnding(String decisionPointId, String optionValue, String[] context){
	}

	/**
	 * Publica os dados do artefato.
	 * @param artifactId Identificador do artefato.
	 * @param context Sequência de indentificadores que define a localização do artefato no experimento.
	 * @param value Valor do artefato.
	 * @throws ProvMonitorException
	 */
	//public boolean setArtifactValue(String artifactId, String[] context, String value) throws CharonException{
		//return repository.getCharon().getCharonAPI().setArtifactValue(artifactId, context, value);
	//}
	public static void setArtifactValue(String artifactId, String[] context, String value) throws ProvMonitorException{
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("\\");
			}
			elementPath.append(path);
		}
		
		//Preparing artifact Objetct to be persisted
		ArtifactInstance artifactInstance = new ArtifactInstance();
		artifactInstance.setArtifactId(artifactId);
		artifactInstance.setArtifactValue(value);
		artifactInstance.setArtifactPath(elementPath.toString());
		
		ArtifactInstanceDAO artifactValueDAO = new ProvMonitorDAOFactory().getArtifactInstanceDAO();
		artifactValueDAO.persist(artifactInstance);
		
	}
	
	
	/**
	 * Publica a localização dos dados do artefato.
	 * @param artifactId Identificador do experimento.
	 * @param context Sequência de indentificadores que define a localização do artefato no experimento.
	 * @param hostURL URL da máquina onde o artefato está localizado.
	 * @param hostLocalPath Diretório local da máquina onde o artefato está localizado.
	 * @return resposta de sucesso de operação (true|false).
	 */
	//public boolean publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath) throws CharonException{
		//return repository.getCharon().getCharonAPI().publishArtifactValueLocation(artifactId, context, hostURL, hostLocalPath);
	//}
	public static void publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath){
		
	}
	
}
