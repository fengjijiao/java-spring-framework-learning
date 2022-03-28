package us.fjj.spring.learning.transactionusage.test2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void insert(String username, String password) {
        jdbcTemplate.update("insert into user (username, password) value (?, ?);", username, password);
        throw new RuntimeException("人为异常！");
    }
}
