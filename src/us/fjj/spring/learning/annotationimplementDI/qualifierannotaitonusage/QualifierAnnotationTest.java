package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1.MainConfig1;
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
}
