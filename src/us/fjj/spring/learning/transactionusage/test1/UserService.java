package us.fjj.spring.learning.transactionusage.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;

    //模拟业务操作1
    public void bus1() {
        this.transactionTemplate.executeWithoutResult(transactionStatus -> {
            //先删除表数据
            this.jdbcTemplate.update("delete from user");
            //调用bus2
            this.bus2();
        });
    }

    /**
     * bus1中会先删除数据，然后调用bus3，此时bus1中的所有操作和bus2中的所有操作会被放在一个事务中执行，这是spring内部的默认实现，bus1中调用executeWithoutResult的时候，会开启一个事务，而内部又会调用bus2，而bus2内部也调用了executeWithoutResult，bus内部会先判断以下上下文环境中有没有事务，如果有就直接参与到已存在的事务中，刚好发现有bus1已开启的事务，所以就直接参与到bus1的事务中了，最终bus1和bus2会在一个事务中运行。
     */

    //模拟业务操作2
    public void bus2() {
        this.transactionTemplate.executeWithoutResult(transactionStatus -> {
            this.jdbcTemplate.update("insert into user (username, password) value (?, ?)", "java", "javapwd");
            this.jdbcTemplate.update("insert into user (username, password) value (?, ?)", "java2", "javapwd");
            this.jdbcTemplate.update("insert into user (username, password) value (?, ?)", "java3", "javapwd");
        });
    }

    //查询表中所有数据
    public List userList() {
        return jdbcTemplate.queryForList("select * from user");
    }
}
