import java.sql.Connection;
import java.sql.DriverManager;
public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5433/CarRental";
        String user = "postgres";
        String password = "Varma@123";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("CONNECTION SUCCESSFUL TO DB");
        } catch (Exception e) {
            System.out.println("CONNECTION FAILED: "+e.getMessage());
        }
    }
}

