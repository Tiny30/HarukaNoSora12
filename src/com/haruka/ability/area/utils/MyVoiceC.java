package com.haruka.ability.area.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haruka.ability.area.BrainArea;
import com.haruka.ability.area.SecurityArea;
import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.ability.impl.ArtificialEar;
import com.haruka.tools.BrainAreaTools;
import com.iflytek.cloud.speech.ErrorCode;
import com.iflytek.cloud.speech.SpeakerVerifier;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.VerifierListener;
import com.iflytek.cloud.speech.VerifierResult;

public class MyVoiceC implements BrainArea {
	private ArtificialBrain brain;
	private String mNumPwd = "";
	private String[] mNumPwdSegs;
	private final int PWD_TYPE_NUM = 3;
	private SpeakerVerifier mVerify;
	private int pwdType = PWD_TYPE_NUM;
	private boolean isRegistered = false;
	private boolean isModelexist = false;
	private boolean isValidated = false;
	private boolean isDeletemodel = false;
	private boolean flag = false;
	private String mAuthId = "harukazzz";

	public MyVoiceC(ArtificialBrain brain) {
		mVerify = SpeakerVerifier.createVerifier();
		this.brain = brain;
	}

	public String getPassword() {

		BrainAreaTools.displayAsList(this, "开始获取密码");

		mVerify.cancel();
		mVerify.setParameter(SpeechConstant.PARAMS, null);
		mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
		mVerify.getPasswordList(mPwdListenter);
		while (!flag) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		flag = false;
		return mNumPwd;
	}

