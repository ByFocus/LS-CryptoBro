package Business.Entities;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.CryptoManager;
import Business.EventType;
import Business.MarketManager;
import Persistance.PersistanceExceptions.PersistanceException;

import javax.xml.crypto.Data;
import java.util.*;

/**
 * The type Market.
 */
public class Market extends Thread{
    private Map<String, LinkedList<Muestra>> hitoricalValues = new HashMap<>();
    private List<Bot> bots;
    private int pollingRate;
    private int maxNumPoints;
    private boolean running;

    /**
     * Instantiates a new Market.
     *
     * @param bots        the bots
     * @param cryptoNames the crypto names
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

    @Override
    public void start() {
        for (Bot bot : bots) {
            bot.start();
        }
        super.start();
    }

    @Override
    public void run() {
        while (running) { //TODO: A lo mejor es preferible tener un booleano
            try {
                CryptoManager c = new CryptoManager();
                Date date = new Date();
                for (Map.Entry<String, LinkedList<Muestra>> entry : hitoricalValues.entrySet()) {
                    LinkedList<Muestra> list = entry.getValue();
                    if (list.size() == maxNumPoints) {
                        list.removeFirst(); // treu l'element més antic
                    }

                    list.addLast(new Muestra(c.getCryptoByName(entry.getKey()).getCurrentPrice(), date));
                }
                MarketManager.getMarketManager().notify(EventType.NEW_HISTORICAL_VALUE);
                Thread.sleep(pollingRate);
            } catch (InterruptedException _) {
                //
            } catch (BusinessExeption e) {
                throw new DataPersistanceError(e.getMessage());
            }

        }
    }

    /**
     * Kill.
     */
    public void kill() {
        for (Bot bot : bots) {
            bot.kill();
        }
        interrupt();
        running = false;
    }

    /**
     * Gets historical from crypto.
     *
     * @param cryptoName the crypto name
     * @return the historical from crypto
     */
    public LinkedList<Muestra> getHistoricalFromCrypto(String cryptoName) {
        LinkedList<Muestra> copyHistoric= (LinkedList<Muestra>) hitoricalValues.get(cryptoName).clone();
        return copyHistoric;
    }
}
