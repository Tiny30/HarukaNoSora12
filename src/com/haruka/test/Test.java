package com.haruka.test;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import com.haruka.sora.impl.Sora;
import com.haruka.tools.FormatTools;
import com.haruka.tools.HttpTools;
import com.haruka.tools.JSONTools;
import com.iflysse.tuling.MyTuLing;
import com.iflysse.yuyin.MyYuyin;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizerListener;

public class Test extends JFrame {
	public static int size = 500;
	public static BufferedImage realout;
	public static BufferedImage out;
	public static BufferedImage realinside;
	public static BufferedImage inside;
	public static BufferedImage sideFrame;
	public static BufferedImage backFrame;
	
	public static List<BufferedImage> listinside = new ArrayList<>();
	public static List<BufferedImage> listoutside = new ArrayList<>();
	int offsetX = 5;
	int offsetY = 5;
	static boolean isPress = false;
	
	public static int oringX = 0 ;
	public static int oringY = 0 ;

	public Test() {
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setContentPane(new MyPanel(this));
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				isPress = false;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				offsetX = e.getX();
				offsetY = e.getY();
				isPress = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {

			}

			@Override
			public void mouseDragged(MouseEvent e) {
				oringX = Test.this.getX() + e.getX() - offsetX;
				oringY =  Test.this.getY() + e.getY() - offsetY ;
				setLocation(Test.this.getX() + e.getX() - offsetX, Test.this.getY() + e.getY() - offsetY);
			}
		});
	}
//	
	public static void main(String[] args) {
		try {
			realinside = ImageIO.read(new File("imgs/sora_apperance/wel_inside.png"));
			realout = ImageIO.read(new File("imgs/sora_apperance/wel_outside.png"));
			sideFrame = ImageIO.read(new File("imgs/sora_apperance/sideFrame.png"));
			backFrame = ImageIO.read(new File("imgs/sora_apperance/message.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inside = realinside ;
		out = realout ;

		int i = 0 ;
		for(i = 0; i < 360; i++){
			listinside.add(rotateImage(realinside, i)) ;
			listoutside.add(rotateImage(realout, -i)) ;
		}
		Test test = new Test();
		Test.size = 0 ;
		test.setBounds(0, 0, 500, 500);
		test.setVisible(true);
		while(Test.size < 250){
			//test.setSize(test.getWidth()+10, test.getHeight()+10);
			Test.size += 10 ;
			try {
				Thread.currentThread().sleep(5);
				test.repaint();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		new Thread(){
			public void run() {
				int d = 0;
				int index = 0 ;
				while(true){
					if(! isPress){
					out = listoutside.get(d) ;
					inside = listinside.get(d) ;
					test.repaint();
					d = ++d%360;
						if(index < 30){
							if(index <= 20){
								size += 1 ;
							}else{
								size -= 2 ;
							}
						}else{
							size = 500 ;
							index = 0 ;
						}
						test.setLocation(oringX-size+500, oringY-size+500);
						index ++;
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();

	}

	public static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
		int w = bufferedimage.getWidth();
		int h = bufferedimage.getHeight();
		int type = bufferedimage.getColorModel().getTransparency();
		BufferedImage img;
		Graphics2D graphics2d;
		(graphics2d = (img = new BufferedImage(w, h, type)).createGraphics())
				.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.rotate(Math.toRadians(degree), w / 2, h / 2);
		graphics2d.drawImage(bufferedimage, 0, 0, null);
		graphics2d.dispose();
		return img;
	}

//	 @Override
//	 public void paint(Graphics g) {
//	 // TODO Auto-generated method stub
//	 // super.paint(g);
//	 // g.drawImage(Test.img, 0, 0, 500, 500, null) ;
//	 Graphics2D g2 = (Graphics2D)g; //强转成2D
//	 //dimision.width是窗体的宽度，dimision.height是窗体的高度
//	 g2.drawImage(img, 0, 0, 200,200,null);
//	 }
}

class MyPanel extends JPanel {

	private JFrame owner;

	public MyPanel(JFrame owner) {
		super(true);
		this.owner = owner;
		this.setLayout(null);
		this.setBackground(new Color(0, 0, 0, 0));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	//	g.drawImage(Test.sideFrame, 0, 0,(int)(Test.size), (int)(Test.size),  null) ;
		g.drawImage(Test.backFrame, 500, 0,Test.size*2, Test.size,null);
		g.drawImage(Test.inside, 0, 0, Test.size, Test.size, null);
		g.drawImage(Test.out, 0, 0, Test.size, Test.size, null);
	}
}
