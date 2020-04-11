package com.haruka.view.panel;

import javax.swing.JPanel;

import org.eclipse.swt.graphics.RGB;

import com.haruka.player.SimpleAudioPlayer;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class ListItemPanel extends JPanel {
	private JPanel contentPanel;
	private BufferedImage itemImg;
	private SimpleAudioPlayer launchSound;

	private ItemContent itemContext;

	private ListViewPanel owner;

	class Margin extends JLabel {
		public Margin(String text) {
			super(text);
			this.setBackground(new Color(0, 0, 0, 0));
		}
	}

	public class ItemContent extends JLabel {
		private BufferedImage itemImg;
		private int curWidth;
		private int curHeight;
		private String context = "";
		private int xOffset;
		private int align;
		private int origWidth;
		private int origHeight;

		public ItemContent(BufferedImage itemImg, String context, int align, int width, int height) {
			super(context, align);
			this.itemImg = itemImg;
			this.context = context;
			if (JLabel.LEFT == align) {
				this.setForeground(Color.CYAN);
			} else if (JLabel.RIGHT == align) {
				this.setForeground(Color.WHITE);
			}
			this.setFont(new Font("宋体", Font.BOLD, 24));
			this.origWidth = width;
			this.origHeight = height;
			this.setBounds(width, 0, width, height);

			// this.setLocation(width, 0);
			this.setVerticalAlignment(CENTER);
			this.align = align;
			if (this.align == JLabel.LEFT) {
				this.xOffset = -20;
			} else if (this.align == JLabel.RIGHT) {
				this.xOffset = 20;
			}

		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.drawImage(itemImg, xOffset, 0, this.getWidth(), this.getHeight(), null);
		}

		public BufferedImage getItemImg() {
			return itemImg;
		}

		public void setItemImg(BufferedImage itemImg) {
			this.itemImg = itemImg;
		}

		public int getCurWidth() {
			return curWidth;
		}

		public void setCurWidth(int curWidth) {
			this.curWidth = curWidth;
			if (this.align == JLabel.LEFT) {
				this.setLocation(this.origWidth - curWidth, 0);
			} else if (this.align == JLabel.RIGHT) {
				this.setLocation(curWidth - this.origWidth, 0);
			}
		}

		public int getCurHeight() {
			return curHeight;
		}

		public void setCurHeight(int curHeight) {
			this.curHeight = curHeight;
		}

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}

		public void diPose() {
			owner.remove(ListItemPanel.this);
		}

	}

	public ListItemPanel(ListViewPanel owner, String context, int align) {
		try {
			this.itemImg = ImageIO.read(new File("imgs/sora_apperance/smallWindow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.owner = owner;
		setLayout(new BorderLayout(0, 0));

		JLabel label = new Margin(" ");
		add(label, BorderLayout.NORTH);

		JLabel label_1 = new Margin("                  ");
		add(label_1, BorderLayout.WEST);

		JLabel label_2 = new Margin("   ");
		add(label_2, BorderLayout.EAST);

		JLabel label_3 = new Margin(" ");
		add(label_3, BorderLayout.SOUTH);

		contentPanel = new JPanel();
		contentPanel.setBackground(new Color(0, 0, 0, 0));
		contentPanel.setLayout(null);
		add(contentPanel, BorderLayout.CENTER);
		this.itemContext = new ItemContent(itemImg, context, align, 500, 50);
		contentPanel.add(itemContext);
		this.setBackground(new Color(255, 255, 255, 0));
		try {
			launchSound = new SimpleAudioPlayer(new File("sounds/listViewAperance.wav"));
			System.out.println("文件===========" + new File("sounds/listViewAperance.wav") );
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ListItemPanel launch() {
		this.launchSound.play();
		return this;
	}

	public ItemContent getItemContext() {
		return itemContext;
	}

	public void setItemContext(ItemContent itemContext) {
		this.itemContext = itemContext;
	}
}
