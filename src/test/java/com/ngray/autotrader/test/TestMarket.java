package com.ngray.autotrader.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ngray.autotrader.markets.Market;

public class TestMarket {

	// FTSE 100 Daily
	private String testJson = "{\"bid\":7345.2,\"offer\":7346.2,\"delayTime\":0,\"epic\":\"IX.D.FTSE.DAILY.IP\",\"expiry\":\"DFB\","
			+ "\"high\":7347.0,\"low\":7310.3,\"instrumentName\":\"FTSE 100\",\"instrumentType\":\"INDICES\",\"lotSize\":10,"
			+ "\"marketStatus\":\"TRADEABLE\",\"netChange\":6.6,\"otcTradeable\":true,\"percentageChange\":0.09,\"scalingFactor\":1.0,"
			+ "\"streamingPricesAvailable\":true,\"updateTime\":\"31715000\",\"updateTimeUTC\":\"09:48:35\"}";
	
	@Test
	public void testFromJson() {
		Market market = Market.fromJson(testJson);
		
		// call each getter asserting the expected value
		assertTrue(market.getBid() == 7345.2);
		assertTrue(market.getOffer() == 7346.2);
		assertTrue(market.getDelayTime() == 0);
		assertTrue(market.getEpic().equals("IX.D.FTSE.DAILY.IP"));
		assertTrue(market.getExpiry().equals("DFB"));
		assertTrue(market.getHigh() == 7347.0);
		assertTrue(market.getLow() == 7310.3);
		assertTrue(market.getInstrumentName().equals("FTSE 100"));
		assertTrue(market.getInstrumentType().equals("INDICES"));
		assertTrue(market.getLotSize() == 10);
		assertTrue(market.getMarketStatus().equals("TRADEABLE"));
		assertTrue(market.getNetChange() == 6.6);
		assertTrue(market.isOtcTradeable() == true);
		assertTrue(market.getPercentageChange() == 0.09);
		assertTrue(market.getScalingFactor() == 1.0);
		assertTrue(market.isStreamingPricesAvailable() == true);
		assertTrue(market.getUpdateTime().equals("31715000"));
		assertTrue(market.getUpdateTimeUTC().equals("09:48:35"));
		
	}
	
	@Test
	public void testAsJson() {
		Market market = new Market();
		market.setBid(7345.2);
		market.setOffer(7346.2);
		market.setDelayTime(0);
		market.setEpic("IX.D.FTSE.DAILY.IP");
		market.setExpiry("DFB");
		market.setHigh(7347.0);
		market.setLow(7310.3);
		market.setInstrumentName("FTSE 100");
		market.setInstrumentType("INDICES");
		market.setLotSize(10);
		market.setMarketStatus("TRADEABLE");
		market.setNetChange(6.6);
		market.setOtcTradeable(true);
		market.setPercentageChange(0.09);
		market.setScalingFactor(1.0);
		market.setStreamingPricesAvailable(true);
		market.setUpdateTime("31715000");
		market.setUpdateTimeUTC("09:48:35");
		String json = market.asJson();
		
		assertTrue(json.equals(testJson));		
	}

}
