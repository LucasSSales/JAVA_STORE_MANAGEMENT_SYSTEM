package Strategy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Item_Menu.UserItemManager;
import Item_Menu.UserItemManager.*;

import MainProgram.SourceMain;
import Proxy.Sell;
import Proxy.SellProxy;
import Signup_Login_Manager.LoginManager;
import State.Avaliable;
import State.ProductState;
import State.Unavaliable;

public class ItemMenu extends JFrame{
	JComboBox category;
    JButton searchB,logoutB, sellB;
    JList itemList;
    JScrollPane jScrollPane;
    JTextField search, sellQuantity;
    JLabel price,itemAvailable, amount,grossSoldText;
    String[] cat;
    int[] currentItemId;
    LoginManager lmg;
    ItemMenu itemMenu = this;
    JFrame acess;
    int grossSold=0;
    int selectedItemQuantity,selectedItemPrice,selectedItemID;
    private ProductState state;
    
    public ItemMenu(String type){
    	super(type);
    }

    public void createItemMenu(JFrame acess, LoginManager lmgr){
        setLayout(null);
        this.lmg = lmgr;

        ItemHandler itemHandler = new ItemHandler();
        ListHandler listHandler = new ListHandler();
        Sell proxy = new SellProxy(acess);

        search = new JTextField();
        search.setBounds(200,15,100,25);
        add(search);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] str = new String[]{"Yes","No"};
                int exit = JOptionPane.showOptionDialog(null, "Are you sure you want to exit", "Exit", JOptionPane.NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, str, null);

