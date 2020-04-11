package com.haruka.ability.impl;

import java.awt.Desktop;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.haruka.ability.AbilityConsider;
import com.haruka.ability.AbilityMemory;
import com.haruka.ability.area.BrainArea;
import com.haruka.ability.area.LogicArea;
import com.haruka.ability.area.MusicArea;
import com.haruka.ability.area.PCOperateArea;
import com.haruka.ability.area.SearchArea;
import com.haruka.ability.area.SecurityArea;
import com.haruka.ability.area.UtilManager;
import com.haruka.ability.area.MailManager;
import com.haruka.ability.area.MoveArea;
import com.haruka.dto.MemoryDTO;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.ChineseTools;
import com.haruka.tools.FaceTools;
import com.haruka.tools.HttpTools;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.BodyActionController;
import com.haruka.view.panel.action.ListViewActionController;
import com.iflysse.tuling.MyTuLing;

import redis.clients.jedis.Jedis;

public class ArtificialBrain extends Jedis implements AbilityConsider<String>, AbilityMemory<MemoryDTO> {

	private ArtificialIntelligence<String> owner;
	private int status;

	// 处理区域

	// 音乐区
	private MusicArea musicArea = new MusicArea(this);

	// 逻辑判断区
	private LogicArea logicArea = new LogicArea(this);

	// 身份验证区
	private SecurityArea securityArea = new SecurityArea(this);

	// 查找区域
	private SearchArea searchArea = new SearchArea(this);

	// 邮件管理器
	private MailManager mailManager = new MailManager(this);

	// 工具管理器
	private UtilManager utilManager = new UtilManager(this);

	private PCOperateArea pcOperateArea = new PCOperateArea(this);
	
	private MoveArea moveArea = new MoveArea(this);

	// 状态

	// 正常状态
	public final static int NORMAL_MODEL = 0;

	// 处理音乐状态
	public final static int MUSIC_MODEL = 1;

	// 安全模式状态
	public final static int SECURITY_MODEL = 2;

	// 查找模式
	public final static int SEARCH_MODEL = 3;

	// 邮件模式
	public final static int MAIL_MODEL = 4;

	public static final int OPERATEPC_MODEL = 5;



	// 疲劳度
	private int fatigue = 0;

	public ArtificialBrain(ArtificialIntelligence owner) {
		super("127.0.0.1", 6379);
		this.owner = owner;
		this.status = ArtificialBrain.NORMAL_MODEL;
		this.initMemory();
		this.initConsider();
	}

	@Override
	public void initConsider() {
	}

	/**
	 * 初始化音乐记忆 读取音乐文件夹下所有音乐并且生成索引表
	 */
	public void initMusicMemory() {
		Map<String, String> map = new HashMap();
		File[] files = new File(this.get(MusicArea.MUSIC_ROOT_KEY)).listFiles();
		if(files == null) {
			files = new File[0] ;
		}
		StringBuffer musicList = new StringBuffer("");
		for (File file : files) {
			musicList.append(file.getName() + "|");
			map.put(file.getName(), file.getAbsolutePath());
		}
		map.put(MusicArea.MUSIC_FILE_LIST_KEY, musicList.toString());
		this.hmset(MusicArea.ALL_MUSIC_FILES_MAPPER, map);
	}

	// 初始化记忆
	@Override
	public void initMemory() {
		this.set(MusicArea.MUSIC_ROOT_KEY, "F:\\Music");
		this.set(MusicArea.MUSIC_PLAYER_KEY, "D:\\Program Files\\tencent\\qqmusic\\QQMusic.exe");
		this.initMusicMemory();
	}

	@Override
	public void startCondsider() {
		this.duringConsider();
		this.stopCondisder();
	}

	String url = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd=key&rsv_pq=aff8313800017464&rsv_t=eb32ZPREnftn4UL1yolNSI4pkdFHkDN4%2FtpO8qqDcCltAeEAc8RYx7BUoco&rqlang=cn&rsv_enter=1&rsv_sug3=4&rsv_sug1=3&rsv_sug7=100&rsv_sug2=0&inputT=642&rsv_sug4=1816";

