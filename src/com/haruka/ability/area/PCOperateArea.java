package com.haruka.ability.area;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.ability.impl.ArtificialOperater;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.ChineseTools;
import com.haruka.tools.RegexTools;

public class PCOperateArea implements BrainArea {
	private ArtificialBrain brain;
	private StringBuffer curUrl;

	private Desktop desktop = Desktop.getDesktop();

	public PCOperateArea(ArtificialBrain brain) {
		this.brain = brain;
	}

	public void handlePCOperate(String order) {
		if (order.matches(".*?打开[defDEF]盘.*?")) {
			curUrl = new StringBuffer();
			order = RegexTools.getFirstConform(order, "打开[defDEF]盘");
			if (order.matches(".*?[dD].*?")) {
				curUrl.append("D:\\");
			} else if (order.matches(".*?[eE].*?")) {
				curUrl.append("E:\\");

			} else if (order.matches(".*?[fF].*?")) {
				curUrl.append("F:\\");

			}

			try {
				desktop.open(new File(this.curUrl.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				BrainAreaTools.speakFirstAndDisplayAsList(this, "文件夹" + this.curUrl + "未找到");
			}
			this.brain.getOwner().getOperater().maxCurExe();
		} else if (order.matches("打开.*?文件[^夹].*?")) {
			this.brain.getOwner().getOperater().closeCurExe();
			order = order.replaceAll("(打开)|(文件(夹)?)，?", ".");
			System.out.println(order);
			String[] fileName = order.split("\\.");
			this.curUrl.append(fileName[2]).append(".").append(fileName[1]);
			try {
				desktop.open(new File(this.curUrl.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				BrainAreaTools.speakFirstAndDisplayAsList(this, "文件" + this.curUrl + "未找到");
			}
			this.brain.getOwner().getOperater().maxCurExe();
		} else if (order.matches("打开.*?文件夹.*?")) {
			this.brain.getOwner().getOperater().closeCurExe();
			order = order.replaceAll("(打开)|(文件夹)，?", "");
			this.curUrl.append(order).append("\\");
			System.out.println("=====" + this.curUrl);
			try {
				desktop.open(new File(this.curUrl.toString()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.brain.getOwner().getOperater().maxCurExe();
		} else if (ChineseTools.pinyinMatch(order, ".*?(xiafan|xiaban).*?")) {
			this.brain.getOwner().getOperater().pageDown();
		} else if (ChineseTools.pinyinMatch(order, ".*?(shangfan|shangban).*?")) {
			this.brain.getOwner().getOperater().pageUp();
		}

	}

	@Override
	public ArtificialBrain getBrain() {
		return this.brain;
	}
}