                if(exit == 0) {   //Delete Row From Table
                	itemMenu.dispose();
                    lmg.clearTextFields();
                    lmg.setVisible(true);
                }
            }
        });

        try {
            int ind=0;
            ResultSet myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM categorie");
            while (myRs.next()){
                ind++;
            }
            myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM categorie");
            cat = new String[ind];
            ind=0;
            while (myRs.next()){
                cat[ind++] = myRs.getString("categories");
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Database - Category Error");
        }

        ButtonHandler buttonHandler = new ButtonHandler();

        category = new JComboBox(cat);
        category.setBounds(20,50,100,category.getPreferredSize().height);
        category.addItemListener(itemHandler);
        add(category);

        itemList = new JList();
        itemList.addListSelectionListener(listHandler);

        jScrollPane = new JScrollPane(itemList);
        jScrollPane.setBounds(200,50,200,400);
        add(jScrollPane);

        price = new JLabel("Price : ");
        price.setBounds(470,50,price.getPreferredSize().width,price.getPreferredSize().height);
        add(price);

        itemAvailable = new JLabel("Available : ");
        itemAvailable.setBounds(470,70,itemAvailable.getPreferredSize().width,itemAvailable.getPreferredSize().height);
        add(itemAvailable);
        
        try {
        	amount = (JLabel) add(proxy.showSellingQuantity());
        }catch(NullPointerException e) {}
        
        searchB = new JButton("SEARCH");
        searchB.setBounds(310,15,120,25);
        searchB.addActionListener(buttonHandler);
        add(searchB);
        
        try {
        	sellB = (JButton) add(proxy.showSellButton());
        	sellB.addActionListener(buttonHandler);
        	
        	
        	grossSoldText = (JLabel) add(proxy.showTotalSold());
        	sellQuantity = (JTextField) add(proxy.showSellingtext());
        	
        }catch(NullPointerException e) {}
        
        logoutB = new JButton("Log Out");
        logoutB.addActionListener(buttonHandler);
        logoutB.setBounds(600,500,120,25);
        add(logoutB);
        
    }

    public class ButtonHandler implements ActionListener{
    	Strategy strategy = new Strategy();
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == searchB) {
            	strategy.searchButton(search.getText(), itemList, price, itemAvailable);
            }
            else if(e.getSource() == sellB){
            	System.out.println("1");
                boolean cnd;
                String enteredQuantity;
                if(sellQuantity.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter amount before clicking sell");
                }
                System.out.println("2");
                try {
                    System.out.println("sla -> " + Integer.parseInt(sellQuantity.getText()));
                    enteredQuantity = sellQuantity.getText();
                    cnd=true;
                } catch (Exception ex) {
                    cnd = false;
                    enteredQuantity = null;
                    JOptionPane.showMessageDialog(null, "Make sure Quantity/Price Field is a number", "Input Error", JOptionPane.ERROR_MESSAGE);

                }

                System.out.println("3");
                if(cnd && itemList.getSelectedValue() != null && selectedItemQuantity != 0){
                    Date date= new Date(); //Get Calaender Date START-------
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH)+1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);//--------- Get Calaender Date END

                    if(enteredQuantity != null){

                        int quantityValue = Integer.parseInt(enteredQuantity);
                        System.out.println("Typed : "+quantityValue);

                        if((quantityValue > 0) && quantityValue <= selectedItemQuantity){
                            int totalSold = quantityValue * selectedItemPrice;
                            grossSold += totalSold;
                            SourceMain.jDataBase.insertRow("INSERT INTO `itemlist`.`history2` VALUES ( '"+selectedItemID+"','"+quantityValue+"','"+day+"','"+month+"','"+year+"','"+selectedItemPrice+"','"+itemList.getSelectedValue()+"')");
                            SourceMain.jDataBase.insertRow("UPDATE `itemlist`.`itemlist2` SET `quantity`='"+(selectedItemQuantity - quantityValue)+"' WHERE `id`='"+selectedItemID+"'");
                            selectedItemQuantity -= quantityValue;
                            itemAvailable.setText("Available : " + selectedItemQuantity);
                            itemAvailable.setBounds(470,70,itemAvailable.getPreferredSize().width,itemAvailable.getPreferredSize().height);

                            grossSoldText.setText("Total Sold : "+String.valueOf(grossSold));
                            grossSoldText.setBounds(470,250,grossSoldText.getPreferredSize().width,grossSoldText.getPreferredSize().height);
                            JOptionPane.showMessageDialog(null, "Item sold successfully", "Sold", JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showMessageDialog(null, "Total selling price : "+totalSold, "Sold", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            if(quantityValue == 0) //Previos (quantityValue <= selectedItemQuantity)
                                JOptionPane.showMessageDialog(null, "Value must be Greater then 0", "Input Error", JOptionPane.ERROR_MESSAGE);
                            else {
                                JOptionPane.showMessageDialog(null, "Selling item quantity exceeds item available", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        System.out.println("Available PROBLEMMMMMM : "+selectedItemQuantity);
                    }
                    System.out.println("4");

                }
                else {
                    if(selectedItemQuantity == 0)
                        JOptionPane.showMessageDialog(null, "Item Out of Stock", "Input Error", JOptionPane.ERROR_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(null, "You must select an item first", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("5");
            }
            else if(e.getSource() == logoutB){
            	strategy.logoutButton(itemMenu, lmg);
            }
        }
    }

    public class ItemHandler implements ItemListener{  //ItemLister for JComboBox, variable named category.
    	
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(e.getSource() == category){

                if(e.getStateChange() == ItemEvent.SELECTED) { //It means that is there any item in list is selected. SOURCE : JAVA Tutorial by Bucky
                    String[] items=new String[0];
                    String cat = e.getItem().toString();
                    ResultSet myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM itemlist2 WHERE category LIKE '%"+cat+"%'");
                    int rownum=0;

                    try {
                        while (myRs.next()){
                            rownum++;
                        }
                        myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM itemlist2 WHERE category LIKE '%"+cat+"%'");
                        items = new String[rownum];
                        currentItemId = new int[rownum];
                        for(int i=0 ; myRs.next() ; i++){
                            items[i] = myRs.getString("itemname");
                        }
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(null,"Database - Fetch Item Error");
                    }
                    itemList.setListData(items); //JList.setListData() takes an array of Strings as parameter and shows data serially from the array in the table. SOURCE : JAVA Tutorial by Bucky
                }
            }
        }
    }

    public class ListHandler implements ListSelectionListener{ //ListSelectionListener for JList, variable named itemlist.

        @Override
        public void valueChanged(ListSelectionEvent e) {  //Item Description from Database Work goes here
            if (itemList.getSelectedValue() != null) {
                System.out.println(itemList.getSelectedValue());
                ResultSet myRs = SourceMain.jDataBase.getQueryResult("SELECT * FROM `itemlist2` WHERE itemname LIKE '%" + itemList.getSelectedValue() + "%'");
                try {
                    myRs.next();
                    price.setText("Price : " + myRs.getString("price"));
                    selectedItemPrice = Integer.parseInt(myRs.getString("price"));

                    itemAvailable.setText("Available : " + myRs.getString("quantity"));
                    selectedItemQuantity = Integer.parseInt(myRs.getString("quantity"));
                    //description.setText("Really Dude");
                    
                    try {
                        if(selectedItemQuantity > 0)
                        	state = new Avaliable();
                        else
                        	state = new Unavaliable();
                        
                        sellB.setEnabled(state.enableButton());
                    	
                    }catch(Exception exc) {}
                                      
                    selectedItemID = Integer.parseInt(myRs.getString("id"));

                    price.setBounds(470, 50, price.getPreferredSize().width, price.getPreferredSize().height);
                    itemAvailable.setBounds(470, 70, itemAvailable.getPreferredSize().width, itemAvailable.getPreferredSize().height);
                    //description.setBounds(470,120, description.getPreferredSize().width, description.getPreferredSize().height);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,"Database - Show Item Description Error");
                }
            }

        }
    }

}
