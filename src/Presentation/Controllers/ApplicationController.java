package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.CryptoManager;
import Business.MarketManager;
import Business.EventType;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.MainFrame;
import Presentation.View.Popups.CryptoInfo;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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

        appFrame.registerController().addMouseListener(new MouseAdapter() {
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
                // se tiene que mirar si las ganancias del user han cambiado
                break;
            case EventType.USER_BALANCE_CHANGED:
                double balance = AccountManager.getInstance().getCurrentUser().getBalance();
                appFrame.setBalance(balance);
                //AccountViewController.getInstance().setBalance();
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
