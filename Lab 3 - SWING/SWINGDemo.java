//package com.mybank.gui;

import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import com.mybank.reporting.CustomerReport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class SWINGDemo {
    
    private final JEditorPane log;
    private final JButton show;
    private final JButton report;
    private final JComboBox clients;
    
    public SWINGDemo() {
        log = new JEditorPane("text/html", "");
        log.setPreferredSize(new Dimension(250, 600));
        show = new JButton("Show");
        report = new JButton("Report");
        clients = new JComboBox();
        for (int i=0; i<Bank.getNumberOfCustomers();i++)
        {
            clients.addItem(Bank.getCustomer(i).getLastName()+", "+Bank.getCustomer(i).getFirstName());
        }
        
    }
    
    private void launchFrame() {
        JFrame frame = new JFrame("MyBank clients");
        frame.setLayout(new BorderLayout());
        JPanel cpane = new JPanel();
        cpane.setLayout(new GridLayout(1, 2));
        
        cpane.add(clients);
        cpane.add(show);
        cpane.add(report);
        frame.add(cpane, BorderLayout.NORTH);
        frame.add(log, BorderLayout.CENTER);
        
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer current = Bank.getCustomer(clients.getSelectedIndex());
                String accType = current.getAccount(0)instanceof CheckingAccount?"Checking":"Savings";                
                String custInfo="<br>&nbsp;<b><span style=\"font-size:2em;\">"+current.getLastName()+", "+
                        current.getFirstName()+"</span><br><hr>"+
                        "&nbsp;<b>Acc Type: </b>"+accType+
                        "<br>&nbsp;<b>Balance: <span style=\"color:red;\">$"+current.getAccount(0).getBalance()+"</span></b>";
                log.setText(custInfo);                
            }
        });
        
        report.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
               
                String title = "CUSTOMERS REPORT";
                String customers = "";
                String customerName = "";
                String account_type = "";          
    
    for ( int cust_idx = 0;
          cust_idx < Bank.getNumberOfCustomers();
          cust_idx++ ) {
      Customer customer = Bank.getCustomer(cust_idx);
      customerName = customer.getFirstName() + ", " + customer.getLastName();
     
      for ( int acct_idx = 0;
            acct_idx < customer.getNumberOfAccounts();
            acct_idx++ ) {
        Account account = customer.getAccount(acct_idx);
        
        if ( account instanceof SavingsAccount ) {
          account_type = "Savings Account";
        } else if ( account instanceof CheckingAccount ) {
          account_type = "Checking Account";
        } else {
          account_type = "Unknown Account Type";
        }

        customers += "<b>" + customerName + "<br>Acc type: </b>" + account_type + "<br><b>Balance: </b> $" + account.getBalance() + "<br><hr>";
      }
    }
        String report = "<br>&nbsp;<b><span style=\"font-size:2em;\">"+title+"</span><br><hr><br>" +customers+ "";
        log.setText(report);  
            }
        });
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        frame.setResizable(true);
        frame.setVisible(true);        
    }
    
    public static void main(String[] args) {
                
        try (BufferedReader br = new BufferedReader(new FileReader("test.dat"))) {
            int numberOfCustomers = Integer.parseInt(br.readLine());
            for (int i = 0; i < numberOfCustomers; i++) {
                br.readLine();
                String[] customerInfo = br.readLine().split("\t");

                Bank.addCustomer(customerInfo[0], customerInfo[1]);
                
                int numberOfAccounts = Integer.parseInt(customerInfo[2]);
                
                Customer customer = Bank.getCustomer(i);

                for (int j = 0; j < numberOfAccounts; j++) {
                    String[] accountInfo = br.readLine().split("\t");
                    String accountType = accountInfo[0];
                    double balance = Double.parseDouble(accountInfo[1]);
                    switch (accountType) {
                        case "S":
                            double interestRate = Double.parseDouble(accountInfo[2]);
                            customer.addAccount(new SavingsAccount(balance, interestRate));
                            break;
                        case "C":
                            double overdraftAmount = Double.parseDouble(accountInfo[2]);
                            customer.addAccount(new CheckingAccount(balance, overdraftAmount));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SWINGDemo demo = new SWINGDemo();        
        demo.launchFrame();
    }
    
}
