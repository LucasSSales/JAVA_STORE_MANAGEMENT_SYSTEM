package Proxy;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Item_Menu.SellerItemManager;
import Strategy.ItemMenu.ButtonHandler;

public class SellProxy extends JFrame implements Sell {
	
	JFrame acess;
	
	public SellProxy(JFrame acess) {
		this.acess = acess;
	}
	
	@Override
	public JButton showSellButton() {
		SellOptions selloptions = new SellOptions(acess);	
		return selloptions.showSellButton();
	}

	@Override
	public JLabel showSellingQuantity() {
		SellOptions selloptions = new SellOptions(acess);
		return selloptions.showSellingQuantity();
	}

	@Override
	public JLabel showTotalSold() {
		SellOptions selloptions = new SellOptions(acess);
        return selloptions.showTotalSold();
	}
	
	@Override
	public JTextField showSellingtext() {
		SellOptions selloptions = new SellOptions(acess);
		return selloptions.showSellingtext();
	}	
}
