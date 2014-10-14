package br.uff.ic.provmonitor.model;

/**
 * Represents the instance of an activity execution
 * */
public class ActivityInstance {
	
	private String instanceId;
	private String activityInstanceId;
	private String activityId;
	private String activityName;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getActivityInstanceId() {
		return activityInstanceId;
	}
	public void setActivityInstanceId(String activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
}
