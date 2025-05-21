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


    public MarketTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    public static MarketTabController getInstance() {
        if (instance == null) {
            instance = new MarketTabController();
        }
        return instance;
    }



    public MarketTab getMarketTab(boolean admin) {
        if (marketTab == null) {
            List<Crypto> cryptos = new CryptoManager().getAllCryptos();
            marketTab = new MarketTab(cryptos);
            attachTableMouseListener(admin);
        }
        return marketTab;
    }

    private void attachTableMouseListener(boolean admin) {
        marketTab.getTablaData().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = marketTab.getTablaData().getSelectedRow();
                int col = marketTab.getTablaData().getSelectedColumn();
                if (row != -1 && col == 0) {
                    try {
                        CryptoManager cryptoManager = CryptoManager.getCryptoManager();
                        String cryptoName = String.valueOf(marketTab.getTablaData().getValueAt(row, col));
                        CryptoInfoTabController.getInstance().displayCryptoInfo(cryptoManager.getCryptoByName(cryptoName),  admin ? CryptoInfo.MODE_ADMIN : CryptoInfo.MODE_BUY_CRYPTO);
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                }
            }
        });
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

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        marketTab = null;
        instance = null;
    }
}