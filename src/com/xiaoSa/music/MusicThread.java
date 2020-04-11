package com.xiaoSa.music;

import java.awt.Image;
import java.awt.Point;

public class MusicThread extends Thread {
	public static volatile int volume;

	public static void paint(int imageWidth,int imageHeight,Image image) {
		Constant.musicFrame.setImageSize(imageWidth, imageHeight,image);
		Constant.musicFrame.paint(volume);
		Constant.musicFrame.stop();
	}
}
