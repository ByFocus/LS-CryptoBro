import persistance.SQLConnector;
import persistance.User;
import persistance.UserSQLDAO;

public class Main {
    public static void main(String[] args) {
        // Get database connection
        SQLConnector connector = SQLConnector.getInstance();

        // Create DAO instance
        UserSQLDAO userDAO = new UserSQLDAO();

        // Create a new user
        User newUser = new User(
                "pol_porres",
                "pol.porres@example.com",
                "securePassword123"
        );



                boolean userDeleted = userDAO.deleteUser("pol_porres");
                if(userDeleted){
                    System.out.println("User deleted!");
                }else{
                    System.out.println("Error delenting user");
                }
            }

    }
