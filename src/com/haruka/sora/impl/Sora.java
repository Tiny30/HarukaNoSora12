package com.haruka.sora.impl;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.haruka.ability.AbilityConsider;
import com.haruka.ability.AbilityListen;
import com.haruka.ability.AbilityMemory;
import com.haruka.ability.AbilityOperatePC;
import com.haruka.ability.AbilitySpeak;
import com.haruka.ability.AbilityWatch;
import com.haruka.ability.area.BrainArea;
import com.haruka.ability.impl.ArtificalEye;
import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.ability.impl.ArtificialEar;
import com.haruka.ability.impl.ArtificialMouth;
import com.haruka.ability.impl.ArtificialOperater;
import com.haruka.dto.CommandDTO;
import com.haruka.dto.MemoryDTO;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.MemoryImpl;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.player.SimpleAudioPlayer;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.BrainAreaTools;
import com.haruka.view.MainInterface;
import com.haruka.view.listener.WindowsMoveWithMouseListener;
import com.haruka.view.panel.ListViewPanel;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.BodyActionController;
import com.haruka.view.panel.action.ListViewActionController;
import com.iflysse.tuling.MyTuLing;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechUtility;

/**
 * 实现智能的类
 * 
 * @author haru
 */
public class Sora implements ArtificialIntelligence<String> {

//	public static final String APP_ID = "=57d657ba"; 
//	public static final String APP_ID = "=5da0750a";
	public static final String APP_ID = "=57e8d047"; // NEW

	private AbilityListen<String> listen;

	private AbilityConsider<String> consider;

	private AbilityMemory<MemoryDTO> memory;

	private AbilityOperatePC operater;

	private AbilitySpeak mouth;

	private AbilityWatch watch;

	private boolean isActive = false;

	private JPanel disPlayPanel;

	private int status;

	private MainInterface mainInterface;

	private BodyActionController bodyActionController;

	private ListViewActionController chfController;

	private BufferedImage chooseFrame;

	private ListViewPanel listView;
	private SoraBody soraBody;

