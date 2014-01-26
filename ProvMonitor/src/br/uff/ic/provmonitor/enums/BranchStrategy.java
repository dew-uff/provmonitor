package br.uff.ic.provmonitor.enums;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;
import br.uff.ic.provmonitor.output.ProvMonitorOutputType;

/**
 * Automated branch strategy supported by RestrospectivePronenanceBusinessServices
 * @see RetrospectiveProvenanceBusinessServices
 * @author Vitor C. Neves - vcneves@ic.uff.br
 *
 */
public enum BranchStrategy {
	
	ACTIVITY(2, "ACTIVITY"),
	TRIAL(1, "TRIAL");
	
	private Integer strategyId;
	private String strategyName;
	
	private BranchStrategy(Integer strategyId, String strategyName){
		this.strategyId = strategyId;
		this.strategyName = strategyName;
	}

	public Integer getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(Integer strategyId) {
		this.strategyId = strategyId;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	
	public static BranchStrategy valueOf(Integer code){
		for (BranchStrategy branchStrategy: BranchStrategy.values()){
			if (branchStrategy.getStrategyId().equals(code)){
				return branchStrategy;
			}
		}
		return null;
	}
	
	/**
	 * Get the {@link ProvMonitorOutputType} by the name.
	 * @param name Name of the desired {@link ProvMonitorOutputType}. Null if not found.
	 * @return ProvMonitorOutputType
	 */
	public static BranchStrategy valueOfName(String name){
		if (name != null && name.length() >0){
			for (BranchStrategy branchStrategy: BranchStrategy.values()){
				if (branchStrategy.getStrategyName().equals(name)){
					return branchStrategy;
				}
			}
			
		}
		return null;
	}
}
