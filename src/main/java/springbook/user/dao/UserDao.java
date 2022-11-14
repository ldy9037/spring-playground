package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import springbook.user.domain.User;

public class UserDao {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) throws SQLException {
        jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        return jdbcTemplate.queryForObject("select * from users where id = ?",
                new RowMapper<User>() {
                    @Override
                    @Nullable
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User(); 
                        user.setId(rs.getString( "id")); 
                        user.setName(rs.getString( "name")); 
                        user.setPassword(rs.getString("password"));
                        return user;
                    }                  
                }, new Object[] {id});
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() throws SQLException {
        return jdbcTemplate.query("select * from users order by id", 
                new RowMapper<User>() {
                    @Override
                    @Nullable
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User(); 
                        user.setId(rs.getString( "id")); 
                        user.setName(rs.getString( "name")); 
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                });
    }
}