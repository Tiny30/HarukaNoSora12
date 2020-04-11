package com.haruka.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTools {
	public static InputStream getURLConnectionInputStream(String url, String cookies) {
		try {
			return getURLConnectionInputStream(new URL(url), cookies) ;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
	public static InputStream getURLConnectionInputStream(URL url, String cookies) {
		try {
			
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(true);
			urlConn.setReadTimeout(5000);
			urlConn.setRequestMethod("GET");
			urlConn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
//		urlConn.setChunkedStreamingMode(51200);

			if(cookies != null ){
				urlConn.setRequestProperty("Cookie", cookies);
			}
			return urlConn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	
	

	public static String getHtml(InputStream input) {
		if(input == null){
			return null ;
		}
		StringBuffer str = new StringBuffer("");
		try {

			InputStreamReader isr = new InputStreamReader(input);

			BufferedReader buffer = new BufferedReader(isr);
			String inputLine = null;

			while ((inputLine = buffer.readLine()) != null) {
				str.append(inputLine);
			}
			buffer.close();
			isr.close();
		} catch (Exception e) {
			
		}
		return str.toString();
	}

}
