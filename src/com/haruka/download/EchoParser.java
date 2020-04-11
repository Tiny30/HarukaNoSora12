package com.haruka.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haruka.download.vo.SearchItem;
import com.haruka.dto.EchoResp;
import com.haruka.dto.EchoSong;
import com.haruka.tools.FileTools;
import com.haruka.tools.HttpTools;

public class EchoParser {

	/**
	 * 解析查询界面
	 * 
	 * @param html
	 *            查询界面
	 * @return 返回该页所有歌曲信息（歌曲id， 歌曲名字， 歌曲图片）
	 */
	public static List<SearchItem> getSearcheItems(String html) {
//		String regex;
		final List<SearchItem> list = new ArrayList<SearchItem>();
//		regex = "<li>.*?</li>";
//		final Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
//		final Matcher ma = pa.matcher(html);
//		Matcher ama;
//		Matcher imgma;
//		Matcher hrefma ;
//		while (ma.find()) {
//			String s = ma.group();
//			if (s.contains("class=\"playbtn js-mp-play-one\"") && !s.contains("_eSearch.format_str")) {
//				ama = Pattern.compile(
//						"<a[^>]*class=\"song-name\" *href=(\"([^\"]*)\"|\'([^\']*)\'|([^\\s>]*))[^>]*>(.*?)</a>",
//						Pattern.DOTALL).matcher(s);
//				imgma = Pattern.compile("<\\s*img\\s+([^>]*)\\s*>", Pattern.DOTALL).matcher(s);
//				SearchItem si = new SearchItem();
//				if (ama.find()) {
//					// 解析歌曲id
//					 hrefma = Pattern.compile("href=\"([^\"]*\")",  2 | Pattern.DOTALL).matcher(ama.group());
//					 if(hrefma.find()){
//						 si.setSongID(hrefma.group().replaceAll("[^\\d]", ""));
//					 }
//
//					// 解析歌曲名字
//					si.setSongName(ama.group().replaceAll("<.*?>", ""));
//				}
//				if (imgma.find()) {
//
//					// 解析歌曲图片地址
//					si.setSongImgSrc(imgma.group().split("src=\"")[1].replaceAll("\">", ""));
//				}
//				list.add(si);
//			}
//		}
		EchoResp<List<EchoSong>> songs = new Gson().fromJson(html, new TypeToken<EchoResp<List<EchoSong>>>() {}.getType()) ;
		if(songs.getData() != null) {
			for (Iterator iterator = songs.getData().iterator(); iterator.hasNext();) {
				EchoSong song = (EchoSong) iterator.next();
				list.add(new SearchItem(song.getId(), song.getPic(), song.getName(),song.getSource())) ;
			}
		}
		return list;

	}
	
	public static String getSongDownloadURLWithID(String id, String cookie) {
		String path = "http://www.app-echo.com/sound/"+id ;
		InputStream input = HttpTools.getURLConnectionInputStream(path, cookie) ;
		if(input == null){
			return null ;
		}
		String html = HttpTools.getHtml(input) ;
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		return getSongDownloadURL(html) ;
	}
	
	/**
	 * 解析下载地址
	 * 
	 * @param html
	 *            传入歌曲详细信息界面的html
	 * @return
	 */
	public static String getSongDownloadURL(String html) {

		try {
			return html.split(",\"source\":")[1].split("\\};\\$\\(function\\(\\) \\{")[0].replaceAll("\\\\/", "/").replaceAll("\"", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	/**
	 * 开始下载
	 * 
	 * @param input
	 *            传入文件的输入流
	 * @param parent
	 *            传入文件的父目录
	 * @param fileName
	 *            传入文件名
	 * @return 若下载成功则返回true 否则返回false （文件名冲突返回false）
	 */
	public static boolean download(InputStream input, File parent, String fileName) {

		File storage = new File(parent, fileName);
		
		
		if (!storage.getParentFile().exists()) {
			storage.getParentFile().mkdirs();
		}

		FileTools.writeToFile(input, storage);
		return true;
	}
}
