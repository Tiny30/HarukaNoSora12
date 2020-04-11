package com.haruka.view.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.ImageTools;
import com.xiaoSa.music.MusicThread;

public class SoraBody extends JPanel {

	public final static int SPEAKER_MADONGMEI = 0;
	public final static int SPEAKER_XIAOXIN = 1;
	public final static int SPEAKER_XIAORONG = 2;
	public final static int SPEAKER_IKAROS = 3;
	
	private List<BufferedImage> bodyImgs ;
	private List<BufferedImage> maDongMeiImgs = new ArrayList<>();
	private List<BufferedImage> xiaoRongImgs = new ArrayList<>();
	private List<BufferedImage> xiaoXinImgs = new ArrayList<>();
	private List<BufferedImage> iKarosImgs = new ArrayList<>();
	private BufferedImage bodyImg;
	private int origBodyWidth;
	private int oriBodyHeight;
	private double zoom = 1;
	private double zoomChangeSize = 1;
	private int curBodyWidth;
	private int curBodyHeight;
	private boolean isActive = true;
	private long space = 30;
	private int degree = 0;
	private double[] action;
	private BufferedImage maDongMeiImg ;
	private BufferedImage xiaoXinImg ;
	private BufferedImage xiaoRongImg ;
	private BufferedImage iKarosImg ;

	private ArtificialIntelligence owner ;
	private int speaker = 3 ;
	
