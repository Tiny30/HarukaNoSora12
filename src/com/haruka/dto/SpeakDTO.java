package com.haruka.dto;

import java.util.List;

public interface SpeakDTO {

	public static final int NORMAL_SPEAK = 0;

	public static final int DISPLAY_AS_LIST = 1;

	public static final int SPEAK_AND_DISPLA = 2;

	public static final int SPEAK_FIRST_AND_DISPLAY_AS_LIST = 3;

	public static final int ALARM = 4;

	public static final int CLEAN_AT_ONCE = 5;

	public static final int MASTER_CONTEXT = 6;
	public static final int DISPLAY_AS_LIST_WITH_SYNCHRONIZED = 7;

	public List<String> getContexts();

	public int getAnswerType();
}
