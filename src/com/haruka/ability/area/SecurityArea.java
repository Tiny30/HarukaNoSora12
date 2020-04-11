package com.haruka.ability.area;

import java.io.File;

import com.haruka.ability.area.utils.MyVoiceC;
import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.FaceTools;

//========================================安全区域开始==================================
public class SecurityArea implements BrainArea {

	private ArtificialBrain brain;

	private static MyVoiceC mv;

	public static final String MASTER_NAME = "星野";

	public static final String MASTER_GROUP_NAME = "Master";

	public static final int NORMAL = 30000;

	public static final int WAIT_REGISTER = 30004;

	public static final int WAIT_RECONGNITION = 30005;
	/**
	 * 主人识别
	 */
	public static final int MASTER_FACE_RECOGNITION = 30001;

	public static final int MASTER_FACE_REGISTER = 30002;

	public static final int CONFIRM_STATUS = 30003;

	public static final int MASTER_VOICE_RECONGNITION = 30006;

	public static final int MASTER_VOICE_REGISTER = 30007;

	public static final int WAIT_MASTER_OPEARTE = 30008;
	private int status;

	public SecurityArea(ArtificialBrain brain) {
		this.brain = brain;
		this.status = SecurityArea.WAIT_MASTER_OPEARTE;
		this.mv = new MyVoiceC(brain);
	}

	// 身份验证的面部信息临时文件
	public static final String VERIFY_IMG_PATH = "imgs/captureImgs/veriyImg/temp.jpg";

	// 保存所有待处理脸部图片的文件夹
	public static final String CAPTURE_IMAGE_STORAGE = "imgs/captureImgs/";

