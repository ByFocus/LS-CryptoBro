package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
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
 * The type Wallet tab controller.
 */
public class WalletTabController implements EventListener, ActionListener {
    private WalletTab walletTab;
    private static WalletTabController instance;

    /**
     * Instantiates a new Wallet tab controller.
     */
    public WalletTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static WalletTabController getInstance() {
        if (instance == null) {
            instance = new WalletTabController();
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_ESTIMATED_GAINS_CHANGED); // si la crypto que tiene el user cambia esto pasa
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_BALANCE_CHANGED); //si el balance cambia, el
        }
        return instance;
    }

    /**
     * Gets wallet tab.
     *
     * @return the wallet tab
     */
    public WalletTab getWalletTab() {
        updateWalletTab();
        return walletTab;
    }

    private void updateWalletTab() {
        String user = AccountManager.getInstance().getCurrentUserName();
        List<Purchase> compras = new WalletManager().getWalletByUserName(user);
        if (walletTab == null) {
            walletTab = new WalletTab(compras);
            SwingUtilities.invokeLater(() ->attachTableMouseListener());
        } else {
            walletTab.loadPurchasesData(compras);
        }
    }

    private void attachTableMouseListener() {
        walletTab.getTablaData().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = walletTab.getTablaData().getSelectedRow();
                int col = walletTab.getTablaData().getSelectedColumn();
                if (row != -1 && col == 4) {
                    try {
                        CryptoManager cryptoManager = CryptoManager.getCryptoManager();
                        String cryptoName = String.valueOf(walletTab.getTablaData().getValueAt(row, 0));
                        CryptoInfoTabController.getInstance().displayCryptoInfo(cryptoManager.getCryptoByName(cryptoName),  CryptoInfo.MODE_SELL_CRYPTO, row);
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.USER_BALANCE_CHANGED:
            case EventType.USER_ESTIMATED_GAINS_CHANGED:
                updateWalletTab();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * Gets purchase by row.
     *
     * @param row the row
     * @return the purchase by row
     */
    public Purchase getPurchaseByRow(int row) {
        return ((WalletTableModel)walletTab.getTablaData().getModel()).getPurchaseAtRow(row);
    }
}