	public Sora() {
		this.mainInterface = new MainInterface();
		
		WindowsMoveWithMouseListener wwm = new WindowsMoveWithMouseListener(this.mainInterface) ;
		this.mainInterface.addMouseListener(wwm);
		this.mainInterface.addMouseMotionListener(wwm);
		this.mainInterface.setVisible(true);
		try {
			chooseFrame = ImageIO.read(new File("imgs/sora_apperance/normalWindow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.listView = new ListViewPanel(chooseFrame, 390, 780);
		new Thread(()->{
			ListViewActionController.apperance(mainInterface.getChooseFramePanel(), listView);
		}).start();
		this.soraBody = new SoraBody(this, 300, 300);
		new Thread(()->{
			BodyActionController.apperance(mainInterface.getBodyPanel(), soraBody);
			
		}).start();
		this.status = ArtificialIntelligence.TEST_MODEL;
		this.init();
		this.listen();
		this.speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, "正在赶来的路上...."));
	}

	@Override
	public void init() {
		SpeechUtility.createUtility(SpeechConstant.APPID + APP_ID);
		System.out.println(SpeechUtility.getUtility().toString());
		this.mouth = new ArtificialMouth(this);
		try {
			new SimpleAudioPlayer(new File("sounds/sora_apperance.wav")).play();
		} catch (MalformedURLException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		this.listen = new ArtificialEar(this);
		//MyTuLing.init("bd494c8efd8d4b759d8d0c7738f53dd9");
		//NEW
		MyTuLing.init("22a8c9685e6548629dac9ab9219e8755");
		
		// 开启数据库
		try {
			Process progress = Runtime.getRuntime().exec("e:\\Redis\\redis-server e:\\redis\\redis.conf");
			while (!progress.isAlive()) {
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArtificialBrain brain = new ArtificialBrain(this);
		this.consider = brain;
		this.memory = brain;
		this.watch = new ArtificalEye(this);
		try {
			this.operater = new ArtificialOperater();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, "初始化完成"));
	}

	@Override
	public void memory(MemoryDTO memory) {

		Sora.this.memory.startMemory(memory);
	}

	@Override
	public void memoryCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void speak(SpeakDTO speakContext) {
		List<String> contexts = speakContext.getContexts();
		System.out.println(contexts);
		new Thread() {
			public void run() {
				synchronized (Sora.this) {
					switch (speakContext.getAnswerType()) {
					case SpeakDTO.DISPLAY_AS_LIST:
						ListViewActionController.disPlay(listView, contexts, JLabel.LEFT, false);
						break;
					case SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST:
						Sora.this.mouth.startSpeak(contexts.get(0));
						ListViewActionController.disPlay(listView, contexts, JLabel.LEFT, false);
						break;
					case SpeakDTO.CLEAN_AT_ONCE:
						ListViewActionController.clean(listView);
						ListViewActionController.disPlay(listView, speakContext.getContexts(), JLabel.LEFT, false);
						break;
					case SpeakDTO.MASTER_CONTEXT:
						ListViewActionController.disPlay(listView, speakContext.getContexts(), JLabel.RIGHT, false);
						break;
					case SpeakDTO.ALARM:
						ListViewActionController.disPlay(listView, speakContext.getContexts(), JLabel.LEFT, false);
						break;
					case SpeakDTO.DISPLAY_AS_LIST_WITH_SYNCHRONIZED:
						ListViewActionController.disPlay(listView, speakContext.getContexts(), JLabel.LEFT, true);
					default:
						break;
					}
				}
			};
		}.start();
	}

	@Override
	public void speakCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void listen() {

		new Thread() {
			@Override
			public void run() {
				Sora.this.listen.startListen();
			}
		}.start();
	}

	@Override
	public void listenCompleted() {
		String result = this.listen.getResult();
		System.out.println(result);
		if (!this.isActive) {
			if (!result.matches(".*?[迪伊一你比英]卡[洛璐个多螺老][丝斯诗车师]，?.*")) {
				return;
			} else {
				new Thread(()->{
					ListViewActionController.apperance(this.getMainInterface().getChooseFramePanel(), listView);
					BodyActionController.apperance(this.mainInterface.getBodyPanel(), soraBody);
				}).start();
				result = result.replaceFirst(".*?[迪伊一你比英]卡[洛璐个多螺老][丝斯诗车师]，?", "");
				this.isActive = true;
				if (this.consider.getStatus() == ArtificialBrain.SECURITY_MODEL) {
					this.speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "让我来确认一下你的真面目吧",
							"请先注册或者直接验证"));
				} else {
					this.speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "就是你在叫我？"));
				}
				
			}
		}

