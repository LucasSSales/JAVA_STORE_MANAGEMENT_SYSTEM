package Strategy;

import java.awt.HeadlessException;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;

import Item_Menu.SellerItemManager;
import MainProgram.SourceMain;
import Signup_Login_Manager.LoginManager;

public class Strategy {
	
    public JFrame screen(JFrame j, LoginManager lmgr) {
        j.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        j.setSize(800, 600);
        j.setVisible(true);
        lmgr.setVisible(false);
    	return j;
    }
    
    public JFrame searchUser(String table, String infos) throws HeadlessException, SQLException {
    	ResultSet myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM " + table);
    	String[] info = infos.split("/");
        while (myRs.next()) {
            if(info[0].equals(myRs.getString("name"))){
            	if(info.length == 1) {
            		return new JFrame();
            	}else {
                	if(info[1].equals(myRs.getString("password"))) {
                		JOptionPane.showMessageDialog(null,"Login Successful");
                		return new JFrame();
                	}
            	}
            }
        }
    	return null;
    }
    
    public void searchButton(String str, JList itemList, JLabel price, JLabel itemAvailable) {
        ListModel listModel = itemList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).equals(str)) {

                String[] serItem;
                System.out.println(itemList.getSelectedValue());
                ResultSet myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM `itemlist2` WHERE itemname REGEXP '" + str + "'");
                try {
                    myRs.next();
                    price.setText("Price : " + myRs.getString("price"));
                    itemAvailable.setText("Available : " + myRs.getString("quantity"));
                    serItem = new String[]{myRs.getString("itemname")};

                    price.setBounds(470, 50, price.getPreferredSize().width, price.getPreferredSize().height);
                    itemAvailable.setBounds(470, 80, itemAvailable.getPreferredSize().width, itemAvailable.getPreferredSize().height);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Database - Show Item Description Error");
                    serItem = new String[]{"No Result"};
                }
                itemList.setListData(serItem);
            }
        }
    }
    
    public void logoutButton(Window w, LoginManager lmg) {
        String[] str = new String[]{"Yes","No"};
        int logout = JOptionPane.showOptionDialog(null, "Are you sure you want to Log Out", "Logout", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, str, null);
        if(logout == 0) {   //Delete Row From Table
            w.dispose();
            lmg.clearTextFields();
            lmg.setVisible(true);
        }
    }
    
}
