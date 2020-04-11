package com.haruka.view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.haruka.player.SimpleAudioPlayer;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;

public class MainInterface extends JFrame{
	private JPanel bodyPanel;
	private JPanel chooseFramePanel;
	private File sora_link_start = new File("sounds/sora_link_start.wav");
	
	private File sora_launch = new File("sounds/sora_launch.wav") ;
	
	public MainInterface() {
		this.setBounds(0, 0, 1178, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
//		this.sora_launch = new File("sounds/sora_launch.wav");
//		this.sora_link_start = new File("sounds/sora_link_start.wav");
		
		System.out.println(this.sora_launch);
		
		bodyPanel = new JPanel();
		bodyPanel.setBounds(14, 13, 600, 600);
		getContentPane().add(bodyPanel);
		bodyPanel.setLayout(null);
		bodyPanel.setBackground(new Color(0, 0, 0, 0));
			
		chooseFramePanel = new JPanel();
		chooseFramePanel.setBounds(433, 103, 600, 780);
		chooseFramePanel.setBackground(new Color(0, 0, 0, 0));
		getContentPane().add(chooseFramePanel);
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setAlwaysOnTop(true);
		this.apperance();
	}


	public JPanel getBodyPanel() {
		return bodyPanel;
	}


	public void setBodyPanel(JPanel bodyPanel) {
		this.bodyPanel = bodyPanel;
	}


	public JPanel getChooseFramePanel() {
		return chooseFramePanel;
	}


	public void setChooseFramePanel(JPanel chooseFramePanel) {
		this.chooseFramePanel = chooseFramePanel;
	}
	
	public void apperance(){
		new Thread(()->{
			try {
				new SimpleAudioPlayer(new File("sounds/sota_link_start.wav")).play();
				Thread.currentThread().sleep(1500);
				new SimpleAudioPlayer(new File("sounds/sora_launch.wav")).play();
			} catch (MalformedURLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}).start();

	}
}