	public void handleRecongnitionOrder(String order) {
		System.out.println("安全模式::::::::::" + order);
		if (order.matches(".*?回到正常模式.*?")) {
			this.setStatus(WAIT_MASTER_OPEARTE);
			this.brain.setStatus(ArtificialBrain.NORMAL_MODEL);
//			BrainAreaTools.speakFirstAndDisplayAsList(this, "测试又炸了？", "吾之召唤者就这么弱吗...", "身为侍从的我很伤心啊");
			BrainAreaTools.displayAsList(this, "测试又炸了？", "吾之召唤者就这么弱吗...", "身为侍从的我很伤心啊");
			return;
		}
		if(this.getBrain().getLogicArea().judgeOrderFalse(order)){
			this.setStatus(WAIT_MASTER_OPEARTE);
			BrainAreaTools.speakFirstAndDisplayAsList(this, "请选择注册还是验证");
			return ;
		}
		switch (this.status) {
		case WAIT_MASTER_OPEARTE:
			System.out.println("===========等待主人操作");
			if (order.matches(".*?注册.*?")) {
				this.setStatus(SecurityArea.WAIT_REGISTER);
				BrainAreaTools.speakFirstAndDisplayAsList(this, "侍从伊卡洛斯等待召唤 ", "请与我缔结契约吧_(:3 」∠)_", " ( • ω • ).人脸注册",
						"ヾ(=^▽^=)ノ 声音注册");
			} else if (order.matches(".*?验证.*?")) {
				this.setStatus(SecurityArea.WAIT_RECONGNITION);
				BrainAreaTools.speakFirstAndDisplayAsList(this, " ", " ( • ω • ).人脸识别", "ヾ(=^▽^=)ノ 声纹验证");
			}
			break;
		case SecurityArea.WAIT_REGISTER:
			if (order.matches(".*?(开始)?[脸面]部(信息)?[采收]集(信息)?(开始)?|.*?人脸注册.*?")) {
				this.status = SecurityArea.MASTER_FACE_REGISTER;
				BrainAreaTools.speakFirstAndDisplayAsList(this, "颜之契约缔结开始...", "请保持光线充足", "并喊下茄子 (≖ ‿ ≖)✧");
			} else if (order.matches(".*?(开始)?声[音纹]信息[采收]集(开始)?|.*?声音注册.*?")) {
				BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "声之契约缔结开始...", "请吟唱下面五段咒文！");
				new Thread() {
					public void run() {
						if (mv.queryMode()) {
							SecurityArea.this.mv.deleteMode();
						}
						BrainAreaTools.cleanAtOnce(SecurityArea.this, mv.getPassword());
						SecurityArea.this.mv.register();
					};
				}.start();
			}
			break;
		case SecurityArea.WAIT_RECONGNITION:
			if (order.matches(".*?(开始)?[脸面]部(信息)?(校验|比对|认证|验证|注册)(开始)?|.*?人脸识别.*?")) {
				this.status = SecurityArea.MASTER_FACE_RECOGNITION;
				BrainAreaTools.speakFirstAndDisplayAsList(this, "请面朝摄像头", "确认拍照吧", "我来呲两眼");
			} else if (order.matches(".*?(开始)?声[音纹](信息)?(校验|比对|认证|验证|注册)(开始)?|.*?声纹验证.*?")) {
				System.out.println("声音验证开始");
//				new Thread() {
//					@Override
//					public void run() {
						BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "请吟唱下面一段咒文");
						SecurityArea.this.mv.verify();
//					}
//				}.start();
			}
			break;
		case SecurityArea.MASTER_FACE_REGISTER:
			if (brain.getLogicArea().judgeTakePohtoTrue(order)) {
				new Thread() {
					public void run() {
						System.out.println("收集" + order);
						brain.getOwner().captureImage(SecurityArea.CAPTURE_IMAGE_STORAGE, true);
					};
				}.start();
			} else if (brain.getLogicArea().judgeTakePhotoFalse(order)) {
				new Thread() {
					public void run() {
						BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "收集完毕", "辛苦了", "下面开始创建Group");
						if (FaceTools.getGroupInfo(MASTER_GROUP_NAME) == FaceTools.NOT_FOUND) {
							FaceTools.createGroup(MASTER_GROUP_NAME);
						}

						BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "辛苦了", "下面开始录入信息", "正在尝试连接英灵殿...");
						boolean success = false;
						for (File img : new File(CAPTURE_IMAGE_STORAGE).listFiles()) {
							if (img.isDirectory()) {
								continue;
							}
							System.out.println("geiFaceid" + "?" + img.getAbsolutePath());
							String faceId = null;
							try {
								faceId = FaceTools.getFaceId(img.getAbsolutePath());
							} catch (IndexOutOfBoundsException e) {
								BrainAreaTools.displayAsList(SecurityArea.this, "尚未解析到面部信息");
								continue;
							}
							System.out.println(faceId);
							if (FaceTools.createPerson(MASTER_NAME, faceId, "master",
									MASTER_GROUP_NAME) == FaceTools.HAS_EXITED) {
								FaceTools.addFaceToPerson(faceId, MASTER_NAME);
							}
							BrainAreaTools.displayAsList(SecurityArea.this, "解析成功");
							success = true;
						}
						if (success) {
							BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "录入完毕", "开始验证", "请面向镜头",
									"茄子!");
							SecurityArea.this.setStatus(SecurityArea.MASTER_FACE_RECOGNITION);
						} else {
							BrainAreaTools.speakFirstAndDisplayAsList(SecurityArea.this, "录入失败", "可能是你脸比较黑");
							SecurityArea.this.setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
						}
						// for (File img : new
						// File(CAPTURE_IMAGE_STORAGE).listFiles()) {
						// img.delete();
						// }
					};
				}.start();
			}
			break;

		case SecurityArea.MASTER_VOICE_REGISTER:
			// this.mv.deleteMode(); // 删除模型
			System.out.println(mv.getPassword());
			this.mv.verify();
			break;

		case SecurityArea.MASTER_FACE_RECOGNITION:
			BrainAreaTools.displayAsList(this, "开始验证颜之契约");
			if (brain.getLogicArea().judgeTakePohtoTrue(order)) {
				brain.getOwner().captureImage(SecurityArea.VERIFY_IMG_PATH, false);
				try {
					if (FaceTools.verifyPerson(FaceTools.getFaceId(VERIFY_IMG_PATH),
							MASTER_NAME) == FaceTools.SUCCESS) {
						BrainAreaTools.speakFirstAndDisplayAsList(this, "你就是我的master吗？" + MASTER_NAME);
						brain.setStatus(ArtificialBrain.NORMAL_MODEL);
					} else {

						BrainAreaTools.speakFirstAndDisplayAsList(this, "抱歉", "英灵殿中尚未有你的信息");
					}
				} catch (IndexOutOfBoundsException e) {
					BrainAreaTools.speakFirstAndDisplayAsList(this, "抱歉", "你脸太黑了，我看不清 ");
					this.setStatus(WAIT_RECONGNITION);
					return;
				}
				this.setStatus(SecurityArea.NORMAL);
			}
			break;

		case SecurityArea.MASTER_VOICE_RECONGNITION:
			mv.verify();
			break;
		}
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public ArtificialBrain getBrain() {
		return this.brain;
	}
}

// ========================================安全区域结束==================================
