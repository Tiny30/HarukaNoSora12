package com.haruka.view.panel.action;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import com.haruka.player.SimpleAudioPlayer;
import com.haruka.view.panel.ListItemPanel;
import com.haruka.view.panel.ListViewPanel;

public class ListViewActionController {
	
	public static void apperance(Container container, ListViewPanel listView){
		new Thread(){
			public void run() {
				container.setSize(listView.getOrigWidth()+180, 780);
				System.out.println(listView.getSize());
				container.setLayout(new BorderLayout());
			//	container.setBackground(Color.red);
				container.setBackground(new Color(0, 0, 0, 0));
				container.add(listView) ;
				listView.setCurHeight(4);
				int i ;
				listView.launch();
				for( i = 0; i <= listView.getOrigWidth(); i+=8){
					listView.setCurWidth(i);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for( i = 12; i <= listView.getOrigHeight(); i+=12){
					listView.setCurHeight(i);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				};
		}.start();
	}
	

	public static void disPlay(ListViewPanel listView, List<String> displayContexts, int align, boolean isSynchronized){
		System.out.println("开始展示");
				int index = 0 ;
				for(String context:displayContexts){
					if(index++ == 10){
						break ;
					}
					ListItemPanel list = new ListItemPanel(listView, context, align) ;
					listView.add(list) ;
					listView.revalidate();
					ItemContentActionCtronller.apperance(list.getItemContext(), isSynchronized);
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			
	}
	
	public static void clean(ListViewPanel listView){
		System.out.println("2132121231231231231231232132");
		listView.launch();
		for(ListItemPanel listItem : listView.getItems()){
			ItemContentActionCtronller.disMiss(listItem.getItemContext());
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			listView.remove(listItem);
		}
		listView.getItems().clear();
	} 
	
	public static void disApperance(ListViewPanel listView){
		listView.launch();
		clean(listView);
		int i ;
		for( i = listView.getOrigHeight(); i >=0 ; i-=12){
			listView.setCurHeight(i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//listView.getParent().remove(listView);
	}
}
