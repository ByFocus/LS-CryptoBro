package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Business.Entities.Market;
import Business.Entities.User;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tabs.MarketTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarketTabController implements EventListener, ActionListener {
    private static MarketTabController instance;

    private MarketTab marketTab;
    private List<CryptoInfo> cryptoInfos;

    public MarketTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        cryptoInfos = new ArrayList<>();
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
            attachTableMouseListener();
        }
        return marketTab;
    }

    private void attachTableMouseListener() {
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

    public void displayCryptoInfo(Crypto crypto) {
        CryptoInfo cryptoInfo =new CryptoInfo(crypto.getName());
        cryptoInfo.getGrafica().setMuestras( MarketManager.getMarketManager().getHistoricalValuesByCryptoName(crypto.getName()) );
        cryptoInfo.registerController(this);
        cryptoInfo.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                cryptoInfos.remove(cryptoInfo);

                if (cryptoInfos.isEmpty()) {
                    MarketManager.getMarketManager().unsubscribe(MarketTabController.this, EventType.NEW_HISTORICAL_VALUE);
                }
            }
        });
        cryptoInfos.add(cryptoInfo);
        cryptoInfo.setVisible(true);
        // si es el primer infopanel hay que suscribirse al evento
        if (cryptoInfos.size() == 1) {
            MarketManager.getMarketManager().subscribe(this, EventType.NEW_HISTORICAL_VALUE);
        }
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
                updateMarketTab();
                break;
            case EventType.NEW_HISTORICAL_VALUE:
                for (CryptoInfo cryptoInfo : cryptoInfos) {
                    cryptoInfo.getGrafica().setMuestras(MarketManager.getMarketManager().getHistoricalValuesByCryptoName(cryptoInfo.getCryptoName()));
                }
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