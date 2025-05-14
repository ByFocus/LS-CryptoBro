package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.MarketManager;
import Presentation.View.LoadFrame;
import Presentation.View.Popups.MessageDisplayer;

public class LoadViewController {
    private final LoadFrame loadFrame;

    public LoadViewController() {
        loadFrame = new LoadFrame();
    }


    public void start() {
        loadFrame.setVisible(true);
        load();
        AccountViewController.getInstance().start();
    }

    public void load() {
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(30); // Simula tiempo de carga (ajusta según lo necesario)
                loadFrame.setProgress(i);
                if (i == 50) {
                    MarketManager.getMarketManager().startMarket();
                }
            } catch (InterruptedException _) {
            } catch (BusinessExeption e) {
                MessageDisplayer.displayError(e.getMessage());
            }
        }

        loadFrame.dispose();
    }
}
