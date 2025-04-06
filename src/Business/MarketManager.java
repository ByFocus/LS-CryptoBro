package Business;

import Business.Entities.Bot;

import java.util.ArrayList;
import java.util.List;

public class MarketManager {
    private List<Bot> bots;

    public MarketManager() {
        bots = createBots();
    }

    private List<Bot> createBots() {
        List<Bot> bots = new ArrayList<>();
        new CryptoManager().getAllCryptos()
    }
}
