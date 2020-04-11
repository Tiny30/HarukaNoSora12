package com.haruka.test;

import java.awt.Color;
import java.util.Random;

import javax.swing.JFrame;

import com.haruka.sora.impl.Sora;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.BodyActionController;
import com.iflysse.camera.ui.VideoPanel;
import com.iflysse.camera.util.Camera;
import com.xiaoSa.music.Constant;
import com.xiaoSa.music.MusicThread;

public class TestSora {
	
	
	public static void main(String[] args) {
		Sora sora = new Sora();
		sora.startWatch();
		Random random = new Random() ;
		new Thread(){
			public void run() {
				 int i = 0 ;
				while(true){
					MusicThread.volume = random.nextInt(20)+10;
					sora.action();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
//	new Thread(){
//			public void run() {
//				try {
//				Thread.sleep(6000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				BodyActionController.changeSpeaker(sora.getMainInterface().getBodyPanel(), sora.getSoraBody(), SoraBody.SPEAKER_XIAOXIN);
//			};
//		}.start();
		
	}
}
