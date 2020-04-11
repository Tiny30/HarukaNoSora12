package com.haruka.ability;

import com.haruka.dto.SpeakDTO;

/**
 * 说话能力
 * 
 * @author haru
 *
 */
public interface AbilitySpeak {

	/**
	 * 初始化说话能力
	 */
	public void initSpeak();

	/**
	 * 开始说话
	 */
	public void startSpeak(String context);

	/**
	 * 正在说话
	 */
	public void duringSpeak();

	/**
	 * 停止说话
	 */
	public void stopSpeak();

	/**
	 * 是否正在说话
	 * 
	 * @return 正在说话返回true 否则返回false
	 */
	public boolean isSpeaking();
	
	public void setSpeaker(String speaker) ;

}