		this.memory(new MemoryImpl("order", result));
		this.consider();
	}

	@Override
	public void consider() {
		this.consider.startCondsider();

	}

	@Override
	public void considerCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void action() {
		// this.mainInterface.revalidate();
		this.mainInterface.repaint();
	}

	@Override
	public void figure(Graphics g) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getOrder() {
		return this.listen.getResult();
	}

	@Override
	public void operate(CommandDTO cmd) {
		// TODO Auto-generated method stub
	}

	@Override
	public void operateCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionCompleted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		this.memory.initMemory();
	}

	@Override
	public boolean isSpeaking() {
		return this.mouth.isSpeaking();
	}

	@Override
	public void stopListen() {
		if (this.listen != null) {
			this.listen.stopListen();
		}
	}

	@Override
	public JPanel getDisplayPanel() {
		return this.disPlayPanel;
	}

	@Override
	public void startWatch() {
		this.watch.startWatch();
	}

	@Override
	public void watchCompleted() {
		System.out.println("观察结束");
	}

	@Override
	public void setDisplayPanel(JPanel disPlayPanel) {
		this.disPlayPanel = disPlayPanel;
	}

	@Override
	public void stopWatch() {
		this.watch.stopWatch();
	}

	@Override
	public void captureImage(String storagePath, boolean isDirectory) {
		this.watch.catureImage(storagePath, isDirectory);
	}

	@Override
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;

	}

	public AbilityListen<String> getListen() {
		return listen;
	}

	public void setListen(AbilityListen<String> listen) {
		this.listen = listen;
	}

	public AbilityConsider<String> getConsider() {
		return consider;
	}

	public void setConsider(AbilityConsider<String> consider) {
		this.consider = consider;
	}

	public AbilityMemory<MemoryDTO> getMemory() {
		return memory;
	}

	public void setMemory(AbilityMemory<MemoryDTO> memory) {
		this.memory = memory;
	}

	public AbilitySpeak getMouth() {
		return mouth;
	}

	public void setMouth(AbilitySpeak mouth) {
		this.mouth = mouth;
	}

	public AbilityWatch getWatch() {
		return watch;
	}

	public void setWatch(AbilityWatch watch) {
		this.watch = watch;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public JPanel getDisPlayPanel() {
		return disPlayPanel;
	}

	public void setDisPlayPanel(JPanel disPlayPanel) {
		this.disPlayPanel = disPlayPanel;
	}

	public AbilityOperatePC getOperater() {
		return operater;
	}

	public void setOperater(AbilityOperatePC operater) {
		this.operater = operater;
	}

	@Override
	public int getStatus() {
		return this.status;
	}

	@Override
	public AbilityListen getEar() {
		return this.listen;
	}

	public MainInterface getMainInterface() {
		return mainInterface;
	}

	public void setMainInterface(MainInterface mainInterface) {
		this.mainInterface = mainInterface;
	}

	public BodyActionController getBodyActionController() {
		return bodyActionController;
	}

	public void setBodyActionController(BodyActionController bodyActionController) {
		this.bodyActionController = bodyActionController;
	}

	@Override
	public ListViewActionController getChfController() {
		return chfController;
	}

	public void setChfController(ListViewActionController chfController) {
		this.chfController = chfController;
	}

	public BufferedImage getChooseFrame() {
		return chooseFrame;
	}

	public void setChooseFrame(BufferedImage chooseFrame) {
		this.chooseFrame = chooseFrame;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void sleep() {
		ListViewActionController.disApperance(listView);
	}

	@Override
	public void move(int direction, int range) {
		int x = mainInterface.getX();
		int y = mainInterface.getY();
		switch (direction) {
		case ArtificialIntelligence.DIR_UP:
			this.mainInterface.setLocation(x, y + range);
			break;
		case ArtificialIntelligence.DIR_DOWN:
			this.mainInterface.setLocation(x, y - range);
			break;
		case ArtificialIntelligence.DIR_LEFT:
			this.mainInterface.setLocation(x - range, y);
			break;
		case ArtificialIntelligence.DIR_RIGHT:
			this.mainInterface.setLocation(x + range, y);
			break;
		case ArtificialIntelligence.DIR_CENTER:
			this.mainInterface.setLocationRelativeTo(null);
			break;
		case ArtificialIntelligence.DIR_UPPER_LEFT:
			this.mainInterface.setLocation(x - range, y - range);
			break;
		case ArtificialIntelligence.DIR_UPPER_RIGHT:
			this.mainInterface.setLocation(x - range, y + range);
			break;
		case ArtificialIntelligence.DIR_LOWER_LEFT:
			this.mainInterface.setLocation(x - range, y + range);
			break;
		case ArtificialIntelligence.DIR_LOWER_RIGHT:
			this.mainInterface.setLocation(x + range, y + range);
			break;

		}
	}

	public SoraBody getSoraBody() {
		return soraBody;
	}

	public ListViewPanel getListView() {
		return listView;
	}

	public void setListView(ListViewPanel listView) {
		this.listView = listView;
	}

	public void setSoraBody(SoraBody soraBody) {
		this.soraBody = soraBody;
	}
	
	
}
