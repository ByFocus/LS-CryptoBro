package Presentation.View.Popups;

import javax.swing.*;

/**
 * The type Message displayer.
 */
public class MessageDisplayer {
    private static String ERROR_TITTLE = "CryptoBro Error MSG";
    private static String WARNING_TITLE = "CryptoBro Quien Avisa no es traidor";
    private static String CONFIRMATION_TITLE = "Jurao bro?";
    private static String INFO_TITLE = "Bro, debes saber esto";
    /**
     * Display error.
     *
     * @param message the message
     */
    public static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message , ERROR_TITTLE, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display warning.
     *
     * @param message the message
     */
    public static void displayWarning(String message) {
        JOptionPane.showMessageDialog(null, message , WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Ask confirmation int.
     *
     * @param message the message
     * @return the int
     */
    public static int askConfirmation(String message) {
        return JOptionPane.showConfirmDialog(null, message, CONFIRMATION_TITLE, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Display informative message.
     *
     * @param message the message
     */
    public static void displayInformativeMessage(String message) {
        JOptionPane.showMessageDialog(null, message , INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }
}
