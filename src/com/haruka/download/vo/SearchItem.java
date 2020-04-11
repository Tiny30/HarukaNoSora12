package com.haruka.download.vo;

public class SearchItem {
	
	private String songID ;
	
	private String songImgSrc ;
	
	private String songName ;
	
	private String src ;
	
	

	
	public SearchItem() {
		super();
	}


	public SearchItem(String songID, String songImgSrc, String songName, String src) {
		super();
		this.songID = songID;
		this.songImgSrc = songImgSrc;
		this.songName = songName;
		this.src = src ;
	}


	public String getSongID() {
		return songID;
	}


	public void setSongID(String songID) {
		this.songID = songID;
	}


	public String getSongImgSrc() {
		return songImgSrc;
	}


	public void setSongImgSrc(String songImgSrc) {
		this.songImgSrc = songImgSrc;
	}


	public String getSongName() {
		return songName;
	}


	public void setSongName(String songName) {
		this.songName = songName;
	}


	
	public String getSrc() {
		return src;
	}


	public void setSrc(String src) {
		this.src = src;
	}


	@Override
	public String toString() {
		return "SearchItem [songID=" + songID + ", songImgSrc=" + songImgSrc + ", songName=" + songName + "]";
	}
	
	
	
}
