package us.fjj.spring.learning.lazyannotationusage;

/**
 * @Lazy: 延迟初始化
 * 等效于bean xml中bean元素的lazy-init属性，可以实现bean的延迟初始化。
 * 所谓延迟初始化：就是使用到的时候才会去进行初始化
 */

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.lazyannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.lazyannotationusage.test1.Service1;
import us.fjj.spring.learning.lazyannotationusage.test2.MainConfig2;

/**
 * 常用3种方式
 * 1.和@Component一起标注在类上，可以是这个类延迟初始化
 * 2.和@Configuration一起标注在配置类上，可以让当前配置类种通过@Bean注册的bean延迟初始化
 * 3.和@Bean一起使用，可以使当前bean延迟初始化
 */
public class LazyAnnotationTest {
    //案例一：和@Component一起使用
    @Test
    public void test1() {
        System.out.println("Spring容器加载开始");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        System.out.println("Spring容器加载完成");
        context.getBean(Service1.class);
        /**
         * Spring容器加载开始
         * Spring容器加载完成
         * 创建了Service1
         */
    }
    //案例二：和@Configuration一起使用加在配置类上
    /**
     * @Lazy和@onfiguration一起使用，此时配置类中所有通过@Bean方式注册的bean都会被延迟初始化，不过也可以在@Bean标注的方法上使用@Lazy来覆盖配置类上的@lazy配置。
     */
    @Test
    public void test2() {
        System.out.println("spring容器加载开始");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("spring容器加载完成");
        context.getBean("service1");
        context.getBean("service2");
        context.getBean("service3");
        /**
         * spring容器加载开始
         * 创建了Service3
         * spring容器加载完成
         * 创建了Service1
         * 创建了Service2
         */
    }

    /**
     * 总结：
     * 1.@Scope: 用来定义bean的作用域；2种用法：第一种：标注在类上；第二种：和@Bean一起标注在方法上
     * 2.@DependsOn: 用来指定当前bean依赖的bean，可以确保在创建当前bean之前，先将依赖的bean创建好；2种用法：第一种：标注在类上；第二种：和@Bean一起标注在方法上
     * 3.@ImportResource: 标注在配置类上，用来引入bean定义的配置文件
     * 4.#Lazy: 让bean延迟初始化；常见3种用法：第一种：比哦住在类上；第二种：标注在配置类上，会对配置类种所有的@bean标注的方法有效；第三种：和@Bean一起标注在方法上
     */
}