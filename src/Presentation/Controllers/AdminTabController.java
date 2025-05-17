package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.CryptoManager;
import Business.Entities.Crypto;
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

public class AdminTabController implements ActionListener, EventListener {
    private static AdminTabController instance;
    private AdminTab adminTab;

    private AdminTabController() throws BusinessExeption{
            adminTab = new AdminTab(CryptoManager.getCryptoManager().getAllCryptoNames());
            adminTab.registerController(this);
    }

    public static AdminTabController getInstance() {
        if (instance == null) {
            instance = new AdminTabController();
        }
        return instance;
    }

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
                    MarketManager.getMarketManager().notify(EventType.NEW_CRYPTOS);
                    adminTab.resetTab(cryptoManager.getAllCryptoNames());
                }else {
                    MessageDisplayer.displayError("Brother, no has añadido ningun fichebro!");

                }
                break;
        }
    }

    private void handleFiles() {

    }
}
