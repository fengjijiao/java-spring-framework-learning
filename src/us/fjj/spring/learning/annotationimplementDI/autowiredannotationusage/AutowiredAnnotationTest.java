package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainRun;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.MainConfig2;

/**
 * @Autowired：注入依赖对象
 * 作用：实现依赖注入，Spring容器会对bean中所有字段、方法进行遍历，标注有@Autowired注解的，都会进行注入
 * Autowired注解中有一个required属性，当为true时，表示必须注入，在容器中找不到匹配的候选者会报错；为false时，找不到也没关系。
 */

/**
 * Autowired操作候选者可以简化为下面这样
 * 按类型找->通过限定符@Qualifier过滤->@Primary->@Priority->根据名称找（字段名或者参数名称）
 * 概括为：先按类型找，然后按名称找
 *
 */
public class AutowiredAnnotationTest {
    /**
     * 测试Priority和Autowired注解
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        MainRun instance = context.getBean(MainRun.class);
        instance.m1();
        /**
         * 这里是MainRun的m1方法!
         * 这里是Service2的m1方法！
         */
    }
    /**
     * 案例1：@Autowired标注在构造器上，通过构造器注入依赖对象
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service2 service2 = context.getBean(us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service2.class);
        System.out.println(service2.toString());
        /**
         * Service2{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service1@62f4ff3b}
         */
    }
}
