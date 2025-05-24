package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Bot;
import Business.Entities.Crypto;

import java.util.*;

import Business.Entities.Market;
import Business.Entities.Muestra;
import Persistance.ConfigurationDAO;
import Persistance.ConfigurationJSONDAO;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.Controllers.EventListener;

/**
 * The type Market manager.
 */
public class MarketManager  {
    private Market market;
    private Map<EventType, List<EventListener>> listeners = new HashMap<>();

    private static MarketManager instance;

    private MarketManager() {
    }

    /**
     * Gets market manager.
     *
     * @return the market manager
     */
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

    /**
     * Stop market.
     */
    public synchronized void stopMarket() {
        if (market != null) {
            market.kill();
            market = null;
        }
    }

    /**
     * Restart market.
     */
    public synchronized void restartMarket() {
        stopMarket();
        startMarket();
    }
    private void createMarket() throws BusinessExeption {
        try {
            List<Bot> bots = new ArrayList<>();
            List<Crypto> cryptoList = new CryptoManager().getAllCryptos();
            List<String> cryptoNames = new ArrayList<>();
            for (Crypto crypto : cryptoList) {
                bots.add(new Bot(crypto));
                cryptoNames.add(crypto.getName());
            }
            ConfigurationDAO confDAO = new ConfigurationJSONDAO();

            market = new Market(bots, cryptoNames, confDAO.getPollingInterval() ,confDAO.getMaximumDataPoints());
        }catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Gets historical values by crypto name.
     *
     * @param cryptoName the crypto name
     * @return the historical values by crypto name
     */
    public LinkedList<Muestra> getHistoricalValuesByCryptoName(String cryptoName) {
        return market.getHistoricalFromCrypto(cryptoName);
    }

    /**
     * Notify.
     *
     * @param event the event
     */
    public synchronized void notify(EventType event) {
        List<EventListener> subscribers = listeners.get(event);
        if (subscribers != null) {
            for (EventListener eventListener : subscribers) {
                eventListener.update(event);
            }
        }
    }

    /**
     * Subscribe.
     *
     * @param eventListener the event listener
     * @param event         the event
     */
    public synchronized void subscribe(EventListener eventListener, EventType event) {
        List<EventListener> listSubscribers = listeners.get(event);
        if (listSubscribers == null) {
            listSubscribers = new ArrayList<>();
            listeners.put(event, listSubscribers);
        }
        listSubscribers.add(eventListener);
    }

    /**
     * Unsubscribe.
     *
     * @param eventListener the event listener
     * @param event         the event
     */
    public synchronized void unsubscribe(EventListener eventListener, EventType event) {
        if (listeners != null) {
            listeners.get(event).remove(eventListener);
        }
    }



}
