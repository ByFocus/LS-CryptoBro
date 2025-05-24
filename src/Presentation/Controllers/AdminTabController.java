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
 * The type Admin tab controller.
 */
public class AdminTabController implements ActionListener {
    private static AdminTabController instance;
    private AdminTab adminTab;

    private AdminTabController() throws BusinessExeption{
            adminTab = new AdminTab(CryptoManager.getCryptoManager().getAllCryptoNames());
            adminTab.registerController(this);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AdminTabController getInstance() {
        if (instance == null) {
            instance = new AdminTabController();
        }
        return instance;
    }

    /**
     * Gets admin tab.
     *
     * @return the admin tab
     */
    public AdminTab getAdminTab() {
        return adminTab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case AdminTab.ADD_CRYPTO_COMMAND:
                List<File> files = adminTab.getFilesDropped();
                if(files != null && !files.isEmpty()) {
                    CryptoManager cryptoManager = new CryptoManager();
                    try {
                        MessageDisplayer.displayInformativeMessage(cryptoManager.addCryptoFromFiles(files));
                        MarketManager.getMarketManager().restartMarket();
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                    adminTab.resetTab(cryptoManager.getAllCryptoNames());
                }else {
                    MessageDisplayer.displayError("Brother, no has añadido ningun fichebro!");

                }
                break;

            case AdminTab.DEL_CRYPTO_COMMAND:
                String cryptoToDel = adminTab.getSelectedCryto();
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
                }else {
                    MessageDisplayer.displayError("No has seleccionado ninguna crypto, bro!");
                }
        }
    }

}
