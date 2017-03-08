package com.ngray.autotrader.ui;

import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.ngray.autotrader.markets.Market;

@SuppressWarnings("serial")
public final class MarketTableModel extends AbstractTableModel {

	private final String[] columnNames;
	private final Object[][] data;
	
	public MarketTableModel(List<Market> markets) {
		columnNames = Market.getAttributeNames();
		data = new Object[markets.size()][getColumnCount()];
		int row = 0;
		for (Iterator<Market> i = markets.iterator(); i.hasNext(); ++row) {
			Market market = i.next();
			Object[] values = market.getAttributeValues();
			data[row] = values;
		}
	}
	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}
	
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
    }
	
	@Override
	public boolean isCellEditable(int row, int col) {     
	    return false;
	
	}

}
