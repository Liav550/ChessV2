package com.chess.gui;

import com.chess.login.DBOperations;
import com.chess.login.GmailHandler;

import java.awt.*;
import java.awt.event.*;
import javax.mail.MessagingException;
import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField registerUsernameField;
    private JTextField emailField;
    private JTextField signInUsernameField;
    private JPasswordField registerPasswordField, confirmPasswordField, signInPasswordField;
    private JButton registerButton, signInButton, forgotPasswordButton;

    public LoginFrame() {
        super("Login");

        // Create Panels for Registration and Sign In
        JPanel registrationPanel = createRegistrationPanel();
        JPanel signInPanel = createSignInPanel();

        // Create JTabbedPane to switch between forms
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab("Register", registrationPanel);
        tabbedPane.addTab("Sign In", signInPanel);


        // Layout components
        JPanel mainPanel = new JPanel();
        mainPanel.add(tabbedPane);

        add(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        registerUsernameField = new JTextField(20);
        registerUsernameField.setBackground(Color.pink);
        registerPasswordField = new JPasswordField(20);
        registerPasswordField.setBackground(Color.pink);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBackground(Color.pink);
        emailField = new JTextField(20);
        emailField.setBackground(Color.pink);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password: ");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password: ");
        JLabel emailLabel = new JLabel("Email (Optional): ");

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        registerButton.setBackground(Color.orange);

        panel.add(usernameLabel);
        panel.add(registerUsernameField);
        panel.add(passwordLabel);
        panel.add(registerPasswordField);
        panel.add(confirmPasswordLabel);
        panel.add(confirmPasswordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(registerButton);

        return panel;
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.red);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        signInUsernameField = new JTextField(20);
        signInUsernameField.setBackground(Color.pink);
        signInPasswordField = new JPasswordField(20);
        signInPasswordField.setBackground(Color.pink);

        JLabel usernameLabel = new JLabel("Username: ");
        JLabel passwordLabel = new JLabel("Password or Google authentication key: ");

        // Create a horizontal panel to hold Sign In and Forgot Password buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        signInButton = new JButton("Sign In");
        signInButton.addActionListener(this);
        signInButton.setBackground(Color.orange);
        forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(this);
        forgotPasswordButton.setBackground(Color.orange);

        buttonPanel.add(signInButton);
        buttonPanel.add(forgotPasswordButton);
        buttonPanel.setBackground(Color.red);

        panel.add(usernameLabel);
        panel.add(signInUsernameField);
        panel.add(passwordLabel);
        panel.add(signInPasswordField);
        panel.add(buttonPanel); // Add the button panel instead of individual buttons

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO - for the whole method, use popup messages
        if (e.getSource() == registerButton) {

            if(registerUsernameField.getText().trim().length() == 0 ||
                    registerPasswordField.getPassword().length == 0 ||
                    confirmPasswordField.getPassword().length == 0){
                PopupMessage.showErrorMessage("Make sure you fill in all mandatory fields",
                        "Oops. You missed some fields");
                return;
            }

            if(!String.valueOf(registerPasswordField.getPassword()).equals(String.valueOf(confirmPasswordField.getPassword()))){
                PopupMessage.showErrorMessage("The password and the confirm password fields must be the same","Looks like you messed up");
                return;
            }

            if(emailField.getText().trim().length() == 0){
                int result = PopupMessage.showGmailUsageYesNoDialog();
                if(result == 0){
                    DBOperations.persistUserPasswordPair(registerUsernameField.getText(), null,
                            String.valueOf(registerPasswordField.getPassword()));
                    PopupMessage.showSuccessMessage("You are all set."
                            , "Registered successfully");
                    clear();
                }
                return;
            }

            if(!GmailHandler.isValidEmailAddress(emailField.getText())){
                PopupMessage.showErrorMessage("Invalid email address","Looks like you messed up");
                return;
            }

            String key = DBOperations.persistUserPasswordPair(registerUsernameField.getText(), emailField.getText(),
                    String.valueOf(registerPasswordField.getPassword()));

            PopupMessage.showSuccessMessage("You are all set. \n check out your email for the google authentication key"
                    , "Registered successfully");

            GmailHandler handler = new GmailHandler(emailField.getText());
            try {
                handler.draftWelcomeEmail(key);
                handler.sendEmail();
            } catch (MessagingException ex) {
                throw new RuntimeException(ex);
            }

            clear();

        }



        else if (e.getSource() == signInButton) {
            if(this.signInUsernameField.getText().trim().length() == 0 ||
            signInPasswordField.getPassword().length == 0){
                PopupMessage.showErrorMessage("Make sure you fill in all fields",
                        "Oops. You missed some fields");
                return;
            }
            boolean entered =
                    DBOperations.signIn(this.signInUsernameField.getText(),
                            String.valueOf(this.signInPasswordField.getPassword()));
            if(!entered){
                clear();
                return;
            }
            WelcomeFrame welcomeFrame = new WelcomeFrame(DBOperations.getUserByName(signInUsernameField.getText()));
            this.dispose();
        }
        else if (e.getSource() == forgotPasswordButton) {
            System.out.println("FORGOT");
        }
    }

    public void clear(){
        this.registerUsernameField.setText("");
        this.registerPasswordField.setText("");
        this.confirmPasswordField.setText("");
        this.emailField.setText("");

        this.signInUsernameField.setText("");
        this.signInPasswordField.setText("");
    }
}
