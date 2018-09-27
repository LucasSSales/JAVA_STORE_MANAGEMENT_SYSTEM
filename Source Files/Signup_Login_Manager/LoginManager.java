package Signup_Login_Manager;


import Item_Menu.SellerItemManager;
import MainProgram.SourceMain;
import Strategy.ItemMenu;
import Strategy.Strategy;
import Inventory_System.InventoryManager;
import Item_Menu.*;
import javax.swing.*;

import java.awt.HeadlessException;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginManager extends JFrame{
    private JLabel label_user,label_password;
    private JButton button_signup,button_login;
    private JTextField tf_username;
    private JPasswordField pf_user_password;
    private LoginManager reference;
    private LoginManager lmgr = this;

    public LoginManager(){
        super("Signup/Login Menu");
        reference = this;
        setLayout(null);
        EventHandler eh = new EventHandler();
        KeyboardHandler kl = new KeyboardHandler();
        addKeyListener(kl);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] str = new String[]{"Yes","No"};
                int exit = JOptionPane.showOptionDialog(null, "Are you sure you want to exit", "Exit", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, str, null);

                if(exit == 0) {   //Delete Row From Table
                    lmgr.dispose();
                }
            }
        });

        label_user = new JLabel("USERNAME : ");
        add(label_user);
        label_user.setBounds(100,60,label_user.getPreferredSize().width,label_user.getPreferredSize().height);

        label_password = new JLabel("PASSWORD : ");
        add(label_password);
        label_password.setBounds(370,60,label_password.getPreferredSize().width,label_password.getPreferredSize().height);

        tf_username = new JTextField();
        add(tf_username);
        tf_username.addKeyListener(kl);
        tf_username.setBounds(180,57,130,25);

        pf_user_password = new JPasswordField();
        pf_user_password.addKeyListener(kl);
        add(pf_user_password);
        pf_user_password.setBounds(460, 57, 130, 25);

        button_login = new JButton("LOGIN");
        button_login.addActionListener(eh);
        add(button_login);
        button_login.setBounds(488,100,100,button_login.getPreferredSize().height);

        button_signup = new JButton("SIGN UP");
        add(button_signup);
        button_signup.addActionListener(eh);
        button_signup.setBounds(488,150,100,button_signup.getPreferredSize().height);



    }
    public void clearTextFields(){ //This method called from every place where Log Out button is present so that last username nad password not shown.
        tf_username.setText("");
        pf_user_password.setText("");
    }

    public class KeyboardHandler implements KeyListener{

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                System.out.println("Enter Pressed");
                button_login.doClick();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
    
    public class EventHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
        	Strategy strategy = new Strategy();
            if(e.getSource() == button_login){
                try {
                	if(strategy.searchUser("seller", tf_username.getText() + "/" + pf_user_password.getText()) != null) {
                		SellerItemManager sellerItemManager = (SellerItemManager) strategy.screen(new SellerItemManager(lmgr), lmgr);
                	}else if (strategy.searchUser("user", tf_username.getText() + "/" + pf_user_password.getText()) != null){
                		if(tf_username.getText().equals("admin")) {
                			InventoryManager inventoryManager = (InventoryManager) strategy.screen(new InventoryManager(lmgr), lmgr);
                		}else {
                			UserItemManager userItemManager = (UserItemManager) strategy.screen(new UserItemManager(lmgr), lmgr);
                		}
                	}else
                		JOptionPane.showMessageDialog(null, "Username/Password Incorrect");
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"DataBase Read Error");
                }

            }
            else if(e.getSource() == button_signup){ //If signup button clicked a new signup window will appear
            	
            	SignupManager signupManager = (SignupManager) strategy.screen(new SignupManager(), reference);
                signupManager.setUserIdentifier("user"); //Trick used to consider weather Signup will be for user or seller.
                //parameter is a userIdentifier variable which is set by calling a public function in setUserIdentifier in SignupManager
                //and the userIdentifier used in INSERT query.
                signupManager.setLoginManagerReference(reference);
            }

        }
    }
}
