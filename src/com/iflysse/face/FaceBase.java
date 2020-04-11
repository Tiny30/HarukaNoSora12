package com.iflysse.face;

import com.facepp.http.HttpRequests;

public class FaceBase {
	public final static String SERVER = "http://api.cn.faceplusplus.com";
	private static String API_KEY = "";
	private static String API_SECRET = "";
	private static HttpRequests httpRequests = null;

	public static void init(String apiKey, String apiSecret) {
		API_KEY = apiKey;
		API_SECRET = apiSecret;
		httpRequests = new HttpRequests(API_KEY, API_SECRET, true, true);
	}

	public static HttpRequests getHttpRequests() {
		if (httpRequests == null) {
			httpRequests = new HttpRequests(API_KEY, API_SECRET, true, true);
		}
		return httpRequests;
	}
}
