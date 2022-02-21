package us.fjj.spring.learning.recycledependence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.recycledependence.test1.MainConfig1;
import us.fjj.spring.learning.recycledependence.test3.MainConfig3;

public class RecycleDependenceTest {
    /**
     * 为什么需要3级缓存
     * 原因：
     * 这样做可以解决：早期暴露给其他依赖者的bean和最终暴露的bean不一致的问题。
     * 若是将刚刚实例化好的bean直接丢到二级缓存中暴露出去，如果后期这个bean对象被更改了，比如可能在上面加了一些拦截器，将其包装为一个代理了，那么暴露出去的bean和最终的这个bean就是不一样的，将自己暴露出去的时候就是一个原始对象，而自己最终却是一个代理对象，最终会导致被暴露出去的和最终的bean不是同一个bean，将产生意想不到的效果，而三级缓存就可以发现这个问题。
     */
    /**
     * 案例1：下面来2个bean，相互依赖，通过set方法相互注入，并且其内部都有一个m1方法，用来输出一行日志。
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig1.class);
        context.refresh();
        us.fjj.spring.learning.recycledependence.test1.Service2 service2 = context.getBean(us.fjj.spring.learning.recycledependence.test1.Service2.class);
        service2.m1();
        /**
         * org.springframework.beans.factory.BeanCurrentlyInCreationException: Error creating bean with name 'service1': Bean with name 'service1' has been injected into other beans [service2] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching - consider using 'getBeanNamesOfType' with the 'allowEagerInit' flag turned off, for example.
         *
         * 	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:624)
         * 	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:517)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:323)
         * 	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:321)
         * 	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
         * 	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:879)
         * 	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:878)
         * 	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:550)
         * 	at us.fjj.spring.learning.recycledependence.RecycleDependenceTest.test1(RecycleDependenceTest.java:21)
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
         */
    }
    /**
     * 上面报错的原因是：早期注入Service2的Service1与最后生成的Service1版本不一致（最后生成的是一个代理对象），且allowRawInjectionDespiteWrapping又是false，所以报异常了。
     *
     * 如何解决这个问题：
     * 将allowRawInjectionDespiteWrapping设置为true就可以了
     *
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //创建一个BeanFactoryPostProcessor: BeanFactory后置处理器
        context.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                if (beanFactory instanceof DefaultListableBeanFactory) {
                    //将allowRawInjectionDespiteWrapping设置为true
                    ((DefaultListableBeanFactory)beanFactory).setAllowRawInjectionDespiteWrapping(true);
                }
            }
        });
        context.register(MainConfig1.class);
        context.refresh();
        us.fjj.spring.learning.recycledependence.test1.Service2 service2 = context.getBean(us.fjj.spring.learning.recycledependence.test1.Service2.class);
        System.out.println("---Service2----");
        service2.m1();
        System.out.println("---Service1----");
        us.fjj.spring.learning.recycledependence.test1.Service1 service1 = context.getBean(us.fjj.spring.learning.recycledependence.test1.Service1.class);
        service1.m1();
        System.out.println("-------EQ------");
        System.out.println(service2.getService1() == service1);
        /**
         * ---Service2----
         * service2的m1方法
         * service1的m1方法
         * ---Service1----
         * 你好，service1
         * service1的m1方法
         * -------EQ------
         * false
         */
        /**
         * 而Service2.m1方法中调用了Service1.m1方法，这个里面拦截器没有起效，但是单独调用Service1.m1方法却起效了，说明Service2中注入的Service1不是代理对象，所以没有加上拦截器的功能，那是因为Service2注入的是早期的Serivce1，注入的时候Service1还不是一个代理对象，所以没有拦截器中的功能。
         *
         */
    }

    /**
     * 在暴露早期bean的源码中
     * `addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));`，
     * 有个getEarlyBeanReference方法。
     * 从3级缓存中获取bean时，会调用上面这个方法来获取bean,这个方法内部会看以下容器中是否有SmartInstantiationAwareBeanPostProcessor这种处理器，然后会依次调用这种处理器中的getEarlyBeanReference方法，那么思路来了，我们可以自定义一个SmartInstantiationAwareBeanPostProcessor，然后在其getEarlyBeanReference中来创建代理。
     *
     */
    @Test
public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //创建一个BeanFactoryPostProcessor: BeanFactory后置处理器
        context.addBeanFactoryPostProcessor(new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                if (beanFactory instanceof DefaultListableBeanFactory) {
                    //将allowRawInjectionDespiteWrapping设置为true
                    ((DefaultListableBeanFactory)beanFactory).setAllowRawInjectionDespiteWrapping(true);
                }
            }
        });
        context.register(MainConfig3.class);
        context.refresh();
        us.fjj.spring.learning.recycledependence.test3.Service2 service2 = context.getBean(us.fjj.spring.learning.recycledependence.test3.Service2.class);
        System.out.println("---Service2----");
        service2.m1();
        System.out.println("---Service1----");
        us.fjj.spring.learning.recycledependence.test3.Service1 service1 = context.getBean(us.fjj.spring.learning.recycledependence.test3.Service1.class);
        service1.m1();
        System.out.println("-------EQ------");
        System.out.println(service2.getService1() == service1);
        /**
         * ---Service2----
         * service2的m1方法
         * 你好，service1
         * service1的m1方法
         * ---Service1----
         * 你好，service1
         * service1的m1方法
         * -------EQ------
         * true
         */
    }

    /**
     * 单例bean解决了循环依赖，还存在什么问题？
     * 循环依赖的情况下，由于注入的是早期bean，此时早期的bean中还未被填充属性，初始化等各种操作，也就是说此时bean并没有被完全初始化完毕，此时若直接拿去使用，可能存在有问题的风险。
     */

}
