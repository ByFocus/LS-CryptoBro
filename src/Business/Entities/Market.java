package Business.Entities;

import Business.BusinessExceptions.DataPersistanceError;
import Business.CryptoManager;
import Business.EventType;
import Business.MarketManager;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.*;

public class Market extends Thread{
    private Map<String, Queue<Double>> hitoricalValues = new HashMap<>();
    private List<Bot> bots;
    private final static int MAX_SIZE = 120; // 10 min every 5 secs
    private final int TIME_TO_GET = 5000;

    public Market(List<Bot> bots, List<String> cryptoNames) {
        this.bots = bots;
        for (String cryptoName : cryptoNames) {
            hitoricalValues.put(cryptoName, new LinkedList<>());
        }
    }

    @Override
    public void start() {
        for (Bot bot : bots) {
            bot.start();
        }
        super.start();
    }

    @Override
    public void run() {
        while (isAlive()) { //TODO: A lo mejor es preferible tener un booleano
            try {
                CryptoManager c = new CryptoManager();
                for (Map.Entry<String, Queue<Double>> entry : hitoricalValues.entrySet()) {
                    Queue<Double> queue = entry.getValue();
                    if (queue.size() == MAX_SIZE) {
                        queue.poll(); // treu l'element més antic
                    }
                    queue.offer(c.getCryptoByName(entry.getKey()).getCurrentPrice());
                }
                MarketManager.getMarketManager().notify(EventType.NEW_HISTORICAL_VALUE);
                Thread.sleep(TIME_TO_GET);
            } catch (InterruptedException _) {
                //
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }

        }
    }

    public void kill() {
        interrupt();
    }

    public Queue<Double> getHistoricalFromCrypto(String cryptoName) {
        return hitoricalValues.get(cryptoName);
    }
}
