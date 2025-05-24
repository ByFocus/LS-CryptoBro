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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Market tab controller.
 */
public class MarketTabController implements EventListener, ActionListener {
    private static MarketTabController instance;

    private MarketTab marketTab;


    /**
     * Instantiates a new Market tab controller.
     */
    public MarketTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static MarketTabController getInstance() {
        if (instance == null) {
            instance = new MarketTabController();
        }
        return instance;
    }

    private void initializeMarketTab(boolean admin) {
        List<Crypto> cryptos = new CryptoManager().getAllCryptos();
        marketTab = new MarketTab(cryptos);
        attachTableMouseListener(admin);
    }
    /**
     * Gets market tab.
     *
     * @param admin the admin
     * @return the market tab
     */
    public MarketTab getMarketTab(boolean admin) {
        if (marketTab == null) {
            List<Crypto> cryptos = new CryptoManager().getAllCryptos();
            marketTab = new MarketTab(cryptos);
            SwingUtilities.invokeLater(() ->attachTableMouseListener(admin));
        }
        return marketTab;
    }

    private void attachTableMouseListener(boolean admin) {
        marketTab.getTablaData().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouseClicked");
                int row = marketTab.getTablaData().getSelectedRow();
                int col = marketTab.getTablaData().getSelectedColumn();
                if (row != -1 && col == 0) {
                    try {
                        CryptoManager cryptoManager = CryptoManager.getCryptoManager();
                        String cryptoName = String.valueOf(marketTab.getTablaData().getValueAt(row, col));
                        CryptoInfoTabController.getInstance().displayCryptoInfo(cryptoManager.getCryptoByName(cryptoName),  admin ? CryptoInfo.MODE_ADMIN : CryptoInfo.MODE_BUY_CRYPTO, row);
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Update market tab.
     */
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

    /**
     * Close.
     */
    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        marketTab = null;
        instance = null;
    }
}