package com.haruka.download;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.haruka.download.vo.BaiduSearchItem;

public class BaiduParser {
	
	public static List<BaiduSearchItem> parseBaidu(String html){
		List<BaiduSearchItem> items = new ArrayList<>();
		String regex = "<h3.*?class=\"t.*?\".*?>.*?</h3>" ;
		String regx = "href *= *.*?<em>.*?</em>.*?</a>" ;
		Matcher ma = Pattern.compile(regex).matcher(html);
		while(ma.find()){
			String h3 = ma.group();
			Matcher mm = Pattern.compile(regx).matcher(h3);
			 mm.find();
			 String itemContext = mm.group();
			 BaiduSearchItem item = new BaiduSearchItem();
			 item.setTitle(itemContext.replaceAll("href.*?>", "").replaceAll("<em>|</em>|</a>", ""));
			 item.setUrl(itemContext.replaceAll(" *?target.*", "").replaceAll("\"", "").replaceAll("href *= *",""));
			items.add(item);
		}
		return items ;
	}
}
