package com.coredroid.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.coredroid.util.LogIt;

public class CoreServiceCall {

	protected String makeRequest(String url) throws IOException {
		return makeRequest(url, null);
	}
	
	protected String makeRequest(String url, Map<String, String> paramMap) throws IOException {

		String queryString = "";
    	if (paramMap != null && paramMap.size() > 0) {
    		queryString = "?";
            for (Map.Entry<String, String> entry : paramMap.entrySet()) { 
            	queryString += URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()) + "&";
            }             
            queryString = queryString.substring(0, queryString.length()-1); // strip trailing "&"
        }
LogIt.d(this, "QUERY: " + url + queryString);
    	HttpGet get = new HttpGet(url+queryString);
    	DefaultHttpClient client = new DefaultHttpClient();
		try {
		
			String contentString = client.execute(get, new BasicResponseHandler());
			return contentString;

		} catch (Throwable t) {
			LogIt.e(this, t);
		}
		
		return null;
	}
}
