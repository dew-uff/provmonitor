package br.uff.ic.provmonitor.model;

/**
 * Represents the experiment instance: a trial of an specific experiment version
 * */
public class ExperimentInstance {
	
	private String instanceId;
	private String versionId;
	private String experimentId;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	public String getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}
	
	
}
