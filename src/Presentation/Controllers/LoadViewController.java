package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.MarketManager;
import Presentation.View.LoadFrame;
import Presentation.View.Popups.MessageDisplayer;

import java.util.Random;

/**
 * The type Load view controller.
 */
public class LoadViewController {
    /**
     * The Max load.
     */
    public final int MAX_LOAD = 75;
    /**
     * The Min load.
     */
    public final int MIN_LOAD = 30;

    private final LoadFrame loadFrame;

    /**
     * Instantiates a new Load view controller.
     */
    public LoadViewController() {
        loadFrame = new LoadFrame();
    }


    /**
     * Start.
     */
    public void start() {
        loadFrame.setVisible(true);
        try {
            load();
            AccountViewController.getInstance().start();
        } catch (BusinessExeption e) {
            MessageDisplayer.displayError(e.getMessage());
        }
        loadFrame.dispose();
    }

    /**
     * Load.
     *
     * @throws BusinessExeption the business exeption
     */
    public void load() throws BusinessExeption {
        Random r= new Random();

        int loadMarket = r.nextInt(MAX_LOAD - MIN_LOAD + 1) + MIN_LOAD;

        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(30); // Simula tiempo de carga (ajusta según lo necesario)
                loadFrame.setProgress(i);
                if (i == loadMarket) {
                    MarketManager.getMarketManager().restartMarket();
                }
            } catch (InterruptedException _) {}
        }

        loadFrame.dispose();
    }
}
