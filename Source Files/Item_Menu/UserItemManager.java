package Item_Menu;

import MainProgram.SourceMain;
import Signup_Login_Manager.LoginManager;
import Strategy.ItemMenu;
import Strategy.Strategy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.ResultSet;


public class UserItemManager extends ItemMenu{
	UserItemManager user = this;
	ItemMenu menu;
	
	public UserItemManager(LoginManager lmgr) {
		super("Item List");
		createItemMenu(user, lmgr);
	}
	
}

