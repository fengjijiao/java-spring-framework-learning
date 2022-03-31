package us.fjj.spring.learning.transactioninterceptorordercontrolusage.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void addUser() {
        System.out.println("-----------------UserService.addUser start------------------");
        this.jdbcTemplate.update("insert into t_user(name) value (?)", "张三");
        System.out.println("-----------------UserService.addUser end------------------");
    }
}
