package Presentation.Controllers;

import Business.CryptoManager;
import Business.Entities.Crypto;
import Business.EventType;
import Business.MarketManager;
import Persistance.CryptoFileReadingDAO;
import Presentation.View.Tabs.MarketTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        }
        return marketTab;
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
            case CRYPTO_VALUES_CHANGED:
                updateMarketTab();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}