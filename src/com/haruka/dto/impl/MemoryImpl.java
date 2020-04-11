package com.haruka.dto.impl;

import com.haruka.dto.MemoryDTO;

/**记忆实体
 * @author haru
 *
 */
public class MemoryImpl implements MemoryDTO {

	/**
	 * 记忆索引 存进数据库里的key
	 */
	private String memoryIndex ;
	
	/**
	 * 记忆内容
	 */
	private String memoryContext ;

	
	
	public MemoryImpl() {
		super();
	}



	public MemoryImpl(String memoryIndex, String memoryContext) {
		super();
		this.memoryIndex = memoryIndex;
		this.memoryContext = memoryContext;
	}

	public String getMemoryIndex() {
		return memoryIndex;
	}

	public void setMemoryIndex(String memoryIndex) {
		this.memoryIndex = memoryIndex;
	}

	public String getMemoryContext() {
		return memoryContext;
	}

	public void setMemoryContext(String memoryContext) {
		this.memoryContext = memoryContext;
	}
	
	

}
