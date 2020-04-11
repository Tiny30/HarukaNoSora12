package com.haruka.dto.impl;

import java.util.Arrays;
import java.util.List;

import com.haruka.dto.SpeakDTO;
import com.haruka.tools.JSONTools;

/**
 * 谈话数据传输对象的实现类
 * 
 * @author haru
 *
 */
public class SpeakDTOImpl implements SpeakDTO{

	/**
	 * 回复的类型
	 */
	private int answerType;

	/**
	 * 回复的内容 
	 */
	private List<String> contexts;

	

	public SpeakDTOImpl(int answerType,  String... contexts) {
		super();
		this.answerType = answerType;
		this.contexts = Arrays.asList(contexts) ;
	}
	public SpeakDTOImpl(int answerType,  List<String> contexts) {
		super();
		this.answerType = answerType;
		this.contexts = contexts ;
	}

	public int getAnswerType() {
		return answerType;
	}

	public void setAnswerType(int answerType) {
		this.answerType = answerType;
	}
	public void setContexts(List<String> contexts) {
		this.contexts = contexts;
	}
	public void addContext(String context){
		this.contexts.add(context) ;
 	}
	@Override
	public List<String> getContexts() {
		return this.contexts;
	}
	
	
	
}
