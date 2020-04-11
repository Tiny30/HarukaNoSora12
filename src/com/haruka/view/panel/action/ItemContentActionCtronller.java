package com.haruka.view.panel.action;

import java.awt.Container;
import java.util.List;

import javax.swing.JLabel;

import com.haruka.view.panel.ListItemPanel;
import com.haruka.view.panel.ListItemPanel.ItemContent;
import com.haruka.view.panel.ListViewPanel;

public class ItemContentActionCtronller {


	public static void apperance(ItemContent itemContent, boolean isSynchronized) {
		if(isSynchronized){
			itemAperance(itemContent);
		}else{
			new Thread() {
				public void run() {
					itemAperance(itemContent);
				};
			}.start();
		}
	}
	
	private static void itemAperance(ItemContent itemContent){
		int i;
		for (i = 0; i <= 507; i += 10) {
			itemContent.setCurWidth(i);
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void disMiss(ItemContent itemContent){
				int i;
				for (i = 507; i>= 0; i -= 50) {
					itemContent.setCurWidth(i);
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
	}
}
