package br.uff.ic.provmonitor.connection;


/**
 * Supported database types.
 * */
public enum DatabaseType {
	JAVADB (1, "JavaDB"),
	PROVMANAGER_SERVICE (2, "ProvManagerWebServcice"),
	LOCAL_FILE (3, "LocalFile"),
	MYSQL (4, "MySQL");
	
	private Integer code;
	private String name;
	
	private DatabaseType(Integer code, String name){
		this.code = code;
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the enum instance with the code value informed.
	 * @param code <code>Integer</code> The code of the enum searched.
	 * @return DatabaseType 
	 * */
	public DatabaseType valueOf(Integer code){
		for (DatabaseType pctValue: DatabaseType.values()){
			if (pctValue.getCode().equals(code)){
				return pctValue;
			}
		}
		return null;
	}	
}
