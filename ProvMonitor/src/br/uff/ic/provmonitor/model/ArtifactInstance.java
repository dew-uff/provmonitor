package br.uff.ic.provmonitor.model;

/**
 * Represents the instance of an artifact value on an execution
 * */
public class ArtifactInstance {
	
	/**New Model*/
	private String instanceId;
	private String artifactName;
	private String activityInstanceId;
	private String artifact;
	
	/**Charon Model*/
	private String artifactId;
	private String artifactValue;
	private String artifactPath;
	 
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getArtifactName() {
		return artifactName;
	}
	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}
	public String getArtifact() {
		return artifact;
	}
	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}
	public String getActivityInstance() {
		return activityInstanceId;
	}
	public void setActivityInstance(String activityInstance) {
		this.activityInstanceId = activityInstance;
	}
	public String getArtifactValue() {
		return artifactValue;
	}
	public void setArtifactValue(String artifactValue) {
		this.artifactValue = artifactValue;
	}
	public String getArtifactPath() {
		return artifactPath;
	}
	public void setArtifactPath(String artifacPath) {
		this.artifactPath = artifacPath;
	}
	
	
}
