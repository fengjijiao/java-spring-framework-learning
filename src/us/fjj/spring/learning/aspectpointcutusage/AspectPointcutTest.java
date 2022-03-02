package us.fjj.spring.learning.aspectpointcutusage;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import us.fjj.spring.learning.aspectpointcutusage.test1.AspectA;
import us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA;
import us.fjj.spring.learning.aspectpointcutusage.test2.AspectB;
import us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB;
import us.fjj.spring.learning.aspectpointcutusage.test2.ServiceC;
import us.fjj.spring.learning.aspectpointcutusage.test3.Aspect3;
import us.fjj.spring.learning.aspectpointcutusage.test3.C2;

/**
 * @Aspect中@Pointcut的12种用法
 * AspectJProxyFactory是手动Aop中三种方式（ProxyFactory、ProxyFactoryBean、AspectJProxyFactory）之一。
 * 通过@Aspect注解在spring环境中实现aop特别的方便。
 *
 * AspectJ: 是一个面向切面的框架，是目前最好用，最方便的AOP框架，和spring中的aop可以集成在一起使用，通过Aspectj提供的一些功能实现aop代理变得非常方便。
 *
 * AspectJ使用步骤
 * 1.创建一个类，使用@Aspect标注
 * 2.@Aspect标注的类中，通过@Pointcut定义切入点
 * 3.@Aspect标注的类中，通过AspectJ提供的一些通知相关的注解定义通知
 * 4.使用AspectJProxyFactory结合@Aspect标注的类，来生成代理对象
 */
public class AspectPointcutTest {
    /**
     * 案例1：（感受一下AspectJ的便捷）
     */
    @Test
    public void test1() {
        try {
            ServiceA serviceA = new ServiceA();
            AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
            aspectJProxyFactory.setTarget(serviceA);
            aspectJProxyFactory.addAspect(AspectA.class);
            ServiceA serviceA1 = aspectJProxyFactory.getProxy();
            serviceA1.m1();
            serviceA1.m2();
        }catch (Exception e) {
            //
        }
        /**
         *前置通知,execution(void us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA.m1())
         * 我是m1方法
         * 前置通知,execution(void us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA.m2())
         * execution(void us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA.m2())发生异常/ by zero
         */
    }
    /**
     * AspectJProxyFactory原理
     * @Aspect标注的类上，这个类中，可以通过@Pointcut来定义切入点，可以通过@Before\@Around\@After\@AfterReturning\@AfterThrowing标注在方法上来定义通知，定义好了之后，将@Aspect 标注的这个类交给AspectJPorxyFactory来解析生成Advisor链，进而结合目标对象一起来生成代理对象。
     * @Before: 前置通知, 在方法执行之前执行
     * @After: 后置通知, 在方法执行之后执行 。
     * @AfterReturning: 返回通知, 在方法返回结果之后执行(返回通知方法可以拿到目标方法(连接点方法)执行后的结果。)
     * @AfterThrowing: 异常通知, 在方法抛出异常之后
     * @Around: 环绕通知, 围绕着方法执行
     *
     * @Aspect标注的类上，@Aspect中有2个关键点
     * @Pointcut: 标注在方法上，用来定义切入点，有11种用法，
     * @Aspect类中定义通知：可以通过@Before\@Around\@After\@AfterReturning\@AfterThrowing定义通知
     */
    @Test
    public void test2() {
        ServiceB serviceB = new ServiceB();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceB);
        aspectJProxyFactory.addAspect(AspectB.class);
        ServiceB serviceB1 = aspectJProxyFactory.getProxy();
        serviceB1.m1();

        for (int i = 0; i < 30; i++) {
            System.out.print("-");
        }
        System.out.println("");

        try {
            serviceB1.m2(6);
        }catch (Exception e) {
            //
        }


        for (int i = 0; i < 30; i++) {
            System.out.print("-");
        }
        System.out.println("");

        ServiceC serviceC = new ServiceC();
        AspectJProxyFactory aspectJProxyFactory1 = new AspectJProxyFactory();
        aspectJProxyFactory1.setTarget(serviceC);
        aspectJProxyFactory1.addAspect(AspectB.class);
        ServiceC serviceC1 = aspectJProxyFactory1.getProxy();
        try {
            serviceC1.m2(6);
        }catch (Exception e) {
            //
        }

