package br.uff.ic.provmonitor.enums;

public enum MethodType {
	
	INITIALIZE_EXPERIMENT_EXECUTION 	(1, "initializeExperimentExecution"),
	NOTIFY_ACTIVITY_EXECUTION_STARTUP	(2, "notifyActivityExecutionStartup"),
	NOTIFY_PROCESS_EXECUTION_STARTUP	(3, "notifyProcessExecutionStartup"),
	NOTIFY_ACTIVITY_EXECUTION_ENDING	(4, "notifyActivityExecutionEnding"),
	NOTIFY_PROCESS_EXECUTION_ENDING		(5, "notifyProcessExecutionEnding"),
	NOTIFY_DECISION_POINT_ENDING		(6, "notifyDecisionPointEnding"),
	SET_ARTIFACT_VALUE					(7, "setArtifactValue"),
	PUBLISH_ARTIFACT_VALUE_LOCATION		(8, "publishArtifactValueLocation"),
	FINALIZE_EXPERIMENT_EXECUTION		(9, "finalizeExperimentExecution");
	
	private Integer code;
	private String name;
	
	private MethodType (Integer code, String name){
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static MethodType valueOf(Integer code){
		for (MethodType methodType: MethodType.values()){
			if (methodType.getCode().equals(code)){
				return methodType;
			}
		}
		return null;
	}
	
}
