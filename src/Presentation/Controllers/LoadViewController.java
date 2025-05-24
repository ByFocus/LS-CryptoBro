package Presentation.Controllers;

import Business.BusinessExceptions.BusinessExeption;
import Business.MarketManager;
import Presentation.View.LoadFrame;
import Presentation.View.Popups.MessageDisplayer;

import java.util.Random;

/**
 * The type Load view controller.
 * Handles the loading screen and initializes the market system during app startup.
 */
public class LoadViewController {

    /**
     * The maximum value at which the market will be initialized.
     */
    public final int MAX_LOAD = 75;

    /**
     * The minimum value at which the market will be initialized.
     */
    public final int MIN_LOAD = 30;

    private final LoadFrame loadFrame;

    /**
     * Instantiates a new Load view controller and prepares the loading frame.
     */
    public LoadViewController() {
        loadFrame = new LoadFrame();
    }

    /**
     * Starts the loading process.
     * Shows the loading frame, simulates progress, and launches the user interface.
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
     * Simulates a loading bar and initializes the MarketManager at a random loading point.
     *
     * @throws BusinessExeption if the market fails to restart
     */
    public void load() throws BusinessExeption {
        Random r = new Random();
        int loadMarket = r.nextInt(MAX_LOAD - MIN_LOAD + 1) + MIN_LOAD;

        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(30); // Simulate load time
                loadFrame.setProgress(i);
                if (i == loadMarket) {
                    MarketManager.getMarketManager().restartMarket();
                }
            } catch (InterruptedException _) {
                // Interrupted during sleep, safe to ignore
            }
        }

        loadFrame.dispose();
    }
}
