package group.phenikaa.hotelmanager.asm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String DB_URL = "jdbc:mysql://avnadmin:AVNS_fLXK_Ki8PUpk5ejsf0Z@mysql-177353fb-st-3589.i.aivencloud.com:12362/defaultdb?ssl-mode=REQUIRED";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to the MySQL database successfully!");
        } catch (SQLException e) {
            System.out.println("Connection to MySQL database failed.");
            e.printStackTrace();
        }
        return connection;
    }
}
