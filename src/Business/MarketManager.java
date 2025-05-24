package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Bot;
import Business.Entities.Crypto;
import Business.Entities.Market;
import Business.Entities.Sample;
import Persistance.ConfigurationDAO;
import Persistance.ConfigurationJSONDAO;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.Controllers.EventListener;

import java.util.*;

/**
 * The type MarketManager.
 * Manages the market simulation, including bots, historical data, and event subscription.
 * Implements the observer pattern for notifying listeners of market events.
 */
public class MarketManager {
    private Market market;
    private Map<EventType, List<EventListener>> listeners = new HashMap<>();
    private static MarketManager instance;

    private MarketManager() {}

    /**
     * Gets the singleton instance of MarketManager.
     *
     * @return the market manager instance
     */
    public static synchronized MarketManager getMarketManager() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }

    /**
     * Starts the market simulation if it's not already running.
     */
    private synchronized void startMarket() {
        if (market == null || !market.isAlive()) {
            createMarket();
            market.start();
        }
    }

    /**
     * Stops the market simulation.
     */
    public synchronized void stopMarket() {
        if (market != null) {
            market.kill();
            market = null;
        }
    }

    /**
     * Restarts the market simulation by stopping and then starting it again.
     */
    public synchronized void restartMarket() {
        stopMarket();
        startMarket();
    }

    /**
     * Creates the market instance with configured polling rate and historical depth.
     *
     * @throws BusinessExeption if configuration or crypto retrieval fails
     */
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

            market = new Market(bots, cryptoNames, confDAO.getPollingInterval(), confDAO.getMaximumDataPoints());
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Retrieves historical price samples for a given cryptocurrency.
     *
     * @param cryptoName the name of the cryptocurrency
     * @return a list of historical samples
     */
    public LinkedList<Sample> getHistoricalValuesByCryptoName(String cryptoName) {
        return market.getHistoricalFromCrypto(cryptoName);
    }

    /**
     * Notifies all subscribed listeners about a specific event.
     *
     * @param event the event to notify about
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
     * Subscribes a listener to a specific event type.
     *
     * @param eventListener the listener to subscribe
     * @param event         the event type to subscribe to
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
     * Unsubscribes a listener from a specific event type.
     *
     * @param eventListener the listener to unsubscribe
     * @param event         the event type to unsubscribe from
     */
    public synchronized void unsubscribe(EventListener eventListener, EventType event) {
        if (listeners != null) {
            listeners.get(event).remove(eventListener);
        }
    }
}
