package com.xiaoSa.music;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private int width = 500,height = 500;
	
	public MainFrame (){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setBounds(500,500,width,height);
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		this.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				MainFrame.this.setLocation(e.getLocationOnScreen());
			}
			
		});
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
//		BufferedImage image1 = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
//		image1.getGraphics().drawImage(Constant.musicFrame.getImage().getScaledInstance(400, 400,java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		g.drawImage(Constant.musicFrame.getImage(), 0, 0,this.getWidth(),this.getHeight(), null);
//		g.drawImage(image1, 0, 0, null);
	}
	
}
