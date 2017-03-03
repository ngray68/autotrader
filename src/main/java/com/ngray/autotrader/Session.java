package com.ngray.autotrader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.ngray.autotrader.exception.SessionException;

/**
 * Session class
 * This class is the main class of the AutoTrader application
 * @author nigelgray
 *
 */
public final class Session {
		
	private final List<Header> sessionHeaders;
	private final boolean isLive;
	
	private Session(List<Header> sessionHeaders, boolean isLive) {
		this.sessionHeaders = sessionHeaders;
		this.isLive = isLive;
	}
	
	/**
	 * Static method login
	 * Returns a valid Session object if login is successful, throws a SessionException if not
	 * @param username
	 * @param password
	 * @param encrypted (true if the password supplied is encrypted)
	 * @param isLive    (true if the required session is live, false if a demo session is required)
	 * @return a Session
	 * @throws SessionException
	 */
	public static Session login(String username, String password, boolean encrypted, boolean isLive) throws SessionException {
		
		AutoTrader.getLogger().log(Level.INFO, "Attempting login...");
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost login = new HttpPost(SessionConstants.getSessionUrl(isLive));	
		for(String[] header : SessionConstants.getHeaders(isLive)) {
			login.addHeader(header[0], header[1]);
		}
	
		String body = SessionConstants.getLoginMessageBody(username, password);
		try {
			login.setEntity(new ByteArrayEntity(body.getBytes("UTF-8")));
			HttpResponse response = client.execute(login);
			
			StatusLine statusLine = response.getStatusLine();
			switch (statusLine.getStatusCode()) {
				case HttpStatus.SC_OK:				// 200
					if (!response.containsHeader(SessionConstants.CST))
						throw new SessionException("Login attempt failed to provide a CST token in the HTTP response header");
					if (!response.containsHeader(SessionConstants.X_SECURITY_TOKEN))
						throw new SessionException("Login attempt failed to provide an X-SECURITY-TOKEN in the HTTP response header");
				
					List<Header> sessionHeaders = new ArrayList<>();
					sessionHeaders.add(response.getFirstHeader(SessionConstants.CST));
					sessionHeaders.add(response.getFirstHeader(SessionConstants.X_SECURITY_TOKEN));
				
					AutoTrader.getLogger().log(Level.INFO, "Login successful - creating new session");
					return new Session(sessionHeaders, isLive);
				default:
					throw new SessionException("Login attempt failed: " + statusLine.toString());
			}
			
		} catch (UnsupportedEncodingException e) {
			throw new SessionException(e);
		} catch (ClientProtocolException e) {
			throw new SessionException(e);
		} catch (IOException e) {
			throw new SessionException(e);
		}
	}
	
	/**
	 * Logout from the current session
	 * @throws SessionException
	 */
	public void logout() throws SessionException{
		
		AutoTrader.getLogger().log(Level.INFO, "Logging out of session");
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpDelete logout = new HttpDelete(SessionConstants.getSessionUrl(isLive));
		
		for(String[] header : SessionConstants.getHeaders(isLive)) {
			logout.addHeader(header[0], header[1]);
		}
		
		for (Header header : sessionHeaders) {
			logout.addHeader(header);
		}
		
		try {
			HttpResponse response = client.execute(logout);
			
			StatusLine statusLine = response.getStatusLine();
			switch (statusLine.getStatusCode()) {
			
				case HttpStatus.SC_NO_CONTENT:
					// success - nothing to do except log
					break;
				default:
					throw new SessionException("Logout attempt failed: " + statusLine.toString());
			}
		} catch (ClientProtocolException e) {
			throw new SessionException(e);
		} catch (IOException e) {
			throw new SessionException(e);
		}		
	}
	
	/**
	 * Return a list of the headers needed to submit requests using this session
	 * This will be one of the list of headers in SessionConstants plus the CST and X-SECURITY-TOKEN
	 * headers obtained on login
	 * @return
	 */
	public List<Header> getSessionHeaders() {
		
		final List<Header> headers = new ArrayList<>(SessionConstants.getHeaderList(isLive));
		headers.addAll(sessionHeaders);
		return headers;
	}
	
	public boolean getIsLive() {
		return isLive;
	}

}
