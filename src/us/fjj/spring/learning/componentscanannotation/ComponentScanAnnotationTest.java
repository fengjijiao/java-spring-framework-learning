package us.fjj.spring.learning.componentscanannotation;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.componentscanannotation.test2.ScanBean3;
import us.fjj.spring.learning.componentscanannotation.test3.ScanBean4;
import us.fjj.spring.learning.componentscanannotation.test4.ScanBean5;
import us.fjj.spring.learning.componentscanannotation.test5.ScanBean6;

/**
 * 到目前为止有两种注册bean的方式：
 * 1.xml中bean元素的方式
 * 2.@Bean注解标注方法的方式
 * 通常情况下，项目中大部分类都需要交给spring去管理，按照上面的2中方式，代码量还是挺大的。
 * 为了更方便bean的注册，Spring提供了批量的方式注册bean，方便大量bean批量注册，spring中的@ComponentScan就是干这个事情的
 *
 * @ComponentScan用于批量注册bean 这个注解会让spring去扫描某些包及其子包中所有的类，然后将满足一定条件的类作为bean注册到spring容器中。
 * 具体需要扫描哪些包？以及这些包中的类满足上面条件时被注册到容器中，这些都可以通过这个注解中的参数动态配置。
 * @ComponentScan工作流程： 1.Spring会扫描指定的包，且会递归下面的子包，得到一批类的数组
 * 2.然后这些类会经过上面的各种过滤器，最后剩下的类会被注册到容器中
 * <p>
 * 需要扫描哪些包？通过value、backPackages、basePackageClasses这3个参数来控制
 * 过滤器有哪些？通过useDefaultFilters、includeFilters、excludeFilters这3个参数来控制过滤器
 * <p>
 * 默认情况下，任何参数都不设置的情况下，会将@ComponentScan修饰的类所在的包作为扫描包；默认情况下useDefaultFilters为true，为true时spring容器内会使用默认过滤器，规则是：
 * 凡是类上有@Repository、@Service、@Controller、@Component这几个注解中的任何一个的，那么这个类就会被作为bean注册到spring容器中，所以默认情况下，只需在类上加上这几个注解中的任何一个，这个类就会自动交给spring容器来管理。
 */
