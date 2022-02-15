package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test1.MainRun;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test3.MainConfig3;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test4.MainConfig4;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5.MainConfig5;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6.MainConfig6;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test7.MainConfig7;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test7.Service3;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8.MainConfig8;
import us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.MainConfig9;

/**
 * @Autowired：注入依赖对象
 * 作用：实现依赖注入，Spring容器会对bean中所有字段、方法进行遍历，标注有@Autowired注解的，都会进行注入
 * Autowired注解中有一个required属性，当为true时，表示必须注入，在容器中找不到匹配的候选者会报错；为false时，找不到也没关系。
 */

/**
 * Autowired操作候选者可以简化为下面这样
 * 按类型找->通过限定符@Qualifier过滤->@Primary->@Priority->根据名称找（字段名或者参数名称）
 * 概括为：先按类型找，然后按名称找
 *
 */
public class AutowiredAnnotationTest {
    /**
     * 测试Priority和Autowired注解
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        MainRun instance = context.getBean(MainRun.class);
        instance.m1();
        /**
         * 这里是MainRun的m1方法!
         * 这里是Service2的m1方法！
         */
    }
    /**
     * 案例1：@Autowired标注在构造器上，通过构造器注入依赖对象（也可不用标注）
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service2 service2 = context.getBean(us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service2.class);
        System.out.println(service2.toString());
        /**
         * Service2{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test2.Service1@62f4ff3b}
         */
    }
    /**
     * 案例2：@Autowired标注在方法上，通过方法注入依赖的对象（类似setter注入）
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        /**
         * 执行了Service2的injectService1方法
         */
    }
    /**
     * 案例3：@Autowired标注在setter方法上，通过setter方法注入（相当于与上方法等同）
     * 上面2种（通过构造器、通过普通方法注入）不是很常见，可以将@Autowired标注在set方法上，来注入指定对象
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        /**
         *执行了Service2的setService1方法！
         */
    }
    /**
     * 案例4：@Autowired标注在方法参数上（使用见test6）
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        for (String beanName:
             context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'service2': Unsatisfied dependency expressed through method 'injectTest' parameter 1; nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.lang.String' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
         *
         * 	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.inject(AutowiredAnnotationBeanPostProcessor.java:723)
         * 	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:116)
         * 	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:399)
         * 	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1422)
         * 	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:594)
         * 	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:517)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:323)
         * 	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:321)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
         * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:879)
         * 	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:878)
         * 	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550)
         * 	at org.springframework.context.annotation.AnnotationConfigApplicationContext.<init>(AnnotationConfigApplicationContext.java:89)
         * 	at us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.AutowiredAnnotationTest.test5(AutowiredAnnotationTest.java:76)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
         * 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
         * 	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:688)
         * 	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:149)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:140)
         * 	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:84)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(ExecutableInvoker.java:115)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.lambda$invoke$0(ExecutableInvoker.java:105)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
         * 	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:104)
         * 	at org.junit.jupiter.engine.execution.ExecutableInvoker.invoke(ExecutableInvoker.java:98)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$6(TestMethodTestDescriptor.java:210)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:206)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:131)
         * 	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:65)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:139)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$7(NodeTestTask.java:129)
         * 	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:127)
         * 	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:126)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:84)
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:32)
         * 	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
         * 	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:51)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:108)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:88)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:54)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:67)
         * 	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:52)
         * 	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:96)
         * 	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:75)
         * 	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:71)
         * 	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:33)
         * 	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:235)
         * 	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)
         * Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'java.lang.String' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
         * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:1695)
         * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1253)
         * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1207)
         * 	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredMethodElement.inject(AutowiredAnnotationBeanPostProcessor.java:715)
         * 	... 79 more
         */

        /**
         * 产生UnsatisfiedDependencyException的原因是因为在执行injectTest方法的时候，找不到String类型的bean
         * 当添加BeanConfig1.java后，执行成功，如下：
         *
         */

        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@51df223b
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@fd46303
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@60d8c0dc
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@4204541c
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@6a62689d
         * mainConfig5->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5.MainConfig5@4602c2a9
         * beanConfig1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5.BeanConfig1$$EnhancerBySpringCGLIB$$a8519dd8@60fa3495
         * service1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5.Service1@3e2822
         * service2->Service2{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5.Service1@3e2822}
         * name->OK
         */
    }

    /**
     * 解决上面问题的方法正是在方法参数上添加@Autowired注解
     */
    @Test
    public void test6() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6.Service1@10c626be,name=null
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@e077866
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@ed3068a
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@7c2b6087
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@3fffff43
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@a8e6492
         * mainConfig6->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6.MainConfig6@1c7fd41f
         * service1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6.Service1@10c626be
         * service2->Service2{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test6.Service1@10c626be}
         */
        /**
         * 可以看到name=null
         */
    }

    /**
     * 注解@Autowired在字段上
     */
    @Test
    public void test7() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig7.class);
        Service3 service3 = (Service3) context.getBean("service3");
        System.out.println(service3);
        /**
         * Service3{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test7.Service1@6cfcd46d, service2=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test7.Service2@52045dbe}
         */
    }

    @Test
    public void test8() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig8.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         *org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@2bd2b28e
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@16746061
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@57fd91c9
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@6cfcd46d
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@52045dbe
         * mainConfig8->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8.MainConfig8@674658f7
         * service1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8.Service1@5c8eee0f
         * service2->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8.Service2@565b064f
         * service3->Service3{service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8.Service1@5c8eee0f}
         */
        /**
         * 按照候选者查找过程，最后会注入和字段名称一样的bean，即：service1
         */
    }
    /**
     * 案例7：将指定类型的所有bean，注入到Collection、Map种
     * 1. 注入到Collection中
     * 被注入的类型为Collection类型或者Collection子接口类型，注意必须是接口类型，如：
     * Collection<IService>
     * List<IService>
     * Set<IService>
     * 会在容器中找到所有IService类型的bean，放到这个集合中
     * 2. 注入到Map中
     * 被注入的类型为Map类型或者Map子接口类，注意必须是接口类型，如:
     * Map<String, IService>
     * 会在容器中找到所有IService类型的bean，放到这个Map中，key为bean的名称,value为bean对象。
     *
     */
    @Test
    public void test9() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig9.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@41813449
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@4678a2eb
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@5b43fbf6
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@1080b026
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@58ebfd03
         * mainConfig9->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.MainConfig9@5b07730f
         * service0->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service0@57fd91c9
         * service1->us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service1@6cfcd46d
         * service2->Service2{services=[us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service0@57fd91c9, us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service1@6cfcd46d], serviceMap={service0=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service0@57fd91c9, service1=us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9.Service1@6cfcd46d}}
         */
    }
    /**
     * spring使用下面这个类处理@Autowired注解
     * org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
     */
}