package com.ngray.autotrader.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.ngray.autotrader.AutoTrader;
import com.ngray.autotrader.Session;
import com.ngray.autotrader.exception.SessionException;

public final class LoginDialog {
	
	private final JDialog jDialog;
	
	private final JLabel username;
	private final JLabel password;
	private final JTextField usernameField;
	private final JPasswordField passwordField;
	private final JButton login;
	private final JButton cancel;
	
	private volatile boolean succeeded;
	private volatile boolean cancelled;
	
	public LoginDialog(JFrame parent) {
		jDialog = new JDialog(parent);
		jDialog.setTitle(parent.getTitle());
		final JPanel panel = new JPanel(new GridBagLayout());
        final GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        		
		username = new JLabel("Username:");
		cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(username, cs);
        
        usernameField = new JTextField(20);
		cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(usernameField, cs);
        
		password = new JLabel("Password:");
		cs.gridx = 0;
	    cs.gridy = 1;
	    cs.gridwidth = 1;
	    panel.add(password, cs);
	    
	    passwordField = new JPasswordField(20);
		cs.gridx = 1;
	    cs.gridy = 1;
	    cs.gridwidth = 1;
	    panel.add(passwordField, cs);
	    
		login = new JButton("Login");
		cancel = new JButton("Cancel");
		
		login.addActionListener(new ActionListener() {			 
	            public void actionPerformed(ActionEvent e) {
	            	Session session;
					try {
						session = Session.login(getUsername(), getPassword(), false, false);
						if (session != null) {
							succeeded = true;
							AutoTrader.getLogger().log(Level.INFO, "Login succeeded");
							AutoTrader.setSession(session);
		                    JOptionPane.showMessageDialog(jDialog,
		                            "Login succeeded",
		                            "Login",
		                            JOptionPane.INFORMATION_MESSAGE);
		                  
		                    jDialog.dispose();
		                } else {
		                	throw new SessionException("Login failed - null Session generated");
		                }
					} catch (SessionException ex) {
						succeeded = false;
						JOptionPane.showMessageDialog(jDialog,
	                            "Invalid username or password",
	                            "Login",
	                            JOptionPane.ERROR_MESSAGE);
	                    // reset username and password
	                    usernameField.setText("");
	                    passwordField.setText("");
	                    AutoTrader.getLogger().log(Level.SEVERE, ex.getMessage(), true);
					}     
	            }
	        });
		
		cancel.addActionListener(new ActionListener() {			 
	            public void actionPerformed(ActionEvent e) {
	            	cancelled = true;
	                jDialog.dispose();
	            }
	        });
		
		final JPanel buttonPanel = new JPanel();
		buttonPanel.add(login);
		buttonPanel.add(cancel);
		
		succeeded = false;
		cancelled = false;
		
        jDialog.add(panel, BorderLayout.CENTER);
        jDialog.add(buttonPanel, BorderLayout.PAGE_END);
        
        jDialog.setAlwaysOnTop(true);
        jDialog.pack();
        jDialog.setResizable(false);
        jDialog.setLocationRelativeTo(parent);
       
	}
	
	public String getUsername() {
	    return usernameField.getText().trim();
	}
	 
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public boolean getSucceeded() {
    	return succeeded;
    }
    
    public boolean getCancelled() {
    	return cancelled;
    }
    
    /**
     * Show the login dialog
     * This method will block until the action succeeds or is cancelled
     */
    public void show() {
    	jDialog.setVisible(true);
    	while(!getSucceeded() && !getCancelled()) {
		};
    }
}
