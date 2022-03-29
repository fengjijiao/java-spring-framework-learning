package us.fjj.spring.learning.transactionusage;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import us.fjj.spring.learning.transactionusage.test1.MainConfig1;
import us.fjj.spring.learning.transactionusage.test1.UserService;
import us.fjj.spring.learning.transactionusage.test2.MainConfig2;
import us.fjj.spring.learning.transactionusage.test3.MainConfig3;
import us.fjj.spring.learning.transactionusage.test3.User;
import us.fjj.spring.learning.transactionusage.test3.UserService3;

import javax.sql.DataSource;
import java.lang.reflect.Method;
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


    /**
     * 声明式事务
     * 所谓声明式事务，就是通过配置的方式，比如通过配置文件（xml）或者注解的方式，告诉spring，哪些方法需要spring帮忙管理事务，然后开发者只用关注业务代码，而事务的事情spring自动帮我们控制。
     *
     * 比如注解的方式，只需在方法上面加上一个@Transaction注解，那么方法执行之前spring会自动开启一个事务，方法执行完毕之后，会自动提交或者回滚事务，而方法内部没有任何事务相关代码，用起来特别的简单。
     *
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        us.fjj.spring.learning.transactionusage.test2.UserService userService = context.getBean(us.fjj.spring.learning.transactionusage.test2.UserService.class);
        try {
            userService.insert("io", "iokpwd");
        }catch (RuntimeException ex) {
            System.out.println("遇到异常，自动回滚！");
        }
    }

    /**
     * 声明式事务的2种实现方式
     * 1.配置文件的方式，即在spring xml文件中进行统一配置，开发者基本上就不用关注事务的事情了，代码中无需关心任何和事务相关的代码，一切交给spring处理。
     * 2.注解的方式，只需在需要spring来帮忙管理事务的方法上加上@Transaction注解就可以了，注解的方式相对来说更简洁一些，都需要开发者自己去进行配置，可能有些同学对spring不太熟悉，所以配置这个有一定的风险，做好代码review就可以了。
     *
     * 配置文件的方式这里就不讲了，用的比较少。
     */

    /**
     * 声明式事务注解方式5个步骤
     * 1. 启动Spring的注解驱动事务管理功能
     * 在Spring配置类上加上@EnableTransactionManagement注解
     *
     * 原理：当Spring容器启动的时候，发现有@EnableTransactionManagement注解，此时会拦截所有bean的创建，扫描看一下bean上是否有@Transaction注解(类、或者父类、或者接口、或者方法中有这个注解都可以)，如果有这个注解，spring会通过aop的方式给bean生成代理对象，代理对象中会增加一个拦截器，拦截器会拦截bean中public 方法执行，会在方法执行之前启动事务，方法执行完毕之后提交或者回滚事务。
     * {@link org.springframework.transaction.interceptor.TransactionInterceptor#invoke}
     * {@link org.springframework.transaction.annotation.EnableTransactionManagement}
     *
     *
     * 2.定义事务管理器
     * 事务交给spring管理，那么你肯定要创建一个或者多个事务管理器，有这些管理器来管理具体的事务，比如启动事务、提交事务、回滚事务，这些都是管理者来负责的。
     *
     * spring中使用PlatformTransactionManager这个接口来表示事务管理者。
     *
     *
     * 3.需使用事务的目标上加@Transactional注解
     * @Transactional 放在接口上，那么接口的实现类中所有public都被spring自动加上事务。
     * @Transactional 放在类上，那么当前类以及旗下无限级子类中所有public方法将被spring自动加上事务
     * @Transactional 放在public方法上，那么该方法将被spring自动加上事务
     * 注意：@Transactional只对public方法有效
     * @Transactional注解参数：
     * transactionManager: 同value，如果为空，默认会从容器中按类型查找一个事务管理器bean
     * propagation: 事务的传播属性
     * isolation: 事务的隔离级别，就是制定数据库的隔离级别。
     * timeout: 事务执行的超时时间（s），执行一个方法，比如有问题，那我不可能等你一天把，可能我最多只能等你10s，10s后还没执行完毕，就弹出一个超时异常。
     * readOnly: 是否是只读事务，比如某个方法中只有查询操作，我们可以知道事务是只读的，设置了这个参数，可能数据库会做一些性能优化，提升查找速度。
     * rollbackFor: 定义零个或更多异常类，这些异常必须是Throwable的子类，当方法抛出这些异常及其子类异常的时候，spring会让事务回滚。
     * rollbackForClassName: 和rollbackFor作用一样，只是这个地方使用的是类名。
     * noRollbackFor: 定义零个或者多个异常类，这些异常必须是Throwable的子类，当方法抛出这些异常的时候，事务不会回滚。
     * noRollbackForClassName: 和noRollbackFor作用一样，只是这个地方使用的是类名。
     *
     *
     * 4.执行db业务操作
     * 在@Transaction标注类或者目标方法上执行业务操作，此时这些方法会自动被spring进行事务管理。
     * 如下面的insertBatch操作，先删除数据，然后批量插入数据，方法上加上了@Transactional注解，此时这个方法会自动受spring事务控制，要么都成功，要么都失败。
     * @Component
     * public class UserService {
     * @Autowired
     * private JdbcTemplate jdbcTemplate;
     * //先清空表中数据，然后批量插入数据，要么都成功要么都失败
     * @Transactional
     * public void insertBatch(String... usernames) {
     *     jdbcTemplate.update("truncate table user");
     *     for(String username: usernames) {
     *         jdbcTempalte.update("insert into user (username) value (?)", username);
     *     }
     * }
     * }
     *
     *
     *
     * 5.启动spring容器，使用bean执行业务操作
     * @Test
     * public void test1() {
     *    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
     *    context.register(MainConfig1.class);
     *    context.refresh();
     *    UserService userService = context.getBean(UserService.class);
     *    userService.insertBatch("zhangshang", "lisi", "wangwu");
     * }
     *
     *
     * 案例1
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        UserService3 userService3 = context.getBean(UserService3.class);
        User[] users = new User[]{
                new User().setUsername("ok").setPassword("oj"),
                new User().setUsername("ok2").setPassword("oj2"),
                new User().setUsername("ok3").setPassword("oj3")
        };
        int rows = userService3.insertBatch(users);
        //受影响行数
        System.out.println(rows);
        System.out.println(userService3.findAll());
        /**
         * 3
         * [User(username=ok, password=oj), User(username=ok2, password=oj2), User(username=ok3, password=oj3)]
         */
    }

    /**
     * 如何确定方法有没有用到sping事务
     * 方式一：断点调试
     * spring事务是由TransactionInterceptor拦截器处理的，最后会调用下面这个方法，设置个断点可以看到详细过程。
     * {@link org.springframework.transaction.interceptor.TransactionAspectSupport#invokeWithinTransaction(Method, Class, TransactionAspectSupport.InvocationCallback)}
     *
     *
     * 方式二：看日志
     * spring处理事务的过程，有详细的日志输出，开启日志，控制台就可以看到事务的详细过程了。
     *
     * 1.添加maven配置
     * <dependency>
     *     <groupId>ch.qos.logback</groupId>
     *     <artifactId>logback-classic</artifactId>
     *     <version>1.2.3</version>
     * </dependency>
     *
     * 2.src\main\resources\新建logback.xml
     * <?xml version="1.0" encoding="UTF-8">
     * <configuration>
     *     <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
     *          <encoder>
     *              <pattern>[%d{MM-dd HH:mm:ss.SSS}][%thread{20}:${PID:-}][%x{trace_id}][%level][%logger{56}]:%line:%method\(\)]:%msg%n#########*********#########%n</pattern>
     *          </encoder>
     *     </appender>
     *
     *     <logger name="org.springframework" level="debug">
     *          <appender-ref ref="STDOUT"/>
     *     </logger>
     * </configuration>
     */


    /**
     * 总结
     * 本文讲解了一下spring中编程式事务的使用步骤。
     * 主要涉及到了2个注解：
     * @EnableTransactionManagement：开启spring事务管理功能。
     * @Transaction: 将其加在需要spring管理事务的类、方法、接口上，只会对public方法有效。
     *
     */


    /**
     * 详解Spring事务中7种传播行为
     *
     * 事务的传播行为用来描述：系统中的一些方法交由Spring来管理事务，当这些方法之间出现嵌套调用的时候，事务所表现出来的行为是什么样的？
     *
     * 如何配置事务传播行为？
     * 通过@Transactional注解中的propagation属性来指定事务的传播行为：
     * Propagation propagation() default Propagation.REQUIRED;
     *
     * 7种传播行为
     * Propagation是个枚举，有7种值，如下：
     * 事务传播行为类型             说明
     * REQUIRED                 如果当前事务管理器中没有事务，就新建一个事务，如果已经存在一个事务，就加入到这个事务中。这是最常见的选择，是默认的传播行为。
     * SUPPORTS                 支持当前事务，如果当前事务管理器中没有事务，就以非事务方式执行。
     * MANDATORY                （强制）使用当前的事务，如果当前事务管理器中没有事务，就抛出异常。
     * REQUIRES_NEW             新建事务，如果当前事务管理器中存在事务，把当前事务挂起，然后会新建一个事务。
     * NOT_SUPPORTED            以非事务方式执行操作，如果当前事务管理器中存在事务，就把当前事务挂起。
     * NEVER                    以非事务方式执行，如果当前事务管理器中存在事务，则抛出异常。
     * NESTED                   如果当前事务管理器中存在事务，则在嵌套事务内执行；如果当前事务管理器中没有事务，则执行与PROPAGATION_REQUIRED类似的操作。
     *
     * 注意：这7种传播行为有个前提，他们的事务管理器是同一个的时候，才会有上面描述中的表现行为。
     *
     * 知识点
     * 1.Spring声明式事务处理事务的过程
     * spring声明式事务是通过事务拦截器TransactionInterceptor拦截目标方法，来实现事务管理的功能的，事务管理器处理过程大致如下：
     * 1.获取事务管理器
     * 2.通过事务管理器开启事务
     * try {
     *     3.提交业务方法执行db操作
     *     4.提交事务
     * }catch(RuntimeException | Error) {
     *     5.回滚事务
     * }
     *
     *
     *
     * 2.何时事务会回滚？
     * 默认情况下，目标方法抛出RuntimeException或者Error的时候，事务会被回滚。
     *
     *
     *
     *
     *
     *
     *
     * 3.Spring事务管理器中的Connection和业务中操作db的Connection如何使用同一个的？
     * 以DataSourceTransactionManager为事务管理器，操作db使用JdbcTemplate来说明一下。
     * 创建DataSourceTransactionManager和JdbcTemplate的时候都需要指定dataSource，需要将他俩的dataSource指定为同一个对象。
     * 当事务管理器开启事务的时候，会通过dataSource.getConnection()方法获取一个db连接connection，然后会将dataSource->connection丢到一个Map中，然后将map放到ThreadLocal中。
     * 当jdbcTemplate执行sql的时候，以jdbcTemplate.dataSource去上面的ThreadLocal中查找，是否有可用的连接，如果有，就直接拿来用，否则调用jdbcTemplate.dataSource.getConnnection()方法获取一个连接来用。
     * 所以spring中可以确保事务管理器中的Connection和JdbcTemplate中操作db的Connection是同一个，这样才能确保spring可以控制事务。
     *
     *
     * 代码验证：
     * 测试用例test4
     * before方法会在每个@Test标注的方法之前执行一次，这个方法主要用来做一些准备工作：启动spring容器、清理2个表中的数据；after方法会在每个@Test标注的方法执行完毕之后执行一次，我们在这个里面输出2张表的数据；方便查看测试用例效果。
     */
    @Test
    public void test6() {
        //详见TransactionDemoTest.java
    }


    /**
     * Spring如何管理多数据源事务？
     * 本篇内容：通过原理和大量案例带大家吃透Spring多数据源事务。
     * Spring中通过事务管理器来控制事务，每个数据源都需要指定一个事务管理器，如果我们的项目中需要操作多个数据库，那么需要我们配置多个数据源，也就需要配置多个数据管理器。
     *
     *
     * 多数据源事务使用2个步骤
     * 1、为每个数据源定义一个事务管理器
     * 如下面代码，有2个数据源分别连接数据源ds1和ds2,然后为每个数据源定义1个事务管理器，此时spring容器中有2个数据源和2个事务管理器。
     *
     * 2.Spring配置类
     * 定义2个数据源
     * 定义2个JdbcTemplate
     * 2个数据源对应2个事务管理器
     *
     * 3.Ds1User1Service
     * 用来操作user1.user1表，注意下面代码中@Transactional注解中transactionManager的值为transactionManager1
     * 同样方式定义另外3个Service
     *
     * 4.Tx1Service&Tx2Service
     *
     * 案例1
     */
    @Test
    public void test7() {
        //详见Test5Test.java
    }
    /**
     * 案例2
     * 在MainConfig6中定义了一个数据源dataSource，2个jdbcTemplate: jdbcTemplate1和jdbcTemplate2，他们的dataSource都是dataSource。
     * 2个事务管理器： transactionManager1和transactionManager2，他们的dataSource都是dataSource。
     * 这样写是不是很奇怪，不是说一个数据源定义一个事务管理器么，这什么操作？
     * 不急，我们这样写，是为了让你更深入了解其原理。
     */
    @Test
    public void test8() {
        //详见Test6Test.java
    }


    /**
     * 总结
     * 1.本文介绍了多数据源事务的使用，2个步骤：先为每个数据源定义一个事务管理器，然后在@Transactional中指定具体要使用的事务管理器。
     * 2.事务管理器运行过程、事务管理器如何判断当前是否有事务，这2点非常重要。
     */


    /**
     * spring事务源码解析
     * spring编程式事务源码深度解析，理解spring事务的本质。
     *
     * 回顾一下编程式事务用法
     */
    @Test
    public void test9() {
        //定义一个数据源
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://39.173.182.109:3306/test1?characterEncoding=UTF-8");
        dataSource.setUsername("f");
        dataSource.setPassword("fengjijiaodatebase");
        dataSource.setInitialSize(5);
        //定义一个JdbcTemplate，用来方便执行数据库增删改查
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //1.定义事务管理器，给其指定一个数据源（可以把事务管理器想象为一个人，这个人来负责事务的控制操作）
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        //2.定义事务属性：TransactionDefinition，TransactionDefinition可以用来配置事务的属性信息，比如事务的隔离级别、事务超时时间、事务传播方式、是否是只读事务等等。
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        //3.获取事务：调用platformTransactionManager.getTransaction开启事务操作，得到事务状态（TransactionStatus）对象
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        //4.执行业务操作，下面就执行2个插入操作
        try {
            System.out.println("before:"+jdbcTemplate.queryForList("select * from user1"));
            jdbcTemplate.update("insert into user1 (name) value (?)", "test1-1");
            jdbcTemplate.update("insert into user1 (name) value (?)", "test1-2");
            //5.提交事务：platformTransactionManager.commit
            transactionManager.commit(transactionStatus);
        } catch (Exception ex) {
            //6.回滚事务：platformTransactionManager.rollback
            transactionManager.rollback(transactionStatus);
        }
        System.out.println("after:"+jdbcTemplate.queryForList("select * from user1"));
    }

    /**
     * 编程式事务过程
     * 1.定义事务属性信息：TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
     * 2.定义事务管理器：PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
     * 3.获取事务：TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
     * 4.执行sql操作：比如上面通过JdbcTemplate的各种方法执行各种sql操作
     * 5.提交事务或者回滚事务
     *
     *
     *
     * 获取事务的过程：
     * 1.获取db连接：从事务管理器的dataSource中调用getConnection获取一个新的数据库连接，将连接置为手动提交
     * 2.将dataSource关联连接丢到ThreadLocal中；将第一步获取到的连接丢到ConnectionHolder中，然后将事务管理器的dataSource->ConnectionHolder丢到了resource ThreadLocal中，这样我们可以通过dataSource在ThreadLocal中获取关联的数据库连接。
     * 3.尊卑事务同步：将事务的一些信息放到ThreadLocal中。
     *
     *
     * 存在事务的情况如何走？
     *下面来看另外一个流程，REQUIRED中嵌套一个REQUIRES_NEW,然后走到REQUIRES_NEW的时候，代码是如何运行的？大致过程如下：
     * 1.判断上下文中是否有事务
     * 2.挂起当前事务
     * 3.开启新事务，并执行新事务
     * 4.恢复被挂起的事务
     *
     *
     *
     * 事务执行过程中的回调结果：TransactionSynchronization
     * 作用：spring事务运行的过程中，给开发者预留了一些扩展点，在事务执行的不同阶段，将回调扩展点中的一些方法。
     * 比如我们想在事务提交之前、提交之后、回滚之前、回滚之后做一些事务，那么我们可以通过扩展点来实现。
     *
     * 扩展点的用法
     * 1.定义事务TransactionSynchronization对象
     * TransactionSynchronization接口中的方法在spring事务执行的过程中会自动被回调
     * 2.将TransactionSynchronization注册到当前事务中
     * 通过下面静态方法将事务扩展点TransactionSynchronization注册到当前事务中
     * TransactionSynchronizationManager.registerSynchronization(transactionSynchronization);
     * 当有多个TransactionSynchronization的时候，可以指定其顺序，可以实现org.springframework.core.Ordered接口，来指定顺序，从小到大的顺序被调用，TransactionSynchronization有个默认适配器TransactionSynchronizationAdapter，这个类实现了Ordered接口，所以，如果我们要使用的时候，直接使用TransactionSynchronizationAdapter这个类。
     * 3.回调扩展点TransactionSynchronization中的方法
     * TransactionSynchronization中的方法是spring事务管理器自动调用的，本文上面有提到，事务管理器在事务提交或者事务回滚的过程中，有很多地方会调用trigger开头的方法，这个trigger方法内部就会遍历当前事务中的transactionSynchronization列表，然后调用transactionSynchronization内部的一些指定的方法。
     *
     * 案例
     */
    @Test
    public void test10() {
        //见Test7Test.java
    }


    /**
     * @Transactional事务源码解析
     *
     * 2.@Transactional事务的用法
     * 咱们先来回顾一下，@Transactional事务的用法，特别简单，2个步骤
     * 1.在需要让spring管理事务的方法上添加@Transactional注解
     * 2.在spring配置类上添加@EnableTransactionManagement注解，这步特别重要，别给忘了，有了这个注解之后，@Transactional标注的方法才会生效。
     *
     * 3.@Transactional事务原理
     * 原理比较简单，内部是通过spring aop的功能，通过拦截器拦截@Transactional方法的执行，在方法前后添加事务的功能。
     *
     * 4.@EnableTransactionManagement注解作用
     * @EnableTransactionManagement注解会开启spring自动管理事务的功能，有了这个注解之后，spring容器启动的过程中，会拦截所有bean的创建过程，判断bean是否需要让spring来管理事务，即判断bean中是否有@Transaction注解，判断规则如下：
     * 1.一直沿着当前bean的类向上找，先从当前类中，然后父类，父类的父类，当前类的接口、接口的父接口、父接口的父接口，一直向上找，一下这些类型上面是否有@Transactional注解
     * 2.类的任意public方法上面是否有@Transactional注解
     * 如果bean满足上面任意一个规则，就会被spring容器通过aop的方式创建代理，代理中会添加一个拦截器。
     * {@link org.springframework.transaction.interceptor.TransactionInterceptor}
     * TransactionInterceptor拦截器是关键，他会拦截@Transactional方法的执行，在方法执行前后添加事务的功能，这个拦截器中大部分都是编程式事务的代码。
     */










}
