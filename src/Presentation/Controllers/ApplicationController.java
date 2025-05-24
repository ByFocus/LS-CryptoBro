package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.NoCurrentUser;
import Business.MarketManager;
import Business.EventType;
import Business.WalletManager;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller for the main application window.
 * Manages the lifecycle of the application interface and updates user data based on events.
 */
public class ApplicationController implements EventListener {
    private static ApplicationController instance;

    private MainFrame appFrame;

    /**
     * Private constructor for singleton pattern.
     */
    private ApplicationController() {
    }

    /**
     * Returns the singleton instance of the ApplicationController.
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
     * Initializes and launches the application window.
     *
     * @param identifier the user or admin identifier
     * @param balance    the balance to display
     * @param gains      the estimated gains to display
     * @param admin      whether the current user is an admin
     * @throws PersistanceException if an error occurs during data loading
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

    /**
     * Handles updates based on event notifications.
     *
     * @param context the triggered event
     */
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
                    // Skip update if admin is logged in
                }
                break;
            case EventType.USER_BALANCE_CHANGED:
                try {
                    if (appFrame != null) {
                        double balance = AccountManager.getInstance().getCurrentUser().getBalance();
                        appFrame.setBalance(balance);
                    }
                } catch (NoCurrentUser _) {
                    // Skip update if admin is logged in
                }
                break;
        }
    }

    /**
     * Closes the application window and unsubscribes from event listeners.
     */
    public void close() {
        MarketManager.getMarketManager().unsubscribe(this, EventType.USER_ESTIMATED_GAINS_CHANGED);
        MarketManager.getMarketManager().unsubscribe(this, EventType.USER_BALANCE_CHANGED);
        appFrame.close();
        appFrame = null;
    }
}
