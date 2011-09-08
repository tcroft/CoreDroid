package com.coredroid.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import android.webkit.WebView;

public class MiscUtil {

	private static SecureRandom random = new SecureRandom();
	
	{
		random.setSeed(System.currentTimeMillis());
	}
	
	public static double random() {
		return random.nextDouble();
	}
	
	public static <T extends Object> void scramble(List<T> list) {
		
		List<T> tmpList = new ArrayList<T>();
		for (int i = 0; i < 2; i++) {
			tmpList.clear();
			tmpList.addAll(list);
			list.clear();
			
			while (tmpList.size() > 0) {
				list.add(tmpList.remove((int)(tmpList.size()*random())));
			}
		}
	}
	
	public final static void webViewLoadData(WebView web, String html) {
        StringBuilder buf = new StringBuilder(html.length());
        for (char c : html.toCharArray()) {
            switch (c) {
              case '#':  buf.append("%23"); break;
              case '%':  buf.append("%25"); break;
              case '\'': buf.append("%27"); break;
              case '?':  buf.append("%3f"); break;                
              default:
                buf.append(c);
                break;
            }
        }
        web.loadData(buf.toString(), "text/html", "utf-8");
    }
	
}
