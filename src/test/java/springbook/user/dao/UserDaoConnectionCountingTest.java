package springbook.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import springbook.user.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoConnectionCountingTest {
    
    @Autowired
    private UserDao dao;

    @Autowired
    private CountingDataSource dataSource;

    @Test
    public void counting() throws SQLException {
        int expected = 0;

        dao.deleteAll();
        expected++;

        assertThat(dao.getCount()).isZero();
        expected++;
        
        for (int i = 0; i < 5; i++) {
            User user = new User("ldy" + i, "이동열", "test123");
            
            dao.add(user);
            expected++;
        } 

        assertThat(dataSource.getCounter()).isEqualTo(expected);
    }    
}
