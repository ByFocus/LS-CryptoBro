package Presentation.Controllers;

import Business.*;
import Business.Entities.Purchase;
import Presentation.View.Tabs.WalletTab;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class WalletTabController implements EventListener, ActionListener {
    private WalletTab walletTab;
    private static WalletTabController instance;

    public WalletTabController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
    }

    public static WalletTabController getInstance() {
        if (instance == null) {
            instance = new WalletTabController();
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_ESTIMATED_GAINS_CHANGED); // si la crypto que tiene el user cambia esto pasa
            MarketManager.getMarketManager().subscribe(instance, EventType.USER_BALANCE_CHANGED); //si el balance cambia, el
        }
        return instance;
    }

    public WalletTab getWalletTab() {
        updateWalletTab();
        return walletTab;
    }

    private void updateWalletTab() {
        String user = AccountManager.getInstance().getCurrentUserName();
        List<Purchase> compras = new WalletManager().getWalletByUserName(user);
        if (walletTab == null) {
            walletTab = new WalletTab(compras);
        } else {
            walletTab.loadPurchasesData(compras);
        }
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
}
