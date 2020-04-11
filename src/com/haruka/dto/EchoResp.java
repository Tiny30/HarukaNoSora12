package com.haruka.dto;

public class EchoResp<T> {
	private T data ;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
