package com.ngray.autotrader;

import java.io.IOException;

import java.util.logging.Level;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ngray.autotrader.exception.SessionException;

/**
 * class RestAPIGet implements RestAPIAction
 * Executes an HttpGet request over the supplied Session.
 * The request is passed to the constructor
 * @author nigelgray
 *
 */
public final class RestAPIGet implements RestAPIAction {
	
	private final String request;
	
	public RestAPIGet(String request) {
		this.request = request;
	}

	@Override
	public RestAPIResponse execute(Session session) throws SessionException {
		try {
			final HttpGet get = new HttpGet(getUrl(session.getIsLive()));		
			session.getSessionHeaders().forEach(get::addHeader);				
			final HttpClient client = HttpClientBuilder.create().build();
			final HttpResponse response = client.execute(get);
			StatusLine statusLine = response.getStatusLine();
			switch (statusLine.getStatusCode()) {
				case HttpStatus.SC_OK:				// 200	
					AutoTrader.getLogger().log(Level.INFO, "Request " + getUrl(session.getIsLive()) + " successful");
					return new RestAPIResponse(response);
				default:
					throw new SessionException("Request " + getUrl(session.getIsLive()) + " failed: " + statusLine.toString());
			}
		} catch (IOException e) {
			throw new SessionException(e);
		}
	}
	
	private String getUrl(boolean isLive) {
		return SessionConstants.getProtocol() + SessionConstants.getGateway(isLive) + request;
	}

}
