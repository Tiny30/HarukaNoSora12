package com.haruka.ability;

import com.haruka.dto.SpeakDTO;

public interface AbilityWatch {

	public void initWatch();


	public void startWatch();


	public void duringWatch();
	

	
	/**捕捉图像
	 * @param path 图像保存路径
	 */
	void catureImage(String parentPath, boolean isDirectory);
	
	public void stopWatch();

	/**
	 * 注册使用者脸部信息
	 */
	void registerMasterFace(String masterName, String masterGroupName, String imgsRootPath);


}
