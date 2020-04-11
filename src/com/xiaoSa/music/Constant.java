package com.xiaoSa.music ;
public class Constant {
	
	public static DasktopMusic musicFrame;
	public static MainFrame mainFrame;
	
	static{
		 musicFrame = new DasktopMusic();
		 mainFrame = new MainFrame();
		 mainFrame.setVisible(true);
	}
}
