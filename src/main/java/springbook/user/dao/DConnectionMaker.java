package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(URL, USER, PASSWORD);

        return c;
    }
}
