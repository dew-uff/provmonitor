package br.uff.ic.provmonitor.cvsmanager;


/**
 * Supported Version Control Systems. <br />
 * <b>Currently Only Support Git</b>
 * @see GitManager
 * @see JavaGitManager
 * */
public enum VCSType {
	//Mercurial HG and SVN Support to be implemented.
	GIT (1, "GIT"), //JGit
	GIT_COMAND_LINE(2,"GIT_COMAND_LINE"); //Git through system command calls.
	
	private Integer code;
	private String name;
	
	private VCSType(Integer code, String name){
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
	public VCSType valueOf(Integer code){
		for (VCSType pctValue: VCSType.values()){
			if (pctValue.getCode().equals(code)){
				return pctValue;
			}
		}
		return null;
	}	
	
	public VCSType valueOfName(String name){
		for (VCSType pctValue: VCSType.values()){
			if (pctValue.getName().equals(name)){
				return pctValue;
			}
		}
		return null;
	}	
}
