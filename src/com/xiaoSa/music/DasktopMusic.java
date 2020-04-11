package com.xiaoSa.music;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;

public class DasktopMusic{
	
	private final int minNumber = 72;

	private Graphics g;
	private int imageSize = 2000;
	private int smallOvalR,largeOvalR;
	private int ovalCenterX,ovalCenterY;
	private BufferedImage image;
	private Corner[] corners = new Corner[minNumber];

	private int imageWidth,imageHeight;
	private Image ovalImage;
	
	public void setImageSize(int imageWidth,int imageHeight,Image image){
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.ovalImage = image;
	}
	
	public DasktopMusic(){
		init();
		for(int i=0;i<corners.length;i++)
			corners[i] = new Corner(i);
		paint(0);
	}
	
	public void init(){
		this.ovalCenterX = imageSize/2;
		this.ovalCenterY = imageSize/5;
		this.smallOvalR = imageSize/5;
		this.largeOvalR = imageSize/5+50;
//		this.smallOvalR = this.imageWidth*2;
	//	System.out.println(smallOvalR);
//		this.largeOvalR = this.imageSize/5;
	}
	
	public void paint(int volume) {
		init();
		removeAll();
		fillRect(volume);
		//drawOval();
		//fillPoint(volume);
	}
	
	
	private void drawOval(){
		g.drawImage(ovalImage,ovalCenterX-largeOvalR,ovalCenterY-largeOvalR,imageWidth*3,imageWidth*3,null);
	}
	
	class Corner{
		private int volume;
		private final int minHeight = 50;
		private Point margin[] = new Point[4];
		private MyPolygon[] rectArray = new MyPolygon[30];

		public Corner(int address) {
			for (int i = 0; i < margin.length; i++)
				margin[i] = new Point();
			margin[0].x = ovalCenterX - (int) (Math.cos(address * 2.0 / minNumber * Math.PI) * smallOvalR);
			margin[0].y = ovalCenterY - (int) (Math.sin(address * 2.0 / minNumber * Math.PI) * smallOvalR);
			margin[1].x = ovalCenterX - (int) (Math.cos((address + 1) * 2.0 / minNumber * Math.PI) * smallOvalR);
			margin[1].y = ovalCenterY - (int) (Math.sin((address + 1) * 2.0 / minNumber * Math.PI) * smallOvalR);

			for (int i = 0; i < rectArray.length; i++)
				rectArray[i] = new MyPolygon();

			Point minMargin[] = new Point[4];
			for (int i = 0; i < minMargin.length; i++)
				minMargin[i] = new Point();
			double proportion = minHeight
					/ Math.sqrt(Math.pow(margin[1].x - margin[0].x, 2) + Math.pow(margin[1].y - margin[0].y, 2));
			double minX = proportion * (margin[0].y - margin[1].y);
			double minY = proportion * (margin[0].x - margin[1].x);
			for (int i = 0; i < rectArray.length - 1; i++) {
				rectArray[i] = new MyPolygon();
				minMargin[0].x = (int) (margin[0].x - i * minX);
				minMargin[0].y = (int) (margin[0].y + i * minY);
				minMargin[1].x = (int) (margin[1].x - i * minX);
				minMargin[1].y = (int) (margin[1].y + i * minY);
				minMargin[2].x = (int) (margin[1].x - (i + 1) * minX);
				minMargin[2].y = (int) (margin[1].y + (i + 1) * minY);
				minMargin[3].x = (int) (margin[0].x - (i + 1) * minX);
				minMargin[3].y = (int) (margin[0].y + (i + 1) * minY);
				rectArray[i].addPoint(minMargin);
			}
		}
		
		public void setVolume(int volume){
			this.volume = volume;
		}
		
		public void volumeReduce(){
			this.volume-=5;
		}
		
		public void drawLine(){
			
			for(int i=0;i<corners.length;i++){
				
			}
		}
		
		public void fillRect(){
			for(int i=0;i<this.volume;i++){
				g.setColor(new Color(0,255,255));
				g.fillPolygon(rectArray[i]);
				g.setColor(new Color(43,255,0));
				g.drawPolygon(rectArray[i]);
			}
		}
		
		public void fillRect(int volume){
			for(int i=0;i<volume;i++){
				g.setColor(new Color(0,255,255));
				g.fillPolygon(rectArray[i]);
				g.setColor(new Color(43,255,0));
				g.drawPolygon(rectArray[i]);
			}
		}

		public int getVolume(){
			return this.volume;
		}
		
	}
	
	private void fillPoint(int volume){
		int rectwidth = 17,rectHeight=10;
		int lengthSize = largeOvalR/10;
		g.setColor(new Color(220,220,220));
		for(int i=0;i*20<2*largeOvalR;i++){
			for(int j=0;j<2;j++){
				g.fillRect(ovalCenterX-largeOvalR-20+i*20, ovalCenterY-15+j*15, rectwidth, rectHeight);
			}
		}
		for(int i=0;i<corners.length;i++){
			for(int j=0;j<corners[i].getVolume()*2;j++){
				g.fillRect(ovalCenterX-largeOvalR+corners[i].getVolume()*lengthSize, ovalCenterY-corners[i].getVolume()*15+j*15, rectwidth, rectHeight);
				g.fillRect(ovalCenterX+largeOvalR-corners[i].getVolume()*lengthSize, ovalCenterY-corners[i].getVolume()*15+j*15, rectwidth, rectHeight);
			}
		}
	}
	
	public void start(){
		MusicThread.volume = 0;
	}
	
	public void stop(){
		MusicThread.volume = 0;
	}
	
	private static int address;
	private static int rectNumber;
	private Random random = new Random();
	public void fillRect(int volume){
		if(volume>=0){
			address++;
			rectNumber++;
			if(address>=minNumber)
				address-=minNumber;
		}
		if(rectNumber>=5){
			rectNumber=0;
			address = random.nextInt(minNumber);
		}
		for(int i=0;i<corners.length;i++)
			corners[i].volumeReduce();
		corners[address].setVolume(volume);
		for(int i=0;i<corners.length;i++)
			corners[i].fillRect();
	}
	
	public void removeAll(){
		this.image = new BufferedImage(this.imageSize, this.imageSize, BufferedImage.TYPE_INT_ARGB);
		this.g = image.getGraphics();
	}
	
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public Graphics getG() {
		return g;
	}
	public void setG(Graphics g) {
		this.g = g;
	}
	public int getOutOvalR() {
		return smallOvalR;
	}
	public void setOutOvalR(int outOvalR) {
		this.smallOvalR = outOvalR;
	}
	public int getPutOvalR() {
		return largeOvalR;
	}
	public void setPutOvalR(int putOvalR) {
		this.largeOvalR = putOvalR;
	}
	public int getOvalCenterX() {
		return ovalCenterX;
	}
	public void setOvalCenterX(int ovalCenterX) {
		this.ovalCenterX = ovalCenterX;
	}
	public int getOvalCenterY() {
		return ovalCenterY;
	}
	public void setOvalCenterY(int ovalCenterY) {
		this.ovalCenterY = ovalCenterY;
	}
	public int getImageSize() {
		return imageSize;
	}
	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}
	
}

class MyPolygon extends Polygon{

	private static final long serialVersionUID = 1L;

	public void addPoint(Point point) {
		super.addPoint(point.x, point.y);
	}
	
	public void addPoint(Point point[]) {
		for(int i=0;i<point.length;i++){
			super.addPoint(point[i].x, point[i].y);
		}
	}
}