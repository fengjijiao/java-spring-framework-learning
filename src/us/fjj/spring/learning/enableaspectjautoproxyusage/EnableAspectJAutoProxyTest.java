package us.fjj.spring.learning.enableaspectjautoproxyusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.CarService;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.MainConfig1;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.UserService;

/**
 * @EnableAspectJAutoProxy自动为bean创建代理对象
 * 需要结合@Aspect注解一起使用。
 */
public class EnableAspectJAutoProxyTest {
    /**
     * 使用案例
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context= new AnnotationConfigApplicationContext(MainConfig1.class);
        UserService userService = context.getBean("userService", UserService.class);
        CarService carService = context.getBean("carService", CarService.class);
        userService.say();
        carService.say();
        /**
         * 将要执行say
         * 我是UserService
         * 将要执行say
         * 我是CarService
         */
    }
}
