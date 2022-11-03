package springbook.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import springbook.user.domain.User;

public class UserDaoConnectionCountingTest {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        int expected = 5;
        
        for (int i = 0; i < expected; i++) {
            User user = new User();
            user.setId("ldy" + i);
            user.setName("이동열");
            user.setPassword("test123");    
            
            dao.add(user);

            System.out.println(user.getId() + " 등록 성공");
        } 

        CountingDataSource countingDataSource = context.getBean("dataSource", CountingDataSource.class);

        System.out.println("연결 횟수" + countingDataSource.getCounter());
    }    
}
