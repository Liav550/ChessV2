package com.chess.gui;

import javax.swing.*;

public class PopupMessage {
    public static void showErrorMessage(String message, String title){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static int showGmailUsageYesNoDialog(){
        String title = "You didn't put your email address...";
        String message = "Putting your email address allows us to contact you in case you forget your password. \n" +
                "Also, we can send you your google authentication key, for easier connection. \n" +
                "Are you sure you want to continue?";
        String[] options = {"Yes","No"};
        int result = JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        // yes is 0 no is 1
        return result;
    }

    public static void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
