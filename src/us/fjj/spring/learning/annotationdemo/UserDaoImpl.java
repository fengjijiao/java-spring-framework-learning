package us.fjj.spring.learning.annotationdemo;

import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl  implements UserDao {
    @Override
    public void outContent() {
        System.out.println("spring!!!");
    }
}
