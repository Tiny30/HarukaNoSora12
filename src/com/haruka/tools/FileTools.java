package com.haruka.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileTools {
	
	
	public static int writeToFile(InputStream input, File file){
		int size = 0 ;
		OutputStream out = null ;
		try {
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs() ;
			}
			
			out = new FileOutputStream(file) ;
			byte[] bytes = new byte[1024] ;
			int len ;
			
			while((len = input.read(bytes)) != -1){
				out.write(bytes, 0, len);
				size += len ;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return size ;
		}finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return size ;
	}
	
	
	public static void copyFile(String srcPath, String tarPath){
		FileInputStream input = null ;
		try {
			input = new FileInputStream(srcPath) ;
			writeToFile(input, new File(tarPath)) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}
