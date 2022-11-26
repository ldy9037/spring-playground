package springbook.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        users.add(new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0));
        users.add(new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0));
        users.add(new User("erwins", "신승환", "p3", Level.SILVER, 60, 29));
        users.add(new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30));
        users.add(new User("green", "오민규", "p5", Level.GOLD, 100, 100));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);    
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }
}