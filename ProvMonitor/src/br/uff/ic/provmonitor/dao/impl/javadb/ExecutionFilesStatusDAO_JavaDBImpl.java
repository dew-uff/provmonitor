package br.uff.ic.provmonitor.dao.impl.javadb;

import br.uff.ic.provmonitor.dao.ExecutionFilesStatusDAO;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ExecutionStatus;

public class ExecutionFilesStatusDAO_JavaDBImpl implements ExecutionFilesStatusDAO{

	@Override
	public ExecutionStatus getById(String elementId, String elementPath)
			throws ProvMonitorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persist(ExecutionStatus executionStatus)
			throws ProvMonitorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ExecutionStatus executionStatus)
			throws ProvMonitorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTableCreated() throws ProvMonitorException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createTable() throws ProvMonitorException {
		// TODO Auto-generated method stub
		
	}

}