	public SoraBody(ArtificialIntelligence owner, int origBodyWidth, int origHeight) {
		super();
		this.owner = owner ;
		this.bodyImg = bodyImg;
		try {
			maDongMeiImg = ImageIO.read(new File("imgs/sora_apperance/madongmei.png"));
			xiaoXinImg = ImageIO.read(new File("imgs/sora_apperance/xiaoxin.png"));
			xiaoRongImg = ImageIO.read(new File("imgs/sora_apperance/xiaorong.png"));
			iKarosImg = ImageIO.read(new File("imgs/sora_apperance/iKaros.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 360; i++) {
			this.maDongMeiImgs.add(ImageTools.rotateImage(maDongMeiImg, i));
			this.xiaoXinImgs.add(ImageTools.rotateImage(xiaoXinImg, i));
			this.xiaoRongImgs.add(ImageTools.rotateImage(xiaoRongImg, i));
			this.iKarosImgs.add(ImageTools.rotateImage(iKarosImg, i));
		}
		this.bodyImgs = iKarosImgs ;
		this.curBodyHeight = this.oriBodyHeight = origHeight;
		this.curBodyWidth = this.origBodyWidth = origBodyWidth;
		this.setSize(curBodyWidth, curBodyHeight);
		this.setBounds(0, 0, (int) (origBodyWidth * 1.5), (int) (origHeight * 1.5));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setVisible(true);
		action = ArtificialIntelligence.NORMAL_ACTION;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
//		MusicThread.paint(curBodyWidth, curBodyHeight, bodyImgs.get(this.degree >= 0 ? this.degree : -this.degree));
//		g.drawImage(com.xiaoSa.music.Constant.musicFrame.getImage(), (int) ((origBodyWidth * 1.5 - curBodyWidth) / 2),
//				(int) ((oriBodyHeight * 1.5 - curBodyHeight) / 2), curBodyWidth, curBodyHeight, null);
		g.drawImage(bodyImgs.get(this.degree >= 0 ? this.degree : -this.degree),
				(int) ((origBodyWidth * 1.5 - curBodyWidth) / 2), (int) ((oriBodyHeight * 1.5 - curBodyHeight) / 2),
				curBodyWidth, curBodyHeight, null);
//		g.drawImage(com.xiaoSa.music.Constant.musicFrame.getImage(), curBodyWidth/4-50, curBodyHeight/4-50, curBodyWidth, curBodyHeight, null) ;
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
		this.curBodyWidth = (int) (origBodyWidth * zoom);
		this.curBodyHeight = (int) (oriBodyHeight * zoom);
	}

	public BufferedImage getBodyImg() {
		return bodyImg;
	}

	public void setBodyImg(BufferedImage bodyImg) {
		this.bodyImg = bodyImg;
	}

	public int getOrigBodyWidth() {
		return origBodyWidth;
	}

	public void setOrigBodyWidth(int origBodyWidth) {
		this.origBodyWidth = origBodyWidth;
	}

	public int getOriBodyHeight() {
		return oriBodyHeight;
	}

	public void setOriBodyHeight(int oriBodyHeight) {
		this.oriBodyHeight = oriBodyHeight;
	}

	public int getCurBodyWidth() {
		return curBodyWidth;
	}

	public void setCurBodyWidth(int curBodyWidth) {
		this.curBodyWidth = curBodyWidth;
	}

	public int getCurBodyHeight() {
		return curBodyHeight;
	}

	public void setCurBodyHeight(int curBodyHeight) {
		this.curBodyHeight = curBodyHeight;
	}

	public double getZoom() {
		return zoom;
	}

	public double getZoomChangeSize() {
		return zoomChangeSize;
	}

	public void setZoomChangeSize(double zoomChangeSize) {
		this.zoomChangeSize = zoomChangeSize;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getSpace() {
		// TODO Auto-generated method stub
		return this.space;
	}

	public void setSpace(long space) {
		this.space = space;
	}

	public void rotate(int degree) {
		this.bodyImg = bodyImgs.get(degree % 360);
	}

	public List<BufferedImage> getBodyImgs() {
		return bodyImgs;
	}

	public void setBodyImgs(List<BufferedImage> bodyImgs) {
		this.bodyImgs = bodyImgs;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree % 360;
	}

	public  double[] getAction() {
		// TODO Auto-generated method stub
		return this.action;
	}

	public  void setAction(double[] action) {
		this.action = action;
	}

	public ArtificialIntelligence getOwner() {
		return owner;
	}

	public void setOwner(ArtificialIntelligence owner) {
		this.owner = owner;
	}

	public int getSpeaker() {
		return speaker;
	}

	public void setSpeaker(int speaker) {
		this.speaker = speaker;
		switch (speaker) {
		case SoraBody.SPEAKER_IKAROS:
			this.bodyImgs = iKarosImgs ;
			this.owner.getMouth().setSpeaker("xiaoyan");
			break;
		case SoraBody.SPEAKER_MADONGMEI:
			this.bodyImgs = maDongMeiImgs ;
			this.owner.getMouth().setSpeaker("vixying");
			break;
		case SoraBody.SPEAKER_XIAORONG:
			this.bodyImgs = xiaoRongImgs ;
			this.owner.getMouth().setSpeaker("vixr");
			
			break;
		case SoraBody.SPEAKER_XIAOXIN:
			this.bodyImgs = xiaoXinImgs ;
			this.owner.getMouth().setSpeaker("vixx");
			break;
		default:
			break;
		}
	}

	public void launch() {
		String context = "" ;
		switch (this.speaker) {
		case SoraBody.SPEAKER_IKAROS:
			context = "吾为吾剑之骨";
			break;
		case SoraBody.SPEAKER_MADONGMEI:
			context = "马  什么梅 啊";
			break;
		case SoraBody.SPEAKER_XIAORONG:
			context = "我在";
			break;
		case SoraBody.SPEAKER_XIAOXIN:
			context = "动感光波， 哔哔哔哔哔哔";
			break;
		default:
			break;
		}
		this.getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, context));
	}

	public void close() {
		String context = "" ;
		switch (this.speaker) {
		case SoraBody.SPEAKER_IKAROS:
			context = "我们的契约就到此为止了吗";
			break;
		case SoraBody.SPEAKER_MADONGMEI:
			context = "马  什么梅 啊";
			break;
		case SoraBody.SPEAKER_XIAORONG:
			context = "再见";
			break;
		case SoraBody.SPEAKER_XIAOXIN:
			context = "动感光波， 哔哔哔哔哔哔";
			break;
		default:
			break;
		}
		this.getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, context));
	}
	
	
}
