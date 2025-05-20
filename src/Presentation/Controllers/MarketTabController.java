package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Business.Entities.User;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tabs.MarketTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MarketTabController implements EventListener, ActionListener {

    private MarketTab marketTab;
    private static MarketTabController instance;

    public MarketTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    public static MarketTabController getInstance() {
        if (instance == null) {
            instance = new MarketTabController();
        }
        return instance;
    }

    public MarketTab getMarketTab() {
        if (marketTab == null) {
            List<Crypto> cryptos = new CryptoManager().getAllCryptos();
            marketTab = new MarketTab(cryptos);
            marketTab.getTablaData().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = marketTab.getTablaData().getSelectedRow();
                    int col = marketTab.getTablaData().getSelectedColumn();
                    if (row != -1 && col == 0) {
                        try {
                            CryptoManager cryptoManager = CryptoManager.getCryptoManager();
                            String cryptoName = String.valueOf(marketTab.getTablaData().getValueAt(row, col));
                            displayCryptoInfo(cryptoManager.getCryptoByName(cryptoName));
                        } catch (BusinessExeption ex) {
                            MessageDisplayer.displayError(ex.getMessage());
                        }
                    }
                }
            });
        }
        return marketTab;
    }

    public void displayCryptoInfo(Crypto crypto) {
        CryptoInfo cryptoInfo = new CryptoInfo(crypto);
        cryptoInfo.registerController(this);
        cryptoInfo.setVisible(true);
    }

    public void updateMarketTab() {
        List<Crypto> cryptos = new CryptoManager().getAllCryptos();
        if (marketTab == null)    {
            marketTab = new MarketTab(cryptos);
        } else {
            marketTab.loadCryptoData(cryptos);

        }
    }


    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.CRYPTO_VALUES_CHANGED:
                //System.out.println("Value changed\n");
                updateMarketTab();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CryptoInfo.BUY_CRYPTO:
                JButton sourceButton = (JButton) e.getSource();
                CryptoInfo cryptoInfo = (CryptoInfo) sourceButton.getClientProperty("parentCryptoInfo");
                int units = cryptoInfo.getAmountOfCrypto();
                if (units > 0) {
                    String cryptoName = cryptoInfo.getCryptoName();
                    try {
                        buyCrypto(cryptoName, units);
                        MessageDisplayer.displayInformativeMessage("Has comprado " + units + " " + cryptoName + "!\n Así se hace brother, tú pa lante como los de Alicante");
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                    cryptoInfo.resetAmount();
                } else {
                    MessageDisplayer.displayError("Brooooo, como vas a comprar 0?\nDe hecho, ¿qué es el ZerO?, tremenda rallada");
                }

                break;
        }
    }

    private void buyCrypto(String cryptoName, int units) throws BusinessExeption {
        WalletManager walletManager = WalletManager.getInstance();
        User user = AccountManager.getInstance().getCurrentUser();
        Crypto crypto = CryptoManager.getCryptoManager().getCryptoByName(cryptoName);
        walletManager.addTransaction(user, crypto, units);
    }
}