package br.uff.ic.provmonitor.vcsmanager;

public enum GITCloneOptions {
	
	DEFAULT(1, "DEFAULT"),
	SHARE(2, "SHARE"),
	HARDLINK(3, "HARDLINK"),
	SHARE_HARDLINK(4, "SHARE_HARDLINK");
	
	private Integer code;
	private String name;
	
	private GITCloneOptions(Integer code, String name){
		this.code = code;
		this.name = name;
	}
	
	/**
	 * Returns the enum instance with the code value informed.
	 * @param code <code>Integer</code> The code of the enum searched.
	 * @return DatabaseType 
	 * */
	public static GITCloneOptions valueOf(Integer code){
		for (GITCloneOptions pctValue: GITCloneOptions.values()){
			if (pctValue.getCode().equals(code)){
				return pctValue;
			}
		}
		return null;
	}	
	
	public static GITCloneOptions valueOfName(String name){
		for (GITCloneOptions pctValue: GITCloneOptions.values()){
			if (pctValue.getName().equals(name)){
				return pctValue;
			}
		}
		return null;
	}

	/*
	 * Getters && Setters 
	 */
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

	