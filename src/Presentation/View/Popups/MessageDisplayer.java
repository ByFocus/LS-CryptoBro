package Presentation.View.Popups;

import javax.swing.*;

/**
 * Utility class for displaying different types of user messages in pop-up dialogs.
 */
public class MessageDisplayer {
    private static String ERROR_TITTLE = "CryptoBro Error MSG";
    private static String WARNING_TITLE = "CryptoBro Quien Avisa no es traidor";
    private static String CONFIRMATION_TITLE = "Jurao bro?";
    private static String INFO_TITLE = "Bro, debes saber esto";

    /**
     * Displays an error dialog with the specified message.
     *
     * @param message the error message to display
     */
    public static void displayError(String message) {
        JOptionPane.showMessageDialog(null, message , ERROR_TITTLE, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a warning dialog with the specified message.
     *
     * @param message the warning message to display
     */
    public static void displayWarning(String message) {
        JOptionPane.showMessageDialog(null, message , WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays a confirmation dialog asking the user to confirm an action.
     *
     * @param message the confirmation question to display
     * @return an integer indicating the user's choice:
     *         {@link JOptionPane#YES_OPTION} or {@link JOptionPane#NO_OPTION}
     */
    public static int askConfirmation(String message) {
        return JOptionPane.showConfirmDialog(null, message, CONFIRMATION_TITLE, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Displays an informational dialog with the specified message.
     *
     * @param message the information message to display
     */
    public static void displayInformativeMessage(String message) {
        JOptionPane.showMessageDialog(null, message , INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }
}
