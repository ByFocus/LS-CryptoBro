import Presentation.Controllers.LoadViewController;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application, starts de loadViewController.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        LoadViewController loadView = new LoadViewController();
        loadView.start();
    }
}

