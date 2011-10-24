package com.coredroid.net;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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

    	HttpGet get = new HttpGet(url+queryString);
    	configure(get);
    	DefaultHttpClient client = new DefaultHttpClient();
		try {
		
			String contentString = client.execute(get, new BasicResponseHandler());
			return contentString;
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			LogIt.e(this, t);
		}
		
		return null;
	}
	
	protected void makeBinaryRequest(String url, File destinationFile) throws IOException {
		makeBinaryRequest(url, null, destinationFile);
	}
	
	protected void makeBinaryRequest(String url, Map<String, String> paramMap, File destinationFile) throws IOException {

		String queryString = "";
    	if (paramMap != null && paramMap.size() > 0) {
    		queryString = "?";
            for (Map.Entry<String, String> entry : paramMap.entrySet()) { 
            	queryString += URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()) + "&";
            }             
            queryString = queryString.substring(0, queryString.length()-1); // strip trailing "&"
        }

    	HttpGet get = new HttpGet(url+queryString);
    	configure(get);
    	DefaultHttpClient client = new DefaultHttpClient();
		try {
		
			client.execute(get, new LocalSavedContentResponseHandler(destinationFile));
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			LogIt.e(this, t);
		}
	}
	
	protected void configure(HttpGet get) {
		// No-op
	}
}
