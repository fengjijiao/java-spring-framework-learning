package us.fjj.spring.learning.transactioninterceptorordercontrolusage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import us.fjj.spring.learning.transactioninterceptorordercontrolusage.test1.MainConfig1;
import us.fjj.spring.learning.transactioninterceptorordercontrolusage.test1.UserService;

/**
 * spring事务拦截器顺序如何控制？
 * <p>
 * 1.前言
 * 我们知道Spring事务是通过aop的方式添加了一个事务拦截器，事务拦截器会拦截目标方法的执行，再方法执行前后添加了事务控制。
 * 那么spring事务拦截器的顺序如何控制呢，若我们自己也添加了一些拦截器，此时事务拦截器和自定义拦截器共存的时候，他们的顺序是怎么执行的？如何手动来控制他们的顺序？
 * 可能有些朋友会问，控制他们的顺序，这个功能有什么用？为什么要学这个
 * 学会了这些，你可以实现很多牛逼的功能，比如
 * 1.读写分离
 * 2.通用幕等框架
 * 3.分布式事务框架
 */
public class TransactionInterceptorOrderControlTest {
    /**
     * 2.事务拦截器顺序设置
     * @EnableTransactionManagement注解有个order属性，默认值是Integer.MAX_VALUE,用来指定事务拦截器的顺序，值越小，拦截器的优先级越高，如：
     * @EnableTransactionManagement(order = 2)
     */
    /**
     * 3.案例
     * 我们自定义2个拦截器：一个放在事务拦截器之前执行，一个放在事务拦截器之后执行
     * 拦截器                                      顺序
     * TransactionInterceptorBefore                 1
     *
     * @EnableTransactionManagement 事务拦截器        2
     * TransactionInterceptorAfter                  3
     */
    private UserService userService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void before() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        userService = context.getBean(UserService.class);
        this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        jdbcTemplate.update("truncate table t_user");
    }

    @Test
    public void test1() {
        this.userService.addUser();
        /**
         * ---------before start----------
         * ----------after start-------------
         * -----------------UserService.addUser start------------------
         * -----------------UserService.addUser end------------------
         * ----------after end-------------
         * ---------before end  ----------
         */
    }

    /**
     * 总结
     * 重点掌握如何设置事务拦截器的顺序
     * @EnableTransactionManagement有个order属性，默认值是Integer.MAX_VALUE, 用来指定事务拦截器的顺序，值越小，拦截器的优先级越高。
     *
     * 后面我们会通过这个功能实现读写分离，通用幕等功能。
     */
}
