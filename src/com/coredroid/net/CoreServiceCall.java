package com.coredroid.net;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.coredroid.util.IOUtil;
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
            	queryString += URLEncoder.encode(entry.getKey()) + "=" + URLEncoder.encode(entry.getValue()!=null?entry.getValue():"") + "&";
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
	
	public String postRequest(String url, String body) throws IOException, ParseException {

		HttpPost post = new HttpPost(url);
		configure(post);
		post.setEntity(new StringEntity(body));

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(post);

		HttpEntity entity = response.getEntity();
		if (entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
			GZIPInputStream in = new GZIPInputStream(entity.getContent());
			String str = new String(IOUtil.read(in));
			return str;
		}
		
		return EntityUtils.toString(response.getEntity());
	}	

	protected String composeRequestParams(Map<String, Object> paramMap) {
		StringBuilder builder = new StringBuilder();
		
		// Encode for shipping
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			Object value = entry.getValue();
			String valueStr = value != null ? value.toString() : "";

			valueStr = valueStr.replace("&", "&amp;");
			valueStr = URLEncoder.encode(valueStr);

			if (builder.length() > 0) {
				builder.append("&");
			}
			
			builder.append(entry.getKey()).append("=").append(valueStr);
		}
		
		return builder.length() > 0 ? "?" + builder : "";
	}
	
	protected void configure(HttpRequestBase request) {
		// No-op
	}
}
