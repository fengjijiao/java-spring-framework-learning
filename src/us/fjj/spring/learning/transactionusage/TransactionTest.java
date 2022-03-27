package us.fjj.spring.learning.transactionusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import us.fjj.spring.learning.transactionusage.test1.MainConfig1;
import us.fjj.spring.learning.transactionusage.test1.UserService;

import javax.sql.DataSource;
import java.util.function.Consumer;

/**
 * 讲解spring中编程式事务的使用。
 *
 * spring中使用事务的2种方式
 *
 * spring使事务操作变的异常容易了，spring中控制事务主要有2种方式
 * 1.编程式事务：硬编码的方式
 * 2.声明式事务：大家比较熟悉的注解@Transaction的方式
 *
 *
 * 编程式事务：通过硬编码的方式使用spring中提供的事务相关的类来控制事务。
 *
 * 变成式事务主要有2种用法：
 * 方式一：通过PlatformTransactionManager控制事务
 * 方式二：通过TransactionTemplate控制事务
 */
public class TransactionTest {
    public static javax.sql.DataSource getDataSource() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("sad");
        dataSource.setPassword("sadad");
        dataSource.setInitialSize(5);
        return dataSource;
    }
    /**
     * 方式一：PlatformTransactionManager
     * 这种是最原始的方式，代码量比较大，后面其他方式都是对这种方式的封装。
     *
     */
    @Test
    public void test1() {
        //定义一个数据源
        javax.sql.DataSource dataSource = getDataSource();
        //定义一个JdbcTemplate，用来方便执行数据库增删改查
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //1.定义事务管理器，给其指定一个数据源（可以把事务管理器想象为一个人，这个人来负责事务的控制操作）
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        //2.定义事务属性：TransactionDefinition，TransactionDefinition可以用来配置事务的属性信息，比如事务隔离级别、事务超时时间、事务传播方式、是否只读事务等等。
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        //3.开启事务：调用platformTransactionManager.getTransaction开启事务操作，得到事务状态（TransactionStatus）对象
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        //4.执行业务操作，下面就执行2个插入操作
        try {
            System.out.println("before:" + jdbcTemplate.queryForList("select * from user"));
            jdbcTemplate.update("insert into user (username, password) value (?, ?)", "ok", "okpwd");
            jdbcTemplate.update("insert into user (username, password) value (?, ?)", "o2k", "o2kpwd");
            //5.提交事务：platformTransactionManager.commit
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            //6.回滚事务：platformTransactionManager.rollback
            platformTransactionManager.rollback(transactionStatus);
        }
        System.out.println("after: " + jdbcTemplate.queryForList("select * from user"));
        /**
         *
         * before:[{id=1, username=a, password=aa}, {id=2, username=b, password=bb}, {id=3, username=jyx, password=jyx123}, {id=4, username=yk, password=yk123}, {id=5, username=获取自增列的值, password=获取自增列的值123}, {id=6, username=c, password=c123}, {id=7, username=d, password=d123}, {id=8, username=e, password=e123}, {id=9, username=f, password=f123}, {id=10, username=ok, password=okpwd}, {id=11, username=o2k, password=o2kpwd}]
         * after: [{id=1, username=a, password=aa}, {id=2, username=b, password=bb}, {id=3, username=jyx, password=jyx123}, {id=4, username=yk, password=yk123}, {id=5, username=获取自增列的值, password=获取自增列的值123}, {id=6, username=c, password=c123}, {id=7, username=d, password=d123}, {id=8, username=e, password=e123}, {id=9, username=f, password=f123}, {id=10, username=ok, password=okpwd}, {id=11, username=o2k, password=o2kpwd}, {id=12, username=ok, password=okpwd}, {id=13, username=o2k, password=o2kpwd}]
         */

        /**
         * 代码分析
         * 代码中主要有5个步骤
         * 步骤1：定义事务管理器PlatformTransactionManager
         * 事务管理器相当于一个管理员，这个管理员就是用来帮你控制事务的，比如开启事务，提交事务，回滚事务等等。
         * spring中使用PlatformTransactionManager这个接口来表示事务管理器.
         * {@link PlatformTransactionManager}
         * 其中getTransaction用于获取一个事务相当于开启事务。
         * commit用于提交事务。
         *
         * PlatformTransactionManager有多个实现类，用来应对不同的环境。
         * JpaTransactionManager: 如果你用Jpa来操作DB，那么就需要用这个管理器来帮你控制事务。
         * DataSourceTransactionManager: 如果你用的是指定数据源的方式，比如操作数据库用的是：JdbcTemplate、mybatis、ibatis，那么需要用这个管理器来帮你控制事务。
         * HibernateTransactionManager: 如果你用hibernate来操作db，那么需要用这个管理器来帮你控制事务。
         * JtaTransactionManager: 如果你用的是java中的jta来操作DB，这种通常是分布式事务，此时需要用这种管理器来控制事务。
         * 上面的案例代码中我们使用的是JdbcTemplate来操作db，所以用的是DataSourceTransactionManager这个管理器。
         * PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
         *
         *
         * 步骤2：定义事务属性TransactionDefinition
         * 定义事务属性，比如事务隔离级别、事务超时时间、事务传播方式、是否是只读事务等等。
         * spring中使用TransactionDefinition接口来表示事务的定义信息，有个子类比较常用：DefaultTransactionDefinition。
         * 关于事务属性细节比较多，篇幅比较长。
         *
         *
         * 步骤3：开启事务
         * 调用事务管理器的getTransaction方法，即可开启一个事务。
         * TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
         * 这个方法会返回一个TransactionStatus表示事务状态的一个对象，通过TransactionStatus提供的一些方法可以用来控制事务的一些状态，比如事务最终是需要回滚还是需要提交。
         * 执行了getTransaction后，spring内部会执行一些操作，为了方便大家理解，下面是伪代码：
         * //有一个全局共享的threadLocal对象 resources
         * static final ThreadLocal<Map<Object, Object>> resources = new NamedThreadLocal<>("Transactional resources");
         * //获取一个db的连接
         * DataSource datasource = platformTransactionManager.getDataSource();
         * Connection connection = datasource.getConnection();
         * //设置手动提交事务
         * connection.setAutoCommit(false);
         * Map<Object, Object> map = new HashMap<>();
         * map.put(datasource, connection);
         * resources.set(map);
         *
         * 上面的代码，将数据源datasource和connection映射起来放在了ThreadLocal中，ThreadLocal大家比较熟悉，用于在同一线程中共享数据；后面我们可以通过resources这个ThreadLocal获取datasource其对应的connection对象.
         *
         *
         * 步骤4：执行业务操作
         * 我们使用jdbcTemplate插入2条记录。
         * jdbcTemplate.update("insert into user (username, password) value (?, ?)", "ok3", "ok3pwd");
         * jdbcTemplate.update("insert into user (username, password) value (?, ?)", "ok4", "ok4pwd");
         *
         *
         * 大家看一下创建JdbcTemplate的代码，需要指定一个datasource
         * JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
         * 再来看看创建事务管理器的代码
         * PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
         *
         * 2者用到的是同一个dataSource，而事务管理器开启事务的时候，会创建一个连接，将datasource和connection映射之后丢在了ThreadLocal中，而JdbcTemplate内部执行db操作的时候，也需要获取连接，JdbcTempalte会以自己内部的datasource去上面的threadLocal中找有没有关联的连接，有就直接拿来用，若没有找到就重新创建一个连接，而此时是可以找到的，那么JdbcTempalte就参与到Spring的事务中了。
         *
         *
         * 步骤5：提交   or 回滚
         * //5.提交事务：platformTransactionManager.commit
         * //6.回滚事务: platformTransactionManager.rollback
         * */
    }

    /**
     * 方式二：TransactionTemplate
     * 方式一中部分代码是可以重用的，所以spring对其进行了优化，采用模板方法模式就其进行封装，主要省去了提交或者回滚事务的代码。
     *
     *
     *
     */
    @Test
    public void test2() throws  Exception {
        //定义一个数据源
        DataSource dataSource = getDataSource();
        //定义一个JdbcTemplate，用来方便执行数据库的增删改查
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //1.定义事务管理器，给其指定一个数据源（可以把事务管理器想象为一个人，这个人来负责事务的控制操作）
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        //2.定义事务的属性
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setTimeout(10);//如设置超时时间为10s
        //3.创建TransactionTemplate对象
        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager, transactionDefinition);
        /**
         * 4.通过TransactionTempalte提供方法执行业务操作
         * 主要有2个方法：
         * (1). executeWithoutResult(Consumer<TransactionStatus> action): 没有返回值的，需传递一个Consumer对象，在accept方法中做业务操作
         * (2). <T> T execute(TransactionCallback<T> action): 有返回值的，需要传递一个TransactionCallback对象，在doInTransaction方法中做业务操作
         *
         * 调用execute方法或者executeWithoutResult方法执行完毕之后，事务管理器会自动提交事务或者回滚事务。
         * 那么什么时候事务会回滚，有2种方式：
         * （1）transactionStatus.setRollbackOnly()；将事务状态标记为回滚状态。
         * (2) execute方法或者executeWithoutResult方法内部抛出异常
         * 什么时候事务会提交？
         * 方法没有异常 && 未调用过transactionStatus.setRollbackOnly();
         */
        transactionTemplate.executeWithoutResult(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                jdbcTemplate.update("insert into user (username, password) value (?, ?)", "tTempalte", "testpwd");
                jdbcTemplate.update("insert into user (username, password) value (?, ?)", "tTempalte2", "testpwd");
            }
        });
        System.out.println("after:"+jdbcTemplate.queryForList("select * from user"));
        /**
         * after:[{id=1, username=a, password=aa}, {id=2, username=b, password=bb}, {id=3, username=jyx, password=jyx123}, {id=4, username=yk, password=yk123}, {id=5, username=获取自增列的值, password=获取自增列的值123}, {id=6, username=c, password=c123}, {id=7, username=d, password=d123}, {id=8, username=e, password=e123}, {id=9, username=f, password=f123}, {id=10, username=ok, password=okpwd}, {id=11, username=o2k, password=o2kpwd}, {id=12, username=ok, password=okpwd}, {id=13, username=o2k, password=o2kpwd}, {id=14, username=tTempalte, password=testpwd}, {id=15, username=tTempalte2, password=testpwd}]
         */


        /**
         * 代码分析
         * TransactionTemplate，主要有2个方法
         * executeWithoutResult: 无返回值场景
         * executeWithoutResult(Consumer<TransactionSataus> action)：没有返回值，需传递一个Consumer对象，在accept方法中做业务操作。
         * execute: 有返回值场景
         * <T> T execute(TransactionCallback<T> action): 有返回值的，需要传递一个TransactionCallback对象，在doInTransaction方法中做业务操作。
         * Integer result = transactionTemplate.execute(new TransactionCallback<Integer>())
         * {
         * @Nullable
         * @Override
         * public Integer doInTransaction(TransactionStatus status) {
         *  retrun jdbcTemplate.update("insert into user (username, password) value (?, ?)", "ok9", "ok9pwd");
         * }
         * }
         *
         * 通过上面两个方法，事务管理器会自动提交事务或者回滚事务。
         *
         *
         * 什么时候事务会回滚，有2种方式
         * 方式一
         * 在execute或者executeWithoutResult内部执行transactionStatus.setRollbackOnly();将事务状态标记为回滚状态，spring会自动让事务回滚。
         * 方式二
         * execute方法或者executeWithoutResult方法内部抛出任意异常即可回滚。
         *
         * 什么时候事务会提交
         * 方法没有异常 && 为调用transactionStatus.setRollbackOnly();
         *
         */
    }

    /**
     * 编程式事务正确的使用姿势
     *
     * 如果大家确实想在系统中使用编程式事务，那么可以参考下面的代码，使用spring来管理对象，更简洁一些。
     *
     * 先来个配置类，将事务管理器PlatformTransactionManager、事务模板TransactionTemplate都注册到spring中，重用。
     * 通常我们会将业务操作放在service中。
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        UserService userService = context.getBean(UserService.class);
        userService.bus1();
        System.out.println(userService.userList());
        /**
         * [{id=16, username=java, password=javapwd}, {id=17, username=java2, password=javapwd}, {id=18, username=java3, password=javapwd}]
         */
    }

















}
