package br.uff.ic.provmonitor.dao;

import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ActivityBenchmarkMarkup;

public interface ActivityBenchmarkMarkupDAO {

	/**
	 * Get the object representing the Activity Instance Benchmark markup searching by activiy instance Id.
	 * @param activityInstanceId <code>String</code> - The desired activity instance ID
	 * @return ActivityBenchmarkMarkup - The activity instance benchmark markup object. <b>NULL</b> otherwise.
	 * @throws ProvMonitorException
	 * */
	public ActivityBenchmarkMarkup getById(String activityInstanceId) throws ProvMonitorException;
	
	/**
	 * Persist the activity benchmark markup.
	 * @param activityBenchmarkMarkup <code>ActivityBenchmarkMarkup</code> - Object to be persisted.
	 * @throws ProvMonitorException
	 * */
	public void persist(ActivityBenchmarkMarkup activityBenchmarkMarkup) throws ProvMonitorException;
	
	
	/**
	 * Update the activity benchmark markup.
	 * @param activityBenchmarkMarkup <code>ActivityBenchmarkMarkup</code> - Object with activity benchmark markup to be updated.
	 * */
	public void update(ActivityBenchmarkMarkup activityBenchmarkMarkup) throws ProvMonitorException;
	
	/**
	 * Verify table existence in database. <br />
	 * @return <code><b>true</b></code>  - If table exists. <br />
	 * 		   <code><b>false</b></code> - If table does not exist.
	 * @throws ProvMonitorException If table could not be created. <br />
	 * 
	 */
	public boolean isTableCreated() throws ProvMonitorException;
	
	/**
	 * Create table in database. <br />
	 * @throws ProvMonitorException If table could not be created. <br />
	 * @throws DatabaseException <code>Database related problems.</code><br />
	 * 		<ul>
	 * 			<li>ConnectionException - Connection problems related.</li>
	 * 			<li>DatabaseException - Problems with the table creation itself.</li>
	 * 		</ul>
	 * */
	public void createTable() throws ProvMonitorException;
	
}
