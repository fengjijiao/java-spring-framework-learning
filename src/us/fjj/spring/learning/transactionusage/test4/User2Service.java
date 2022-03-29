package us.fjj.spring.learning.transactionusage.test4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class User2Service {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void required(String name) {
        this.jdbcTemplate.update("insert into user2(name) value (?)", name);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredException(String name) {
        this.jdbcTemplate.update("insert into user2(name) value (?)", name);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNew(String name) {
        this.jdbcTemplate.update("insert into user2 (name) value (?)", name);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewException(String name) {
        this.jdbcTemplate.update("insert into user2 (name) value (?)", name);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.NESTED)
    public void nested(String name) {
        this.jdbcTemplate.update("insert into user2(name) value (?)", name);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void nestedException(String name) {
        this.jdbcTemplate.update("insert into user2(name) value (?)", name);
        throw new RuntimeException();
    }
}
