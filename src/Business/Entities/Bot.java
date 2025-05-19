package Business.Entities;

import Business.CryptoManager;

import java.util.Random;

public class Bot extends Thread {
    private int buyingPeriod;
    private String cryptoName;
    private boolean running;

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
                CryptoManager.getCryptoManager().botMakeTransaction(cryptoName, buy);
                Thread.sleep(buyingPeriod);
            } catch (InterruptedException _) {
                //
            }

        }
    }

    public void kill() {
        interrupt();
        running = false;
    }

}
