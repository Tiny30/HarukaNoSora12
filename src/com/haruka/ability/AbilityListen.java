package com.haruka.ability;

import com.haruka.dto.SpeakDTO;

/**聆听能力
 * @author haru
 *
 */
public interface AbilityListen <Result>{
	
	/**
	 * 初始化聆听
	 */
	public void initListen() ;
	
	/**
	 * 聆听开始
	 */
	void startListen();
	

	/**
	 * 聆听结束
	 */
	void stopListen();

	
	/**
	 * 正在聆听
	 */
	void duringListen();
	
	
	
	
	/**获得聆听结果
	 * @return
	 */
	public Result  getResult() ;
	
	public void setListContext(String context) ;
		

}
