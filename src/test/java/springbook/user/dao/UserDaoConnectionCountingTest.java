package springbook.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoConnectionCountingTest {
    
    @Test
    public void counting() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        int expected = 0;

        dao.deleteAll();
        expected++;

        assertThat(dao.getCount()).isZero();
        expected++;
        
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setId("ldy" + i);
            user.setName("이동열");
            user.setPassword("test123");    
            
            dao.add(user);
            expected++;
        } 

        CountingDataSource countingDataSource = context.getBean("dataSource", CountingDataSource.class);

        assertThat(countingDataSource.getCounter()).isEqualTo(expected);
    }    
}
