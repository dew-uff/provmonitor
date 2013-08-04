package br.uff.ic.provmonitor.output;

/**
 * ProvMonitor output types supported by {@link ProvMonitorOutputManager}.
 * @see ProvMonitorOutputManager
 * @author Vitor C. Neves - vcneves@ic.uff.br
 */
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
	
	/**
	 * Get the {@link ProvMonitorOutputType} by the code.
	 * @param code Code of the desired {@link ProvMonitorOutputType}. Null if not found.
	 * @return ProvMonitorOutputType
	 */
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
	
	/**
	 * Get the {@link ProvMonitorOutputType} by the name.
	 * @param name Name of the desired {@link ProvMonitorOutputType}. Null if not found.
	 * @return ProvMonitorOutputType
	 */
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
