package com.haruka.view.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import com.haruka.player.SimpleAudioPlayer;
import com.haruka.view.panel.action.ListViewActionController;

import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JLabel;

public class ListViewPanel extends JPanel {

	private BufferedImage chooseFrame;
	
	private BufferedImage indicator;
	
	private File launchSound = new File("sounds/listViewAperance.wav") ;
	
	
	private List<ListItemPanel> items =  new CopyOnWriteArrayList() ;
	
	private int origWidth ;
	private int origHeight ;
	
	private int curWidth = 0;
	private int curHeight = 0;

	public ListViewPanel(BufferedImage chooseFrame, int width, int height) {
		super();
		this.chooseFrame = chooseFrame;
		this.setSize(width, height);
		this.origWidth  = width ;
		this.origHeight = height ;
	//	this.setBounds(0, 0, width,height);
		this.setBackground(new Color(0, 0, 0, 0));
//		this.setBackground(Color.red);
		setLayout(new GridLayout(10, 1, 0, 0)) ;

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(chooseFrame, 0,this.curHeight-this.origHeight,this.getWidth(), this.getHeight(), null) ;
	}

	public int getOrigWidth() {
		return origWidth;
	}

	public void setOrigWidth(int origWidth) {
		this.origWidth = origWidth;
	}

	public int getOrigHeight() {
		return origHeight;
	}

	public void setOrigHeight(int origHeight) {
		this.origHeight = origHeight;
	}

	public int getCurWidth() {
		return curWidth;
	}

	public void setCurWidth(int curWidth) {
		this.curWidth = curWidth;
	}

	public int getCurHeight() {
		return curHeight;
	}

	public void setCurHeight(int curHeight) {
		this.curHeight = curHeight;
	}

	public BufferedImage getChooseFrame() {
		return chooseFrame;
	}

	public void setChooseFrame(BufferedImage chooseFrame) {
		this.chooseFrame = chooseFrame;
	}

	public BufferedImage getIndicator() {
		return indicator;
	}

	public void setIndicator(BufferedImage indicator) {
		this.indicator = indicator;
	}
	
	@Override
	public Component add(Component comp) {
		if(comp instanceof ListItemPanel){
			if(this.items.size() == 10){
				System.out.println("清除开始");
				ListViewActionController.clean(this);
				System.out.println("清除结束");
			}
				this.items.add(((ListItemPanel)comp)) ;
			
		}
		return super.add(comp);
	}

	public List<ListItemPanel> getItems() {
		return items;
	}
	
	public void launch(){
		try {
			new SimpleAudioPlayer(launchSound).play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
