package com.haruka.ability;

/**操作电脑能力
 * @author haru
 */
public interface AbilityOperatePC {
	
	/** 按下电脑按键
	 * @param keycodes
	 */
	public void pressKeys(int... keycodes) ;
	
	/**
	 * 最小化当前应用程序
	 */
	public void minCurExe() ;
	
	public void maxCurExe() ;
	
	public void closeCurExe() ; 
	
	public void pageUp() ;
	
	public void pageDown() ;

	public void resetCurExe();
}
