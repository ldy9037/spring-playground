package springbook.user.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import springbook.user.domain.User;

public class UserDaoTest {

    @Test    
    public void addAndGet() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

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
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();
        
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> dao.get("unknown_id"));
    }

    @Test
    public void count() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount()).isZero();

        int expected = 5;

        for (int i = 1; i <= expected; i++) {
            dao.add(new User("ldy" + i, "이동열", "Test123"));
            assertThat(dao.getCount()).isEqualTo(i);
        }
    }
}