	@Override
	public void duringConsider() {
		String order = this.get("order");
		System.out.println("\n=======");
		System.out.println(order);
		if (order == null) {
			return;
		} else if ("".equals(order.trim())) {
			return;
		}
		if(order.matches(".*?回到正常模式.*?")){
			this.reset();
//			this.owner.speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "测试又炸了？"));
			this.owner.speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, "已回到正常模式"));
			return ;
		}
		if (this.status != ArtificialBrain.MUSIC_MODEL) {
			this.getOwner().speak(new SpeakDTOImpl(SpeakDTO.MASTER_CONTEXT, order));
		}
		if (ChineseTools.pinyinMatch(order, ".*?ma(dong|du)meizaibuzai.*?")) {
			this.owner.getMouth().setSpeaker("vixying");
			BodyActionController.changeSpeaker(this.owner.getMainInterface().getBodyPanel(), this.owner.getSoraBody(), SoraBody.SPEAKER_MADONGMEI);
			return;
		} else if (ChineseTools.pinyinMatch(order, ".*?xiaoxinzaibuzai.*?")) {
			this.owner.getMouth().setSpeaker("vixx");
			BodyActionController.changeSpeaker(this.owner.getMainInterface().getBodyPanel(), this.owner.getSoraBody(), SoraBody.SPEAKER_XIAOXIN);
			return;
		} else if (ChineseTools.pinyinMatch(order, ".*?xiao[rl]ongzaibuzai.*?")) {
			this.owner.getMouth().setSpeaker("vixr");
			BodyActionController.changeSpeaker(this.owner.getMainInterface().getBodyPanel(), this.owner.getSoraBody(), SoraBody.SPEAKER_XIAORONG);
			return;
		} else if (order.matches(".*?[迪伊一你比英]卡[洛璐个多螺老][丝斯诗车师]，?在不在.*")) {
			this.owner.getMouth().setSpeaker("xiaoyan");
			BodyActionController.changeSpeaker(this.owner.getMainInterface().getBodyPanel(), this.owner.getSoraBody(), SoraBody.SPEAKER_IKAROS);
			return;
		}

		if (order.matches(".*?最小化当前窗口.*")) {
			try {
				this.owner.getOperater().minCurExe();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.status = ArtificialBrain.NORMAL_MODEL;
			return;
		} else if (order.matches(".*?最大化当前窗口.*")) {
			try {
				this.owner.getOperater().maxCurExe();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.status = ArtificialBrain.NORMAL_MODEL;
			return;
		} else if (order.matches(".*?还原当前窗口.*")) {
			try {
				this.owner.getOperater().resetCurExe();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.status = ArtificialBrain.NORMAL_MODEL;
			return;
		} else if (order.matches(".*?关闭当前窗口.*")) {
			try {
				this.owner.getOperater().closeCurExe();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.status = ArtificialBrain.NORMAL_MODEL;
			return;
		}
		switch (this.status) {
		case ArtificialBrain.NORMAL_MODEL:
			System.out.println("正常模式");
			this.owner.getSoraBody().setAction(ArtificialIntelligence.NORMAL_ACTION);
			if (order.matches(".*?搜索.*")) {
				order = order.replaceFirst(".*?搜索", "");
				this.setStatus(ArtificialBrain.SEARCH_MODEL);
				this.searchArea.handleSearchOrder(order);
			}
			else if(order.matches(".*?你爹我帅不帅.*?")){
				BrainAreaTools.speakFirstAndDisplayAsList(this.getMusicArea(), "我爹梓杰最帅了！");
			}
			else if(order.matches(".*?讲个笑话.*?")){
				BrainAreaTools.speakFirstAndDisplayAsList(this.getMusicArea(), "有一天，我朋友跟我说:“你背上好多痣啊。”然后我就问:“写的啥？”");
			}
			else if (order.matches(".*?打开[defDEF]盘.*?")) {
				this.setStatus(ArtificialBrain.OPERATEPC_MODEL);
				this.pcOperateArea.handlePCOperate(order);
			} else if (order.matches(".*?更?换(桌面|桌面壁纸|壁纸)")) {
				try {
					Runtime.getRuntime().exec("plugins/SetWallPaper.exe");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (order.matches(".*?(播放|打开).*")) {
				System.out.println("正常播放" + order);
				order = order.replaceFirst(".*?(播放|打开)", "");
				if (order.matches("歌曲|音乐.*")) {
					this.setStatus(ArtificialBrain.MUSIC_MODEL);
					this.musicArea.handleMusicOrder(order);
				}

			} else if (order.matches(".*?下载.*")) {
				order = order.replaceFirst(".*?下载", "");
				if (order.matches("(歌曲|音乐)，?.*")) {
					this.setStatus(ArtificialBrain.MUSIC_MODEL);
					this.musicArea.setStatus(MusicArea.DOWNLOAD_MUSIC);
					this.musicArea.handleMusicOrder(order);
				}

			} else if (order.matches(".*?提醒(我)?.*?")) {
				this.utilManager.handleUtilOrder(order);
			} else if (order.matches(".*?关闭.*")) {
				if (order.matches(".*?关闭音乐.*")) {
					this.musicArea.setStatus(MusicArea.CLOSE);
					this.musicArea.handleMusicOrder("关闭");
				}
				else if (order.matches(".*?关闭电脑.*")) {
//					this.musicArea.setStatus(MusicArea.CLOSE);
//					this.musicArea.handleMusicOrder("关闭");
					try {
						Runtime.getRuntime().exec("C:\\WINDOWS\\System32\\shutdown.exe -s -t 0");
					} catch (IOException e) {
						BrainAreaTools.speakFirstAndDisplayAsList(this.getMusicArea(), "请手动关机","");
						System.out.println("请手动关机");
					}
				}
			} else if (order.matches(".*?发(送)?邮件.*?")) {
				this.setStatus(ArtificialBrain.MAIL_MODEL);
				System.out.println("进入邮件模式");
				this.mailManager.handleMailOrder(order);
			} else if (order.matches(".*?(快去)?休息(一)?下吧[！。]?")) {
				System.out.println("关闭");
				this.owner.setIsActive(false);
				ArtificialBrain.this.setStatus(ArtificialBrain.NORMAL_MODEL);
				ArtificialBrain.this.securityArea.setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
				new Thread(()->{
					ListViewActionController.disApperance(this.owner.getListView());
					BodyActionController.disMiss(this.owner.getSoraBody());
				}).start();
			}else if(order.matches(".*?往.{1,2}边[移区去].*?")){
				System.out.println("移动模式");
				//this.moveArea.handleMoveOrder(order);
				
			} else {
				final String turingOrder = order;
				new Thread() {
					public void run() {
						System.out.println("交于图灵");
						com.iflysse.tuling.results.TuLingResult Tr = MyTuLing.chat(turingOrder);
						switch (Tr.getCode()) {
						case MyTuLing.TULING_CODE_TEXT:
							BrainAreaTools.speakFirstAndDisplayAsList(ArtificialBrain.this.musicArea, Tr.getText());
							break;
						case MyTuLing.TULING_CODE_LINK:
							try {
								Desktop.getDesktop().browse(new URI(Tr.getUrl()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;

						}
					};
				}.start();
			}
			break;
		case ArtificialBrain.MUSIC_MODEL:
			if (order.matches("(播放|打开)?下一[首个曲](歌曲)?")) {
				this.musicArea.handleMusicOrder("随机播放");
			} else if (order.matches("关闭音乐.*")) {
				this.musicArea.setStatus(MusicArea.CLOSE);
				this.musicArea.handleMusicOrder("关闭");
			} else {
				this.musicArea.handleMusicOrder(order);
			}
			break;
		case ArtificialBrain.SECURITY_MODEL:
			System.out.println("安全模式");
			this.securityArea.handleRecongnitionOrder(order);
			break;
		case ArtificialBrain.SEARCH_MODEL:
			System.out.println("搜索模式");
			this.searchArea.handleSearchOrder(order);
			break;
		case ArtificialBrain.MAIL_MODEL:
			System.out.println("邮件模式");
			this.mailManager.handleMailOrder(order);
			break;
		case ArtificialBrain.OPERATEPC_MODEL:
			System.out.println("操作电脑模式");
			this.pcOperateArea.handlePCOperate(order);
			break;
		}
		System.out.println("=======\n");
	}

	@Override
	public void stopCondisder() {
		this.owner.considerCompleted();
	}

	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public void startMemory(MemoryDTO memory) {
		this.set(memory.getMemoryIndex(), memory.getMemoryContext());
		this.owner.memoryCompleted();
	}

	@Override
	public void duringMemory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopMemory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWaitForSelect() {
		if (this.musicArea.getStatus() == MusicArea.WAIT_FOR_SELECT_TO_PLAY) {
			return true;
		}
		return false;
	}

	public void initStatus() {
		this.setStatus(ArtificialBrain.NORMAL_MODEL);
		this.musicArea.setStatus(MusicArea.NORMAL);
	}

	public ArtificialIntelligence<String> getOwner() {
		return owner;
	}

	public void setOwner(ArtificialIntelligence<String> owner) {
		this.owner = owner;
	}

	public MusicArea getMusicArea() {
		return musicArea;
	}

	public void setMusicArea(MusicArea musicArea) {
		this.musicArea = musicArea;
	}

	public LogicArea getLogicArea() {
		return logicArea;
	}

	public void setLogicArea(LogicArea logicArea) {
		this.logicArea = logicArea;
	}

	public SecurityArea getSecurityArea() {
		return securityArea;
	}

	public void setSecurityArea(SecurityArea securityArea) {
		this.securityArea = securityArea;
	}

	public SearchArea getSearchArea() {
		return searchArea;
	}

	public void setSearchArea(SearchArea searchArea) {
		this.searchArea = searchArea;
	}
	
	
	public void reset() {
		this.setStatus(ArtificialBrain.NORMAL_MODEL);
		this.musicArea.setStatus(MusicArea.NORMAL);
		this.securityArea.setStatus(SecurityArea.NORMAL);
		this.mailManager.setStatus(MailManager.NORMAL);
	}

}

/**
 * 正常区域
 * 
 * @author haru
 *
 */
class NormalArea {
	/**
	 * 正常状态
	 */
	public static final int NORMAL = 10000;
	private int status;

	public NormalArea() {
		this.status = this.NORMAL;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void handleNormalOrder(String order) {

	}
}
