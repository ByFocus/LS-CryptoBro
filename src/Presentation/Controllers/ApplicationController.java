package Presentation.Controllers;

import Business.MarketManager;

public class ApplicationController implements EventListener{
    private MarketManager market;

    public ApplicationController() {
        market = new MarketManager();
    }

    @Override
    public void update(String context) {
        switch (context) {
            case "market":
                break;
        }
    }
}
