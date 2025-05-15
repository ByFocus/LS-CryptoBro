package Presentation.Controllers;

import Business.Entities.User;
import Business.MarketManager;
import Business.EventType;
import Presentation.View.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ApplicationController implements EventListener{
    private static ApplicationController instance;

    private  MainFrame appFrame;

    private ApplicationController() {
        //todo esto no, lo ponemos en lo otro

    }

    public static ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    public void newApplication(String userName, String balance, boolean admin) {
        // CAMBIAMOS LOS VALORES DEL APPFRAM Y LO OTRO
        appFrame = MainFrame.configureApp(userName, balance, admin);

        appFrame.registerController().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AccountViewController.getInstance().checkUserProfile();
            }
        });

        MarketManager market = MarketManager.getMarketManager();
        market.subscribe(this, EventType.USER_BALANCE_CHANGED);
        market.subscribe(this, EventType.USER_ESTIMATED_GAINS_CHANGED);
        appFrame.configureTabs(admin);
        appFrame.setVisible(true);
    }

    @Override
    public void update(EventType context) {

        switch (context) {
            case EventType.CRYPTO_PRICE_CHANGED:
                break;
            case EventType.USER_BALANCE_CHANGED:
                break;
            case EventType.NEW_HISTORICAL_VALUE:
                //RSI_NewHistorical(): demana l'historic corresponent i li passa a la view, pq actualitzi el gràfic
                break;
        }
    }

    public void userConfirmed(boolean admin) {
        appFrame.configureTabs(admin);
        appFrame.setVisible(true);
    }

    public void close() {
        appFrame.dispose();
    }
}
