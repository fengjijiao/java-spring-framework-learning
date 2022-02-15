package us.fjj.spring.learning.annotationimplementDI.other;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.annotationimplementDI.other.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.other.test2.MainConfig2;
import us.fjj.spring.learning.annotationimplementDI.other.test3.MainConfig3;
import us.fjj.spring.learning.annotationimplementDI.other.test4.MainConfig4;
import us.fjj.spring.learning.annotationimplementDI.other.test4.service.OrderService;
import us.fjj.spring.learning.annotationimplementDI.other.test4.service.UserService;
import us.fjj.spring.learning.utils.AnnUtil;

//其他
public class OtherTest {
    /**
     * @Bean标注的方法参数上使用@Autowired注解
     *
     */
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@1ddae9b5
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@427b5f92
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@24bdb479
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@7e3f95fe
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@34625ccd
         * mainConfig1->us.fjj.spring.learning.annotationimplementDI.other.test1.MainConfig1$$EnhancerBySpringCGLIB$$453747f1@2c7d121c
         * service1->us.fjj.spring.learning.annotationimplementDI.other.test1.Service1@65aa6596
         * service3->Service3{service1=us.fjj.spring.learning.annotationimplementDI.other.test1.Service1@65aa6596, service2=null}
         */
    }
    /**
     * @Bean结合@Qualifier
     */
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@21d8bcbe
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@5be067de
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7383eae2
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@18245eb0
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@7c7d3c46
         * mainConfig2->us.fjj.spring.learning.annotationimplementDI.other.test2.MainConfig2$$EnhancerBySpringCGLIB$$490df716@24fb6a80
         * service0->us.fjj.spring.learning.annotationimplementDI.other.test2.Service0@48c35007
         * service1->us.fjj.spring.learning.annotationimplementDI.other.test2.Service1@72a85671
         * service2->us.fjj.spring.learning.annotationimplementDI.other.test2.Service2@6722db6e
         * service3->Service3{serviceMap={service0=us.fjj.spring.learning.annotationimplementDI.other.test2.Service0@48c35007, service2=us.fjj.spring.learning.annotationimplementDI.other.test2.Service2@6722db6e}}
         */
    }

    /**
     * 注入测试
     */
    @Test
    public void test3() {
        AnnUtil.printAllBean(MainConfig3.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@10c626be
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@2fc0cc3
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@328cf0e1
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@63b1d4fa
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@42e3ede4
         * mainConfig3->us.fjj.spring.learning.annotationimplementDI.other.test3.MainConfig3@201b6b6f
         * service1->us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service1@75459c75
         * service2->us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service2@183e8023
         * service5->Service5{serviceMap={service1=us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service1@75459c75, service2=us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service2@183e8023, service3=us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service3@45efc20d, service4=us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service4@3e5499cc}}
         * service3->us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service3@45efc20d
         * service4->us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service4@3e5499cc
         * service6->Service5{serviceMap={service1=us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service1@75459c75, service2=us.fjj.spring.learning.annotationimplementDI.other.test3.module1.Service2@183e8023, service3=us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service3@45efc20d, service4=us.fjj.spring.learning.annotationimplementDI.other.test3.module2.Service4@3e5499cc}}
         * us.fjj.spring.learning.annotationimplementDI.other.test3.module1.ModuleConfig1->us.fjj.spring.learning.annotationimplementDI.other.test3.module1.ModuleConfig1@67ab1c47
         * us.fjj.spring.learning.annotationimplementDI.other.test3.module2.ModuleConfig2->us.fjj.spring.learning.annotationimplementDI.other.test3.module2.ModuleConfig2@b78a709
         */
        /**
         * 可以看到service5和service6都是注入了service1~4
         */
    }

    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        System.out.println(context.getBean(UserService.class).getDao());
        System.out.println(context.getBean(OrderService.class).getDao());
        /**
         * us.fjj.spring.learning.annotationimplementDI.other.test4.dao.UserDao@a776e
         * us.fjj.spring.learning.annotationimplementDI.other.test4.dao.OrderDao@792bbc74
         */
        /**
         * dao属性并没有指定具体需要注入哪个bean，此时是根据尖括号种的泛型类型来匹配的，👍
         */
    }
}
