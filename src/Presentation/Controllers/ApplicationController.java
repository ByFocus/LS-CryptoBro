package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.NoCurrentUser;
import Business.MarketManager;
import Business.EventType;
import Business.WalletManager;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.MainFrame;
import Presentation.View.Popups.CryptoInfo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ApplicationController implements EventListener {
    private static ApplicationController instance;

    private MainFrame appFrame;
    private CryptoInfo cryptoInfoFrame;

    private ApplicationController() {
        MarketManager.getMarketManager().subscribe(this, EventType.CRYPTO_VALUES_CHANGED);

    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    public void newApplication(String identifier, String balance, String gains, boolean admin) throws PersistanceException {
        // CAMBIAMOS LOS VALORES DEL APPFRAM Y LO OTRO
        appFrame = new MainFrame(identifier, balance, gains);

        appFrame.configureTabs(admin);

        appFrame.getUserPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AccountViewController.getInstance().checkUserProfile();
            }
        });


        MarketManager market = MarketManager.getMarketManager();
        market.subscribe(this, EventType.USER_BALANCE_CHANGED);
        market.subscribe(this, EventType.USER_ESTIMATED_GAINS_CHANGED);
        market.subscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        appFrame.setVisible(true);
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.CRYPTO_VALUES_CHANGED:
                try {
                    if (appFrame != null) {
                        String userName = AccountManager.getInstance().getCurrentUserName();
                        double estimatedGains = WalletManager.getInstance().calculateEstimatedGainsByUserName(userName);
                        appFrame.setEstimatedGains(estimatedGains);
                    }
                } catch (NoCurrentUser _) {
                    //si está el admin no debe actualizarse
                }
                break;
            case EventType.USER_BALANCE_CHANGED:
                double balance = AccountManager.getInstance().getCurrentUser().getBalance();
                appFrame.setBalance(balance);
                break;
        }
    }

    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.CRYPTO_VALUES_CHANGED);
        MarketManager.getMarketManager().unsubscribe(this, EventType.USER_BALANCE_CHANGED);
        appFrame.close();
        appFrame = null;
    }
}
