package com.haruka.ability.area;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.haruka.ability.impl.ArtificialBrain;
import com.haruka.download.BaiduParser;
import com.haruka.download.vo.BaiduSearchItem;
import com.haruka.download.vo.SearchItem;
import com.haruka.tools.BrainAreaTools;
import com.haruka.tools.ChineseTools;
import com.haruka.tools.FormatTools;
import com.haruka.tools.HttpTools;

// ========================================查询区域开始==================================
public class SearchArea implements BrainArea{

	private ArtificialBrain brain;

	public static final String SEARCH_ENGINE_URL = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=0&rsv_idx=1&tn=baidu&wd=key&rsv_pq=aff8313800017464&rsv_t=eb32ZPREnftn4UL1yolNSI4pkdFHkDN4%2FtpO8qqDcCltAeEAc8RYx7BUoco&rqlang=cn&rsv_enter=1&rsv_sug3=4&rsv_sug1=3&rsv_sug7=100&rsv_sug2=0&inputT=642&rsv_sug4=1816";

	public static final String SEARCH_HOME_PAGEL = "https://www.baidu.com";
	
	public static final String BAIDU_SEARCH_ITEMS_KEY = "baidu_search_items_key";
	
	
	public static final int NORMALE = 0 ;
	
	public static final int WAIT_SELECT = 1 ;
	
	private int status ;
	
	private URL prevPage;

	private URL nextPage;

	public SearchArea(ArtificialBrain brain) {
		this.brain = brain;
		this.status = SearchArea.NORMALE ;
		Map<String, String> mapper = new HashMap<>();
		mapper.put("第一条", SearchArea.SEARCH_HOME_PAGEL);
		this.brain.hmset(SearchArea.BAIDU_SEARCH_ITEMS_KEY, mapper);
	}

	public void handleSearchOrder(String order) {
		System.out.println("搜索模式");

		String matchOrder = ChineseTools.getPingYin(order);
		if (order.matches(".*?关闭浏览器.*?")) {
			try {
				Runtime.getRuntime().exec("wmic process where name=\"chrome.exe\" call terminate");
				Runtime.getRuntime().exec("wmic process where name=\"360安全浏览器.exe\" call terminate");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			brain.setStatus(ArtificialBrain.NORMAL_MODEL);
			return;
		}
		switch(this.status){
		case SearchArea.NORMALE:
			
				try {
					this.openBrowser(new URL(SearchArea.SEARCH_ENGINE_URL.replaceFirst("&wd=key", "&wd=" + order)));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					BrainAreaTools.speakFirstAndDisplayAsList(this, "网址错误");
				}
			
			break ;
		case SearchArea.WAIT_SELECT:
			if (matchOrder.matches(".*?(xiayiye|xiaye).*?")) {
				this.openBrowser(nextPage);
			} else if (matchOrder.matches(".*?(shangyiye|shangyan).*?")) {
				this.openBrowser(prevPage);
			}else if(order.matches("第.条")){
				try {
					this.openBrowser(new URL(this.brain.hget(SearchArea.BAIDU_SEARCH_ITEMS_KEY, order)));
				} catch (MalformedURLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					BrainAreaTools.speakFirstAndDisplayAsList(this, "网址错误");
				}
			} 
			break ;
		}
		
		
	}

	public void openBrowser(URL url) {
		if (url == null) {
			return;
		}
		try {
			Desktop.getDesktop().browse(url.toURI());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.parsePage(url);
		this.setStatus(SearchArea.WAIT_SELECT);
		
	}

	private void parsePage(URL url) {
		System.out.println(url);
		InputStream input = HttpTools.getURLConnectionInputStream(url, null);
		if (input == null) {
			return;
		}
		String html = HttpTools.getHtml(input);
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.nextPage = null;
		this.prevPage = null;
		
		List<BaiduSearchItem> items = BaiduParser.parseBaidu(html);
		if(items.size() > 6){
			items = items.subList(0, 6) ;
		}
		List<String> contexts = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			BaiduSearchItem si = items.get(i);
			String key = "第" + FormatTools.formatIntegerToChinese(i + 1) + "条";
			String okey = "第" + (i + 1) + "条";
			contexts.add(key + "：" + si.getTitle()) ;
			this.brain.hset(SearchArea.BAIDU_SEARCH_ITEMS_KEY, key, si.getUrl());
			this.brain.hset(SearchArea.BAIDU_SEARCH_ITEMS_KEY, okey, si.getUrl());
		}
		
		BrainAreaTools.cleanAtOnce(this, contexts);
		
		Matcher ma = Pattern.compile("<a\\s.*?href=\"([^\"]+)\"[^>]*>(.*?)</a>", Pattern.CASE_INSENSITIVE)
				.matcher(html);
		System.out.println("开始解析百度网址");
		while (ma.find()) {
			String s = ma.group();
			if (s.matches("<a.*?>.*?上一页</a>")) {
				try {
					this.prevPage = new URL(SEARCH_HOME_PAGEL + s.replaceAll(".*?href=\"|\" class=\"n\">.*", ""));
					System.out.println(this.prevPage);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (s.matches("<a.*?>下一页.*?</a>")) {
				try {
					this.nextPage = new URL(SEARCH_HOME_PAGEL + s.replaceAll(".*?href=\"|\" class=\"n\">.*", ""));
					System.out.println(this.nextPage);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public ArtificialBrain getBrain() {
		return this.brain ;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}

// ========================================搜索区域结束==================================
