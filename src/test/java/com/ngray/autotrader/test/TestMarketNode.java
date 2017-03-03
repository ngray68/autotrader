package com.ngray.autotrader.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.ngray.autotrader.Session;
import com.ngray.autotrader.exception.SessionException;
import com.ngray.autotrader.markets.MarketNode;

public class TestMarketNode {

	@Test
	public void testGetSubNodesAndMarkets() throws SessionException {
		
		Session session = Session.login("dummyuser", "dummypassword", false, false);
		MarketNode rootNode = MarketNode.getRootNode(session);
		
		assertTrue(rootNode != null);
		assertTrue(rootNode.getSubNodes() != null);
		assertTrue(rootNode.getMarkets() == null);
		
		for (Iterator<MarketNode> iter = rootNode.getSubNodes().iterator(); iter.hasNext(); ) {
			MarketNode node = iter.next();
			node.getSubNodesAndMarkets(session);
		
			assertTrue(node.getMarkets() == null);
			assertTrue(node.getSubNodes() != null);
			assertTrue(!node.getSubNodes().isEmpty());
			// This is to avoid hitting the requests per minute limit
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		session.logout();
	}

}
