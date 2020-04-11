package com.haruka.view.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;


public class WindowsMoveWithMouseListener implements MouseListener, MouseMotionListener {

	private JFrame owner;
	private int orginX ;
	private int orginY ;

	public WindowsMoveWithMouseListener(JFrame owner) {
		super();
		this.owner = owner;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = this.owner.getX() ;
		int y = this.owner.getY() ;
		this.owner.setLocation( x+ e.getX() - orginX, y + e.getY() - orginY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.orginX = e.getX() ;
		this.orginY = e.getY() ;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
