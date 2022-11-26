package springbook.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        users.add(new User("gyumee", "name1", "1234", Level.BASIC, 1, 0));
        users.add(new User("leegw700", "name2", "1234", Level.SILVER, 55, 10));
        users.add(new User("bumjin", "name3", "1234", Level.GOLD, 100, 40));
    }

    @Test    
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        for (User user : users) {
            dao.add(user);
        }

        assertThat(dao.getCount()).isEqualTo(users.size());

        for (User expected : users) {
            User user = dao.get(expected.getId());

            checkSameUser(user, expected);
        }
    }

    @Test
    public void getUserFailure() throws Exception {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();
        
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> dao.get("unknown_id"));
    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        for (int i = 1; i < users.size(); i++) {
            dao.add(users.get(i));
            assertThat(dao.getCount()).isEqualTo(i);
        }
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isZero();

        dao.add(users.get(0));
        List<User> users1 = dao.getAll();
        checkSameUser(users.get(0), users1.get(0));

        dao.add(users.get(1));
        List<User> users2 = dao.getAll();
        checkSameUser(users.get(0), users2.get(0));
        checkSameUser(users.get(1), users2.get(1));

        dao.add(users.get(2));
        List<User> users3 = dao.getAll();
        checkSameUser(users.get(2), users3.get(0));
        checkSameUser(users.get(0), users3.get(1));
        checkSameUser(users.get(1), users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(users.get(0));

        assertThatExceptionOfType(DuplicateKeyException.class)
                .isThrownBy(() -> dao.add(users.get(0)));
    }

    @Test
    public void update() {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        dao.add(users.get(0));
        
        users.get(0).setName("오민규");
        users.get(0).setPassword("springno6");
        users.get(0).setLevel(Level.GOLD);
        users.get(0).setLogin(1000);
        users.get(0).setRecommend(999);
        dao.update(users.get(0));
        
        User user1update = dao.get(users.get(0).getId());
        checkSameUser(users.get(0), user1update);
    }
}
