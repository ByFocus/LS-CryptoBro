import Persistance.CryptoDAO;
import Persistance.CryptoSQLDAO;
import Presentation.View.ViewController;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CryptoDAO cryptoDAO = new CryptoSQLDAO();
        ViewController vc = new ViewController();
        vc.start();



    }

    //Q&A
    // El calculate gains hay una función que no está hecha
    // La db debe handlear mas de una compra por user por una crypto?
}

