package com.ngray.autotrader.markets;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.ngray.autotrader.AutoTrader;
import com.ngray.autotrader.RestAPIGet;
import com.ngray.autotrader.RestAPIResponse;
import com.ngray.autotrader.Session;
import com.ngray.autotrader.exception.SessionException;

public final class MarketNode {
	
	private String id;
	
	private String name;
	
	private List<MarketNode> nodes;
	
	private List<Market> markets;
	
	public MarketNode() {
		id = "";
		name = "root-node";
		nodes = null;
		markets = null;	
	}
	
	public MarketNode(String id, String name) {
		this.id = id;
		this.name = name;
		nodes = null;
		markets = null;
	}
	
	public static MarketNode getRootNode(Session session) throws SessionException {			
		RestAPIGet request = new RestAPIGet("/marketnavigation");
		RestAPIResponse response = request.execute(session);
		return fromJson(response.getResponseBodyAsJson());			
	}
	
	public static MarketNode fromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, MarketNode.class);
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public List<MarketNode> getSubNodes() {
		
		return nodes == null ? null : Collections.unmodifiableList(nodes);
	}
	
	public List<Market> getMarkets() {
		return markets == null ? null : Collections.unmodifiableList(markets);
	}
	
	public void getSubNodesAndMarkets(Session session) {
		try {
			RestAPIGet request = new RestAPIGet("/marketnavigation/" + getId());
			RestAPIResponse response = request.execute(session);
			MarketNode newNode = fromJson(response.getResponseBodyAsJson());
			this.nodes = newNode.getSubNodes();
			this.markets = newNode.getMarkets();
		} catch (SessionException e) {
			AutoTrader.getLogger().log(Level.SEVERE, e.getMessage());
		}			
	}
	
	
	
	
	
	

}
