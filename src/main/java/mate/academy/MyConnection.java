package mate.academy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static final String dataBass = "jdbc:mysql://s8.thehost.com.ua:3306/home";
    private static final String name = "IgorArs";
    private static final String pass = "123123Qw";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dataBass, name, pass);
        } catch (SQLException e) {
            throw new RuntimeException("No connection to the database.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can not find driver.", e);
        }
    }
}
