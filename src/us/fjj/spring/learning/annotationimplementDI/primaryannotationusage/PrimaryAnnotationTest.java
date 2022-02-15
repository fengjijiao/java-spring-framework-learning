package us.fjj.spring.learning.annotationimplementDI.primaryannotationusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @Primary 设置为主要候选者
 * 注入依赖的过程中，当有多个候选者的时候，可以指定哪个候选者为主要的候选者。
 */
public class PrimaryAnnotationTest {
    /**
     * 案例1: 用在类上
     */
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@57fd91c9
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@6cfcd46d
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@52045dbe
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@674658f7
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@5c8eee0f
         * mainConfig1->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1.MainConfig1@565b064f
         * service1->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1.Service1@26425897
         * service2->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1.Service2@73163d48
         * service3->Service3{service=us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1.Service2@73163d48}
         */
        /**
         * 在Service2类上定义了@Primary注解，因此这里注入的是Service2
         */
    }
    /**
     * 案例2：用在方法上，结合@Bean使用
     */
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@7e3f95fe
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@34625ccd
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@2c7d121c
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@65aa6596
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@67389cb8
         * mainConfig2->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2.MainConfig2$$EnhancerBySpringCGLIB$$f93051e8@419a20a6
         * providerService1->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2.Service1@533377b
         * providerService2->us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2.Service2@3383649e
         * providerService3->Service3{service=us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2.Service2@3383649e}
         */
    }
}
