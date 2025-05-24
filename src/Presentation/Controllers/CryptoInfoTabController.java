package Presentation.Controllers;

import Business.*;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Presentation.View.Popups.CryptoInfo;
import Presentation.View.Popups.MessageDisplayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Crypto info tab controller.
 */
public class CryptoInfoTabController implements EventListener, ActionListener {
    private List<CryptoInfo> cryptoInfos;
    private static CryptoInfoTabController instance;

    private CryptoInfoTabController() {
        cryptoInfos = new ArrayList<>();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CryptoInfoTabController getInstance() {
        if (instance == null) {
            instance = new CryptoInfoTabController();
        }
        return instance;
    }

    /**
     * Display crypto info.
     *
     * @param crypto the crypto
     * @param mode   the mode
     * @param row    the row
     */
    public void displayCryptoInfo(Crypto crypto, int mode, int row) {
        String cryptoName = crypto.getName();

        int units = 0;

        if (mode == CryptoInfo.MODE_SELL_CRYPTO) {
            units = WalletTabController.getInstance().getPurchaseByRow(row).getUnits();
        } else if (mode == CryptoInfo.MODE_BUY_CRYPTO) {
            String userName = AccountManager.getInstance().getCurrentUserName();
            units = WalletManager.getInstance().getNumCryptoInWallet(userName, cryptoName);
        }
        CryptoInfo cryptoInfo =new CryptoInfo(cryptoName, mode, crypto.getInitialPrice(), units, row);

        cryptoInfo.getGraph().setMuestras( MarketManager.getMarketManager().getHistoricalValuesByCryptoName(crypto.getName()) );

        cryptoInfo.registerController(this);
        cryptoInfo.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                if (cryptoInfos != null) {
                    cryptoInfos.remove(cryptoInfo);
                    if (cryptoInfos.isEmpty()) {
                        MarketManager.getMarketManager().unsubscribe(CryptoInfoTabController.this, EventType.NEW_HISTORICAL_VALUE);
                    }
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
                    cryptoInfo.getGraph().setMuestras(MarketManager.getMarketManager().getHistoricalValuesByCryptoName(cryptoInfo.getCryptoName()));
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
                        cryptoInfo.modifyUnits(units);
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
                int sellUnits = Integer.parseInt(cryptoInfo.getAmountLabel());

                if (sellUnits > 0) {
                    try {
                        Purchase purchase = WalletTabController.getInstance().getPurchaseByRow(cryptoInfo.getRow());
                        sellCrypto(purchase, sellUnits);
                        MessageDisplayer.displayInformativeMessage("Has vendido " + sellUnits + " " + cryptoName + "!\n !Así se hace brother, de aquí a la luna!");
                        cryptoInfo.modifyUnits(-1*sellUnits);
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }

                    cryptoInfo.resetAmount();

                } else {
                    MessageDisplayer.displayError("¿Vender 0? ¿Brother, seguro que eso es rentable?");
                }
                break;

            case CryptoInfo.CHANGE_PRICE:
                try {
                    String amount = cryptoInfo.getAmountLabel();
                    cryptoInfo.resetAmount();
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

    /**
     * Close.
     */
    public void close() {
        try {
            MarketManager.getMarketManager().unsubscribe(this, EventType.NEW_HISTORICAL_VALUE);
        } catch (NullPointerException ex) {
            // si salta esta excepción es porque no estaba el controller suscrito, ya que no había ninguna cryptoInfo abierta
        }
        for (CryptoInfo cryptoInfo : cryptoInfos) {
            cryptoInfo.dispose();
        }
        cryptoInfos.clear();
        cryptoInfos = null;
        instance = null;
    }
}
