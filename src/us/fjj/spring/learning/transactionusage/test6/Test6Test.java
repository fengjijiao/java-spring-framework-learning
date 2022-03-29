package us.fjj.spring.learning.transactionusage.test6;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Test6Test {
    private User1Service user1Service;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void before() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        this.user1Service = context.getBean(User1Service.class);
        this.jdbcTemplate = context.getBean("jdbcTemplate1", JdbcTemplate.class);
        jdbcTemplate.update("truncate table user1");
        jdbcTemplate.update("truncate table user2");
    }

    @AfterEach
    public void after() {
        System.out.println("user1表数据："+this.jdbcTemplate.queryForList("select * from user1"));
        System.out.println("user2表数据："+this.jdbcTemplate.queryForList("select * from user2"));
    }

    /**
     * * 大家觉得required方法执行完毕之后，会是什么结果？
     *      * A: 张三未插入，李四插入成功
     *      * B: 张三、李四均未插入
     *      *
     *      * A
     */
    @Test
    public void test1() {
        this.user1Service.required();
        /**
         * user1表数据：[]
         * user2表数据：[]
         *
         * java.lang.RuntimeException
         */
    }
    /**
     * 结果是都没有插入
     *
     * 结果分析
     * 分析一下执行过程
     * 1.this.user1Service.required();
     * 2.事务拦截器拦截user1Service.required()方法，事务配置信息；（事务管理器transactionManager1，数据源dataSource，传播行为REQUIRED）
     * 3.因为user1Service.required上传播行为是required，因此先询问transactionManager1是否有事务，transactionManager1发现没有，则创建一个事务tm1（先创建连接或创建事务），通过transactionManager1中的dataSource重新获取一个连接conn1,然后丢到resourceThreadLocal中（dataSource->conn1）。
     * 4.执行this.jdbcTemplate1.update("insert into user1(name)value(?)", "张三");，由于jdbcTemplate1中的dataSource是dataSource，所以会从resourceThreadLocal中拿到conn1来执行sql。
     * 5.执行this.user2Service.required();
     * 6.书屋管理器拦截user2Service.required方法，事务配置信息：（事务管理器transactionManager2，数据源dataSource，传播行为REQUIRED）
     * 7.问一下transactionManager2，当前是否有事务？由于transactionManager2和transactionManager1用到的都是dataSource，所以transactionManager2会发现当前是存在事务的，即tm1
     * 8.执行this.jdbcTemplate2.update("insert into user2(name) value (?)", "李四");由于jdbcTemplate2中的dataSource也是dataSource，所以会从resourceThreadLocal中拿到conn1连接来执行sql。
     * 9.最终整个操作过程中只有一个事务tm1，一个连接conn1，通过conn1执行2个插入操作。
     * 10.执行throw new RuntimeException();抛出异常
     * 11.tm1感受到了异常，所以会执行回滚操作，最终都插入失败
     *
     * 事务管理器查找连接的原理：
     * 整个过程中有2个地方需要用到数据库连接Connnection对象，第一个地方是：spring事务拦截器启动事务的时候会从dataSource中获取一个连接，通过这个连接开启事务手动提交，第二个地方是：最终执行sql操作的时候，也需要用到一个连接。那么必须确保这两个连接是同一个连接，执行sql的操作才会受spring事务控制，那么如何确保这2个是同一个连接呢？从源码中可以看出必须让事务管理器的dataSource和JdbcTemplate的dataSource必须是同一个，那么最终2个连接就是同一个对象。
     */





















}
