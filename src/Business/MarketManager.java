package Business;

import Business.Entities.Bot;
import Business.Entities.Crypto;

import java.util.ArrayList;
import java.util.List;

public class MarketManager {
    private List<Bot> bots;

    public MarketManager() {
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
}
