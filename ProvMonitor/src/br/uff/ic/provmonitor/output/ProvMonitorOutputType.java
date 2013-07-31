package br.uff.ic.provmonitor.output;

public enum ProvMonitorOutputType {
	
	CONSOLE("CONSOLE","CONSOLE"),
	FILE("FILE","OUTPUTFILE");
	
	private String code;
	private String name;
	
	private ProvMonitorOutputType (String code, String name){
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
//	public ProvMonitorOutputType valueOf(String code){
//		if (code != null && code.length() >0){
//			for (ProvMonitorOutputType provValue: ProvMonitorOutputType.values()){
//				if (provValue.getCode().equals(code)){
//					return provValue;
//				}
//			}
//			
//		}
//		return null;
//	}
}
