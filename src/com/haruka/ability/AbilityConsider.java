package com.haruka.ability;

/** 思考能力
 * @author haru
 *
 */
public interface AbilityConsider<Result>{
	
	/**
	 * 初始化思考能力
	 */
	public void initConsider() ;
	
	/**
	 * 开始
	 */
	public void startCondsider() ;
	
	public void duringConsider() ;
	
	public void stopCondisder() ;
	
	public Result getResult() ;
	
	public int getStatus() ;
	
	public boolean isWaitForSelect () ;
}
