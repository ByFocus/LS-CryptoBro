package Presentation.Controllers;

import Business.AccountManager;
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
        //todo esto no, lo ponemos en lo otro
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


        if (admin) {

        } else {
            //Controller wallet
        }

        MarketManager market = MarketManager.getMarketManager();
        market.subscribe(this, EventType.USER_BALANCE_CHANGED);
        market.subscribe(this, EventType.USER_ESTIMATED_GAINS_CHANGED);

        appFrame.setVisible(true);
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.CRYPTO_VALUES_CHANGED:
                if (appFrame != null) {
                    String userName = AccountManager.getInstance().getCurrentUserName();
                    double estimatedGains = WalletManager.getInstance().calculateEstimatedGainsByUserName(userName);
                    appFrame.setEstimatedGains(estimatedGains);
                }
                break;
            case EventType.USER_BALANCE_CHANGED:
                double balance = AccountManager.getInstance().getCurrentUser().getBalance();
                appFrame.setBalance(balance);
                break;
            case EventType.NEW_HISTORICAL_VALUE:
                //RSI_NewHistorical(): demana l'historic corresponent i li passa a la view, pq actualitzi el gràfic
                break;
        }
    }

    public void close() {
        appFrame.dispose();
    }
}
