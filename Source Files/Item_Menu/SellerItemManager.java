package Item_Menu;

import MainProgram.SourceMain;
import Signup_Login_Manager.LoginManager;
import Strategy.ItemMenu;
import Strategy.Strategy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Window;
import java.awt.event.*;
import java.sql.ResultSet;
import java.util.*;

public class SellerItemManager extends ItemMenu{
	SellerItemManager seller = this;
	ItemMenu menu;
	
	public SellerItemManager(LoginManager lmgr) {
		super("POS System");
		createItemMenu(seller, lmgr);
	}
}
