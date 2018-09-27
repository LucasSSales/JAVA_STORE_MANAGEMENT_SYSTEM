package Proxy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Item_Menu.SellerItemManager;

public class SellOptions implements Sell{
	JFrame acess;
	
	public SellOptions(JFrame acess) {
		this.acess = acess;
	}
	
	@Override
	public JButton showSellButton() {
		if(acess instanceof SellerItemManager) {
			
			JButton sellB = new JButton("<< SELL >>");
	        sellB.setBounds(470,180,210,35);
	        return sellB;
		}
		return null;
	}

	@Override
	public JLabel showSellingQuantity() {
		if(acess instanceof SellerItemManager) {
			JLabel amount = new JLabel("Selling Quantity :");
	        amount.setBounds(470,120,amount.getPreferredSize().width,amount.getPreferredSize().height);
	        return amount;
		}
		return null;
	}

	@Override
	public JLabel showTotalSold() {
		JLabel grossSoldText = new JLabel("Total Sold : ");
        grossSoldText.setBounds(470,250,grossSoldText.getPreferredSize().width,grossSoldText.getPreferredSize().height);
        return grossSoldText;
	}
	
	@Override
	public JTextField showSellingtext() {
		JTextField sellQuantity = new JTextField();
        sellQuantity.setBounds(580,120,100,25);
		return sellQuantity;
	}
}
