package Presentation.View.Popups;

import javax.swing.*;

public class MessageDisplayer {
    public static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Error MSG" , JOptionPane.ERROR_MESSAGE);
    }
    public static void displayWarning(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Quien Avisa no es traidor" , JOptionPane.WARNING_MESSAGE);
    }
}
