package com.haruka.ability.impl;

import java.util.Scanner;

import org.json.JSONTokener;

import com.haruka.ability.AbilityListen;
import com.haruka.dto.MemoryDTO;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.MemoryImpl;
import com.haruka.dto.impl.SpeakDTOImpl;
import com.haruka.json.SstResult;
import com.haruka.sora.ArtificialIntelligence;
import com.haruka.tools.JSONTools;
import com.haruka.view.TestOrderInput;
import com.iflytek.cloud.speech.GrammarListener;
import com.iflytek.cloud.speech.LexiconListener;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;

/**
 * 聆听的实现类
 * 
 * @author haru
 */
public class ArtificialEar extends SpeechRecognizer implements AbilityListen<String> {

	/**
	 * 能力的拥有者
	 */
	private ArtificialIntelligence owner;

	/**
	 * 正常说话音量
	 */
	public static int NORMAL_VOICE = 20;

	/**
	 * 延迟次数吧
	 */
	public static int DELAY_TIMES = 20;

	boolean start = false;
	int noVoiceTimes = 0;

	/**
	 * 真正的聆听者
	 */
	private SpeechRecognizer realSpeechRecognizer = SpeechRecognizer.createRecognizer();

	private RecognizerListener mRecoListener;

	/**
	 * 聆听的内容
	 */
	private StringBuffer listenContext;

	public ArtificialEar(ArtificialIntelligence owner) {
		this.owner = owner;
		this.initListen();
	}

	@Override
	public void initListen() {
		// 设置初始化参数
		this.realSpeechRecognizer.setParameter(SpeechConstant.DOMAIN, "iat");
		this.realSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		this.realSpeechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");

		this.mRecoListener = new RecognizerListener() {

			@Override
			public void onVolumeChanged(int arg0) {
				if (start == false && arg0 > ArtificialEar.NORMAL_VOICE) {
					start = true;
					noVoiceTimes = 0;
				}
				if (start && arg0 < ArtificialEar.NORMAL_VOICE) {
					noVoiceTimes++;
					if (noVoiceTimes > ArtificialEar.DELAY_TIMES) {
						start = false;
						ArtificialEar.this.stopListen();
					}
				}
				if (start && arg0 > ArtificialEar.NORMAL_VOICE) {
					noVoiceTimes = 0;
				}
				ArtificialEar.this.duringListen();
			}

			@Override
			public void onResult(RecognizerResult arg0, boolean arg1) {
				if (arg0.getResultString().trim().equals("")) {
					ArtificialEar.this.startListen();
					return;
				}
				SstResult sstr = new SstResult();
				try {

					// 解析JSON对象
					sstr = JSONTools.toObject(SstResult.class, arg0.getResultString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ArtificialEar.this.listenContext.append(sstr.getSentence().replaceAll("。", ""));

				// 如果是最后一句
				if (sstr.getLs()) {
					ArtificialEar.this.owner.listenCompleted();
					ArtificialEar.this.startListen();
				}
			}

			@Override
			public void onEvent(int arg0, int arg1, int arg2, String arg3) {
				System.out.println("onEvent");
			}

			@Override
			public void onError(SpeechError arg0) {
				ArtificialEar.this.owner.speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, "错误" + arg0.getErrorCode()));
				ArtificialEar.this.startListen();
			}

			@Override
			public void onEndOfSpeech() {
				realSpeechRecognizer.stopListening();
				System.out.println();
			}

			@Override
			public void onBeginOfSpeech() {
				System.out.println("onBeginOfSpeech");
			}
		};
	}

	@Override
	public int buildGrammar(String arg0, String arg1, GrammarListener arg2) {
		return this.realSpeechRecognizer.buildGrammar(arg0, arg1, arg2);
	}

	@Override
	public boolean destroy() {
		return this.realSpeechRecognizer.destroy();
	}

	@Override
	public boolean isListening() {
		return this.isListening();
	}

	@Override
	public int updateLexicon(String arg0, String arg1, LexiconListener arg2) {
		return this.realSpeechRecognizer.updateLexicon(arg0, arg1, arg2);
	}

	@Override
	public boolean writeAudio(byte[] arg0, int arg1, int arg2) {
		return this.realSpeechRecognizer.writeAudio(arg0, arg1, arg2);
	}

	@Override
	public void startListen() {
		switch (this.owner.getStatus()) {
		case ArtificialIntelligence.TEST_MODEL:
			TestOrderInput input = new TestOrderInput(this.owner) ;
			input.setVisible(true);
			break;
		default:
			this.listenContext = new StringBuffer("");
			this.realSpeechRecognizer.cancel();
			this.startListening(mRecoListener);
			break;
		}
	}

	@Override
	public void stopListen() {
		this.realSpeechRecognizer.stopListening();
		this.owner.listenCompleted();
	}

	@Override
	public void duringListen() {

	}

	@Override
	public String getResult() {
		return this.listenContext.toString();
	}

	@Override
	public void startListening(RecognizerListener arg0) {
		this.realSpeechRecognizer.startListening(mRecoListener);
	}

	@Override
	public void stopListening() {
		this.realSpeechRecognizer.stopListening();
	}
	
	public void setListContext(String context){
		this.listenContext = new StringBuffer(context);
	}

}
