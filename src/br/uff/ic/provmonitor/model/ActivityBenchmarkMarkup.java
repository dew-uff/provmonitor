package br.uff.ic.provmonitor.model;

import java.util.Date;

/**
 * Benchmarks purposes only
 * @author Vitor
 *
 */
public class ActivityBenchmarkMarkup {
	
	private String experimentId;
	private String experimentInstanceId;
	private String activityInstanceId;
	private Date startTime;
	private Date endTime;
	private String elementPath;
	
	public String getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}
	public String getExperimentInstanceId() {
		return experimentInstanceId;
	}
	public void setExperimentInstanceId(String experimentInstanceId) {
		this.experimentInstanceId = experimentInstanceId;
	}
	public String getActivityInstanceId() {
		return activityInstanceId;
	}
	public void setActivityInstanceId(String activityInstanceId) {
		this.activityInstanceId = activityInstanceId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getElementPath() {
		return elementPath;
	}
	public void setElementPath(String elementPath) {
		this.elementPath = elementPath;
	}
	
	
}
