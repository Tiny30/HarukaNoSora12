package com.haruka.ability.area;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.download.EchoParser;
import com.haruka.download.vo.SearchItem;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.ChineseTools;
import com.haruka.tools.FileTools;
import com.haruka.tools.FormatTools;
import com.haruka.tools.HttpTools;

// ========================================音乐区开始=================================
	/**
	 * 音乐区域
	 * 
	 * @author haru
	 *
	 */
	public class MusicArea implements BrainArea{

		// 音乐根目录key
		public final static String MUSIC_ROOT_KEY = "music_file_rootPath";

		// 音乐播放器目录key
		public final static String MUSIC_PLAYER_KEY = "music_player_path";

		// 音乐文件列表的key
		public final static String MUSIC_FILE_LIST_KEY = "music_file_llist";

		// 所有文件音乐文件映射key
		public final static String ALL_MUSIC_FILES_MAPPER = "all_music_files_mapper";

		// 音乐选项映射
		public final static String MUSIC_SELECTION_KEY = "music_selection_key";

		// 下载音乐文件的mapper名称
		public final static String DOWNLOAD_MUSIC_NAME_MAPPER = "download_music_name_mapper";
		
		//下载的音乐文件的地址
		public final static String DOWNLOAD_MUSIC_SRC_IN_ECHO = "download_music_src_in_echo";
		
		

		// 登录Echo需要的cookie
		public final static String ECHO_COOKIE = "PHPSESSID=hr7bpipqqfctfmd310ef3hlla2";

		// echo查找界面
		public final static String ECHO_SEARCHPAGE = "http://www.app-echo.com/api/search/sound?limit=4&src=0";

		// echo歌曲界面
		public final static String ECHO_SONGINFOPAGE = "http://www.app-echo.com/sound/";

		// 错误代码

		public final static int ERROR_NO_RES_FOUND = 25000;

		/**
		 * 正常状态
		 */
		public static final int NORMAL = 20000;

		/**
		 * 等待选择播放状态
		 */
		public static final int WAIT_FOR_SELECT_TO_PLAY = 20001;

		/**
		 * 等待下载
		 */
		public static final int WAIT_FRO_SELECT_TO_DOWNLOAD = 20002;

		/**
		 * 确认是否下载
		 */
		public static final int CONFIRM_DOWANLOAD = 20003;

		/**
		 * 下载歌曲
		 */
		public static final int DOWNLOAD_MUSIC = 20005;

		/**
		 * 关闭
		 */
		public static final int CLOSE = 20004;

		private boolean isWaitingSelection = false;

		private int status;

		private int curPage = 1;
		private int pageSize = 5;

		private List curListView;

		private List<String> curSelctionsList;

		private List<SearchItem> curDownloadList;

		private String curDownloadMusicName;

		private String curOrder;
		
		private ArtificialBrain brain ;
		
		
		
		
		

		public MusicArea(ArtificialBrain brain) {
			this.brain = brain ;
			this.status = MusicArea.NORMAL;
			Map<String, String> map = new HashMap<String, String>();
			map.put("第一首", "test");
			Map<String, String> downMapper = new HashMap<String, String>();
			downMapper.put("第一首", "test");
			this.brain.hmset(MusicArea.DOWNLOAD_MUSIC_NAME_MAPPER, downMapper);
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		/**
		 * 处理音乐指令
		 * 
		 * @param order
		 *            传入命令
		 */
		public void handleMusicOrder(String order) {
			this.brain.getOwner().getSoraBody().setAction(ArtificialIntelligence.MUSIC_ACTION);
			if(ChineseTools.pinyinMatch(order, ".*?tuichuyin(yue|le)moshi.*?")){
				this.setStatus(MusicArea.NORMAL);
				this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
				try {
					Runtime.getRuntime().exec("wmic process where name=\"QQMusic.exe\" call terminate");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return ;
			}
			if (this.isWaitingSelection) {
				System.out.println("等待选择:===" + order);
			}

			if ("随机播放".equals(order)) {
				this.playMusic(this.getRandomMusicName());
				return;
			}
			switch (this.status) {
			case MusicArea.NORMAL:
				System.out.println("正常模式");
				String musicName = order.replaceFirst(".*?(播放|打开)?音乐|歌曲，?", "");
				System.out.println("音乐名称" + musicName);
				if (musicName.trim().equals("")) {
					this.playMusic(this.getRandomMusicName());
					break;
				}
				Matcher m = Pattern.compile(".*?\\..*?\\|")
						.matcher(this.brain.hget(ALL_MUSIC_FILES_MAPPER, MUSIC_FILE_LIST_KEY));
				List<String> selections = new ArrayList();
				while (m.find()) {
					String[] s = m.group().split("\\|");
					String mn = s[s.length - 1];
					if (!ChineseTools.getPingYin(mn)
							.matches(".*?" + ChineseTools.getPingYin(musicName) + ".*?\\..*?")) {
						continue;
					}
					if (mn.matches(musicName + "\\..*?")) {
						this.playMusic(mn);
						return;
					}
					selections.add(mn);
				}
				if (selections.size() == 1) {
					this.playMusic(selections.get(0));
				} else if (selections.size() > 1) {
					this.curPage = 1;
					this.curSelctionsList = selections;
					this.showPlayList();
					BrainAreaTools.speakFirstAndDisplayAsList(this, "已找到多首歌曲，你想听哪首？");
					this.brain.setStatus(ArtificialBrain.MUSIC_MODEL);
					this.setStatus(MusicArea.WAIT_FOR_SELECT_TO_PLAY);
					this.isWaitingSelection = true;
				} else {
					BrainAreaTools.speakFirstAndDisplayAsList(this, "抱歉本地没有");
					this.curPage = 1;
					this.curDownloadMusicName = musicName;
					this.showDownloadList();
					BrainAreaTools.speakFirstAndDisplayAsList(this, "已在英灵殿找到歌曲...是否确认下载？" );
					this.setStatus(MusicArea.CONFIRM_DOWANLOAD);
				}
				break;
			case MusicArea.WAIT_FOR_SELECT_TO_PLAY:
				System.out.println("等待选择播放");
				if (order.matches("(下一页|夏夜)")) {
					if (this.nextPage()) {
						this.showPlayList();
					}
					break;
				} else if (order.matches("上一页")) {
					if (this.prevPae()) {
						this.showPlayList();
					}
					break;
				}
				System.out.println("选择的Ordergequ " + order);
				String mcName = this.brain.hget(MusicArea.MUSIC_SELECTION_KEY, order);
				if (mcName == null) {
					// this.brain.setStatus(ArtificialBrain.NORMAL);
					this.setStatus(MusicArea.NORMAL);
					return;
				}
				this.playMusic(mcName);
				break;
			case MusicArea.WAIT_FRO_SELECT_TO_DOWNLOAD:
				System.out.println("等待下载");
				if (order.matches("(下一页|夏夜)")) {
					this.curPage++;
					this.showDownloadList();

					break;
				} else if (order.matches("上一页")) {
					if (this.prevPae()) {
						this.showDownloadList();
					}
					break;
				}
				String dmcID = this.brain.hget(MusicArea.MUSIC_SELECTION_KEY, order);
				if (dmcID == null) {
					System.out.println("错误的选项");
					return;
				}

//				String url = EchoParser.getSongDownloadURLWithID(dmcID, MusicArea.ECHO_COOKIE);
				String url = brain.hget(DOWNLOAD_MUSIC_SRC_IN_ECHO, dmcID);
				
				if (url == null) {
					BrainAreaTools.speakFirstAndDisplayAsList(this, "地址解析错误");
					this.setStatus(MusicArea.NORMAL);
					return;
				}
				new Thread() {
					public void run() {
						String dmcName = brain.hget(DOWNLOAD_MUSIC_NAME_MAPPER, dmcID);
						BrainAreaTools.cleanAtOnce(MusicArea.this);
						InputStream input = HttpTools.getURLConnectionInputStream(url, MusicArea.ECHO_COOKIE);
						System.out.println(brain.get(MusicArea.MUSIC_ROOT_KEY)+"\\" + dmcName + ".mp3");
						FileTools.writeToFile(input,
								new File(brain.get(MusicArea.MUSIC_ROOT_KEY)+"\\" + dmcName + ".mp3"));
						try {
							input.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						brain.getOwner().refresh();
						BrainAreaTools.speakFirstAndDisplayAsList(MusicArea.this, "下载完成");
						MusicArea.this.setStatus(MusicArea.NORMAL);
						MusicArea.this.getBrain().setStatus(ArtificialBrain.NORMAL_MODEL);
					};
				}.start();
				this.setStatus(MusicArea.NORMAL);
				// this.brain.setStatus(ArtificialBrain.NORMAL);
				break;
			case CONFIRM_DOWANLOAD:
				System.out.println("确认下载");
				if (order.matches("(下一页|夏夜)")) {
					this.curPage++;
					this.setStatus(MusicArea.WAIT_FRO_SELECT_TO_DOWNLOAD);
					this.showDownloadList();

					break;
				} else if (order.matches("上一页")) {
					this.setStatus(MusicArea.WAIT_FRO_SELECT_TO_DOWNLOAD);
					if (this.prevPae()) {
						this.showDownloadList();
					}
					break;
				}
				if (brain.getLogicArea().judgeOrderTrue(order)) {
					this.setStatus(WAIT_FRO_SELECT_TO_DOWNLOAD);
					BrainAreaTools.speakFirstAndDisplayAsList(this, "第几首？");	
					System.out.println("等待下载" + order);
				} else if (brain.getLogicArea().judgeOrderFalse(order)) {
					BrainAreaTools.cleanAtOnce(this,"已取消下载");	
					this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
					this.setStatus(NORMAL);
				}
				break;

			case MusicArea.DOWNLOAD_MUSIC:
				System.out.println("下载音乐");
				String dmusicName = order.replaceFirst(".*?歌曲|音乐，?", "");
				this.curPage = 1;
				this.curDownloadMusicName = dmusicName;
				System.out.println("dmusicName:" + dmusicName);
				this.showDownloadList();
				BrainAreaTools.speakFirstAndDisplayAsList(this, "是否确认下载？");	
				this.setStatus(MusicArea.CONFIRM_DOWANLOAD);
				break;
			case MusicArea.CLOSE:
				try {
					Runtime.getRuntime().exec("wmic process where name=\"QQMusic.exe\" call terminate");
				} catch (IOException e) {
					e.printStackTrace();
				}
			default:
				this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
				this.setStatus(MusicArea.NORMAL);
			}

		}

		public void playMusic(String musicName) {
			System.out.println(musicName);
			System.out.println("bofangyinyue" + this.brain.hget(ALL_MUSIC_FILES_MAPPER, musicName));
			this.isWaitingSelection = false;
			try {
//				 Desktop.getDesktop().open(new
//				 File(this.brain.hget(ALL_MUSIC_FILES_MAPPER,
//				 musicName)));
				Runtime.getRuntime().exec(this.brain.get(MusicArea.MUSIC_PLAYER_KEY) + " "
						+ this.brain.hget(ALL_MUSIC_FILES_MAPPER, musicName));
			} catch (Exception e) {
				e.printStackTrace();
				this.brain.initMusicMemory();

			}
			this.brain.setStatus(ArtificialBrain.MUSIC_MODEL);
			this.setStatus(MusicArea.NORMAL);
		}

		public String getRandomMusicName() {
			String[] musics = this.brain.hget(ALL_MUSIC_FILES_MAPPER, MUSIC_FILE_LIST_KEY).split("\\|");
			Random r = new Random();
			return musics[r.nextInt(musics.length)];
		}

		public boolean nextPage() {
			if ((this.curPage + 1) * this.pageSize - this.curListView.size() <= this.pageSize) {
				this.curPage++;
				return true;
			}
			return false;
		}

		public boolean prevPae() {
			if (this.curPage - 1 > 0) {
				this.curPage--;
				return true;
			}
			return false;
		}

		public void showPlayList() {
			List<String> selections = this.curSelctionsList;
			this.curListView = selections;
			List<String> contexts = new ArrayList<>();
			for (int i = (this.curPage - 1) * this.pageSize; i < selections.size()
					&& i < this.curPage * this.pageSize; i++) {
				String key = "第" + FormatTools.formatIntegerToChinese(i + 1) + "首";
				String okey = "第" + (i + 1) + "首";
				contexts.add(key +"："+ selections.get(i)) ;
				this.brain.hset(MusicArea.MUSIC_SELECTION_KEY, key, selections.get(i));
				System.out.println(key +"======" + this.brain.hget(MusicArea.MUSIC_SELECTION_KEY, key));
				this.brain.hset(MusicArea.MUSIC_SELECTION_KEY, okey, selections.get(i));
			}
			BrainAreaTools.displayAsList(this, contexts);
		}

		public void showDownloadList() {
			System.out.println("下载音乐" + MusicArea.ECHO_SEARCHPAGE + "&keyword="
						+ URLEncoder.encode(this.curDownloadMusicName.replaceAll("[，。！？]","")));
			InputStream input = null;
			try {
				input = HttpTools.getURLConnectionInputStream(MusicArea.ECHO_SEARCHPAGE + "&keyword="
						+ URLEncoder.encode(this.curDownloadMusicName.replaceAll("[，。！？]","")) + "&page=" + this.curPage,
						MusicArea.ECHO_COOKIE);
				
			} catch (Exception e) {
				this.setStatus(MusicArea.NORMAL);
				return;
			}
			if (input == null) {
				this.error(MusicArea.ERROR_NO_RES_FOUND);
				return;
			}

			List<SearchItem> list = EchoParser.getSearcheItems(HttpTools.getHtml(input));

			if (list.size() < 0) {
				this.error(MusicArea.ERROR_NO_RES_FOUND);
				return;
			}
//			list = list.subList(0, 6) ;
			this.curDownloadList = list;
			System.out.println("下载数量===" + list.size());
			List<String> contexts = new ArrayList<>() ;
			for (int i = 0; i < list.size(); i++) {
				SearchItem si = list.get(i);
				String key = "第" + FormatTools.formatIntegerToChinese(i + 1) + "首";
				String okey = "第" + (i + 1) + "首";
				contexts.add(key + "：" + si.getSongName()) ;
				this.brain.hset(MusicArea.MUSIC_SELECTION_KEY, key, si.getSongID());
				this.brain.hset(MusicArea.MUSIC_SELECTION_KEY, okey, si.getSongID());
				this.brain.hset(MusicArea.DOWNLOAD_MUSIC_NAME_MAPPER, si.getSongID(), si.getSongName());
				this.brain.hset(MusicArea.DOWNLOAD_MUSIC_SRC_IN_ECHO, si.getSongID(), si.getSrc());
			}
			BrainAreaTools.cleanAtOnce(MusicArea.this);
			BrainAreaTools.displayAsList(this, contexts);
		}

		public void error(int code) {
			switch (code) {
			case MusicArea.ERROR_NO_RES_FOUND:
				BrainAreaTools.displayAsListWidthSynchronized(this, "尚未在英灵殿中找到资源","你可以尝试获取圣杯","不过..","我看你也没那个实力吧？");
				this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
				this.setStatus(MusicArea.NORMAL);
				break;
			}

		}

		public boolean isWaitingSelection() {
			return isWaitingSelection;
		}

		public void setWaitingSelection(boolean isWaitingSelection) {
			this.isWaitingSelection = isWaitingSelection;
		}

		public int getCurPage() {
			return curPage;
		}

		public void setCurPage(int curPage) {
			this.curPage = curPage;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public List getCurListView() {
			return curListView;
		}

		public void setCurListView(List curListView) {
			this.curListView = curListView;
		}

		public List<String> getCurSelctionsList() {
			return curSelctionsList;
		}

		public void setCurSelctionsList(List<String> curSelctionsList) {
			this.curSelctionsList = curSelctionsList;
		}

		public List<SearchItem> getCurDownloadList() {
			return curDownloadList;
		}

		public void setCurDownloadList(List<SearchItem> curDownloadList) {
			this.curDownloadList = curDownloadList;
		}

		public String getCurDownloadMusicName() {
			return curDownloadMusicName;
		}

		public void setCurDownloadMusicName(String curDownloadMusicName) {
			this.curDownloadMusicName = curDownloadMusicName;
		}

		public String getCurOrder() {
			return curOrder;
		}

		public void setCurOrder(String curOrder) {
			this.curOrder = curOrder;
		}

		public ArtificialBrain getBrain() {
			return brain;
		}

		public void setBrain(ArtificialBrain brain) {
			this.brain = brain;
		}
	}
	// ========================================音乐区结束=================================
	
	