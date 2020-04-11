package com.haruka.tools;

import java.util.List;

import com.haruka.ability.area.BrainArea;
import com.haruka.dto.SpeakDTO;
import com.haruka.dto.impl.SpeakDTOImpl;

public class BrainAreaTools {
	
	public static void speakFirstAndDisplayAsList(BrainArea area, List<String> contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, contexts));
	}
	
	public static void speakFirstAndDisplayAsList(BrainArea area, String... contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.SPEAK_FIRST_AND_DISPLAY_AS_LIST, contexts)) ;
	}
	
	public static void displayAsList(BrainArea area, String... contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, contexts));
	}
	public static void displayAsList(BrainArea area, List<String> contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST, contexts));
	}
	
	public static void cleanAtOnce(BrainArea area, String... contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.CLEAN_AT_ONCE, contexts));
	}
	public static void cleanAtOnce(BrainArea area, List<String> contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.CLEAN_AT_ONCE, contexts));
	}
	public static void displayAsListWidthSynchronized(BrainArea area, String... contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST_WITH_SYNCHRONIZED, contexts));
	}
	public static void displayAsListWidthSynchronized(BrainArea area, List<String> contexts){
		area.getBrain().getOwner().speak(new SpeakDTOImpl(SpeakDTO.DISPLAY_AS_LIST_WITH_SYNCHRONIZED, contexts));
	}
	
}
