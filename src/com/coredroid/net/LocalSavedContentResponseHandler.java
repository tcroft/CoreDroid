package com.coredroid.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;

import com.coredroid.util.IOUtil;

public class LocalSavedContentResponseHandler implements ResponseHandler<File> {

	private File file;
	
	public LocalSavedContentResponseHandler(File destinationFile) {
		file = destinationFile;
	}
	
	@Override
	public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(),
					statusLine.getReasonPhrase());
		}

		file.getParentFile().mkdirs();
		
		HttpEntity entity = response.getEntity();

		InputStream input = entity.getContent();
		IOUtil.copy(input, new FileOutputStream(file));
		input.close();
		
		entity.consumeContent();
		
		return file;
	}
}
