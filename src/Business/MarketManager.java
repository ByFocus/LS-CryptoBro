package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Bot;
import Business.Entities.Crypto;

import java.util.*;

import Business.Entities.Market;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.Controllers.EventListener;

public class MarketManager  {
    private Market market;
    private Map<EventType, List<EventListener>> listeners = new HashMap<>();

    private static MarketManager instance;

    private MarketManager() {
    }

    public static synchronized MarketManager getMarketManager() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }
    private synchronized void startMarket() {
        if (market == null || !market.isAlive()) {
            createMarket();
            market.start();
        }
    }

    public synchronized void stopMarket() {
        if (market != null) {
            market.kill();
            market = null;
        }
    }

    public synchronized void restartMarket() {
        stopMarket();
        startMarket();
    }
    private void createMarket() {
        try {
            List<Bot> bots = new ArrayList<>();
            List<Crypto> cryptoList = new CryptoManager().getAllCryptos();
            List<String> cryptoNames = new ArrayList<>();
            for (Crypto crypto : cryptoList) {
                bots.add(new Bot(crypto));
                cryptoNames.add(crypto.getName());
            }
            market = new Market(bots, cryptoNames);
        }catch (BusinessExeption e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    public LinkedList<Double> getHistoricalValuesByCryptoName(String cryptoName) {
        return market.getHistoricalFromCrypto(cryptoName);
    }

    public synchronized void notify(EventType event) {
        List<EventListener> subscribers = listeners.get(event);
        if (subscribers != null) {
            for (EventListener eventListener : subscribers) {
                eventListener.update(event);
            }
        }
    }

    public synchronized void subscribe(EventListener eventListener, EventType event) {
        List<EventListener> listSubscribers = listeners.get(event);
        if (listSubscribers == null) {
            listSubscribers = new ArrayList<>();
            listeners.put(event, listSubscribers);
        }
        listSubscribers.add(eventListener);
    }
    public synchronized void unsubscribe(EventListener eventListener, EventType event) {
        listeners.get(event).remove(eventListener);
    }



}
