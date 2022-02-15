package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2.MainConfig2;
import us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.MainConfig3;
import us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.MainConfig4;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * @Qualifier:限定符
 * 作用：可以在依赖注入查找候选者的过程中对候选者进行过滤。
 *
 */
public class QualifierAnnotationTest {
    /**
     * 用在类上：给通过@Qualifier给这个bean打一个标签
     */
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@40f1be1b
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7a791b66
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@6f2cb653
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@14c01636
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@590c73d3
         * mainConfig1->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.MainConfig1@6b9ce1bf
         * service1->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service1@53d1b9b3
         * service2->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service2@2cae1042
         * service3->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service3@61884cb1
         * service4->Service4{services=[us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service1@53d1b9b3, us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service2@2cae1042], serviceMap={service3=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.Service3@61884cb1}}
         */
        /**
         * 可以看到：
         * services中只有Service1和Service2
         * serviceMap中只有Service3
         * 实现了bean的分组效果
         */
    }
    /**
     * 案例2：使用@Autowired结合@Qualifier指定注入的bean
     * 被注入的类型有多个的时候，可以使用@Qualifier来指定需要注入哪个bean，将@Qualifer的value设置为需要注入的bean的名称
     */
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         *org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@4a891c97
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@a5bd950
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@4d18aa28
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@75390459
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@7756c3cd
         * mainConfig2->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2.MainConfig2@2313052e
         * service1->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2.Service1@2bd2b28e
         * service2->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2.Service2@16746061
         * service3->Service3{service=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test2.Service2@16746061}
         */
        /**
         * 可以看到注入的是Service2
         */
    }
    /**
     * 案例3：使用在方法参数上
     */
    @Test
    public void test3() {
        AnnUtil.printAllBean(MainConfig3.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@3b77a04f
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@7b324585
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@2e11485
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@60dce7ea
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@662f5666
         * mainConfig3->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.MainConfig3@fd8294b
         * service1->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.Service1@5974109
         * service2->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.Service2@27305e6
         * service3->Service3{s1=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.Service1@5974109, s2=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test3.Service2@27305e6}
         */
    }
    /**
     * 案例4：使用在set方法/普通方法上
     */
    @Test
    public void test4() {
        AnnUtil.printAllBean(MainConfig4.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@1c7fd41f
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@3b77a04f
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7b324585
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@2e11485
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@60dce7ea
         * mainConfig4->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.MainConfig4@662f5666
         * service1->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.Service1@fd8294b
         * service2->us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.Service2@5974109
         * service3->Service3{s1=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.Service1@fd8294b, s2=us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test4.Service2@5974109}
         */
    }

}
