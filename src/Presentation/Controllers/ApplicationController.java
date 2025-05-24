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

/**
 * The type Application controller.
 */
public class ApplicationController implements EventListener {
    private static ApplicationController instance;

    private MainFrame appFrame;

    private ApplicationController() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    /**
     * New application.
     *
     * @param identifier the identifier
     * @param balance    the balance
     * @param gains      the gains
     * @param admin      the admin
     * @throws PersistanceException the persistance exception
     */
    public void newApplication(String identifier, String balance, String gains, boolean admin) {
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
        appFrame.setVisible(true);
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.USER_ESTIMATED_GAINS_CHANGED:
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
                try {
                    if (appFrame != null) {
                        double balance = AccountManager.getInstance().getCurrentUser().getBalance();
                        appFrame.setBalance(balance);
                    }
                } catch (NoCurrentUser _) {
                    //si está el admin no debe actualizarse
                }
                break;
        }
    }

    /**
     * Close.
     */
    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.USER_ESTIMATED_GAINS_CHANGED);
        MarketManager.getMarketManager().unsubscribe(this, EventType.USER_BALANCE_CHANGED);
        appFrame.close();
        appFrame = null;
    }
}