	public boolean register() {
		try {
			BrainAreaTools.speakFirstAndDisplayAsList(this, "开始注册");
			if (pwdType == PWD_TYPE_NUM) {
				if (mNumPwd.equals("") || mNumPwd == null) {
					BrainAreaTools.speakFirstAndDisplayAsList(this, "请获取密码后进行操作");
					return false;
				}
				mVerify.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
				BrainAreaTools.cleanAtOnce(this, "请读出：" + mNumPwd.substring(0, 8));
				BrainAreaTools.displayAsList(this, "\n训练 第" + 1 + "遍，剩余4遍");
			}

			mVerify.setParameter(SpeechConstant.ISV_AUTHID, mAuthId);
			// 设置业务类型为注册
			mVerify.setParameter(SpeechConstant.ISV_SST, "train");
			// 设置声纹密码类型
			mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
			// 开始注册
			mVerify.startListening(mRegisterListener);

			while (!flag) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			flag = false;
			return isRegistered;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean queryMode() {
		// System.out.println("开始查询模型：");
		performModelOperation("que", mModelOperationListener);
		while (!flag) {
			try {
				new Thread() {

				}.start();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		flag = false;
		return isModelexist;
	}

	public boolean deleteMode() {
		performModelOperation("del", mModelOperationListener);
		while (!flag) {
			try {
				new Thread() {

				}.start();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		flag = false;
		return isDeletemodel;
	}

	public boolean verify() {
		BrainAreaTools.speakFirstAndDisplayAsList(this, "开始验证登陆");

		mVerify.setParameter(SpeechConstant.PARAMS, null);
		mVerify.setParameter(SpeechConstant.ISV_AUDIO_PATH, "./msc/verify.pcm");
		mVerify = SpeakerVerifier.getVerifier();
		// 设置业务类型为验证
		mVerify.setParameter(SpeechConstant.ISV_SST, "verify");

		if (pwdType == PWD_TYPE_NUM) {
			// 数字密码注册需要传入密码
			String verifyPwd = mVerify.generatePassword(8);
			mVerify.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
			BrainAreaTools.cleanAtOnce(this, "请读出：" + verifyPwd);
		}
		mVerify.setParameter(SpeechConstant.ISV_AUTHID, mAuthId);
		mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);
		// 开始验证
		mVerify.startListening(mVerifyListener);
		while (!flag) {
			try {
				new Thread() {

				}.start();
				;
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		flag = false;
		if (!isValidated) {
			BrainAreaTools.speakFirstAndDisplayAsList(this, "验证失败", "重新选择注册还是验证");
			this.brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
		}
		return isValidated;
	}

	public SpeechListener mPwdListenter = new SpeechListener() {
		public void onEvent(int eventType, String params) {

		}

		public void onBufferReceived(byte[] buffer) {
			String result = null;
			try {
				result = new String(buffer, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
				return;
			}

			StringBuffer numberString = new StringBuffer();
			try {
				JSONObject object = new JSONObject(result);
				if (!object.has("num_pwd")) {
					// initTextView();
					return;
				}

				JSONArray pwdArray = object.optJSONArray("num_pwd");
				numberString.append(pwdArray.get(0));
				for (int i = 1; i < pwdArray.length(); i++) {
					numberString.append("-" + pwdArray.get(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mNumPwd = numberString.toString();
			mNumPwdSegs = mNumPwd.split("-");

		}

		public void onCompleted(SpeechError error) {

			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				BrainAreaTools.displayAsList(MyVoiceC.this, "英灵殿连接出错：" + error.getErrorDescription(true));
			}
			flag = true;
		}

	};
	public SpeechListener mModelOperationListener = new SpeechListener() {

		public void onEvent(int eventType, String params) {

		}

		public void onBufferReceived(byte[] buffer) {
			String result = new String(buffer);
			try {
				JSONObject object = new JSONObject(result);
				String cmd = object.getString("cmd");
				int ret = object.getInt("ret");

				if ("del".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						BrainAreaTools.displayAsList(MyVoiceC.this, "删除成功");
						isDeletemodel = true;
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						BrainAreaTools.displayAsList(MyVoiceC.this, "删除失败，模型不存在");
						isDeletemodel = false;
					}
				} else if ("que".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						BrainAreaTools.displayAsList(MyVoiceC.this, "模型存在");
						isModelexist = true;
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						BrainAreaTools.displayAsList(MyVoiceC.this, "模型不存在");
						isModelexist = false;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void onCompleted(SpeechError error) {
			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				// System.out.println("操作失败：" +
				// error.getErrorDescription(true));
				isDeletemodel = false;
				isModelexist = false;
			}
			flag = true;
		}
	};

	public VerifierListener mRegisterListener = new VerifierListener() {

		public void onVolumeChanged(int volume) {
			// Utils.Log("当前正在说话，音量大小：" + volume);
			// updateVolume( volume );
		}

		public void onResult(VerifierResult result) {
			if (result.ret == ErrorCode.SUCCESS) {
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					BrainAreaTools.displayAsList(MyVoiceC.this, "内核异常");
					break;
				case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
					BrainAreaTools.displayAsList(MyVoiceC.this, "训练达到最大次数");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					BrainAreaTools.displayAsList(MyVoiceC.this, "出现截幅");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					BrainAreaTools.displayAsList(MyVoiceC.this, "太多噪音");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					BrainAreaTools.displayAsList(MyVoiceC.this, "录音太短");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					BrainAreaTools.displayAsList(MyVoiceC.this, "您所读的文本不一致");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					BrainAreaTools.displayAsList(MyVoiceC.this, "音量太低");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					BrainAreaTools.displayAsList(MyVoiceC.this, "音频长达不到自由说的要求");
				default:
					break;
				}

				if (result.suc == result.rgn) {
					BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "契约缔结成功");
					BrainAreaTools.cleanAtOnce(MyVoiceC.this, "请选择注册还是验证");
					isRegistered = true;
					flag = true;
					brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
					
					/*//标记
					 if (PWD_TYPE_NUM == pwdType) {
					 System.out.println("您的数字密码声纹ID：\n" + result.vid);
					} */
				} else {
					int nowTimes = result.suc + 1;
					int leftTimes = result.rgn - nowTimes;

					if (PWD_TYPE_NUM == pwdType) {
						BrainAreaTools.cleanAtOnce(MyVoiceC.this, "请吟唱：" + mNumPwdSegs[nowTimes - 1]);
					}
					BrainAreaTools.displayAsList(MyVoiceC.this, "吟唱第" + nowTimes + "遍，剩余" + leftTimes + "遍");
				}

			} else {
				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "契约缔结失败，请重新开始");
				isRegistered = false;
				flag = true;
			}
		}

		public void onError(SpeechError error) {
			if (error.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "模型已存在，请重新缔结契约", "选择验证或者注册");
				isRegistered = false;
				flag = true;
				brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
			} else {
				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "未知错误重新缔结契约", "选择验证或者注册");
				BrainAreaTools.displayAsList(MyVoiceC.this, "onError Code：" + error.getErrorCode());
				flag = true;
				brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
			}
		}

		public void onEndOfSpeech() {
			// System.out.println("结束说话");
			// System.out.println("正等待当轮结果...");
		}

		public void onBeginOfSpeech() {
			// System.out.println("开始说话");
		}

		public void onEvent(int arg0, int arg1, int arg2, Serializable arg3) {

		}
	};

	public VerifierListener mVerifyListener = new VerifierListener() {

		public void onVolumeChanged(int volume) {
			// Utils.Log("当前正在说话，音量大小：" + volume);

			// updateVolume(volume);
		}

		public void onResult(VerifierResult result) {
			if (result.ret == 0) {
				// 验证通过===================================================================================================
				// System.out.println("验证通过");

				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "你就是我的master吗？" + SecurityArea.MASTER_NAME);
				brain.setStatus(ArtificialBrain.NORMAL_MODEL);
				isValidated = true;
			} else {
				// 验证不通过
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					BrainAreaTools.displayAsList(MyVoiceC.this, "内核异常");
					break;
				case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
					BrainAreaTools.displayAsList(MyVoiceC.this, "训练达到最大次数");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					BrainAreaTools.displayAsList(MyVoiceC.this, "出现截幅");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					BrainAreaTools.displayAsList(MyVoiceC.this, "太多噪音");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					BrainAreaTools.displayAsList(MyVoiceC.this, "录音太短");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					BrainAreaTools.displayAsList(MyVoiceC.this, "您所读的文本不一致");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					BrainAreaTools.displayAsList(MyVoiceC.this, "音量太低");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					BrainAreaTools.displayAsList(MyVoiceC.this, "音频长达不到自由说的要求");
				default:
					BrainAreaTools.displayAsList(MyVoiceC.this, "验证不通过");
					break;
				}
				isValidated = false;
			}
			flag = true;
		}

		public void onError(SpeechError error) {

			switch (error.getErrorCode()) {
			case ErrorCode.MSP_ERROR_NOT_FOUND:
				brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "模型不存在，请先注册", "选择注册还是验证");
				break;
			case ErrorCode.MSP_ERROR_NET_CONNECTCLOSE:
				BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "注册失败，请重新开始", "选择注册还是验证");
				brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
				break;
			default:
				 System.out.println("onError Code：" + error.getErrorCode());
				 System.out.println(error.getErrorDescription(true));
				 BrainAreaTools.speakFirstAndDisplayAsList(MyVoiceC.this, "验证失败，请重新开始", "选择注册还是验证");
				 brain.getSecurityArea().setStatus(SecurityArea.WAIT_MASTER_OPEARTE);
				break;
			}
			isValidated = false;
			flag = true;
		}

		public void onEndOfSpeech() {
			// Utils.Log("结束说话");
			BrainAreaTools.displayAsList(MyVoiceC.this, "正在等待结果...");
		}

		public void onBeginOfSpeech() {
			// Utils.Log("开始说话");
		}

		public void onEvent(int arg0, int arg1, int arg2, Serializable arg3) {

		}
	};

	// 执行模型操作
	private void performModelOperation(String operation, SpeechListener listener) {
		// 清空参数
		mVerify.setParameter(SpeechConstant.PARAMS, null);
		mVerify.setParameter(SpeechConstant.ISV_PWDT, "" + pwdType);

		mVerify.sendRequest(operation, mAuthId, listener);
	}

	@Override
	public ArtificialBrain getBrain() {
		return this.brain;
	}
}
