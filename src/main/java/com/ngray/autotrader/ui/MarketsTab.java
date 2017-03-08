package com.ngray.autotrader.ui;

import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.ngray.autotrader.AutoTrader;
import com.ngray.autotrader.exception.SessionException;
import com.ngray.autotrader.markets.MarketNode;

@SuppressWarnings("serial")
public final class MarketsTab extends JPanel {

	private AutoTraderUI parentUI;
	private JTree nodesTree;
	private JTable marketsTable;
	
	public MarketsTab(AutoTraderUI parent) throws SessionException {
		super();
		init(parent);
	}
	
	public JTree getNodesTree() {
		return nodesTree;
	}
	
	public AutoTraderUI getParentUI() {
		return parentUI;
	}
	
	private void init(AutoTraderUI parent) throws SessionException {
	
		parentUI = parent;
		nodesTree = new JTree();
		nodesTree.setModel(new MarketNodeTreeModel(MarketNode.getRootNode(getParentUI().getSession())));
		addTreeListeners();
		JScrollPane treePane = new JScrollPane(nodesTree);
		add(treePane);
		
		
		marketsTable = new JTable();
		marketsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane marketsPane = new JScrollPane(marketsTable);
		marketsTable.setFillsViewportHeight(true);
		add(marketsPane);
	}
	
	private void addTreeListeners() {
		
		nodesTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				AutoTrader.getLogger().log(Level.FINE, "Tree selection event on " + e.getPath().toString());
				TreePath path = e.getPath();
				Object selectedNode = path.getLastPathComponent();
				
				// if the node is a leaf we lazily load sub-nodes
				if (nodesTree.getModel().isLeaf(selectedNode)) {
					((MarketNode)selectedNode).getSubNodesAndMarkets(getParentUI().getSession());
				
				}
				
				// if there are sub-nodes we expand the selection
				if (!((MarketNode)selectedNode).getSubNodes().isEmpty()) {
					nodesTree.expandPath(e.getPath());	
				}
				
				// if there are markets we display in the JTable
				if (!((MarketNode)selectedNode).getMarkets().isEmpty()) {
					marketsTable.setModel(new MarketTableModel(((MarketNode)selectedNode).getMarkets()));
					TableColumnAdjuster adjuster = new TableColumnAdjuster(marketsTable);
					adjuster.adjustColumns();
					
					String json = ((MarketNode)selectedNode).getMarkets().get(0).asJson();
					System.out.println(json);
				}
			}
			
		});
	}




}
