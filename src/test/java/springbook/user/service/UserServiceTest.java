package springbook.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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

    static class TestUserLevelUpgradePolicyImpl extends UserLevelUpgradePolicyImpl {
        private String id;
        
        private TestUserLevelUpgradePolicyImpl(String id) {
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserUpgradeException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserUpgradeException extends RuntimeException {
    }

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        users.add(new User("bumjin", "박범진", "p1", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER - 1, 0));
        users.add(new User("joytouch", "강명성", "p2", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0));
        users.add(new User("erwins", "신승환", "p3", Level.SILVER, 60, UserService.MIN_RECCOMEND_FOR_GOLD - 1));
        users.add(new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserService.MIN_RECCOMEND_FOR_GOLD));
        users.add(new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE));
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);    
    }

    @Test
    public void add() {
        userDao.deleteAll();
        
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        checkLevel(userWithLevelRead, userWithLevel.getLevel());
        checkLevel(userWithoutLevelRead, Level.BASIC);
    }

    @Test
    public void upgradeAllOrNothing() {
        UserLevelUpgradePolicyImpl testUserLevelUpgradePolicyTest = new TestUserLevelUpgradePolicyImpl(users.get(3).getId());
        testUserLevelUpgradePolicyTest.setUserDao(userDao);
        
        UserService testUserService = new UserService();
        testUserService.setUserDao(userDao);
        testUserService.setUserLevelUpgradePolicy(testUserLevelUpgradePolicyTest);

        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserUpgradeException expected");
        } catch (TestUserUpgradeException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }
}