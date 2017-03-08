package com.ngray.autotrader.ui;

import java.util.Iterator;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.ngray.autotrader.markets.MarketNode;

public final class MarketNodeTreeModel implements TreeModel {

	private MarketNode rootNode;

	
	public MarketNodeTreeModel(MarketNode rootNode) {
		this.rootNode = rootNode;
	}
	
	@Override
	public Object getRoot() {
		return rootNode;
	}

	@Override
	public Object getChild(Object parent, int index) {
		MarketNode marketNode = (MarketNode)parent;
		return marketNode.getSubNodes().get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		MarketNode marketNode = (MarketNode)parent;
		return marketNode.getSubNodes().size();
	}

	@Override
	public boolean isLeaf(Object node) {
		MarketNode marketNode = (MarketNode)node;
		return marketNode.getSubNodes().isEmpty();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("*** valueForPathChanged : "
                + path + " --> " + newValue);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		MarketNode parentNode = (MarketNode)parent;
		int index = 0;
		for (Iterator<MarketNode> iter = parentNode.getSubNodes().iterator(); iter.hasNext(); ++index) {
			if (iter.next().equals(child)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// not used
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// not used
	}

}
