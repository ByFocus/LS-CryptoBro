package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.CryptoManager;
import Business.Entities.Crypto;
import Business.Entities.Market;
import Business.EventType;
import Business.MarketManager;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tabs.AdminTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Admin tab controller.
 */
public class AdminTabController implements ActionListener, EventListener {
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
    public void update(EventType context) {
        switch (context) {
            default:
                break;

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case AdminTab.ADD_CRYPTO_COMMAND:
                List<File> files = adminTab.getFilesDropped();
                if(files != null && !files.isEmpty()) {
                    CryptoManager cryptoManager = new CryptoManager();
                    MessageDisplayer.displayInformativeMessage(cryptoManager.addCryptoFromFiles(files));
                    MarketManager.getMarketManager().restartMarket();
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

    private void handleFiles() {

    }
}
