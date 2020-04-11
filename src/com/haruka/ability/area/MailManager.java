package com.haruka.ability.area;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.rmi.CORBA.Util;

import com.haruka.ability.area.utils.MailSender;
import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.ChineseTools;

public class MailManager implements BrainArea {

	public static final String MAIL_CONNECTOR_KEY = "mailConnectors";

	public static final String MAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	public static final String QQ_MAIL = "@qq.com";

	public static final String WY_MAIL = "@163.com";

	public static final String MAILBOX_SERVER_KEY = "mailBoxServer";

	public static final String MAILBOX_PORT_KEY = "mailBoxPort";

	public static final String MAILBOX_USERNAME_KEY = "mailUserName";

	public static final String MAILBOX_USERPWD_KEY = "mailUserPwd";

	public static final String MAILBOX_USER_REGISTER_NAME_KEY = "mailUserRegisterName";
	public static final int NORMAL = 2000;

	public static final int SEND_MAIL = 2001;

	public static final int MAIL_CONNECTORS_REGISTER = 2002;

	public static final int INPUT_MAIL_CONNECTOR_NAME = 2003;

	public static final int INPUT_MAIL_SUBJECT = 2004;

	public static final int INPUT_MAIL_CONTENT = 2005;

	public static final int CONFIRM_CONMECTOR_REGISTER = 2006;

	private ArtificialBrain brain;

	private MailSender.Builder mailBuilder;

	private int status;

	public MailManager(ArtificialBrain brain) {
		super();
		this.brain = brain;
		this.brain.set(MailManager.MAILBOX_SERVER_KEY, "smtp.163.com");
		this.brain.set(MailManager.MAILBOX_PORT_KEY, "25");
		this.brain.set(MailManager.MAILBOX_USERNAME_KEY, "18895635584@163.com");
		this.brain.set(MailManager.MAILBOX_USERPWD_KEY, "55230asdf");
		this.brain.set(MailManager.MAILBOX_USER_REGISTER_NAME_KEY, "18895635584");
		this.init();
	}

	public void init() {
		this.status = MailManager.NORMAL;
		this.mailBuilder = new MailSender.Builder().setServer(this.brain.get(MailManager.MAILBOX_SERVER_KEY))
				.setPort(this.brain.get(MailManager.MAILBOX_PORT_KEY))
				.setUserName(this.brain.get(MailManager.MAILBOX_USERNAME_KEY))
				.setFormName(this.brain.get(MailManager.MAILBOX_USER_REGISTER_NAME_KEY))
				.setUserPwd(this.brain.get(MailManager.MAILBOX_USERPWD_KEY));
		Map<String, String> mapper = new HashMap<>();
		mapper.put("徐胜", "1196756653@qq.com");
		this.brain.hmset(MAIL_CONNECTOR_KEY, mapper);
	}

