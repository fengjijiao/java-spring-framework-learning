package us.fjj.spring.learning.annotationimplementDI.resourceannotationusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.MainConfig3;
import us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test4.MainConfig4;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @Resource： 注意依赖对象
 * 作用：
 * 和@Autowired注解类似，也是用来注入依赖对象的，spring容器会对bean中所有字段、方法进行遍历，标注有@Resource注解的，都会进行注入。
 * 注意：这个注解是在javax中定义的，并不是spring中定义的注解。
 * 用在方法上时，方法参数只能有一个
 *
 * @Resource查找候选者可以简化为
 * 先按Resource的name值作为bean名称找->按名称(字段名称、方法名称、set属性名称)找->按类型->通过限定符@Qualifier过滤->@Primary->@Priority->根据名称找（字段名称或者方法参数名称）
 * 概括为：先按名称找，然后按类型找
 */
public class ResourceAnnotationTest {
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@7bc10d84
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@275fe372
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@40e10ff8
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@557a1e2d
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@26a4842b
         * mainConfig1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainConfig1$$EnhancerBySpringCGLIB$$ad9d4c2f@7e38a7fe
         * us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.Service1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.Service1@366ef90e
         * us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.Service2->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.Service2@33e01298
         * us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainRun->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainRun@31e75d13
         */
    }

    /**
     * @Resource用于接口类型之上
     */
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@75f65e45
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@6eeade6c
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@4a891c97
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@a5bd950
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@4d18aa28
         * mainConfig2->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2.MainConfig2@75390459
         * service0->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2.Service0@7756c3cd
         * service1->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2.Service1@2313052e
         * service2->Service2{service1=us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2.Service1@2313052e}
         */
        /**
         * 可以看出注入的是Service1，因为字段名称为service1
         */
    }
    /**
     * @Resource注入相同类型的bean对象到List/Map内
     */
    @Test
    public void test3() {
        AnnUtil.printAllBean(MainConfig3.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@5c87bfe2
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@2fea7088
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@40499e4f
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@51cd7ffc
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@30d4b288
         * mainConfig3->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.MainConfig3@4cc6fa2a
         * service0->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service0@20a8a64e
         * service1->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service1@62f4ff3b
         * service2->Service2{services=[us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service0@20a8a64e, us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service1@62f4ff3b], serviceMap={service0=us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service0@20a8a64e, service1=us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3.Service1@62f4ff3b}}
         */
    }
    /**
     * @Resource用于方法之上
     */
    @Test
    public void test4() {
        AnnUtil.printAllBean(MainConfig4.class);
        /**
         *org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@ea9b7c6
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@e077866
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@ed3068a
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@7c2b6087
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@3fffff43
         * mainConfig4->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test4.MainConfig4@a8e6492
         * service1->us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test4.Service1@1c7fd41f
         * service2->Service2{service1=us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test4.Service1@1c7fd41f}
         */
    }
    /**
     * @Resource源码
     * spring使用下面这个类处理@Resource注解
     * org.springframework.context.annotaiton.CommonAnnotationBeanPostProcessor
     */
}
