package com.ngray.autotrader.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ngray.autotrader.Session;
import com.ngray.autotrader.exception.SessionException;

public class TestLogin {

	@Test
	public void testLogin() throws SessionException {
		// Note this test will fail with these credentials
		// as the credentials are not real
		Session session = Session.login("dummyuser", "dummypassword", false, false);
		assertNotNull(session);
		session.logout();
	}
	
	@Test(expected=SessionException.class)
	public void testFailedLogin() throws SessionException {
		Session session = Session.login("dummyuser", "dummypassword", false, false);
		assertNotNull(session);
		session.logout();
		fail("Valid session not expected");
	}

}
