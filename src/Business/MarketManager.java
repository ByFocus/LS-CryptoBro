package Business;

import Business.Entities.Bot;
import Business.Entities.Crypto;

import java.util.ArrayList;
import Presentation.Controllers.EventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketManager {
    private List<Bot> bots;
    private Map<EventType, List<EventListener>> listeners = new HashMap<>();
    private static MarketManager instance;

    public static MarketManager getMarketManager() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }

    private MarketManager() {
        bots = createBots();
    }

    public void startMarket() {
        for (Bot bot : bots) {
            bot.start();
        }
    }

    public void stopMarket() {
        for (Bot bot : bots) {
            bot.kill();
        }
    }

    private List<Bot> createBots() {
        List<Bot> bots = new ArrayList<>();
        List<Crypto> cryptoList = new CryptoManager().getAllCryptos();
        for (Crypto crypto : cryptoList) {
            bots.add(new Bot(crypto));
        }
        return bots;
    }

    private void notify(EventType event) {
        List<EventListener> subscribers = listeners.get(event);
        for (EventListener eventListener : subscribers) {
            eventListener.update(event);
        }
    }

    public void subscribe(EventListener eventListener, EventType event) {
        listeners.get(event).add(eventListener);
    }
    public void unsubscribe(EventListener eventListener, EventType event) {
        listeners.get(event).remove(eventListener);
    }
}
