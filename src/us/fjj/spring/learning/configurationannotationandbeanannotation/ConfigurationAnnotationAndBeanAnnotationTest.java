package us.fjj.spring.learning.configurationannotationandbeanannotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class ConfigurationAnnotationAndBeanAnnotationTest {
    public static void main(String[] args) {
        //通过AnnotationConfigApplicationContext来加载@Configuration修饰的类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigBean.class);
        //此时ConfigBean类中没有任何内容，相当于一个空的xml配置文件，此时我们要在ConfigBean类中注册bean，那么我们就要用到@Bean注解了

        //@Bean注解
        AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(ABean.class);
        for (String beanName :
                context1.getBeanDefinitionNames()) {
            String[] alisas = context1.getAliases(beanName);
            //Arrays.stream(alisas).forEach(System.out::println);
            System.out.println(String.format("bean名称：%s，别名：%s，bean对象：%s",
                    beanName,
                    Arrays.asList(alisas),
                    context1.getBean(beanName)
            ));
        }
        /**
         *bean名称：org.springframework.context.annotation.internalConfigurationAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.ConfigurationClassPostProcessor@55493582
         * bean名称：org.springframework.context.annotation.internalAutowiredAnnotationProcessor，别名：[]，bean对象：org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@1a20270e
         * bean名称：org.springframework.context.annotation.internalCommonAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@6b88ca8c
         * bean名称：org.springframework.context.event.internalEventListenerProcessor，别名：[]，bean对象：org.springframework.context.event.EventListenerMethodProcessor@336f1079
         * bean名称：org.springframework.context.event.internalEventListenerFactory，别名：[]，bean对象：org.springframework.context.event.DefaultEventListenerFactory@2f16c6b3
         * bean名称：ABean，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.ABean$$EnhancerBySpringCGLIB$$f0488866@34158c08
         * bean名称：user1，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@19e4fcac
         * bean名称：user2Bean，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@52c3cb31
         * bean名称：user3Bean，别名：[user3BeanAlias2, user3BeanAlias1]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@4b79ac84
         */
        /**
         * 上面的输出，主要关注最后4行，前面的可以忽略
         * 从输出中可以看出，有个名称为ABean的bean，正是ABean类型，可以得出，被@Configuration修饰的类，也被注册到了spring容器中了。
         *
         */

        //去掉@Configuration会怎样？
        AnnotationConfigApplicationContext context2 = new AnnotationConfigApplicationContext(NoConfigurationBean.class);
        for (String beanName :
                context2.getBeanDefinitionNames()) {
            System.out.println(String.format("bean名称：%s，别名：%s，bean对象：%s", beanName, Arrays.asList(context2.getAliases(beanName)), context2.getBean(beanName)));
        }
        /**
         * bean名称：org.springframework.context.annotation.internalConfigurationAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.ConfigurationClassPostProcessor@29a0cdb
         * bean名称：org.springframework.context.annotation.internalAutowiredAnnotationProcessor，别名：[]，bean对象：org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@32a68f4f
         * bean名称：org.springframework.context.annotation.internalCommonAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@73194df
         * bean名称：org.springframework.context.event.internalEventListenerProcessor，别名：[]，bean对象：org.springframework.context.event.EventListenerMethodProcessor@6eb2384f
         * bean名称：org.springframework.context.event.internalEventListenerFactory，别名：[]，bean对象：org.springframework.context.event.DefaultEventListenerFactory@3c9c0d96
         * bean名称：noConfigurationBean，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.NoConfigurationBean@3a4621bd
         * bean名称：user1，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@31dadd46
         * bean名称：user2Bean，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@4ed5eb72
         * bean名称：user3Bean，别名：[user3BeanAlias2, user3BeanAlias1]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.User@12f9af83
         *
         */
        /**
         * 对比有@Configuration和无@Configuration的最后四行得出：
         * 1.对比最后三行，可以看出：有没有@Configuration注解，@Bean都会起效，都会将@Bean修饰的方法作为bean注册到容器中
         * 2.两个内容的第一行有点不一样，被@Configuration修饰的bean最后输出的时候带有EnhancerBySpringCGLIB的字样，而没有@Configuration注解的bean没有CGLIB的字样；有EnhancerBySpringCGLIB字样的说明这个bean被cglib处理过的，变成了一个代理对象。
         * 目前还看不出本质区别
         */

        //@Configuration加不加到底区别在哪？
        //通常情况下，bean之间是有依赖关系的，创建一个有依赖关系的bean。
        AnnotationConfigApplicationContext context3 = new AnnotationConfigApplicationContext(BBean.class);
        for (String beanName :
                context3.getBeanDefinitionNames()) {
            //别名
            String[] aliases = context3.getAliases(beanName);
            System.out.println(String.format("bean的名称：%s，别名：%s，bean对象：%s",
                    beanName,
                    Arrays.asList(aliases),
                    context3.getBean(beanName)));
        }
        /**
         * 调用了ServiceA()方法
         * 调用了ServiceB1()方法
         * 调用了ServiceB2()方法
         * bean的名称：org.springframework.context.annotation.internalConfigurationAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.ConfigurationClassPostProcessor@386f0da3
         * bean的名称：org.springframework.context.annotation.internalAutowiredAnnotationProcessor，别名：[]，bean对象：org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@39655d3e
         * bean的名称：org.springframework.context.annotation.internalCommonAnnotationProcessor，别名：[]，bean对象：org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@34f22f9d
         * bean的名称：org.springframework.context.event.internalEventListenerProcessor，别名：[]，bean对象：org.springframework.context.event.EventListenerMethodProcessor@3d1848cc
         * bean的名称：org.springframework.context.event.internalEventListenerFactory，别名：[]，bean对象：org.springframework.context.event.DefaultEventListenerFactory@7dda48d9
         * bean的名称：BBean，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.BBean$$EnhancerBySpringCGLIB$$2272b307@6e4566f1
         * bean的名称：serviceA，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@4b6e2263
         * bean的名称：serviceB1，别名：[]，bean对象：ServiceB{serviceA=us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@4b6e2263}
         * bean的名称：serviceB2，别名：[]，bean对象：ServiceB{serviceA=us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@4b6e2263}
         *
         */
        //分析
        /**
         * 从输出中可以看出：
         * 1.前3行可以看出，被@Bean修饰的方法都只被调用了一次
         * 2.最后3行中可以看出都是同一个ServiceA对象，都是ServiceA@4b6e2263这个实例
         */
        //这是为什么？
        /**
         * 被@Configuration修饰的类，spring容器中会通过cglib给这个类创建一个代理，代理会拦截所有被@Bean修饰的方法，
         * 默认情况（bean为单例模式）下确保这些方法只被调用一次，从而确保这些bean是同一个bean，即单例的。
         */
        /**
         * 将@Configuration注解注释掉，显示结果如下：
         * bean的名称：serviceA，别名：[]，bean对象：us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@39655d3e
         * bean的名称：serviceB1，别名：[]，bean对象：ServiceB{serviceA=us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@34f22f9d}
         * bean的名称：serviceB2，别名：[]，bean对象：ServiceB{serviceA=us.fjj.spring.learning.configurationannotationandbeanannotation.ServiceA@3d1848cc}
         * 最后三行的serviceA不是同一个serviceA。
         */

        //总结
        /**
         * 1.@Configuration修饰的类，会被spring通过cglib做增强处理，通过cglib会生成一个代理对象，代理会拦截所有被@Bean注解修饰的方法，可以确保一些bean是单例的
         * 2.不管@Bean所在的类上是否有@Configuration注解，都可以将@Bean修饰的方法作为一个bean注册到spring容器中
         */

    }
}
