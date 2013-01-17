package br.uff.ic.provmonitor.dao;

import br.uff.ic.provmonitor.exceptions.DatabaseException;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.exceptions.ServerDBException;

/**
 * DAO Responsible for embedded database control/administration when applicable
 * 	<code>Examples of responsibilities: start/stop database server, verify and create schema, etc. </code>
 * */
public interface DatabaseControlDAO {
	
	/**
	 * Execute all initializing steps needed for the database implementation. <br />
	 * <ul>
	 * 		<li><b>Step1: </b> Start embedded DB server.</li>
	 * 		<li><b>Step2: </b> Create schema if needed</li>
	 * </ul>
	 * @throws ProvMonitorExcepion If some irrecoverable exception occurs.<br />
	 * @throws DatabaseException <code>Database related problems.</code><br />
	 * 		<ul><li><b>ConnectionException: </b><code>Database connection problems.</code><br /></li>
	 * 		<li><b>ServerDBException: </b><code>Database server related problems.</code><br /></li></ul>
	 * */
	public void dbInitialize() throws ProvMonitorException;
	
	/**
	 * Execute all finalizing steps needed for the database implementation. <br />
	 * 	<b>Recommended implementation:</b> <code>Executes once at workflow end.</code>
	 * @throws DatabaseException
	 * @throws ServerDBException 
	 * */
	public void dbFinalize() throws ProvMonitorException;
}
