package Presentation.View.Popups;

import javax.swing.*;

public class ErrorDisplayer {
    public static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Error MSG" , JOptionPane.ERROR_MESSAGE);
    }
}
