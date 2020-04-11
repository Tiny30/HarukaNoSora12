package com.haruka.dto;

/**
 * 记忆的数据传输对象
 * @author haru
 *
 * @param <K> 记忆索引
 * @param <V> 记忆内容
 */
public interface MemoryDTO{
	
	public String getMemoryIndex () ;
	
	public String getMemoryContext() ;
}
