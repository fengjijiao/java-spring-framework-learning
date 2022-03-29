package us.fjj.spring.learning.transactionusage.test7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class Test7Test {
    JdbcTemplate jdbcTemplate;
    PlatformTransactionManager platformTransactionManager;
    @BeforeEach
    public void before() {
        //定义一个数据源
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("df");
        dataSource.setPassword("efg");
        dataSource.setInitialSize(5);
        //定义一个JdbcTemplate，用来方便执行数据库增删改查
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.platformTransactionManager = new DataSourceTransactionManager(dataSource);
        this.jdbcTemplate.update("truncate table user1");
    }

    @Test
    public void test1() throws Exception {
        System.out.println("PROPAGATION_REQUIRED start");
        //2.定义事务属性：TransactionDefinition，TransactionDefinition可以用来配置事务的属性信息，比如事务隔离级别、事务超时时间、事务传播方式、是否只读事务等等。
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
        //3.开启事务：调用platformTransactionManager.getTransaction开启事务操作，得到事务状态(TransactionStatus)对象
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        this.addSynchronization("ts-1", 2);
        this.addSynchronization("ts-2", 1);
        //4.执行业务操作，下面就执行2个插入操作
        jdbcTemplate.update("insert into user1 (name) value (?)", "test1-1");
        jdbcTemplate.update("insert into user1 (name) value (?)", "test1-2");
        this.m1();
        //5.提交事务：platformTransactionManager.commit
        System.out.println("PROPAGATION_REQUIRED 准备commit");
        platformTransactionManager.commit(transactionStatus);
        System.out.println("PROPAGATION_REQUIRED commit完毕");
        System.out.println("after:"+jdbcTemplate.queryForList("select * from user1"));
        /**
         * PROPAGATION_REQUIRED start
         * PROPAGATION_REQUIRES_NEW start
         * ts-2:suspend
         * ts-1:suspend
         * PROPAGATION_REQUIRES_NEW 准备commit
         * ts-4:beforeCommit:false
         * ts-3:beforeCommit:false
         * ts-4:beforeCompletion
         * ts-3:beforeCompletion
         * ts-4:afterCommit
         * ts-3:afterCommit
         * ts-4:afterCompletion:0
         * ts-3:afterCompletion:0
         * ts-2:resume
         * ts-1:resume
         * PROPAGATION_REQUIRES_NEW commit完毕
         * PROPAGATION_REQUIRED 准备commit
         * ts-2:beforeCommit:false
         * ts-1:beforeCommit:false
         * ts-2:beforeCompletion
         * ts-1:beforeCompletion
         * ts-2:afterCommit
         * ts-1:afterCommit
         * ts-2:afterCompletion:0
         * ts-1:afterCompletion:0
         * PROPAGATION_REQUIRED commit完毕
         * after:[{id=1, name=test1-1}, {id=2, name=test1-2}, {id=3, name=test2-1}, {id=4, name=test2-2}]
         */
    }

    public void m1() {
        System.out.println("PROPAGATION_REQUIRES_NEW start");
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        jdbcTemplate.update("insert into user1 (name) value (?)", "test2-1");
        jdbcTemplate.update("insert into user1 (name) value (?)", "test2-2");
        this.addSynchronization("ts-3", 2);
        this.addSynchronization("ts-4", 1);
        System.out.println("PROPAGATION_REQUIRES_NEW 准备commit");
        platformTransactionManager.commit(transactionStatus);
        System.out.println("PROPAGATION_REQUIRES_NEW commit完毕");
    }

    public void addSynchronization(final String name, final int order) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public int getOrder() {
                    return order;
                }

                @Override
                public void suspend() {
                    System.out.println(name+":suspend");
                }

                @Override
                public void resume() {
                    System.out.println(name+":resume");
                }

                @Override
                public void flush() {
                    System.out.println(name+":flush");
                }

                @Override
                public void beforeCommit(boolean readOnly) {
                    System.out.println(name+":beforeCommit:"+readOnly);
                }

                @Override
                public void beforeCompletion() {
                    System.out.println(name+":beforeCompletion");
                }

                @Override
                public void afterCommit() {
                    System.out.println(name+":afterCommit");
                }

                @Override
                public void afterCompletion(int status) {
                    System.out.println(name+":afterCompletion:"+status);
                }
            });
        }
    }
































}
