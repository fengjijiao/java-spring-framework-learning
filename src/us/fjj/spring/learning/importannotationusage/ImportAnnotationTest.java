package us.fjj.spring.learning.importannotationusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.importannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.importannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.importannotationusage.test3.MainConfig3;

/**
 * @Import 出现的背景
 * 我们知道批量定义bean的方式有2种
 * 1.@Configuration结合@Bean注解的方式
 * 2.@ComponentScan扫描包的方式
 * 问题1：
 * 如果需要注册的类是在第三方jar中，那么我们如果想注册这些bean有2种方式：
 * 1.通过@Bean标注方法的方式，一个个来注册
 * 2.@ComponentScan的方式，默认的@ComponentScan是无能为力的，默认情况下只会注册@Component标注的类，此时只能自定义@ComponentScan中的过滤器来实现
 * 这2种方式都不是太好，每次有变化，调整的代码都比较多
 * 问题2：
 * 通常我们的项目中有很多子模块，可能每个模块都是独立开发的，最后通过jar的方式引进来，每个模块中都有各自的@Configuration、@Bean标注的类，
 * 或者使用@ComponentScan标注的类，(被@Configuration、@Bean、@ComponentScan标注的类，我们统称为bean配置类，配置类可以用来注册bean，)此时如果我们只想使用其中几个模块的配置类，怎么办？
 *
 * @import可以很好的解决这2个问题，下面我们来看@import怎么玩
 *
 * @import使用
 * 先看spring对他的注释，总结下来作用就是和xml配置的<import/>标签作用一样，允许通过他引入@Configuration标注的类，引入ImportSelector接口和ImportBeanDefinitionRegistrar接口的实现，也
 * 包括@Component注解的普通类。
 *
 * 总的来说：@import可以用来批量导入需要注册的各种类，如普通类、配置类，完成普通类和配置类中所有bean的注册。
 *
 * 使用步骤：
 * 1.将@import标注在类上，设置value参数
 * 2.将@imort标注的类作为AnnotationConfigApplicationContext构造参数创建AnnotationConfigApplicaiotnContext对象
 * 3.使用AnnotationConfigApplicationContext对象
 *
 *
 * @Import的value常见的5种用法
 * 1.value为普通类
 * 2.value为@Configuration标注的类
 * 3.value为@ComponentScan标注的类
 * 4.value为ImportBeanDefinitionRegistrar接口类型
 * 5.value为ImportSelector接口类型
 * 6.value为DeferredImportSelector接口类型
 */
public class ImportAnnotationTest {
    //value为普通类
    @Test
    public void test1() {
        //1.通过AnnotationConfigApplicationContext创建Spring容器,参数为@Import标注的类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        //2.输出容器中定义的所有bean信息
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@23d1e5d0
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@704f1591
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@58fb7731
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@13e547a9
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@3fb6cf60
         * mainConfig1->us.fjj.spring.learning.importannotationusage.test1.MainConfig1@37ddb69a
         * us.fjj.spring.learning.importannotationusage.test1.Service1->us.fjj.spring.learning.importannotationusage.test1.Service1@349c1daf
         * us.fjj.spring.learning.importannotationusage.test1.Service2->us.fjj.spring.learning.importannotationusage.test1.Service2@dfddc9a
         */
        /**
         * 从输出中可以看出：
         * 1.Service1和Service2成功注册到容器了。
         * 2.通过@Import导入的2个类，bean名称为完整的类名。
         * 我们也可以指定被导入类的bean名称， 使用@Component注解就可以了，如下：
         * @Component("service1")
         * public class Service1 {
         * }
         *
         *
         * 总结一下
         * 按模块的方式进行导入，需要哪个就导入哪个，不需要的时候，直接修改一下总的配置类，调整一下@Import就可以了，非常方便。
         */
    }

    //value为@Configuration标注的配置类
    /**
     * 项目比较大的情况下，会按照模块独立开发，每个模块在maven中就表现为一个个的构建，然后通过坐标的方式进行引入需要的模块。
     *
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@878452d
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@426b6a74
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@4c51bb7
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@83298d7
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@42a9e5d1
         * mainConfig2->us.fjj.spring.learning.importannotationusage.test2.MainConfig2@5b080f3a
         * us.fjj.spring.learning.importannotationusage.test2.module1.ConfigModule1->us.fjj.spring.learning.importannotationusage.test2.module1.ConfigModule1$$EnhancerBySpringCGLIB$$dadc80a8@773cbf4f
         * module1->我是模块1配置类！
         * us.fjj.spring.learning.importannotationusage.test2.module2.ConfigModule2->us.fjj.spring.learning.importannotationusage.test2.module2.ConfigModule2$$EnhancerBySpringCGLIB$$4211392a@6b54655f
         * module2->我是模块2配置类!
         */
    }

    //value为@ComponentScan标注的类
    /**
     * 项目中分为多个模块，每个模块有各自独立的包，我们在每个模块所在的包中配置一个@ComponentScan类，然后通过@Import来导入需要启动的模块。
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@68b6f0d6
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@4044fb95
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@aa549e5
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@36f48b4
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@5c00384f
         * mainConfig3->us.fjj.spring.learning.importannotationusage.test3.MainConfig3@3b7ff809
         * module1Service1->us.fjj.spring.learning.importannotationusage.test3.module1.Module1Service1@1bb564e2
         * module1Service2->us.fjj.spring.learning.importannotationusage.test3.module1.Module1Service2@62e6b5c8
         * module2Service1->us.fjj.spring.learning.importannotationusage.test3.module2.Module2Service1@3f792b9b
         * module2Service2->us.fjj.spring.learning.importannotationusage.test3.module2.Module2Service2@7b8233cd
         * us.fjj.spring.learning.importannotationusage.test3.module1.ComponentScanModule1->us.fjj.spring.learning.importannotationusage.test3.module1.ComponentScanModule1@4b20ca2b
         * us.fjj.spring.learning.importannotationusage.test3.module2.ComponentScanModule2->us.fjj.spring.learning.importannotationusage.test3.module2.ComponentScanModule2@1cbf6e72
         */
    }
}
