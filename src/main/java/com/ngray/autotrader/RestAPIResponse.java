package com.ngray.autotrader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.ngray.autotrader.exception.SessionException;

public final class RestAPIResponse {
	
	private final HttpResponse response;
	
	public RestAPIResponse(HttpResponse response) {
		this.response = response;
	}
	
	public HttpResponse getRawHttpResponse() {
		return response;
	}
	
	public List<Header> getHeaders() {
		assert (response != null);
		return Collections.unmodifiableList(Arrays.asList(response.getAllHeaders()));
	}
	
	public String getResponseBodyAsJson() throws SessionException {
		assert (response != null);
		try {
			String json = "";
			InputStream stream = response.getEntity().getContent();
			
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					json += line;
				}
			}
			return json;
		} catch (UnsupportedOperationException |IOException e) {
			throw new SessionException(e);
		}
		
	}

}