public class ComponentScanAnnotationTest {
    @Test
    public void test1() {
        //使用AnnotationConfigApplicationContext作为ioc容器，将ScanBean1作为参数传入
        //默认会扫描ScanBean1类所在的包中的所有类，类上有@Component、@Repository、@Service、@Controller任何一个注解的都会被注册到容器中
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanBean1.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(beanName + "->" + context.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@418c5a9c
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@18e36d14
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@5082d622
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@13d4992d
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@302f7971
         * scanBean1->us.fjj.spring.learning.componentscanannotation.ScanBean1@332729ad
         * userModel->us.fjj.spring.learning.componentscanannotation.UserModel@75d2da2d
         * userController->us.fjj.spring.learning.componentscanannotation.test1.controller.UserController@4278284b
         * userDao->us.fjj.spring.learning.componentscanannotation.test1.dao.UserDao@9573584
         * userService->us.fjj.spring.learning.componentscanannotation.test1.service.UserService@3370f42
         */
    }

    @Test
    public void test2() {
        //指定需要扫描的包
        AnnotationConfigApplicationContext context2 = new AnnotationConfigApplicationContext(ScanBean2.class);
        for (String beanName :
                context2.getBeanDefinitionNames()) {
            System.out.println(beanName + "->" + context2.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@3eb77ea8
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7b8b43c7
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7aaca91a
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@44c73c26
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@41005828
         * scanBean2->us.fjj.spring.learning.componentscanannotation.ScanBean2@60b4beb4
         * userController->us.fjj.spring.learning.componentscanannotation.test1.controller.UserController@7fcf2fc1
         * userService->us.fjj.spring.learning.componentscanannotation.test1.service.UserService@2141a12
         */
        //可以看出只有controller包和service包中的2个类被注册为bean了。
        /**
         * 注意
         * 指定包名的方式扫描存在的一个隐患，若包被重名了，会导致扫描失效，一般情况下我们使用basePackageClasses
         * 的方法来指定需要扫描的包，这个参数可以指定一些类型，默认会扫描这些类所在的包及其子包中所有的类，这种方式可有效避免这种问题。
         *如下
         */
    }

    @Test
    public void test3() {
        //basePackageClasses指定扫描范围
        /**
         * 可以在需要扫描的包中定义一个标记的接口或者类，他们的唯一的作用是作为basePackageClasses的值，没有其他任何用途。
         */
        AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(ScanBean3.class);
        for (String beanName :
                context1.getBeanDefinitionNames()) {
            System.out.println(beanName + "->" + context1.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@3e7634b9
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@6f0b0a5e
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@6035b93b
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@320de594
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@3dd1dc90
         * scanBean3->us.fjj.spring.learning.componentscanannotation.test2.ScanBean3@abf688e
         * service1->us.fjj.spring.learning.componentscanannotation.test2.t.Service1@478ee483
         * service2->us.fjj.spring.learning.componentscanannotation.test2.t.Service2@1a7288a3
         *
         */
    }

    //includeFilters的使用
    @Test
    public void test4() {

        //Filter[] includeFilters() default {};
        //是一个Filter类型的数组，多个Filter之间为或者关系，即满足任意一个就可以了，看一下Filter的参数。
        /**
         * type: 过滤器的类型：是个枚举类型，5种类型
         * ANNOTATION: 通过注解的方式来筛选候选者，即判断候选者是否有指定的注解
         * ASSIGNABLE_TYPE: 通过指定的类型来筛选候选者，即判断候选者是否是指定的类型
         * ASPECTJ: ASPECTJ表达式方式，即判断候选者是否匹配ASPECTJ表达式
         * REGEX: 正则表达式方式，即判断候选者的完整名称是否和正则表达式匹配
         * CUSTOM: 用户自定义过滤器来筛选候选者，对候选者的筛选交给用户自己来判断
         *
         * value: 和参数classes效果一样，二选一
         * classes: 3中情况如下
         * 当type=FilterType.ANNOTATION时，通过classes参数可以指定一些注解，用来判断被扫描的类上是否有classes参数指定的注解
         * 当type=FilterType.ASSIGNABLE_TYPE时，通过classes参数可以指定一些类型，用来判断被扫描的类是否是classes参数指定的类型
         * 当type=FilterType.CUSTOM时，表示这个过滤器是用户自定义的，classes参数就是用来指定用户自定义的过滤器，自定义的过滤器需要实现org.springframework.core.type.filter.TypeFilter接口
         *
         * pattern: 2种情况下
         * 当type=FilterType.ASPECTJ时，通过pattern来指定需要匹配的ASPECTJ表达式的值
         * 当type=FilterType.REGEX时，通过pattern来指定正则表达式的值
         */

        //扫描包含注解的类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanBean4.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(beanName + "->" + context.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@58fe0499
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@686449f9
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@665df3c6
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@68b6f0d6
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@4044fb95
         * scanBean4->us.fjj.spring.learning.componentscanannotation.test3.ScanBean4@aa549e5
         * service1->us.fjj.spring.learning.componentscanannotation.test3.Service1@36f48b4
         * service2->us.fjj.spring.learning.componentscanannotation.test3.Service2@5c00384f
         */
        /**
         * 可以看到Service2也被注册到容器中了，原因是@ComponentScan注解中的useDefaultFilters默认为true,表示也会启用默认过滤器，而默认过滤器会将标注有@Component、@Repository、@Service、@Controller这几个注解的类也注册到容器中
         * 如果我们只想要将标注@MyBean的bean注册到容器，需要将默认过滤器关闭，即:
         * useDefaultFilters=false,修改ScanBean4后运行如下
         */
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@abf688e
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@478ee483
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@1a7288a3
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@2974f221
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@58fe0499
         * scanBean4->us.fjj.spring.learning.componentscanannotation.test3.ScanBean4@686449f9
         * service1->us.fjj.spring.learning.componentscanannotation.test3.Service1@665df3c6
         */
    }

    //包含指定类型的类
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanBean5.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(beanName+"->"+context.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@5bc9ba1d
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@1021f6c9
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7516e4e5
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@488eb7f2
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@5e81e5ac
         * scanBean5->us.fjj.spring.learning.componentscanannotation.test4.ScanBean5@4189d70b
         * service1->us.fjj.spring.learning.componentscanannotation.test4.Service1@3fa2213
         * service2->us.fjj.spring.learning.componentscanannotation.test4.Service2@3e7634b9
         */
    }

    //自定义Filter
    @Test
    public void test6() {
        //使用自定义过滤器的步骤:
        //1.设置@Filter中type的类型为：FilterType.CUSTOM
        //2.自定义过滤器类，需要实现接口：org.springframework.core.type.filter.TypeFilter
        //3.设置@Filter中的classes为自定义的过滤器类型
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScanBean6.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(beanName+"->"+context.getBean(beanName));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@1a7288a3
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@2974f221
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@58fe0499
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@686449f9
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@665df3c6
         * scanBean6->us.fjj.spring.learning.componentscanannotation.test5.ScanBean6@68b6f0d6
         * service1->us.fjj.spring.learning.componentscanannotation.test5.Service1@4044fb95
         */
    }

    //excludeFilters
    //配置排除的过滤器，满足这些过滤器的类不会被注册到容器中，用法和includeFilters一样

    /**
     * @ComponentScan注解是被下面这个类处理的
     * org.springframework.context.annotation.ConfigurationClassPostProcessor
     * 这个类非常关键，主要用于bean的注册，@Configuration、@Bean注解也是被这个类处理的
     * 以及以下的这些注解：
     * @PropertySource
     * @Import
     * @ImportResource
     * @Component
     * 这些注解都是被ConfigurationClassPostProcessor这个类处理的，内部会递归处理这些注解，完成bean的注册。
     * 以@CompontentScan来说一下过程，第一次扫描之后会得到一批需要注册的类，然后会对这些需要注册的类进行遍历，
     * 判断是否有上面任意一个注解，如果有，会将这个类交给ConfigurationClassPostProcessor继续处理，直到递归完成所有bean的注册
     *
     */
}

/**
 * 总结
 * 1.@ComponentScan用于批量注册bean，Spring会按照这个注解的配置，递归扫描指定包中的所有类，将满足条件的类批量注册到Spring容器中
 * 2.可以通过value、basePackages、basePackageClasses这几个参数来配置包的扫描范围
 * 3.可以通过useDefaultFilters、includeFilters、excludeFilters这几个参数来配置类的过滤器，被过滤器处理之后剩下的类会被注册到容器中
 * 4.指定包名的方式配置扫描范围存在隐患，包名被重命名后，会导致扫描失效，所以一般我们在需要扫描的包中可以创建一个标记的接口或者类，作为basePackageClasses的值，通过这个来控制包的扫描范围
 * 5.@ComponentScan注解会被ConfigurationClassPostProcessor类递归处理，最终得到所有需要注册的类。
 *
 */
