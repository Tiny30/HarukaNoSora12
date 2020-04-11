package com.haruka.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {
	
	public static List<String> getConform(String input, String regex, int index){
		List<String> string = new ArrayList<>() ;
		int i = 0 ;
		Matcher ma = Pattern.compile(regex).matcher(input);
		while(ma.find()){
			if(i++ > index){
				break ;
			}
			string.add(ma.group());
		}
		return string ;
	}
	
	public static String getFirstConform(String input, String regex){
		return getConform(input, regex, 1).get(0);
	}
}
