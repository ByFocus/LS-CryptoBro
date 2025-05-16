package Business;

import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Bot;
import Business.Entities.Crypto;

import java.util.*;

import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.Controllers.EventListener;

public class MarketManager extends Thread {
    private List<Bot> bots;
    private Map<EventType, List<EventListener>> listeners = new HashMap<>();
    private Map<String, Queue<Double>> hitoricalValues = new HashMap<>();
    private static MarketManager instance;
    private final static int MAX_SIZE = 120; // 10 min every 5 secs
    private final int TIME_TO_GET = 5000;

    private MarketManager() {
        createBotsAndHistorics();
    }

    public static MarketManager getMarketManager() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }

    public void startMarket() {
        if (!isAlive()) {
            for (Bot bot : bots) {
                bot.start();
            }
            this.start();
        }
    }

    public void stopMarket() {
        for (Bot bot : bots) {
            bot.kill();
        }
        this.kill();
    }

    private void createBotsAndHistorics() {
        try {
            bots = new ArrayList<>();
            List<Crypto> cryptoList = new CryptoManager().getAllCryptos();
            for (Crypto crypto : cryptoList) {
                bots.add(new Bot(crypto));
                hitoricalValues.put(crypto.getName(), new LinkedList<>());
            }
        }catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public Queue<Double> getHistoricalValuesByCryptoName(String cryptoName) {
        return hitoricalValues.get(cryptoName);
    }

    public void notify(EventType event) {
        List<EventListener> subscribers = listeners.get(event);
        if (subscribers != null) {
            for (EventListener eventListener : subscribers) {
                eventListener.update(event);
            }
        }
    }

    public void subscribe(EventListener eventListener, EventType event) {
        List<EventListener> listSubscribers = listeners.get(event);
        if (listSubscribers == null) {
            listSubscribers = new ArrayList<>();
        }
        listSubscribers.add(eventListener);
    }
    public void unsubscribe(EventListener eventListener, EventType event) {
        listeners.get(event).remove(eventListener);
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
                notify(EventType.NEW_HISTORICAL_VALUE);
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
}
