package com.ngray.autotrader;

import com.ngray.autotrader.exception.SessionException;

public interface RestAPIAction {

	/**
	 * Implement this method to execute the required action using the credentials
	 * in the supplied session. Throw a SessionException of the action fails
	 * @param session
	 * @return
	 * @throws SessionException
	 */
	public RestAPIResponse execute(Session session) throws SessionException;
}
