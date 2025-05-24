package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tabs.MarketTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The controller for the market tab in the GUI.
 * Handles user interactions with the market table, including updates when crypto prices change.
 */
public class MarketTabController implements EventListener, ActionListener {
    private static MarketTabController instance;

    private MarketTab marketTab;

    /**
     * Instantiates a new Market tab controller and subscribes it to crypto value change events.
     */
    public MarketTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    /**
     * Gets the singleton instance of MarketTabController.
     *
     * @return the instance
     */
    public static MarketTabController getInstance() {
        if (instance == null) {
            instance = new MarketTabController();
        }
        return instance;
    }

    /**
     * Gets the market tab, initializing it if necessary.
     *
     * @param admin whether the current user is an admin
     * @return the market tab
     */
    public MarketTab getMarketTab(boolean admin) {
        if (marketTab == null) {
            List<Crypto> cryptos = new CryptoManager().getAllCryptos();
            marketTab = new MarketTab(cryptos);
            SwingUtilities.invokeLater(() -> attachTableMouseListener(admin));
        }
        return marketTab;
    }

    /**
     * Attaches a mouse listener to the table to open the CryptoInfo popup when clicking a crypto name.
     *
     * @param admin whether the current user is an admin
     */
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
                        CryptoInfoTabController.getInstance().displayCryptoInfo(
                                cryptoManager.getCryptoByName(cryptoName),
                                admin ? CryptoInfo.MODE_ADMIN : CryptoInfo.MODE_BUY_CRYPTO,
                                row
                        );
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Updates the market tab with the latest crypto data.
     */
    public void updateMarketTab() {
        List<Crypto> cryptos = new CryptoManager().getAllCryptos();
        if (marketTab == null) {
            marketTab = new MarketTab(cryptos);
        } else {
            marketTab.loadCryptoData(cryptos);

        }
    }

    /**
     * Responds to crypto value change events by updating the market tab.
     *
     * @param context the event type
     */
    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.CRYPTO_VALUES_CHANGED:
                updateMarketTab();
                break;

        }
    }

    /**
     * Currently unused action handler.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // No actions currently handled
    }

    /**
     * Unsubscribes the controller from events and clears the market tab reference.
     */
    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        marketTab = null;
        instance = null;
    }
}
