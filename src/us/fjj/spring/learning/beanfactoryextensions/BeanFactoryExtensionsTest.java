package us.fjj.spring.learning.beanfactoryextensions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import us.fjj.spring.learning.beanfactoryextensions.test1.MainConfig1;
import us.fjj.spring.learning.beanfactoryextensions.test2.MainConfig2;
import us.fjj.spring.learning.beanfactoryextensions.test3.MainConfig3;
import us.fjj.spring.learning.beanfactoryextensions.test4.MainConfig4;
import us.fjj.spring.learning.utils.AnnUtil;

/**
 * BeanFactory扩展（BeanFactoryPostProcessor、BeanDefinitionRegistryPostProcessor）
 *
 * spring容器中的主要的4个阶段
 * 阶段1：Bean注册阶段，此阶段会完成所有bean的注册 {@link AbstractApplicationContext#invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory)} ()}
 * 阶段2：BeanFactory后置处理阶段 {@link AbstractApplicationContext#invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory)} ()}
 * 阶段3：注册BeanPostProcessor {@link AbstractApplicationContext#registerBeanPostProcessors(ConfigurableListableBeanFactory)}
 * 阶段4：bean创建阶段,此阶段完成所有单例bean的注册和装载操作。 {@link AbstractApplicationContext#finishBeanFactoryInitialization(ConfigurableListableBeanFactory)}
 */
public class BeanFactoryExtensionsTest {
    /**
     * 阶段1：此阶段spring为我们提供了一个接口，BeanDefinitionRegistryPostProcessor，spring容器在这个阶段中获取容器中所有类型为BeanDefinitionRegistryPostProcessor的bean,然后会调用他们的postProcessBeanDefinitionRegistry方法。
     * @see org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(BeanDefinitionRegistry)
     */
    @Test
    public void test1() {
        AnnUtil.printAllBean(MainConfig1.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@6f8f9349
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@75c9e76b
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7446d8d5
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@5c3b6c6e
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@4fbda97b
         * mainConfig1->us.fjj.spring.learning.beanfactoryextensions.test1.MainConfig1@75f5fd58
         * myBeanDefinitionRegistryPostProcessor->us.fjj.spring.learning.beanfactoryextensions.test1.MyBeanDefinitionRegistryPostProcessor@37b70343
         * username->lk
         */
    }
    /**
     * 多个指定顺序@PriorityOrdered、@Ordered
     */
    @Test
    public void test2() {
        AnnUtil.printAllBean(MainConfig2.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@dcfda20
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@6d304f9d
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@f73dcd6
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@5c87bfe2
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@2fea7088
         * mainConfig2->us.fjj.spring.learning.beanfactoryextensions.test2.MainConfig2@40499e4f
         * myBeanDefinitionRegistryPostProcessor1->us.fjj.spring.learning.beanfactoryextensions.test2.MyBeanDefinitionRegistryPostProcessor1@51cd7ffc
         * myBeanDefinitionRegistryPostProcessor2->us.fjj.spring.learning.beanfactoryextensions.test2.MyBeanDefinitionRegistryPostProcessor2@30d4b288
         * name->sb lk
         * address->二仙桥
         */
    }
    /**
     * 阶段2：BeanFactory后置处理阶段
     * 到这个阶段，spring容器已经完成所有bean的注册，这个阶段可以对BeanFactory中的一些信息进行修改，比如修改阶段1中一些bean的定义信息，修改BeanFactory的一些配置等等，此阶段spring也提供了一个接口来进行拓展:BeanFactoryPostProcessor，接口中有个方法postProcessBeanFactory，spring会获取容器中所有BeanFactoryPostProcessor类型的bean,然后调用他们的postProcessBeanFactory。
     */
    /**
     * 案例：修改属性值
     */
    @Test
    public void test3() {
        AnnUtil.printAllBean(MainConfig3.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@60dce7ea
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@662f5666
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@fd8294b
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@5974109
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@27305e6
         * mainConfig3->us.fjj.spring.learning.beanfactoryextensions.test3.MainConfig3@1ef3efa8
         * lessonInfo->LessonInfo{name='mathematics'}
         * myBeanFactoryPostProcessor->us.fjj.spring.learning.beanfactoryextensions.test3.MyBeanFactoryPostProcessor@502f1f4c
         */
        /**
         * 可见lessonInfo的name值已经被修改为了mathematics。
         */
    }

    /**
     * 使用注意：
     * BeanFactoryPostProcessor接口的使用有一个需要注意的地方，在其postProcessBeanFactory方法中，强烈禁止去通过容器获取其他bean，此时会导致bean的提前初始化，会出现一些意想不到的问题，因为这个阶段中BeanPostProcessor还未准备好，
     * BeanPostProcessor是在第3个阶段中注册到spring容器的，而BeanPostProcessor可以对bean的创建过程进行干预，比如spring的aop就是在BeanPostProcessor的一些子类中实现的，@Autowired也是在BeanFactoryProcessor的子类中处理的，此时如果去获取bean，此时bean不会被BeanPostProcessor处理，所以创建的bean可能是有问题的。
     * 案例
     */
    @Test
    public void test4() {
        AnnUtil.printAllBean(MainConfig4.class);
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@28276e50
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@62e70ea3
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@3efe7086
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@675d8c96
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@741b3bc3
         * mainConfig4->us.fjj.spring.learning.beanfactoryextensions.test4.MainConfig4$$EnhancerBySpringCGLIB$$d76cbc1c@2ed3b1f5
         * user1->UserModel{username='hello'}
         * user2->UserModel{username='hello'}
         * name->hello
         */
        /**
         * 随后添加一个自定义BeanFactoryPostProcessor后
         */
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@180e6ac4
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@42b64ab8
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7e985ce9
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@2a39fe6a
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@410ae9a3
         * mainConfig4->us.fjj.spring.learning.beanfactoryextensions.test4.MainConfig4$$EnhancerBySpringCGLIB$$1fc69bc5@319988b0
         * myBeanFactoryPostProcessor->us.fjj.spring.learning.beanfactoryextensions.test4.MyBeanFactoryPostProcessor@d5ae57e
         * user1->UserModel{username='null'}
         * user2->UserModel{username='null'}
         * name->hello
         */
        /**
         * user1和user2的username值为null原因是：
         * @Autowired注解是在AutowiredAnnotationBeanPostProcessor中解析的，spring容器调用BeanFactoryPostProcessor#postProcessBeanFactory的使用，此时spring容器中还没有AutowiredAnnotationBeanPostProcessor，所以此时去获取user1这个bean的时候会导致提起初始化，
         * @Autowired并不会处理它，所以username=null。（相当于直接从第2阶段跳到第4阶段）
         */
    }

    /**
     * 总结
     * 1.注意spring的4个阶段：bean定义阶段，BeanFactory后置处理阶段、BeanPostProcessor注册阶段、单例bean创建组装阶段
     * 2.BeanDefinitionRegistryPostProcessor会在第一个阶段被调用，用来实现bean的注册操作，这个阶段会完成所有bean的注册
     * 3.BeanFactoryPostProcessor会在第2个阶段被调用，到这个阶段的时候，bean此时已经完成了所有bean的注册操作，这个阶段中你可以对BeanFactory中的一些信息进行修改，比如修改阶段1中的一些bean的定义信息，修改BeanFactory的一些配置。
     * 4.阶段2的时候，禁止注册bean，禁止从容器中获取bean
     * 5.BeanDefinitionRegistryPostProcessor和BeanFactoryPostProcessor可以通过PriorityOrdered接口或者Ordered接口来实现顺序。
     */
}