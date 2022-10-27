package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";
    
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(URL, USER, PASSWORD);

        return c;
    }
}
