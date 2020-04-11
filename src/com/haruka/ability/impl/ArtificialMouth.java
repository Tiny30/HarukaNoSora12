package com.haruka.ability.impl;

import java.util.ArrayList;
import java.util.List;

import com.haruka.ability.AbilitySpeak;
import com.haruka.dto.SpeakDTO;
import com.haruka.sora.ArtificialIntelligence;
import com.iflytek.cloud.speech.GrammarListener;
import com.iflytek.cloud.speech.LexiconListener;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.iflytek.cloud.speech.SynthesizerListener;

public class ArtificialMouth extends SpeechSynthesizer implements AbilitySpeak{
	
	private  SpeechSynthesizer realMouth = SpeechSynthesizer.createSynthesizer();// 语音合成器
	
	private SynthesizerListener synthesizerListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakResumed() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {
		//	System.out.println("noBuffer:" + "arg0" + arg0 + ", " + "arg1" + arg1 + ", " + "arg1" + arg1 + ", ");
			
		}
		
		@Override
		public void onSpeakPaused() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onSpeakBegin() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onCompleted(SpeechError arg0) {
			ArtificialMouth.this.stopSpeak();
		}
		
		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
			System.out.println("arg0" + arg0 + ", " + "arg1" + arg1 + ", " + "arg1" + arg1 + ", " + "String" + arg3);
		}
	};	
	
	private ArtificialIntelligence owner ;

	public ArtificialMouth(ArtificialIntelligence owner) {
		this.owner = owner ;
		this.initSpeak();
	}
	
	@Override
	public void initSpeak() {
			
	}


	@Override
	public void duringSpeak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopSpeak() {
		this.stopSpeaking();
		this.owner.speakCompleted();
	}


	@Override
	public void startSpeak(String context) {
		this.realMouth.setParameter(SpeechConstant.SPEED, "50") ;
		this.realMouth.startSpeaking(context, this.synthesizerListener);
	}


	@Override
	public boolean destroy() {
		return this.realMouth.destroy() ;
	}


	@Override
	public boolean isSpeaking() {
		return this.realMouth.isSpeaking() ;
	}


	@Override
	public void pauseSpeaking() {
		 this.realMouth.pauseSpeaking(); 
	}


	@Override
	public void resumeSpeaking() {
		this.realMouth.resumeSpeaking();
	}


	@Override
	public void startSpeaking(String arg0, SynthesizerListener arg1) {
		this.realMouth.startSpeaking(arg0, arg1);
	}
	

	@Override
	public void stopSpeaking() {
		this.realMouth.stopSpeaking();
	}


	@Override
	public void synthesizeToUri(String arg0, String arg1, SynthesizeToUriListener arg2) {
		this.realMouth.synthesizeToUri(arg0, arg1, arg2); 
	}

	@Override
	public void setSpeaker(String speaker) {
		this.realMouth.setParameter(SpeechConstant.VOICE_NAME, speaker) ;
	}



}
