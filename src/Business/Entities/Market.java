package Business.Entities;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.CryptoManager;
import Business.EventType;
import Business.MarketManager;

import java.util.*;

/**
 * The type Market.
 * Simulates a live cryptocurrency market by managing bots and storing historical price data.
 * Periodically polls crypto prices and notifies the market manager of updates.
 */
public class Market extends Thread{
    private Map<String, LinkedList<Sample>> hitoricalValues = new HashMap<>();
    private List<Bot> bots;
    private int pollingRate;
    private int maxNumPoints;
    private boolean running;

    /**
     * Instantiates a new Market.
     *
     * @param bots          the list of bots trading in the market
     * @param cryptoNames   the list of crypto names to monitor
     * @param pollingRate   the polling rate in seconds
     * @param maxNumPoints  the maximum number of historical data points per cryptocurrency
     */
    public Market(List<Bot> bots, List<String> cryptoNames, double pollingRate, int maxNumPoints) {
        this.bots = bots;
        this.pollingRate = (int)pollingRate*1000;
        this.maxNumPoints = maxNumPoints;
        for (String cryptoName : cryptoNames) {
            hitoricalValues.put(cryptoName, new LinkedList<>());
        }
        running = true;
    }

    /**
     * Starts all bots and begins the market polling thread.
     */
    @Override
    public void start() {
        for (Bot bot : bots) {
            bot.start();
        }
        super.start();
    }

    /**
     * Market loop that polls the current price of each cryptocurrency
     * and updates its historical record.
     */
    @Override
    public void run() {
        while (running) {
            try {
                CryptoManager c = new CryptoManager();
                Date date = new Date();
                for (Map.Entry<String, LinkedList<Sample>> entry : hitoricalValues.entrySet()) {
                    LinkedList<Sample> list = entry.getValue();
                    if (list.size() == maxNumPoints) {
                        list.removeFirst(); // remove the oldest entry
                    }

                    list.addLast(new Sample(c.getCryptoByName(entry.getKey()).getCurrentPrice(), date));
                }
                MarketManager.getMarketManager().notify(EventType.NEW_HISTORICAL_VALUE);
                Thread.sleep(pollingRate);
            } catch (InterruptedException _) {
                // Thread interrupted, exit loop
            } catch (BusinessExeption e) {
                throw new DataPersistanceError(e.getMessage());
            }

        }
    }

    /**
     * Stops the market simulation and all associated bots.
     */
    public void kill() {
        for (Bot bot : bots) {
            bot.kill();
        }
        interrupt();
        running = false;
    }

    /**
     * Returns a copy of the historical price data for a given cryptocurrency.
     *
     * @param cryptoName the name of the cryptocurrency
     * @return a cloned LinkedList of Sample entries
     */
    public LinkedList<Sample> getHistoricalFromCrypto(String cryptoName) {
        return (LinkedList<Sample>) hitoricalValues.get(cryptoName).clone();
    }
}
