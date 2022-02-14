package us.fjj.spring.learning.importannotationusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.importannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.importannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.importannotationusage.test3.MainConfig3;
import us.fjj.spring.learning.importannotationusage.test4.MainConfig4;
import us.fjj.spring.learning.importannotationusage.test5.MainConfig5;
import us.fjj.spring.learning.importannotationusage.test6.MainConfig6;
import us.fjj.spring.learning.importannotationusage.test7.MainConfig7;
import us.fjj.spring.learning.importannotationusage.test8.MainConfig8;

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


    /**
     * value为ImportBeanDefinitionRegistrar接口类型 用法
     * 1.定义ImportBeanDefinitionRegistrar接口实现类，在registerBeanDefinitions方法中使用registry来注册bean
     * 2.使用@Import来导入步骤1中定义的类
     * 3.使用步骤2中@Import标注的类作为AnnotationConfigApplicationContext构造参数创建spring容器
     * 4.使用AnnotationConfigApplicationContext操作bean
     */
    @Test
    public void test4() {
        //1.通过AnnotationConfigApplicationContext创建Spring容器，参数为@Import标注的类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        //2.输出容器中定义的所有bean信息
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@78fbff54
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@3e10dc6
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7e22550a
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@45e37a7e
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@62452cc9
         * mainConfig4->us.fjj.spring.learning.importannotationusage.test4.MainConfig4@6941827a
         * service1->us.fjj.spring.learning.importannotationusage.test4.Service1@5a7005d
         * service2->Service2{service1=us.fjj.spring.learning.importannotationusage.test4.Service1@5a7005d}
         */
    }

    //value为ImportSelector接口类型

    /**
     * 用法
     * 1.定义ImportSelector接口实现类，在selectImports返回需要导入的类的名称数组
     * 2.使用@Import来导入步骤1中定义的类
     * 3.使用步骤2中@Import标注的类作为AnnotationConfigApplicationContext构造参数创建spring容器
     * 4.使用AnnotationConfigApplicationContext操作bean
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@6d0b5baf
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@631e06ab
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@2a3591c5
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@34a75079
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@346a361
         * mainConfig5->us.fjj.spring.learning.importannotationusage.test5.MainConfig5@107ed6fc
         * us.fjj.spring.learning.importannotationusage.test5.Service1->us.fjj.spring.learning.importannotationusage.test5.Service1@1643d68f
         * us.fjj.spring.learning.importannotationusage.test5.Module1Config->us.fjj.spring.learning.importannotationusage.test5.Module1Config$$EnhancerBySpringCGLIB$$9f67fb5f@186978a6
         * m1->fjj
         * m2->wq
         */
    }

    /**
     * 案例一：
     * 需求：凡是类名中包含service的，调用他们的内部任何方法，调用之后能够输出这些方法的耗时。（通常使用代理实现）
     * <p>
     * 实现代理：MainConfig6-->@EnableMethodCostTime-->MethodCostTimeProxyImportSelector-->MethodCostTimeProxyBeanPostProcessor
     * 在MainConfig6文件中有包含@ComponentScan注解，表示向spring容器中注入同等包名及子包下的对象（默认过滤器），即这些对象会执行MethodCostTimeProxyBeanPostProcessor中的操作。
     */
    @Test
    public void test6() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        us.fjj.spring.learning.importannotationusage.test6.Service1 service1 = context.getBean(us.fjj.spring.learning.importannotationusage.test6.Service1.class);
        us.fjj.spring.learning.importannotationusage.test6.Service2 service2 = context.getBean(us.fjj.spring.learning.importannotationusage.test6.Service2.class);
        us.fjj.spring.learning.importannotationusage.test6.Ncp3 ncp3 = context.getBean(us.fjj.spring.learning.importannotationusage.test6.Ncp3.class);
        service1.m1();
        service2.m1();
        ncp3.m1();
        /**
         *这是Service1的方法m1！
         * 执行m1耗时16481300ns
         * 这是Service2的方法m1！
         * 执行m1耗时7970200ns
         * class us.fjj.spring.learning.importannotationusage.test6.Ncp3.m1()
         *
         * 可以看到Ncp3没有进行耗时统计，而Service1和Service2均有。
         */
        /**
         * 如果像关闭方法耗时统计，只需要将MainConfig6上的@EnableMethodCostTime注解去除即可
         * spring中有很多类似的注解,以@EnableXXX开头的注解，基本上都是通过上面这种方式实现的，如：
         * @EnableAspectJAutoProxy
         * @EnableCaching
         * @EnableAsync
         */
    }

    /**
     * DeferredImportSelector接口
     * spring中的核心功能@EnableAutoConfiguration就是靠@DeferredImportSelector来实现的。
     *
     * DeferredImportSelector是ImportSelector的子接口，所以也可以通过@import进行导入，这个接口和ImportSelector不同的地方有两个：
     * 1.延迟导入
     * 2.指定导入的类的处理顺序
     *
     */
    //延迟导入
    /**
     * 比如@Import的value包含了多个普通类、多个@Configuration标注的配置类、多个ImportSelector接口实现类、多个ImportBeanDefinitionRegistrar接口的实现类，还有DeferredImportSelector接口实现类，
     * 此时spring处理这些被导入的类的时候，
     * 会将DeferredImportSelector类型的放在最后处理，会先处理其他被导入的类,其他类会按照value所在的前后顺序进行处理。
     *
     * 那么我们就可以因此做很多事情，比如可以在DeferredImportSelector导入的类中判断一下容器中是否已经注册了某个bean，如果没有注册过，那么再来注册。
     *
     * 另一个注解@Conditional，这个注解可以按条件来注册bean，比如可以判断某个bean不存在时才进行注册，某个类存在时才进行注册等等各种判断条件，通过
     * @Conditional结合@DeferredImprotSelector可以做很多事情。
     */
    @Test
    public void test7() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig7.class);
        /**
         * name2
         * name1
         * name3
         *
         * 输出的结果结合一下@import中被导入的3个类的顺序，可以看出DeferredImportSelector3是被最后处理的，其他2个是按照在value中所在的先后顺序处理的。
         *
         */
    }

    //指定导入的类的处理顺序
    /**
     * 当@import中有多个DeferredImportSelector接口的实现类时，可以指定他们的顺序，指定顺序常见2种方式
     */
    //1.实现Ordered接口的方式
    //2.使用@Order注解
    @Test
    public void test8() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig8.class);
        /**
         * name1
         * name3
         * name2
         */
    }

    /**
     * 在spring中，@Import注解是被下面这个类处理的
     * org.springframework.context.annotation.ConfigurationClassPostProcessor
     * @Configuration、@Bean、@ComponentScan、@ComponentScans都是被这个类处理的，
     */


    /**
     * 总结：
     * 1.@Import可以用来批量导入任何普通注解、配置类，将这些类中定义的所有bean注册到容器中
     * 2.@import常见的5种用法需要掌握
     * 3.掌握ImportSelector、ImportBeanDefinitionRegistrar、DeferredImportSelector的用法
     * 4.DeferredImportSelector接口可以实现延迟导入、按序导入的功能
     * 5.spring中很多以@Enable开头的都是使用@Import结合ImportSelector的方式实现的
     * 6.BeanDefinitionRegistrar接口：bean定义注册器
     */
}