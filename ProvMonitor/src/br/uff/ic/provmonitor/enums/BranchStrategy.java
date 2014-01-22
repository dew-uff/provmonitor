package br.uff.ic.provmonitor.enums;

import br.uff.ic.provmonitor.business.RetrospectiveProvenanceBusinessServices;

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
}
