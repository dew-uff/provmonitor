package br.uff.ic.provmonitor.output;

public enum ProvMonitorOutputType {
	
	CONSOLE("CONSOLE","CONSOLE"),
	FILE("FILE","FILE");
	
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
	
	public static ProvMonitorOutputType valueOfCode(String code){
		if (code != null && code.length() >0){
			for (ProvMonitorOutputType provValue: ProvMonitorOutputType.values()){
				if (provValue.getCode().equals(code)){
					return provValue;
				}
			}
			
		}
		return null;
	}
	
	public static ProvMonitorOutputType valueOfName(String name){
		if (name != null && name.length() >0){
			for (ProvMonitorOutputType provValue: ProvMonitorOutputType.values()){
				if (provValue.getName().equals(name)){
					return provValue;
				}
			}
			
		}
		return null;
	}
}
