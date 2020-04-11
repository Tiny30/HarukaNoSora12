package com.haruka.ability;

/**记忆能力
 * @author haru
 *
 */
public interface AbilityMemory<Memory> {
	
	/**
	 * 初始化记忆
	 */
	public void initMemory() ;
	
	/**
	 * 开始记忆
	 */
	public void startMemory(Memory memory) ;
	
	/**
	 * 记忆中
	 */
	public void duringMemory() ;
	
	/**
	 * 停止记忆
	 */
	public void stopMemory() ;
	
	
}
