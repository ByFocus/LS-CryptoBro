package Business.Entities;

import Business.BusinessExceptions.BusinessExeption;
import Business.CryptoManager;

import java.util.Random;

/**
 * The type Bot.
 * Simulates a trading bot that performs random transactions on a given cryptocurrency
 * at a frequency based on its volatility.
 */
public class Bot extends Thread {
    private int buyingPeriod;
    private String cryptoName;
    private boolean running;

    /**
     * Instantiates a new Bot.
     *
     * @param crypto the cryptocurrency to trade
     */
    public Bot(Crypto crypto) {
        cryptoName = crypto.getName();
        buyingPeriod = 5000 / crypto.getVolatility();
        running = true;
    }

    /**
     * Runs the bot loop.
     * Randomly decides to buy or sell, and performs a transaction through CryptoManager.
     */
    @Override
    public void run() {
        while (running) {
            try {
                boolean buy = new Random().nextBoolean();
                try {
                    CryptoManager.getCryptoManager().botMakeTransaction(cryptoName, buy);
                    Thread.sleep(buyingPeriod);
                } catch (BusinessExeption _) {
                }
            } catch (InterruptedException _) {
            }
        }
    }

    /**
     * Terminates the bot by stopping its loop and interrupting the thread.
     */
    public void kill() {
        interrupt();
        running = false;
    }
}
