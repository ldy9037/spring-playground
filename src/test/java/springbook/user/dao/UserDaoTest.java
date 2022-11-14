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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @BeforeEach
    void setUp() {

    }

    @Test    
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        List<User> users = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            User user = new User("ldy" + i, "이동열", "test123");

            dao.add(user);
            users.add(user);
        }

        assertThat(dao.getCount()).isEqualTo(users.size());

        for (User expected : users) {
            User user = dao.get(expected.getId());

            assertThat(user.getId()).isEqualTo(expected.getId());
            assertThat(user.getName()).isEqualTo(expected.getName());
            assertThat(user.getPassword()).isEqualTo(expected.getPassword());   
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

        int expected = 5;

        for (int i = 1; i <= expected; i++) {
            dao.add(new User("ldy" + i, "이동열", "Test123"));
            assertThat(dao.getCount()).isEqualTo(i);
        }
    }

    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isZero();

        User user1 = new User("gyumee", "name1", "1234");
        dao.add(user1);
        List<User> users1 = dao.getAll();
        checkSameUser(user1, users1.get(0));

        User user2 = new User("leegw700", "name2", "1234");
        dao.add(user2);
        List<User> users2 = dao.getAll();
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        User user3 = new User("bumjin", "name3", "1234");
        dao.add(user3);
        List<User> users3 = dao.getAll();
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }
}
