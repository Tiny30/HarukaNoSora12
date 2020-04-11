package com.haruka.test;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.http.protocol.HTTP;

import com.haruka.ability.area.utils.MailSender;
import com.haruka.download.BaiduParser;
import com.haruka.player.SimpleAudioPlayer;
import com.haruka.tools.ChineseTools;
import com.haruka.tools.HttpTools;
import com.haruka.view.MainInterface;
import com.haruka.view.panel.ListViewPanel;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.BodyActionController;
import com.haruka.view.panel.action.ListViewActionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Test2 extends JFrame{
	

	public Test2(){
		this.setBounds(0, 0, 0, 20);
		this.setUndecorated(true);
		this.setBackground(new Color(0, 0, 0, 0));
		System.out.println("JFrame");
		this.setContentPane(new MyPanel2(this));
		System.out.println("JFrameEnd");
	}
	
	static Map<String, Integer> mapper = new HashMap<>() ;
	
	public static BufferedImage img = null;
	public static BufferedImage chooseFrame = null;

	public static void main(String[] args) {
		Test2 test = new Test2();
		
	}
	
	private static void parsePage(URL url){
		String html = HttpTools.getHtml(HttpTools.getURLConnectionInputStream(url, null)) ;
		
		Matcher ma = Pattern.compile("<a\\s.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>", Pattern.CASE_INSENSITIVE).matcher(html);
		 while(ma.find()){
			 String s = ma.group() ;
			// System.out.println(s);
			 if(s.matches("<a.*?>.*?上一页</a>")){
				 System.out.println(s.replaceAll(".*?href=\"|\" class=\"n\">.*", ""));
			 }
		}
		
	//	<a href="/s?wd=%E7%99%BE%E5%BA%A6&amp;pn=30&amp;oq=%E7%99%BE%E5%BA%A6&amp;ie=utf-8&amp;usm=5&amp;rsv_idx=1&amp;rsv_pq=a25ea526000337e2&amp;rsv_t=8090Ai83vO5SZlqb4JszfLrH%2BWoruAEf4p%2F9EiZymOOjYdRlcxXZfmnufEo&amp;rsv_page=-1" class="n">&lt;上一页</a>
	}

}

class MyPanel2 extends JPanel{
	
	private Image img ;
	private JFrame owner ;
	
	public MyPanel2(JFrame owner){
//		super(true);
		this.owner = owner;
		this.setLayout(null);
		this.setBackground(new Color(0, 0, 0, 0));
		try {
			this.img = ImageIO.read(new File("imgs/sora_apperance/welcome.png")) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g) ;
		g.drawImage(img, 0, 0,this.owner.getWidth(), this.owner.getHeight(), null) ;
	}
}