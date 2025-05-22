package Business.Entities;

import Business.BusinessExceptions.BusinessExeption;
import Business.CryptoManager;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.Random;

/**
 * The type Bot.
 */
public class Bot extends Thread {
    private int buyingPeriod;
    private String cryptoName;
    private boolean running;

    /**
     * Instantiates a new Bot.
     *
     * @param crypto the crypto
     */
    public Bot(Crypto crypto) {
        //buyingPeriod
        cryptoName = crypto.getName();
        buyingPeriod = 5000/crypto.getVolatility();
        running = true;

    }

    @Override
    public void run() {
        while (running) { //TODO: A lo mejor es preferible tener un booleano
            try {
                boolean buy = new Random().nextBoolean();
                try {
                    CryptoManager.getCryptoManager().botMakeTransaction(cryptoName, buy);
                    Thread.sleep(buyingPeriod);
                } catch (BusinessExeption _) {

                }

            } catch (InterruptedException _) {
                //
            }

        }
    }

    /**
     * Kill.
     */
    public void kill() {
        interrupt();
        running = false;
    }

}
