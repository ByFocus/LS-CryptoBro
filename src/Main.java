import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;
import Presentation.Controllers.LoadViewController;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        CryptoDAO cryptoDAO = new CryptoSQLDAO();
        LoadViewController loadView = new LoadViewController();

        loadView.start();


    }

    //Q&A
    // El calculate gains hay una función que no está hecha
    // La db debe handlear mas de una compra por user por una crypto?
}

