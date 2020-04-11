package com.haruka.view.panel.action;

import java.awt.Color;
import java.awt.Container;
import java.util.Random;

import javax.swing.plaf.basic.BasicBorders;

import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.view.panel.SoraBody;
import com.iflytek.cloud.a.a;
import com.iflytek.cloud.a.b;

public class BodyActionController {
	private static Thread activerThread ;
	private static Thread roateThread ;

	public static void apperance(Container container, SoraBody body) {
		container.setLayout(null);
		container.setSize((int) (body.getOrigBodyWidth() * 1.5), (int) (body.getOriBodyHeight() * 1.5));
		container.add(body);
		body.setActive(true);
		body.launch();
				double i ;
				for ( i = 0; i <= 1.2; i += 0.08) {
					body.setZoom(i);
				//	container.repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (; i >= 0.9; i -= 0.08) {
					body.setZoom(i);
					//container.repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for (; i <= 1.08; i += 0.08) {
					body.setZoom(i);
					//container.repaint();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				activerThread =	new Thread(()->{
					active(body);
				});
				roateThread =new Thread(()->{
					roate(body);
				});
				activerThread.start();
				roateThread.start();
				
				
				
	}
	
	public static void active(SoraBody body){
				boolean flag = true ;
				int index = 0 ;
				while(body.isActive()){
					double[] action = body.getAction();
					try{
						body.setZoom(action[index]);
						if(flag){
							if(++index==action.length){
								index--;
								flag = false ;
							}
						}else{
							if(--index == -1){
								index++;
								flag = true ;
							}
						}
					}catch(IndexOutOfBoundsException e){
						index = action.length-1 ;
						continue ;
					}
					
					try {
						Thread.sleep(body.getSpace());
					} catch (InterruptedException e) {
						return ;
					}
				}
					
			
	}
	
	public static void roate(SoraBody body){
				Random random = new Random() ;
				while(body.isActive()){
					int degree = random.nextInt(360) ;
					boolean dir = random.nextBoolean() ;
					for(int i = 1; i <= degree; i++){
						if(dir){
							body.setDegree(body.getDegree()+1);
						}else{							
							body.setDegree(body.getDegree()-1);
						}
						try {
							Thread.sleep(body.getSpace());
						} catch (InterruptedException e) {
							return ;
						}
					}
					try {
						Thread.sleep(body.getSpace()*5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
	}
	
	public static void disMiss(SoraBody body){
		activerThread.interrupt();
		roateThread.interrupt();
			body.setActive(false);
			for(double i = 1.2; i >= 0; i -= 0.1){
				body.setZoom(i);
				try {
					Thread.currentThread().sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		//	body.getParent().remove(body);
	}
	
	public static void changeSpeaker(Container container, SoraBody body, int speaker){
		new Thread(()->{
			disMiss(body);
			body.setSpeaker(speaker);
			apperance(container, body);
		}).start();
	}
}
