package br.uff.ic.provmonitor.dao;

import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ExecutionCommit;

public interface ExecutionCommitDAO {
	/**
	 * Get the object representing the Element Execution Status searching by Id.
	 * @param elementId <code>String</code> - The desired Element ID
	 * @param elementPath <code>String</code> - The desired Element context (path)
	 * @return ExecutionCommit - The element execution commit information object. <b>NULL</b> otherwise.
	 * @throws ProvMonitorException
	 * */
	public ExecutionCommit getById(String elementId, String elementPath) throws ProvMonitorException;
	
	/**
	 * Persist the element execution status information.
	 * @param executionCommit <code>ExecutionCommit</code> - Object to be persisted.
	 * @throws ProvMonitorException
	 * */
	public void persist(ExecutionCommit executionCommit) throws ProvMonitorException;
	
	
	/**
	 * Update the element execution status information.
	 * @param executionCommit <code>ExecutionCommit</code> - Object with element execution commit to be updated.
	 * */
	public void update(ExecutionCommit executionCommit) throws ProvMonitorException;
	
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
