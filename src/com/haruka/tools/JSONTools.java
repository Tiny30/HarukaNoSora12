package com.haruka.tools;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONTools {
	
	
	public static String toJSON(Object object){
		try {
			return new ObjectMapper().writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static <T> T toObject(Class<T> clazz, String json){
		try {
			return (T)new ObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
