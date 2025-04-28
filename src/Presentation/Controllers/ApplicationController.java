package Presentation.Controllers;

import Business.MarketManager;
import Business.EventType;

public class ApplicationController implements EventListener{
    private MarketManager market;

    public ApplicationController() {
        market = new MarketManager();
        market.subscribe(this, EventType.USER_BALANCE_CHANGED);
        market.subscribe(this, EventType.CRYPTO_PRICE_CHANGED);
    }

    @Override
    public void update(EventType context) {

        switch (context) {
            case EventType.CRYPTO_PRICE_CHANGED:
                break;
            case EventType.USER_BALANCE_CHANGED:
                break;
            case EventType.NEW_HISTORICAL_VALUE:
                break;
            default:
                // si es algun evento que no nos interessa se ignora
                break;
        }

    }
}