        /**
         * Before: execution(void us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m1())
         * lxh yyds!
         * afterReturning: execution(void us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m1()), result: null
         * After: execution(void us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m1())
         * ------------------------------
         * m2->前置通知，参数：java.util.stream.ReferencePipeline$Head@22d6f11
         * Before: execution(String us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m2(int))
         * 0:lxh yyds!
         * 1:lxh yyds!
         * 2:lxh yyds!
         * 3:lxh yyds!
         * 4:lxh yyds!
         * 5:lxh yyds!
         * afterThrowing: execution(String us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m2(int))
         * After: execution(String us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m2(int))
         * java.lang.RuntimeException: lxh yyds!
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB.m2(ServiceB.java:12)
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB$$FastClassBySpringCGLIB$$c7e27d19.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor.invoke(AfterReturningAdviceInterceptor.java:55)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.aspectj.AspectJAfterAdvice.invoke(AspectJAfterAdvice.java:47)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor.invoke(MethodBeforeAdviceInterceptor.java:56)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:88)
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.AspectB.around(AspectB.java:41)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
         * 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
         * 	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:644)
         * 	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:633)
         * 	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689)
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB$$EnhancerBySpringCGLIB$$6bca7692.m2(<generated>)
         * 	at us.fjj.spring.learning.aspectpointcutusage.AspectPointcutTest.test2(AspectPointcutTest.java:76)
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
         * m2->异常通知，Exception：java.lang.RuntimeException: lxh yyds!
         * m2->后置通知
         * ------------------------------
         * m2->前置通知，参数：java.util.stream.ReferencePipeline$Head@b968a76
         * 0:lxh yyds!
         * 1:lxh yyds!
         * java.lang.RuntimeException: lxh yyds!
         * 2:lxh yyds!
         * 3:lxh yyds!
         * 4:lxh yyds!
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceC.m2(ServiceC.java:9)
         * 5:lxh yyds!
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceC$$FastClassBySpringCGLIB$$c7e27d1a.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:88)
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.AspectB.around(AspectB.java:41)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
         * 	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
         * 	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
         * 	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
         * 	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:644)
         * 	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:633)
         * 	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689)
         * 	at us.fjj.spring.learning.aspectpointcutusage.test2.ServiceC$$EnhancerBySpringCGLIB$$45618305.m2(<generated>)
         * 	at us.fjj.spring.learning.aspectpointcutusage.AspectPointcutTest.test2(AspectPointcutTest.java:93)
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
         * m2->异常通知，Exception：java.lang.RuntimeException: lxh yyds!
         * 	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:38)
         * 	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$5(NodeTestTask.java:143)
         * m2->后置通知
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
         *
         */
    }
    /**
     * @Pointcut的12种用法
     * 作用：用来标注在方法上来定义切入点
     * 定义：格式@注解（value="表达式标签（表达式）"）
     * 如
     * @Pointcut(value = "execution(public String us.fjj.spring.learning.aspectpointcutusage.test2.Service*.m2(int))")
     *
     * 表达式标签（10种）
     * 1. execution: 用于匹配方法执行的连接点
     * 2. within: 用于匹配指定类型内的方法执行
     * 3. this: 用于匹配当前AOP代理对象类型的执行方法；注意是AOP代理对象的类型匹配，这样就可能包括引入接口的类型匹配
     * 4. target: 用于匹配当前目标对象类型的执行方法；注意是目标对象的类型匹配，这样就不包括引入接口的类型匹配
     * 5. args: 用于匹配当前执行的方法传入的参数为指定类型的执行方法
     * 6. @winin: 用于匹配所有持有特定注解类型类的方法
     * 7. @target: 用于匹配当前目标对象类型的持有方法，其中目标对象持有指定的注解。
     * 8. args: 用于匹配当前执行的方法传入的参数持有指定注解的执行
     * 9. @annotation: 用于匹配当前执行方法持有指定注解的方法
     * 10. bean: spring aop扩展的，AspectJ没有对应的指示符，用于匹配特定名称的Bean对象的执行方法。
     */
    /**
     *
     * execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern)
     *           throws-pattern?)
     * ‘?’ at the end shows that the part is optional and you can omit it if not needed.
     *
     * modifiers-pattern? (optional) – Access modifiers like public, protected, private.
     * ret-type-pattern – Method’s return type
     * declaring-type-pattern? (optional) – Package or class
     * name-pattern – Name of the method
     * param-pattern – Method parameters
     * throws-pattern? (optional) – Method throwing this exception
     * //中文
     * 其中带 ?号的 modifiers-pattern?，declaring-type-pattern?，hrows-pattern?是可选项
     * ret-type-pattern,name-pattern, parameters-pattern是必选项
     * modifier-pattern? 修饰符匹配，如public 表示匹配公有方法
     * ret-type-pattern 返回值匹配，* 表示任何返回值，全路径的类名等
     * declaring-type-pattern? 类路径匹配
     * name-pattern 方法名匹配，* 代表所有，set*，代表以set开头的所有方法
     * (param-pattern) 参数匹配，指定方法参数(声明的类型)，(..)代表所有参数，(*,String)代表第一个
     * 参数为任何值,第二个为String类型，(..,String)代表最后一个参数是String类型
     * throws-pattern? 异常类型匹配
     *
     * 表达式           描述
     * public *.*(..) 任何公共方法的执行
     * * com.javacode..IPointcutService.*()  com.javacode包及所有子包下IPointcutService接口中的任何无参方法
     * * com.javacode..*.*(..) com.javacode包及所有子包下任何类的任何方法
     * * com.javacode..IPointcutService+.*() com.javacode包及所有子包下IPointcutService接口及子类型的无参方法
     * * Service1.*(String) 匹配Service1中只有1个参数的且参数类型是String的方法
     * * Service1.*(*,String) 匹配Service1中只有2个参数且第二个参数类型是Sting的方法
     * * Service1.*(..,String) 匹配Serivce1中最后一个参数类型是String的方法
     */
    /**
     * 类型匹配语法
     * 很多地方会按照类型的匹配，先来说以下类型匹配的语法
     * AspectJ类型匹配的通配符：
     * * : 匹配任何数量字符
     * .. : 匹配任何数量字符的重复，如在类型模式中匹配任何数量子包；而在方法参数模式中匹配任何数量参数（0个或多个参数）
     * + ： 匹配指定类型及其子类型；仅能作为后缀放在类型模式后边
     * java.lang.String 匹配String类型
     * java.*.String 匹配java包下的任何一级子包下的String类型，如匹配java.lang.String，但不匹配java.lang.ss.String
     * java..* 匹配java包及任何子包下的任何类型，如匹配java.lang.String、java.lang.annotation.Annotation
     * java.lang.*ing 匹配任何java.lang包下的以ing结尾的类型
     * java.lang.Number+ 匹配java.lang包下的任何Number类型及其子类型，如匹配java.lang.Number、java.lang.Integer、java.math.BigInteger
     */

    /**
     * 2. within
     * 用法：within(类型表达式)：目标对象target的类型是否和within中指定的类型匹配
     * 表达式   描述
     *  within(com.javacode..*) com.javacode包及子包下的任何方法执行
     *  within(com.javacode2018..IPointcutService+) com.javacode包或所有子包下IPointcutService类型及子类型的任何方法
     *  within(com.javacode2018.Service1) 匹配类com.javacode.Service1中定义的所有方法，不包含其子类中的方法
     *
     * 匹配原则：target.getClass().equals(within表达式中指定的类型)
     *
     * 案例3: 有2个类，父子关系
     */
    @Test
    public void test3() {
        C2 c2 = new C2();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(c2);
        aspectJProxyFactory.addAspect(Aspect3.class);
        C2 c2proxy = aspectJProxyFactory.getProxy();
        c2proxy.m1();
        c2proxy.m2();
        c2proxy.m3();
        c2proxy.m4();
        /**
         * C1:我是m1方法
         * C1:我是m2方法
         * C2：我是m3方法
         * C2: 我是m4方法
         */
    }
}
