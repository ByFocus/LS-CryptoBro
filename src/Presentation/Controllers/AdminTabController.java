package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.CryptoManager;
import Business.MarketManager;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tabs.AdminTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Controller for the admin tab. Handles admin-related actions such as
 * adding or deleting cryptocurrencies.
 */
public class AdminTabController implements ActionListener {
    private final String FILE_MISSING = "Brother, no has añadido ningun fichebro!";
    private final String CRYPTO_NOT_SELECTED = "No has seleccionado ninguna crypto, bro!";

    private static AdminTabController instance;
    private AdminTab adminTab;

    /**
     * Private constructor for the singleton pattern.
     *
     * @throws BusinessExeption if there is an error retrieving crypto names
     */
    private AdminTabController() throws BusinessExeption {
        adminTab = new AdminTab(CryptoManager.getCryptoManager().getAllCryptoNames());
        adminTab.registerController(this);
    }

    /**
     * Returns the singleton instance of the controller.
     *
     * @return the AdminTabController instance
     */
    public static AdminTabController getInstance() {
        if (instance == null) {
            instance = new AdminTabController();
        }
        return instance;
    }

    /**
     * Returns the admin tab UI view.
     *
     * @return the admin tab
     */
    public AdminTab getAdminTab() {
        return adminTab;
    }

    /**
     * Handles action events triggered by the admin tab UI.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case AdminTab.ADD_CRYPTO_COMMAND:
                List<File> files = adminTab.getFilesDropped();
                if (files != null && !files.isEmpty()) {
                    CryptoManager cryptoManager = new CryptoManager();
                    try {
                        MessageDisplayer.displayInformativeMessage(cryptoManager.addCryptoFromFiles(files));
                        MarketManager.getMarketManager().restartMarket();
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                    adminTab.resetTab(cryptoManager.getAllCryptoNames());
                } else {
                    MessageDisplayer.displayError(FILE_MISSING);
                }
                break;

            case AdminTab.DEL_CRYPTO_COMMAND:
                String cryptoToDel = adminTab.getSelectedCrypto();
                if (cryptoToDel != null) {
                    CryptoManager cryptoManager = new CryptoManager();
                    try {
                        MarketManager.getMarketManager().stopMarket();
                        cryptoManager.deleteCrypto(cryptoToDel);
                        MarketManager.getMarketManager().restartMarket();
                        MessageDisplayer.displayInformativeMessage(cryptoToDel + " ha sucumbido a la selección natural\n(Ha sido eliminada)");
                        adminTab.resetTab(cryptoManager.getAllCryptoNames());
                        MarketTabController.getInstance().updateMarketTab();
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                } else {
                    MessageDisplayer.displayError(CRYPTO_NOT_SELECTED);
                }
        }
    }

}
