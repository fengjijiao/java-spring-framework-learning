package us.fjj.spring.learning.dependsonannotationusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.dependsonannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.dependsonannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @DependsOn等效于bean xml中的bean元素中的depend-on属性。
 * spring在创建bean的时候，如果bean之间没有依赖关系，那么spring容器很难保证bean实例创建的顺序，如果想确保容器在创建某些bean之前，需要先创建好一些其他的bean，可以通过@DependsOn来实现，@DependsOn可以指定当前bean依赖的bean，通过这个可以确保@DependsOn指定的bean在当前bean创建之前先创建好
 */
/**
 * 常见的2种用法
 * 1.和@Component一起使用在类上
 * 2.和@Bean一起标注在方法上
 */
public class DependsOnAnnotationTest {
    //案例一:和@Component一起使用在类上
    //定义3个bean: service1、service2、service3；service1需要依赖于其他2个service，需要确保容器在创建service1之前需要先将其他2个bean先创建好。
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * 创建了Service1
         * 创建了Service2
         * 创建了Service3
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@478ee483
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@1a7288a3
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@2974f221
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@58fe0499
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@686449f9
         * mainConfig1->us.fjj.spring.learning.dependsonannotationusage.test1.MainConfig1@665df3c6
         * service1->us.fjj.spring.learning.dependsonannotationusage.test1.Service1@68b6f0d6
         * service2->us.fjj.spring.learning.dependsonannotationusage.test1.Service2@4044fb95
         * service3->us.fjj.spring.learning.dependsonannotationusage.test1.Service3@aa549e5
         */
    }
    //案例二：和bean一起标注在方法上
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * 创建了Service1
         * 创建了Service2
         * 创建了Service3
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@6731787b
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@16f7b4af
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7adf16aa
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@34a1d21f
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@58bf8650
         * mainConfig2->us.fjj.spring.learning.dependsonannotationusage.test2.MainConfig2$$EnhancerBySpringCGLIB$$fd6502bb@73c60324
         * service1->us.fjj.spring.learning.dependsonannotationusage.test2.Service1@71ae31b0
         * service2->us.fjj.spring.learning.dependsonannotationusage.test2.Service2@4ba534b0
         * service3->us.fjj.spring.learning.dependsonannotationusage.test2.Service3@6f0ca692
         */
    }
}
