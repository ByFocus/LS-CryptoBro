package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;
import Presentation.View.Tables.WalletTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
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

    public void displayCryptoInfo(Crypto crypto, int mode, int row) {
        CryptoInfo cryptoInfo =new CryptoInfo(crypto.getName(),mode, row);
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
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        CryptoInfo cryptoInfo = (CryptoInfo) sourceButton.getClientProperty("parentCryptoInfo");
        String cryptoName = cryptoInfo.getCryptoName();

        switch (e.getActionCommand()) {
            case CryptoInfo.BUY_CRYPTO:
                int units = Integer.parseInt(cryptoInfo.getAmountLabel());
                if (units > 0) {
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
            case CryptoInfo.SELL_CRYPTO:
                JButton sellButton = (JButton) e.getSource();
                CryptoInfo sellCryptoInfo = (CryptoInfo) sellButton.getClientProperty("parentCryptoInfo");
                int sellUnits = Integer.parseInt(sellCryptoInfo.getAmountLabel());

                if (sellUnits > 0) {
                    try {
                        Purchase purchase = WalletTabController.getInstance().getPurchaseByRow(cryptoInfo.getRow());
                        sellCrypto(purchase, sellUnits);
                        MessageDisplayer.displayInformativeMessage("Has vendido " + sellUnits + " " + cryptoName + "!\n !Así se hace brother, de aquí a la luna!");
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }

                    sellCryptoInfo.resetAmount();

                } else {
                    MessageDisplayer.displayError("¿Vender 0? ¿Brother, seguro que eso es rentable?");
                }
                break;

            case CryptoInfo.CHANGE_PRICE:
                try {
                    String amount = cryptoInfo.getAmountLabel();
                    double price = Double.parseDouble(amount);
                    if (price > 0) {
                        CryptoManager.getCryptoManager().updateCryptoPrice(cryptoName, price);
                        MessageDisplayer.displayInformativeMessage("Eres Dios, ahora " + cryptoName + " vale " + String.format("%.4f", price));
                    } else {
                        MessageDisplayer.displayError("Hermano, reales positivos, anda");
                    }
                } catch (NumberFormatException ex) {
                    if (cryptoInfo.getAmountLabel().contains(",")) {
                        MessageDisplayer.displayError("Si quieres poner decimales usa el punto (.)");
                    } else {
                        MessageDisplayer.displayError("Tú no has visto un número en tu vida");
                    }
                } catch (BusinessExeption ex2) {
                    MessageDisplayer.displayError(ex2.getMessage());
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

    private void sellCrypto(Purchase purchase, int units) throws BusinessExeption {
        WalletManager walletManager = WalletManager.getInstance();
        User user = AccountManager.getInstance().getCurrentUser();
        walletManager.removeTransaction(user, purchase, units);

    }

    public void close() {
        for (CryptoInfo cryptoInfo : cryptoInfos) {
            cryptoInfo.dispose();
        }
        cryptoInfos.clear();
        cryptoInfos = null;
        instance = null;
    }
}
