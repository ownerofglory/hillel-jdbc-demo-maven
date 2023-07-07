package ua.ithillel.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");


            // java.sql.Driver <-- com.mysql.cj.jdbc.Driver

            // host address
            // port: MYSQL - 3306
            // database name (optional)
            // username
            // password
            String host = System.getenv("JDBC_HOST");
            String dbName = System.getenv("JDBC_DB_NAME");
            String username = System.getenv("JDBC_USER");
            String password = System.getenv("JDBC_PASSWORD");

            String url = String.format("%s/%s", host, dbName);

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver class not found");
        }

        return null;
    }
}
