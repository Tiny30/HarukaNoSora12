package com.haruka.json;

import java.util.Arrays;

public class PWD {
	
	private String sub ;
	
	private String sst ;
	
	private String[] num_pwd ;

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getSst() {
		return sst;
	}

	public void setSst(String sst) {
		this.sst = sst;
	}

	public String[] getNum_pwd() {
		return num_pwd;
	}

	public void setNum_pwd(String[] num_pwd) {
		this.num_pwd = num_pwd;
	}

	@Override
	public String toString() {
		return "PWD [sub=" + sub + ", sst=" + sst + ", num_pwd=" + Arrays.toString(num_pwd) + "]";
	}
	
	
	
}
