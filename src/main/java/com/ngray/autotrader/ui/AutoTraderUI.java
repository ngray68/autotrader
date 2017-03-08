package com.ngray.autotrader.ui;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.ngray.autotrader.Session;
import com.ngray.autotrader.exception.SessionException;
import com.ngray.autotrader.markets.MarketNode;

// We are never going to serialize this class
@SuppressWarnings("serial")
public final class AutoTraderUI extends JFrame {

	private final Session session;
	
	public AutoTraderUI(Session session) throws HeadlessException, SessionException {
		// TODO Auto-generated constructor stub
		super("AutoTrader");
		this.session = session;
		init();
	}
	
	public Session getSession()  {
		return session;
	}

	private void init() throws SessionException {
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		// markets
		MarketsTab markets = new MarketsTab(this);
		markets.getNodesTree().setModel(new MarketNodeTreeModel(MarketNode.getRootNode(session)));
		tabbedPane.addTab("Markets", markets);
		
		// positions
		JPanel positions = new JPanel();
		tabbedPane.addTab("Positions", positions);
		
		this.getContentPane().add(tabbedPane);
		pack();
	}
	

	

}
