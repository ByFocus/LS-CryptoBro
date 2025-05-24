package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Purchase;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tables.WalletTableModel;
import Presentation.View.Tabs.WalletTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Controller class responsible for managing the Wallet tab.
 * It handles UI updates and user interactions related to user purchases.
 */
public class WalletTabController implements EventListener, ActionListener {
    private WalletTab walletTab;
    private static WalletTabController instance;

    /**
     * Instantiates a new Wallet tab controller and subscribes it to relevant market events.
     */
    public WalletTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    /**
     * Gets the singleton instance of the WalletTabController.
     * Subscribes to additional events upon first instantiation.
     *
     * @return the instance
     */
    public static WalletTabController getInstance() {
        if (instance == null) {
            instance = new WalletTabController();
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_ESTIMATED_GAINS_CHANGED);
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_BALANCE_CHANGED);
        }
        return instance;
    }

    /**
     * Returns the wallet tab UI, ensuring it is updated with current purchase data.
     *
     * @return the wallet tab
     */
    public WalletTab getWalletTab() {
        updateWalletTab();
        return walletTab;
    }

    /**
     * Updates the wallet tab with the current user's purchase data.
     * Initializes the tab if it has not been created yet.
     */
    private void updateWalletTab() {
        String user = AccountManager.getInstance().getCurrentUserName();
        List<Purchase> compras = new WalletManager().getWalletByUserName(user);
        if (walletTab == null) {
            walletTab = new WalletTab(compras);
            SwingUtilities.invokeLater(() -> attachTableMouseListener());
        } else {
            walletTab.loadPurchasesData(compras);
        }
    }

    /**
     * Attaches a mouse listener to the table for responding to interactions (e.g., selling crypto).
     */
    private void attachTableMouseListener() {
        walletTab.getTableData().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = walletTab.getTableData().getSelectedRow();
                int col = walletTab.getTableData().getSelectedColumn();
                if (row != -1 && col == 6) {
                    try {
                        CryptoManager cryptoManager = CryptoManager.getCryptoManager();
                        String cryptoName = String.valueOf(walletTab.getTableData().getValueAt(row, 0));
                        CryptoInfoTabController.getInstance().displayCryptoInfo(
                                cryptoManager.getCryptoByName(cryptoName),
                                CryptoInfo.MODE_SELL_CRYPTO,
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
     * Handles updates triggered by subscribed event types.
     *
     * @param context the event context
     */
    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.USER_BALANCE_CHANGED:
            case EventType.USER_ESTIMATED_GAINS_CHANGED:
                updateWalletTab();
                break;
        }
    }

    /**
     * Not used for any actions currently.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // No actions currently handled
    }

    /**
     * Gets a purchase object based on the table row index.
     *
     * @param row the row index
     * @return the purchase at the specified row
     */
    public Purchase getPurchaseByRow(int row) {
        return ((WalletTableModel)walletTab.getTableData().getModel()).getPurchaseAtRow(row);
    }
}
