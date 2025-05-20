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
        }
        return instance;
    }

    public WalletTab getWalletTab() {
        if (walletTab == null) {
            String user = AccountManager.getInstance().getCurrentUserName();
            List<Purchase> compras = new WalletManager().getWalletByUserName(user);
            walletTab = new WalletTab(compras);
        }
        return walletTab;
    }
/*
    public void updateMarketTab() {
        List<Crypto> cryptos = new CryptoManager().getAllCryptos();
        if (marketTab == null)    {
            marketTab = new MarketTab(cryptos);
        } else {
            marketTab.loadCryptoData(cryptos);

        }
    }
*/
    @Override
    public void update(EventType context) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