	public void handleMailOrder(String order) {
		if (ChineseTools.pinyinMatch(order, ".*?tuichuyoujian(bianxie|moshi).*?")) {
			this.setStatus(MailManager.NORMAL);
			this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
			return;
		}
		switch (this.status) {
		case MailManager.NORMAL:
			System.out.println("邮件--正常模式");
			if (order.matches(".*?发(送)?邮件(给)?.*?")) {
				order = order.replaceAll(".*?发(送)?邮件(给)?", "");
				this.setStatus(MailManager.INPUT_MAIL_CONNECTOR_NAME);
				this.handleMailOrder(order);
				return;
			}
			String connectors = null;
			if (this.brain.hgetAll(MailManager.MAIL_CONNECTOR_KEY).size() > 0) {

			} else {
				SpeakDTOImpl speakDTO = new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST);
				speakDTO.addContext("尚未找到联系人");
				speakDTO.addContext("请先添加联系人,依次告诉我:");
				speakDTO.addContext("联系人的姓名");
				speakDTO.addContext("联系人邮箱类型");
				speakDTO.addContext("联系人的用户名");
				this.brain.getOwner().speak(speakDTO);
				this.setStatus(MailManager.MAIL_CONNECTORS_REGISTER);
			}

			break;
		case MailManager.MAIL_CONNECTORS_REGISTER:
			if (!order.matches(".*?(QQ|网易|腾讯|163)邮箱[0-9]{1,}")) {
				this.brain.getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "格式输入错误"));
				return;
			}
			String connectorName = order.replaceAll("(QQ|网易|腾讯|163)邮箱.*", "");
			if (this.brain.hget(MailManager.MAIL_CONNECTOR_KEY, connectorName) != null) {
				this.brain.getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, "该联系人已存在"));
				return;
			}
			String mailExt = null;
			if (order.matches(".*?(QQ|qq|腾讯)邮箱.*?")) {
				mailExt = MailManager.QQ_MAIL;
			} else if (order.matches(".*?(网易|163)邮箱.*?")) {
				mailExt = MailManager.WY_MAIL;
			}
			String userName = order.replaceAll(".*?(QQ|qq|网易|腾讯|163)邮箱", "");
			if (this.brain.hset(MAIL_CONNECTOR_KEY, connectorName, userName + mailExt) == 1) {
				BrainAreaTools.speakFirstAndDisplayAsList(this, "联系人添加成功");
				this.setStatus(MailManager.NORMAL);
			} else {
				BrainAreaTools.speakFirstAndDisplayAsList(this, "联系人添加失败");
			}
			break;
		case MailManager.INPUT_MAIL_CONNECTOR_NAME:
			String receient = this.brain.hget(MailManager.MAIL_CONNECTOR_KEY, order);
			if (receient == null) {
				BrainAreaTools.speakFirstAndDisplayAsList(this, "未查找到该联系人，是否需要添加");
				this.setStatus(MailManager.CONFIRM_CONMECTOR_REGISTER);
				return;
			}
			System.out.println("联系人" + receient);
			this.mailBuilder.setToName(receient);
			BrainAreaTools.speakFirstAndDisplayAsList(this, "请输入主题");
			this.setStatus(MailManager.INPUT_MAIL_SUBJECT);
			break;
		case MailManager.INPUT_MAIL_SUBJECT:
			this.mailBuilder.setSubject(order);
			BrainAreaTools.speakFirstAndDisplayAsList(this, "请输入内容");
			this.setStatus(MailManager.INPUT_MAIL_CONTENT);
			break;
		case MailManager.INPUT_MAIL_CONTENT:
			if (order.matches(".*?(关闭邮件编写|可以开始发送了|(我)?写[好完]了).*?")) {
					new Thread(()->{
						try {
						BrainAreaTools.speakFirstAndDisplayAsList(this, "开始发送");
						this.mailBuilder.create().sendMail();
						BrainAreaTools.speakFirstAndDisplayAsList(this, "发送成功", "我原以为你会失败。。。");
					} catch (AddressException e) {
						e.printStackTrace();
						BrainAreaTools.speakFirstAndDisplayAsList(this, "邮件地址错误");
						
					} catch (MessagingException e) {
						e.printStackTrace();
						BrainAreaTools.speakFirstAndDisplayAsList(this, "邮件发送失败，可能是被当成垃圾邮件了", "很符合你的气质嘛");
					}
					}).start();

				this.setStatus(MailManager.NORMAL);
				this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
			} else {
				this.mailBuilder.setContext(order);
			}
			break;
		case MailManager.CONFIRM_CONMECTOR_REGISTER:
			if (this.brain.getLogicArea().judgeOrderTrue(order)) {
				BrainAreaTools.speakFirstAndDisplayAsList(this, "请依次告诉我", "联系人的姓名", "联系人的邮箱", "联系人的用户名");
				this.setStatus(MailManager.MAIL_CONNECTORS_REGISTER);
			}
			if (this.brain.getLogicArea().judgeOrderFalse(order)) {
				this.setStatus(MailManager.NORMAL);
			}
			break;
		}

	}

	public ArtificialBrain getBrain() {
		return brain;
	}

	public void setBrain(ArtificialBrain brain) {
		this.brain = brain;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
