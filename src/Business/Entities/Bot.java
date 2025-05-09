package Business.Entities;

import Business.CryptoManager;

import java.util.Random;

public class Bot extends Thread {
    private int buyingPeriod;
    private String cryptoName;

    public Bot(Crypto crypto) {
        //buyingPeriod
        cryptoName = crypto.getName();
        buyingPeriod = 5000/crypto.getVolatility();

    }

    @Override
    public void run() {
        while (isAlive()) { //TODO: A lo mejor es preferible tener un booleano
            try {
                boolean buy = new Random().nextBoolean();
                new CryptoManager().botMakeTransaction(cryptoName, buy);
                Thread.sleep(buyingPeriod);
            } catch (InterruptedException _) {
                //
            }

        }
    }

    public void kill() {
        interrupt();
    }

}
