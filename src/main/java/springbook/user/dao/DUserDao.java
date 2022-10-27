package springbook.user.dao;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DUserDao extends UserDao {
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return null;
    }
}