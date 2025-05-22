package Presentation.View.Popups;

import javax.swing.*;

/**
 * The type Message displayer.
 */
public class MessageDisplayer {
    /**
     * Display error.
     *
     * @param message the message
     */
    public static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Error MSG" , JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display warning.
     *
     * @param message the message
     */
    public static void displayWarning(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Quien Avisa no es traidor" , JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Ask confirmation int.
     *
     * @param message the message
     * @return the int
     */
    public static int askConfirmation(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Jurao bro?", JOptionPane.YES_NO_OPTION);
    }

    /**
     * Display informative message.
     *
     * @param message the message
     */
    public static void displayInformativeMessage(String message) {
        JOptionPane.showMessageDialog(null, message ,"Bro, debes saber esto" , JOptionPane.INFORMATION_MESSAGE);
    }
}
