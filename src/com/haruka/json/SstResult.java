package com.haruka.json;

import java.util.Arrays;

public class SstResult {
	private Integer sn ;
	
	private Boolean ls ;
	
	private Integer bg ;
	
	private Integer ed ;
	
	private Ws[] ws ;

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Boolean getLs() {
		return ls;
	}

	public void setLs(Boolean ls) {
		this.ls = ls;
	}

	public Integer getBg() {
		return bg;
	}

	public void setBg(Integer bg) {
		this.bg = bg;
	}

	public Integer getEd() {
		return ed;
	}

	public void setEd(Integer ed) {
		this.ed = ed;
	}

	public Ws[] getWs() {
		return ws;
	}

	public void setWs(Ws[] ws) {
		this.ws = ws;
	}

	@Override
	public String toString() {
		return "Test [sn=" + sn + ", ls=" + ls + ", bg=" + bg + ", ed=" + ed + ", ws=" + Arrays.toString(ws) + "]";
	}
	
	public String getSentence(){
		String str = "" ;
		for(Ws w : ws){
			str += w.getSentence() ;
		}
		return str;
	}
	
	
}

class Ws{
	private Integer bg ;
	
	private Cw[] cw ;

	public Integer getBg() {
		return bg;
	}

	public void setBg(Integer bg) {
		this.bg = bg;
	}

	public Cw[] getCw() {
		return cw;
	}

	public void setCw(Cw[] cw) {
		this.cw = cw;
	}

	@Override
	public String toString() {
		return "Ws [bg=" + bg + ", cw=" + Arrays.toString(cw) + "]";
	}
	
	public String getSentence(){
		String str = "" ;
		
		for(Cw c : this.cw){
			str += c.getSentence();
		}
		return str ;
	}
}

class Cw {
	private Double sc ;
	
	private String w ;

	public Double getSc() {
		return sc;
	}

	public void setSc(Double sc) {
		this.sc = sc;
	}

	public String getW() {
		return w;
	}

	public void setW(String w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "Cw [sc=" + sc + ", w=" + w + "]";
	}
	
	public String getSentence(){
		return this.w ;
	}
	
}
