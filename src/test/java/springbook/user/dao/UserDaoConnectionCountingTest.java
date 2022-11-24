package springbook.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public class UserDaoConnectionCountingTest {
    
    @Autowired
    private UserDao dao;

    @Autowired
    private CountingDataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        users.add(new User("gyumee", "name1", "1234", Level.BASIC, 1, 0));
        users.add(new User("leegw700", "name2", "1234", Level.SILVER, 55, 10));
        users.add(new User("bumjin", "name3", "1234", Level.GOLD, 100, 40));
    }

    @Test
    public void counting() throws SQLException {
        int expected = 0;

        dao.deleteAll();
        expected++;

        assertThat(dao.getCount()).isZero();
        expected++;
        
        for (User user : users) {        
            dao.add(user);
            expected++;
        } 

        assertThat(dataSource.getCounter()).isEqualTo(expected);
    }    
}
