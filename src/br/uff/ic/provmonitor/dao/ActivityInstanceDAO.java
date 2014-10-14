package br.uff.ic.provmonitor.dao;

import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ActivityInstance;

public interface ActivityInstanceDAO {
	
	public ActivityInstance getById(String activityId);
	public void persist(ActivityInstance activityInstance);
	
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
