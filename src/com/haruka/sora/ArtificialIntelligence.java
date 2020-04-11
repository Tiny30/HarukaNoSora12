package com.haruka.sora;

import java.awt.Graphics;

import javax.swing.JPanel;

import com.haruka.dto.ConsiderDTO;
import com.haruka.dto.ListenDTO;
import com.haruka.dto.MemoryDTO;
import com.haruka.ability.AbilityConsider;
import com.haruka.ability.AbilityListen;
import com.haruka.ability.AbilityOperatePC;
import com.haruka.ability.AbilitySpeak;
import com.haruka.ability.impl.ArtificialMouth;
import com.haruka.dto.CommandDTO;
import com.haruka.dto.SpeakDTO;
import com.haruka.view.MainInterface;
import com.haruka.view.panel.ListViewPanel;
import com.haruka.view.panel.SoraBody;
import com.haruka.view.panel.action.ListViewActionController;

/**
 * 人工智能接口
 * 
 * @author haru
 *
 */
public interface ArtificialIntelligence<Order> {

	public final static int TEST_MODEL = 0;

	public final static int NORMAL_MODEL = 1;

	public final static int DIR_UP = 1;
	public final static int DIR_DOWN = 2;
	public final static int DIR_LEFT = 3;
	public final static int DIR_RIGHT = 4;
	public final static int DIR_CENTER = 5;
	public final static int DIR_UPPER_LEFT = 6;
	public final static int DIR_UPPER_RIGHT = 7;
	public final static int DIR_LOWER_LEFT = 8;
	public final static int DIR_LOWER_RIGHT = 9;

	public final static int RANGE_SMALL = 10;
	public final static int RANGE_NORMAL = 20;
	public final static int RANGE_BIG = 30;

	public static double[] NORMAL_ACTION = { 1, 1.025, 1.05, 1.075, 1.1, 1.125, 1.15, 1.175, 1.2, 1.225, 1.25, 1.275,
			1.3 };
	public static double[] MUSIC_ACTION = { 1, 1.05, 1.1, 1.15, 1.2, 1.25, 1.3 };

	/**
	 * 配置初始化信息
	 */
	public void init();

	/**
	 * 刷新配置信息
	 */
	public void refresh();

	/**
	 * 记忆功能
	 * 
	 * @param memory
	 *            传入记忆数据传输对象
	 */
	public void memory(MemoryDTO memory);

	/**
	 * 停止记忆
	 */
	public void memoryCompleted();

	/**
	 * 需要完成的操作
	 * 
	 * @param cmd
	 *            操作命令
	 */
	public void operate(CommandDTO cmd);

	/**
	 * 操作完成
	 */
	public void operateCompleted();

	/**
	 * 谈话功能
	 * 
	 * @param speak
	 *            传入谈话数据传输对象
	 */
	public void speak(SpeakDTO speakContext);

	/**
	 * 谈话完成
	 */
	public void speakCompleted();

	/**
	 * 聆听功能
	 */
	public void listen();

	/**
	 * 停止聆听
	 */
	public void stopListen();

	/**
	 * 聆听完成
	 */
	public void listenCompleted();

	/**
	 * 思考功能
	 * 
	 * @param considerContext
	 */
	public void consider();

	/**
	 * 思考完成
	 */
	public void considerCompleted();

	/**
	 * 行为
	 */
	public void action();

	/**
	 * 行为完成
	 */
	public void actionCompleted();

	/**
	 * 外形
	 * 
	 * @param g
	 *            传入需要塑形的图表对象
	 */
	public void figure(Graphics g);

	/**
	 * 获得当前命令
	 * 
	 * @return 返回命令数据对象
	 */
	public Order getOrder();

	/**
	 * 开启观看
	 */
	public void startWatch();

	/**
	 * 捕捉当前观察的图像
	 */
	void captureImage(String storagePath, boolean isDirectory);

	/**
	 * 结束观察
	 */
	public void stopWatch();

	/**
	 * 观察结束
	 */
	public void watchCompleted();

	/**
	 * 获得当前的展示面板
	 * 
	 * @return
	 */
	public JPanel getDisplayPanel();

	/**
	 * 设置展示面板
	 * 
	 * @param disPlayPanel
	 *            需要展示的面板
	 */
	public void setDisplayPanel(JPanel disPlayPanel);

	/**
	 * 是否正在说话
	 * 
	 * @return 正在说话返回true 否则返回false
	 */
	public boolean isSpeaking();

	/**
	 * 设置机器是否活动
	 */
	public void setIsActive(boolean isActive);

	public AbilityOperatePC getOperater();

	public AbilityConsider getConsider();

	public int getStatus();

	public AbilityListen getEar();

	ListViewActionController getChfController();

	public void move(int direction, int range);

	void sleep();

	AbilitySpeak getMouth();

	SoraBody getSoraBody();

	void setSoraBody(SoraBody soraBody);

	MainInterface getMainInterface();

	ListViewPanel getListView();

}
