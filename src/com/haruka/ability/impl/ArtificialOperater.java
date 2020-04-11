package com.haruka.ability.impl;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.haruka.ability.AbilityOperatePC;

public class ArtificialOperater extends Robot implements AbilityOperatePC{

	public ArtificialOperater() throws AWTException {
		super();
	}

	@Override
	public void pressKeys(int... keycodes) {
		for(int keycode : keycodes){
			this.keyPress(keycode);
		}
		for(int keycode : keycodes){
			this.keyRelease(keycode);
		}	
	}
	
	@Override
	public void minCurExe() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pressKeys(KeyEvent.VK_ALT,KeyEvent.VK_SPACE, KeyEvent.VK_N);
	}

	@Override
	public void maxCurExe() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pressKeys(KeyEvent.VK_ALT,KeyEvent.VK_SPACE, KeyEvent.VK_X);
	}

	@Override
	public void closeCurExe() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pressKeys(KeyEvent.VK_ALT,KeyEvent.VK_F4);
	}

	@Override
	public void pageUp() {
		this.mouseWheel(-5);
	}

	@Override
	public void pageDown() {
		this.mouseWheel(5);
	}

	@Override
	public void resetCurExe() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pressKeys(KeyEvent.VK_ALT,KeyEvent.VK_SPACE, KeyEvent.VK_R);
	}

}
