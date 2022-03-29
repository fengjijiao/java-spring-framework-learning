package us.fjj.spring.learning.transactionusage.test5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Ds1User1Service {
    @Autowired
    private JdbcTemplate jdbcTemplate1;

    @Transactional(transactionManager = "transactionManager1", propagation = Propagation.REQUIRED)
    public void required(String name) {
        this.jdbcTemplate1.update("insert into user1 (name) VALUE (?)", name);
    }













}
