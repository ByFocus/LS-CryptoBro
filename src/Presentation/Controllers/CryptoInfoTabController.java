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
 * Controller for handling crypto info popups.
 * Manages actions such as buying/selling cryptos and price updates.
 */
public class CryptoInfoTabController implements EventListener, ActionListener {
    private final String BUY_ZERO = "Brooooo, como vas a comprar 0?\nDe hecho, ¿qué es el ZerO?, tremenda rallada";
    private final String SELL_ZERO = "¿Vender 0? ¿Brother, seguro que eso es rentable?";
    private final String INCORRECT_VALUE = "Hermano, reales positivos, anda";
    private final String INCORRECT_FLOAT_FORMAT = "Si quieres poner decimales usa el punto (.)";
    private final String NO_NUMBER = "Tú no has visto un número en tu vida";

    private List<CryptoInfo> cryptoInfos;
    private static CryptoInfoTabController instance;

    /**
     * Private constructor for singleton pattern.
     */
    private CryptoInfoTabController() {
        cryptoInfos = new ArrayList<>();
    }

    /**
     * Gets the singleton instance.
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
     * Displays the CryptoInfo popup for a given crypto.
     *
     * @param crypto the crypto to show
     * @param mode   the interaction mode (buy/sell)
     * @param row    the row in the table associated
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
        CryptoInfo cryptoInfo = new CryptoInfo(cryptoName, mode, units, row);

        cryptoInfo.updateData(MarketManager.getMarketManager().getHistoricalValuesByCryptoName(crypto.getName()));

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

        if (cryptoInfos.size() == 1) {
            MarketManager.getMarketManager().subscribe(this, EventType.NEW_HISTORICAL_VALUE);
        }
    }

    /**
     * Reacts to new historical data updates.
     *
     * @param context the triggered event
     */
    @Override
    public void update(EventType context) {
        switch (context) {
            case EventType.NEW_HISTORICAL_VALUE:
                for (CryptoInfo cryptoInfo : cryptoInfos) {
                    cryptoInfo.updateData(MarketManager.getMarketManager().getHistoricalValuesByCryptoName(cryptoInfo.getCryptoName()));
                }
                break;
        }
    }

    /**
     * Handles button actions in the CryptoInfo popup.
     *
     * @param e the action event
     */
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
                    MessageDisplayer.displayError(BUY_ZERO);
                }
                break;

            case CryptoInfo.SELL_CRYPTO:
                int sellUnits = Integer.parseInt(cryptoInfo.getAmountLabel());

                if (sellUnits > 0) {
                    try {
                        Purchase purchase = WalletTabController.getInstance().getPurchaseByRow(cryptoInfo.getRow());
                        sellCrypto(purchase, sellUnits);
                        MessageDisplayer.displayInformativeMessage("Has vendido " + sellUnits + " " + cryptoName + "!\n !Así se hace brother, de aquí a la luna!");
                        cryptoInfo.modifyUnits(-1 * sellUnits);
                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }
                    cryptoInfo.resetAmount();
                } else {
                    MessageDisplayer.displayError(SELL_ZERO);
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
                        MessageDisplayer.displayError(INCORRECT_VALUE);
                    }
                } catch (NumberFormatException ex) {
                    if (cryptoInfo.getAmountLabel().contains(",")) {
                        MessageDisplayer.displayError(INCORRECT_FLOAT_FORMAT);
                    } else {
                        MessageDisplayer.displayError(NO_NUMBER);
                    }
                } catch (BusinessExeption ex2) {
                    MessageDisplayer.displayError(ex2.getMessage());
                }
                break;
        }
    }

    /**
     * Buys the specified amount of a given crypto.
     *
     * @param cryptoName the crypto name
     * @param units      the amount to buy
     * @throws BusinessExeption if the transaction fails
     */
    private void buyCrypto(String cryptoName, int units) throws BusinessExeption {
        WalletManager walletManager = WalletManager.getInstance();
        User user = AccountManager.getInstance().getCurrentUser();
        Crypto crypto = CryptoManager.getCryptoManager().getCryptoByName(cryptoName);
        walletManager.addTransaction(user, crypto, units);
    }

    /**
     * Sells the specified units of a purchase.
     *
     * @param purchase the purchase
     * @param units    the units to sell
     * @throws BusinessExeption if the transaction fails
     */
    private void sellCrypto(Purchase purchase, int units) throws BusinessExeption {
        WalletManager walletManager = WalletManager.getInstance();
        User user = AccountManager.getInstance().getCurrentUser();
        walletManager.removeTransaction(user, purchase, units);
    }

    /**
     * Closes all open CryptoInfo windows and clears controller state.
     */
    public void close() {
        try {
            MarketManager.getMarketManager().unsubscribe(this, EventType.NEW_HISTORICAL_VALUE);
        } catch (NullPointerException ex) {
            // If exception occurs, controller was not subscribed
        }
        for (CryptoInfo cryptoInfo : cryptoInfos) {
            cryptoInfo.dispose();
        }
        cryptoInfos.clear();
        cryptoInfos = null;
        instance = null;
    }
}
