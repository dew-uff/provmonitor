package br.uff.ic.provmonitor.benchmark;

import java.util.Date;

import br.uff.ic.provmonitor.dao.ActivityBenchmarkMarkupDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.log.ProvMonitorLogger;
import br.uff.ic.provmonitor.model.ActivityBenchmarkMarkup;

/**
 * 
 * @author Vitor
 *
 */
public class Benchmark {
	private ActivityBenchmarkMarkup actBenchmark = new ActivityBenchmarkMarkup();
	
	public Benchmark(){
		super();
	}
	
	public Benchmark(String experimentId, String experimentInstanceId){
		super();
		this.actBenchmark.setExperimentId(experimentId);
		this.actBenchmark.setExperimentInstanceId(experimentInstanceId);
	}
	
	void setExperimentId(String experimentId){
		this.actBenchmark.setExperimentId(experimentId);
	}
	
	public void setExperimentInstanceId(String experimentInstanceId){
		this.actBenchmark.setExperimentInstanceId(experimentInstanceId);
	}
	
	public void setActivityInstanceId(String activityInstanceId){
		this.actBenchmark.setActivityInstanceId(activityInstanceId);
	}
	
	public void setActivityStartTime (Date startTime){
		this.actBenchmark.setStartTime(startTime);
	}
	
	public void setActivityEndTime (Date endTime){
		this.actBenchmark.setEndTime(endTime);
	}
	
	public void setElementPath(String elementPath){
		this.actBenchmark.setElementPath(elementPath);
	}
	
	public void saveMarkup(Date startTime, Date endTime, String activityInstanceId){
		this.actBenchmark.setActivityInstanceId(activityInstanceId);
		saveMarkup(startTime,endTime);
	}
	
	public void saveMarkup(Date startTime, Date endTime){
		this.actBenchmark.setStartTime(startTime);
		this.actBenchmark.setEndTime(endTime);
		this.saveMarkup();
	}
	
	public void saveMarkup(){
//		final ActivityBenchmarkMarkup parameter = this.actBenchmark;
//		Thread t = new Thread(new Runnable() {
//		    public void run() {
//		    	ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
//				ActivityBenchmarkMarkupDAO dao = daoFactory.getActivityBenchmarkMarkupDAO();
//				try {
//					dao.persist(parameter);
//				} catch (ProvMonitorException e) {
//					ProvMonitorLogger.fatal(Benchmark.class.getName(),"Benchmark.saveMarkup", e.getMessage());
//					e.printStackTrace();
//				}
//		    }
//		});
//		t.start();
		
		final ActivityBenchmarkMarkup parameter = this.actBenchmark;
		new Thread(new Runnable() {
		    public void run() {
		    	ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
				ActivityBenchmarkMarkupDAO dao = daoFactory.getActivityBenchmarkMarkupDAO();
				try {
					dao.persist(parameter);
				} catch (ProvMonitorException e) {
					ProvMonitorLogger.fatal(Benchmark.class.getName(),"Benchmark.saveMarkup", e.getMessage());
					e.printStackTrace();
				}
		    }
		}).start();
	}
	
	
	
	
}
