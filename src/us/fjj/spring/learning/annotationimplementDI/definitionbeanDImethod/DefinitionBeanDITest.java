package us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.MainConfig2;
import us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.MainConfig3;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @Bean定义bean依赖注入的几种方式
 * 常见有3种方式
 * 1.硬编码
 * 2.@Autowired、@Resource的方式
 * 3.@Bean标注的方法参数方式
 */
public class DefinitionBeanDITest {
    //方式一
    @Test
    public void method1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@4275c20c
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7c56e013
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@3fc9dfc5
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@40258c2f
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@2cac4385
         * mainConfig1->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.MainConfig1$$EnhancerBySpringCGLIB$$eb63d7ea@6731787b
         * providerService1->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.Service1@16f7b4af
         * providerService2->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.Service2@7adf16aa
         * providerService3->Service3{service1=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.Service1@16f7b4af, service2=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method1.Service2@7adf16aa}
         */
    }
    //方式二
    @Test
    public void method2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@e077866
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@ed3068a
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7c2b6087
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@3fffff43
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@a8e6492
         * mainConfig2->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.MainConfig2@1c7fd41f
         * service1->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.Service1@3b77a04f
         * service2->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.Service2@7b324585
         * service3->Service3{service1=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.Service1@3b77a04f, service2=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2.Service2@7b324585}
         */
    }
    //方式三
    @Test
    public void method3() {
        AnnUtil.printAllBean(MainConfig3.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@24bdb479
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7e3f95fe
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@34625ccd
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@2c7d121c
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@65aa6596
         * mainConfig3->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.MainConfig3$$EnhancerBySpringCGLIB$$9388013f@67389cb8
         * providerService1->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.Service1@419a20a6
         * providerService2->us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.Service2@533377b
         * providerService3->Service3{service1=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.Service1@419a20a6, service2=us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3.Service2@533377b}
         */
    }
}
