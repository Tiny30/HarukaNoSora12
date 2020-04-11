package com.haruka.view.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.chrono.ThaiBuddhistEra;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.haruka.view.MainInterface;
import com.haruka.view.panel.ListViewPanel;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.BodyActionController;
import com.haruka.view.panel.action.ItemContentActionCtronller;
import com.haruka.view.panel.action.ListViewActionController;

public class TestView {

	public static BufferedImage img = null;
	public static BufferedImage chooseFrame = null;

	public static void main(String[] args) {
		try {
			img = ImageIO.read(new File("imgs/sora_apperance/iKaros.png"));
			chooseFrame = ImageIO.read(new File("imgs/sora_apperance/normalWindow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// try {
		// org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		MainInterface mi = new MainInterface();
		JPanel p = mi.getChooseFramePanel();
		List<String> contexts = new ArrayList<>();
		List<String> context = new ArrayList<>();
		contexts.add("你好");
		ListViewPanel listView = new ListViewPanel(chooseFrame, 390, 780);
		ListViewActionController.apperance(p, listView);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
//		ListViewActionController.disPlay(listView, contexts, JLabel.RIGHT);
		new Thread(){
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ListViewActionController.clean(listView);
			};
		}.start();
		mi.setVisible(true);
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			mi.repaint();
		}
	}
}
