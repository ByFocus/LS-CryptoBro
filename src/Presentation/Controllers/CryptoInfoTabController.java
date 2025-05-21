package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Business.Entities.User;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CryptoInfoTabController implements EventListener, ActionListener {
    private List<CryptoInfo> cryptoInfos;
    private static CryptoInfoTabController instance;

    private CryptoInfoTabController() {
        cryptoInfos = new ArrayList<>();
    }

    public static CryptoInfoTabController getInstance() {
        if (instance == null) {
            instance = new CryptoInfoTabController();
        }
        return instance;
    }

    public void displayCryptoInfo(Crypto crypto, int mode) {
        CryptoInfo cryptoInfo =new CryptoInfo(crypto.getName(),mode);
        cryptoInfo.getGrafica().setMuestras( MarketManager.getMarketManager().getHistoricalValuesByCryptoName(crypto.getName()) );
        cryptoInfo.registerController(this);
        cryptoInfo.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                cryptoInfos.remove(cryptoInfo);

                if (cryptoInfos.isEmpty()) {
                    MarketManager.getMarketManager().unsubscribe(CryptoInfoTabController.this, EventType.NEW_HISTORICAL_VALUE);
                }
            }
        });
        cryptoInfos.add(cryptoInfo);
        cryptoInfo.setVisible(true);
        // si es el primer infopanel hay que suscribirse al evento
        if (cryptoInfos.size() == 1) {
            MarketManager.getMarketManager().subscribe(this, EventType.NEW_HISTORICAL_VALUE);
        }
    }

    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.NEW_HISTORICAL_VALUE:
                for (CryptoInfo cryptoInfo : cryptoInfos) {
                    cryptoInfo.getGrafica().setMuestras(MarketManager.getMarketManager().getHistoricalValuesByCryptoName(cryptoInfo.getCryptoName()));
                }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case CryptoInfo.BUY_CRYPTO:
                JButton sourceButton = (JButton) e.getSource();
                CryptoInfo cryptoInfo = (CryptoInfo) sourceButton.getClientProperty("parentCryptoInfo");
                int units = cryptoInfo.getAmountOfCrypto();
                if (units > 0) {
                    String cryptoName = cryptoInfo.getCryptoName();
                    try {
                        buyCrypto(cryptoName, units);
                        MessageDisplayer.displayInformativeMessage("Has comprado " + units + " " + cryptoName + "!\n Así se hace brother, tú pa lante como los de Alicante");
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                    cryptoInfo.resetAmount();
                } else {
                    MessageDisplayer.displayError("Brooooo, como vas a comprar 0?\nDe hecho, ¿qué es el ZerO?, tremenda rallada");
                }

                break;
        }
    }


    private void buyCrypto(String cryptoName, int units) throws BusinessExeption {
        WalletManager walletManager = WalletManager.getInstance();
        User user = AccountManager.getInstance().getCurrentUser();
        Crypto crypto = CryptoManager.getCryptoManager().getCryptoByName(cryptoName);
        walletManager.addTransaction(user, crypto, units);
    }

}
