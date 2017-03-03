package com.ngray.autotrader;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;

import com.ngray.autotrader.exception.SessionException;
import com.ngray.autotrader.markets.MarketNode;
import com.ngray.autotrader.ui.LoginDialog;




/**
 * The main class of the AutoTrader application
 * Provides the entry point through main() and an application level
 * logger that is accessible throughout the app via a static method
 * @author nigelgray
 *
 */
public final class AutoTrader {
	
	/**
	 * The application logger instance
	 */
	private static final Logger logger = Logger.getLogger("AutoTrader");
	
	/**
	 * The application session after successful login
	 */
	private static Session session = null;
	
	/**
	 * Application entry point
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration config = null;
		try {
			config = parseCommandLine(args);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Error parsing command line", e);
			return;
		}
		
		if (config.LogFile != null) {
			
			try {
				FileHandler fileHandler = new FileHandler(config.LogFile);
				SimpleFormatter simpleFormatter = new SimpleFormatter();
				fileHandler.setFormatter(simpleFormatter);
				logger.addHandler(fileHandler);
			} catch (SecurityException | IOException e) {
				logger.log(Level.WARNING, "Error opening logfile " + config.LogFile + ": will log to console only\n", e);
			}

		}
		
		if (config.IsConsoleApp) {
			runAsConsoleApp(config);
		} else {
			runAsGUI();
		}
	}
	
	public static void setSession(Session session) {
		AutoTrader.session = session;
	}
	
	public static Session getSession() {
		return session;
	}
	
	private static void runAsGUI() {
		JFrame parent = new JFrame("AutoTrader");
		LoginDialog login = new LoginDialog(parent);
		login.show();
		
		if(login.getSucceeded()) {		
			// build the main GUI here
		} else if (login.getCancelled()) {
			parent.dispose();
		}
		
		/*
		parent.setVisible(true);
		while(true) {}
		*/
	}

	private static void runAsConsoleApp(Configuration config) {
		Session session = null;
		try {
			session = Session.login(config.Username, config.Password, config.IsEncrypted, config.IsLive);
		}
		catch (SessionException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
		
		if (session != null) {
			// add code here to do stuff with the session
			try {
				MarketNode rootNode = MarketNode.getRootNode(session);
				List<MarketNode> subNodes = rootNode.getSubNodes();
				for (Iterator<MarketNode> iter = subNodes.iterator(); iter.hasNext(); ) {
					iter.next().getSubNodesAndMarkets(session);
				}
			} catch (SessionException e) {
				getLogger().log(Level.SEVERE, "Error getting market nodes", true);
			}
			
		}
		
		try {
			if (session != null) {
				session.logout();
			}
		} catch (SessionException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Static accessor method for the application logger
	 * @return
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * Private helper method to parse command line args
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	private static Configuration parseCommandLine(String[] args) throws ParseException {
		
		// cmd line args are:
		// -U <username>    	- the username to login with
		// -P <password>    	- the password for the given user
		// [-E]					- indicates password encrypted (omit if not encrypted)
		// [-a <LIVE | DEMO>]	- if omitted, connect to demo account
		// [-L <logfile> ]
		Options options = new Options();
		options.addOption("U", true, "-U <username>");
		options.addOption("P", true, "-P <password>");
		options.addOption("E", false, "[E] password encrypted");
		options.addOption("A", true, "[-A LIVE|DEMO] choose demo or live account");
		options.addOption("L", true, "[-L <logfile>]");
		options.addOption("C", true, "[-C TRUE|FALSE] Run as console app (default true)");
	
		DefaultParser parser = new DefaultParser();
		CommandLine cmdLine = parser.parse(options, args);
		Configuration config = new Configuration();
		for (Iterator<Option> optionIter = cmdLine.iterator(); optionIter.hasNext();) {
			Option option = optionIter.next();
			String optName = option.getOpt();
			String optVal = option.getValue();
			if (optName.equals("U")) {
				config.Username = optVal;
			} else if (optName.equals("P")) {
				config.Password = optVal;
			} else if (optName.equals("E")) {
				config.IsEncrypted = true;
			} else if (optName.equals("A")) {
				if (optVal.equals("DEMO")) {
					config.IsLive = false;
				} else if (optVal.equals("LIVE")) {
					config.IsLive = true;
				}
			} else if (optName.equals("C")) {
				if (optVal.equals("TRUE")) {
					config.IsConsoleApp = true;
				} else if (optVal.equals("FALSE")) {
					config.IsConsoleApp = false;
				}
			}else if (optName.equals("L")) {
				config.LogFile = optVal;
			}
			
		}

		return config;		
	}
	
	/**
	 * Private static member class encapsulating configuration options
	 * @author nigelgray
	 *
	 */
	private static class Configuration {
		
		public String 	Username;
		public String 	Password;
		public boolean 	IsLive;
		public boolean  IsEncrypted;
		public String 	LogFile;
		public boolean  IsConsoleApp;
		
		public Configuration() {
			Username = null;
			Password = null;
			IsLive = false;
			IsEncrypted = false;
			LogFile = null;
			IsConsoleApp = true;
		}		
	}
	
	

}
