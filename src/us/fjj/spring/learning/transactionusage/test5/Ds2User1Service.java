package us.fjj.spring.learning.transactionusage.test5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Ds2User1Service {
    @Autowired
    private JdbcTemplate jdbcTemplate2;

    @Transactional(transactionManager = "transactionManager2", propagation = Propagation.REQUIRED)
    public void required(String name) {
        this.jdbcTemplate2.update("insert into user1 (name) VALUE (?)", name);
    }













}
