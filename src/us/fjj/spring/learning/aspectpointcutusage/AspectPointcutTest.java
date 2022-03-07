package us.fjj.spring.learning.aspectpointcutusage;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ClassUtils;
import us.fjj.spring.learning.aspectpointcutusage.test1.AspectA;
import us.fjj.spring.learning.aspectpointcutusage.test1.ServiceA;
import us.fjj.spring.learning.aspectpointcutusage.test10.Ann10;
import us.fjj.spring.learning.aspectpointcutusage.test10.Aspect10;
import us.fjj.spring.learning.aspectpointcutusage.test10.ServiceI;
import us.fjj.spring.learning.aspectpointcutusage.test11.Aspect11;
import us.fjj.spring.learning.aspectpointcutusage.test11.ServiceJ;
import us.fjj.spring.learning.aspectpointcutusage.test12.Aspect12;
import us.fjj.spring.learning.aspectpointcutusage.test12.ServiceK;
import us.fjj.spring.learning.aspectpointcutusage.test13.Aspect13;
import us.fjj.spring.learning.aspectpointcutusage.test13.S13;
import us.fjj.spring.learning.aspectpointcutusage.test14.BeanService;
import us.fjj.spring.learning.aspectpointcutusage.test14.MainConfig1;
import us.fjj.spring.learning.aspectpointcutusage.test15.MainConfig15;
import us.fjj.spring.learning.aspectpointcutusage.test15.Service15;
import us.fjj.spring.learning.aspectpointcutusage.test2.AspectB;
import us.fjj.spring.learning.aspectpointcutusage.test2.ServiceB;
import us.fjj.spring.learning.aspectpointcutusage.test2.ServiceC;
import us.fjj.spring.learning.aspectpointcutusage.test3.Aspect3;
import us.fjj.spring.learning.aspectpointcutusage.test3.C2;
import us.fjj.spring.learning.aspectpointcutusage.test4.*;
import us.fjj.spring.learning.aspectpointcutusage.test5.Aspect5;
import us.fjj.spring.learning.aspectpointcutusage.test5.ServiceE;
import us.fjj.spring.learning.aspectpointcutusage.test6.Aspect6;
import us.fjj.spring.learning.aspectpointcutusage.test6.ServiceF;
import us.fjj.spring.learning.aspectpointcutusage.test7.Aspect7;
import us.fjj.spring.learning.aspectpointcutusage.test7.ServiceG;
import us.fjj.spring.learning.aspectpointcutusage.test8.Aspect8;
import us.fjj.spring.learning.aspectpointcutusage.test8.ServiceHC;

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


    /**
     * 3. this
     * this(类型全限定名)：通过aop创建的代理对象的类型是否和this中指定的类型匹配；注意判断的目标是代理对象；this中使用的表达式必须是类型全限定名。不支持通配符。
     * 匹配原则：this(x)，则代理对象proxy满足下面条件时会匹配
     * x.getClass().isAssignableFrom(proxy.getClass());
     *
     * 案例4
     */
    @Test
    public void tes4() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(C3.class);
        enhancer.setCallback(NoOp.INSTANCE);
        Object proxy = enhancer.create();
        System.out.println(String.format("C3.class.isAssignableFrom(proxy.getClass()): %b", C3.class.isAssignableFrom(proxy.getClass())));
        System.out.println(String.format("C3.class.isInstance(proxy.getClass()): %b", C3.class.isInstance(proxy.getClass())));
        System.out.println(String.format("proxy.getClass().isInstance(C3.class): %b", proxy.getClass().isInstance(C3.class)));
        System.out.println(String.format("C4.class.isAssignableFrom(proxy.getClass()): %b", C4.class.isAssignableFrom(proxy.getClass())));
        System.out.println(String.format("C4.class.isInstance(proxy.getClass()): %b", C4.class.isInstance(proxy.getClass())));
        System.out.println(String.format("proxy.getClass().isInstance(C4.class): %b", proxy.getClass().isInstance(C4.class)));
        /**
         * C3.class.isAssignableFrom(proxy.getClass()): true
         * C3.class.isInstance(proxy.getClass()): false
         * proxy.getClass().isInstance(C3.class): false
         * C4.class.isAssignableFrom(proxy.getClass()): false
         * C4.class.isInstance(proxy.getClass()): false
         * proxy.getClass().isInstance(C4.class): false
         */


        //正式开始
        ServiceD serviceD = new ServiceD();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        Class<?>[] interfaces = ClassUtils.getAllInterfaces(serviceD);
        aspectJProxyFactory.setTarget(serviceD);
        aspectJProxyFactory.setInterfaces(interfaces);//添加这一行后底层实现为jdk代理
        aspectJProxyFactory.addAspect(Aspect4.class);
        aspectJProxyFactory.setProxyTargetClass(true);//添加这一行后为cglib代理
        Object prox = aspectJProxyFactory.getProxy();
        System.out.println("Aop是否是jdk代理对象"+ AopUtils.isJdkDynamicProxy(prox));
        System.out.println("Aop是否是cglib代理对象"+ AopUtils.isCglibProxy(prox));
        ((I1) prox).m1();
        System.out.println(ServiceD.class.isAssignableFrom(prox.getClass()));
        /**
         * jdk proxy
         * Aop是否是jdk代理对象true
         * Aop是否是cglib代理对象false
         * 我是m1方法
         * false
         */
        /**
         * cglib proxy
         * Aop是否是jdk代理对象false
         * Aop是否是cglib代理对象true
         * 将要执行m1方法
         * 我是m1方法
         * true
         */
    }
    /**
     * 4.target
     * 用法：target(类型全限定名)：判断目标对象的类型是否和指定的类型匹配；注意判断的是目标对象的类型；表达式必须是类型全限定名，不支持通配符
     * 匹配原则：如target(x)，则目标对象target满足下面条件时会匹配
     * x.getClass().isAssignableFrom(target.getClass());
     */
    @Test
    public void test5() {
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        ServiceE serviceE = new ServiceE();
        aspectJProxyFactory.setTarget(serviceE);
        aspectJProxyFactory.setProxyTargetClass(true);
        aspectJProxyFactory.addAspect(Aspect5.class);
        ServiceE prox = aspectJProxyFactory.getProxy();
        prox.m1();
        System.out.println("ServiceE.class.isAssignableFrom(prox.getClass()):"+ServiceE.class.isAssignableFrom(prox.getClass()));
        /**
         * 将要执行m1
         * 我是m1方法
         * ServiceE.class.isAssignableFrom(prox.getClass()):true
         */
    }

    /**
     * within、this、target对比
     * 表达式标签    判断的对象   判断规则（x: 指表达式中指定的类型）
     * within   target对象    target.getClass().equals(表达式中指定的类型);
     * this proxy对象 x.getClass().isAssignableFrom(proxy.getClass());
     * target   target对象    x.getClass().isAssignableFrom(target.getClass());
     */


    /**
     * 5.args
     * 用法：args(参数类型列表)匹配当前执行方法传入的参数是否为args中指定的类型；注意是匹配传入的参数类型，不是匹配方法签名的参数类型；参数类型列表中的参数必须是类型全限定名，不支持通配符；
     * args属于动态切入点，也就是执行方法的时候进行判断的，这种切入点开销非常大，非特殊情况下最好不要使用。
     * 举例说明
     * 表达式  描述
     * args(String) 匹配只有一个参数且传入的参数类型是String类型的方法
     * args(*,String) 匹配只有2个参数且第2个参数类型是String的方法
     * args(..,String) 匹配最后1个参数类型是String的方法
     *
     * 案例6
     */
    @Test
    public void test6() {
        ServiceF serviceF = new ServiceF();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceF);
        aspectJProxyFactory.addAspect(Aspect6.class);
        ServiceF prox = aspectJProxyFactory.getProxy();
        prox.m1("jyx");
        prox.m1(1.0f);
        /**
         * 参数为：jyx
         * 传入的参数类型是：class java.lang.String，值为：jyx
         * 传入的参数类型是：class java.lang.Float，值为：1.0
         *
         * 输出中可以看出，m1第一次调用被增强了，第二次没有被增强。
         * args会在调用的过程中对参数实际的类型进行匹配，比较耗时，慎用。
         */
    }


    /**
     * 6. @within
     * 用法 @within(注解类型) : 匹配指定的注解内定义的方法
     * 匹配规则
     * 调用目标方法的时候，通过java中Method.getDeclaringClass()获取当前的方法是哪个类中定义的，然后会看这个类上是否有指定注解。
     * 被调用的目标方法Method对象.getDeclaringClass().getAnntation(within中指定的注解类型) != null
     *
     * 案例7~9
     */
    /**
     * 案例7：目标对象上有@within中指定的注解，这种情况时，目标对象的所有方法都会被拦截
     */
    @Test
    public void test7() {
        ServiceG serviceG = new ServiceG();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceG);
        aspectJProxyFactory.addAspect(Aspect7.class);
        ServiceG prox = aspectJProxyFactory.getProxy();
        prox.m1();
        /**
         * 将要执行m1
         * 我是m1方法
         */
    }
    /**
     * 案例8: 定义注解时未使用@Inherited，说明子类无法继承父类上的注解，这个案例中我们将定义一个这样的注解，将注解放在目标类的父类上， 来看一下效果。
     */
    @Test
    public void test8() {
        ServiceHC serviceHC = new ServiceHC();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceHC);
        aspectJProxyFactory.addAspect(Aspect8.class);
        ServiceHC prox = aspectJProxyFactory.getProxy();
        prox.m1();
        prox.m2();
        prox.m3();
        /**
         * m2方法虽然也在ServiceH中定义了，但是这个方法被子类ServiceHC重写了，所以调用目标对象中的m2方法的时候，此时发现m2方法是由ServiceHC定义的，而ServiceHC.class.getAnnotation(Ann8.class)为null，所以这个方法不会被拦截。
         *
         * 将要执行m1
         * 我是m1方法
         * 我是m2
         * 我是m3
         *
         * 将Ann8中添加@Inherited后输出如下
         *
         * 将要执行m1
         * 我是m1方法
         * 将要执行m2
         * 我是m2
         * 将要执行m3
         * 我是m3
         */
    }
    /**
     * 案例9: 对案例2进行改造，在注解的定义上面加上@Inherited，此时子类可以继承父类的注解，此时3个方法都会被拦截了。
     */
    @Test
    public void test9() {
        //
    }


    /**
     * 7. @target
     * 用法：@target(注解类型): 判断目标对象target类型上是否有指定的注解；@target中注解类型也必须是全限定类型名。
     * 匹配规则：target.class.getAnnotation(指定的注解类型) != null;
     * 2种情况可以匹配：1.注解直接标注在目标类上 2.注解标注在父类上，但是注解必须是可以继承的，即定义注解的时候，需要使用@Inherited标注
     */
    @Test
    public void test10() {
        ServiceI target = new ServiceI();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(target);
        aspectJProxyFactory.addAspect(Aspect10.class);
        ServiceI prox = aspectJProxyFactory.getProxy();
        prox.m1();
        System.out.printf("目标类上是否有Ann注解：%b\n", target.getClass().getAnnotation(Ann10.class) != null);
        /**
         * 将要执行m1
         * 我是m1方法
         * 目标类上是否有Ann注解：true
         */
    }
    /**
     * 当注解标注在父类上， 注解上没有@Inherited，这种情况下，目标类无法匹配到。
     * 加上@Inherited后目标对象被拦截。
     */


    /**
     * 8. @args
     * 用法：@args(注解类型)：方法参数所属的类上有指定的注解；注意不是参数上有指定的注解，而是参数类型的类上有指定的注解。
     *
     * 案例11:
     */
    @Test
    public void test11() {
        ServiceJ serviceJ = new ServiceJ();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceJ);
        aspectJProxyFactory.addAspect(Aspect11.class);
        ServiceJ prox = aspectJProxyFactory.getProxy();
        prox.m1(new us.fjj.spring.learning.aspectpointcutusage.test11.Car());
        /**
         * 将要执行m1
         * us.fjj.spring.learning.aspectpointcutusage.test11.Car@2488b073
         */
    }
    /**
     * 案例12: 匹配方法只有2个参数，且第2个参数所属的类型上有Ann11注解
     * @args(*, Ann11)
     */
    @Test
    public void test12() {
        ServiceK serviceK = new ServiceK();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(serviceK);
        aspectJProxyFactory.addAspect(Aspect12.class);
        ServiceK prox = aspectJProxyFactory.getProxy();
        prox.m1("yk", new us.fjj.spring.learning.aspectpointcutusage.test12.Car());
        /**
         * us.fjj.spring.learning.aspectpointcutusage.test12.Car@34a1d21f
         */
    }

    /**
     * @Pointcut("@args(..,com.javacode2018.aop.demo9.test8.Ann8)")：匹配参数数量大于等于1，
     * 且最后一个参数所属的类型上有Ann8注解
     * @Pointcut("@args(*,com.javacode2018.aop.demo9.test8.Ann8,..)")：匹配参数数量大于等于
     * 2，且第2个参数所属的类型上有Ann8注解
     * @Pointcut("@args(..,com.javacode2018.aop.demo9.test8.Ann8,*)")：匹配参数数量大于等于
     * 2，且倒数第2个参数所属的类型上有Ann8注解
     */


    /**
     * @annotation
     * 用法：@annotation(注解类型)：匹配被调用的方法上有指定的注解
     * 案例13
     */
    @Test
    public void test13() {
        S13 target = new S13();
        AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory();
        aspectJProxyFactory.setTarget(target);
        aspectJProxyFactory.addAspect(Aspect13.class);
        S13 prox = aspectJProxyFactory.getProxy();
        prox.m1();
        prox.m2();
        prox.m3();
        prox.m4();
        /**
         * 将要执行m1
         * 我是m1方法
         * m2
         * 将要执行m3
         * m3
         * m4
         */
    }

    /**
     * 10. bean
     * 用法：bean(bean名称)：这个用在spring环境中，匹配容器中指定名称的bean。
     * 案例：
     */
    @Test
    public void test14() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        BeanService beanService1 = context.getBean("beanService1", BeanService.class);
        beanService1.m1();
        BeanService beanService2 = context.getBean("beanService2", BeanService.class);
        beanService2.m1();
        /**
         *lxh yyds!.m1()
         * 将要运行：m1
         * jyx yyds!.m1()
         */
    }

    /**
     * 11.  reference pointcut
     * 表示引用其他命名切入点
     * 有时，我们可以将切入点专门放在一个类中集中定义
     * 其他地方可以通过引用的方式引入其他类中定义的切入点。
     * 语法如下：@Pointcut("完整包名类名.方法名称()")
     * 若引用同一个类中定义切入点，包名和类名可以省略，直接通过方法就可以引用。
     *
     */
    @Test
    public void test15() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig15.class);
        Service15 service1 = context.getBean("bean1", Service15.class);
        System.out.println(service1.getClass());
        service1.m1();
        service1.m2();
        service1.m3();
        Service15 service2 = context.getBean("bean2", Service15.class);
        service2.m1();
        service2.m2();
        service2.m3();
        Service15 service3 = context.getBean("bean3", Service15.class);
        service3.m1();
        service3.m2();
        service3.m3();
        /**
         * class us.fjj.spring.learning.aspectpointcutusage.test15.Service15$$EnhancerBySpringCGLIB$$8868200d
         * 111将要执行m1
         * 222将要执行m1
         * jyx我是m1方法
         * 111将要执行m2
         * 222将要执行m2
         * jyx我是m2方法
         * 111将要执行m3
         * 222将要执行m3
         * jyx我是m3方法
         * 222将要执行m1
         * ssw我是m1方法
         * 222将要执行m2
         * ssw我是m2方法
         * 222将要执行m3
         * ssw我是m3方法
         * yk我是m1方法
         * yk我是m2方法
         * yk我是m3方法
         */
    }

    /**
     * 组合型的pointcut
     * pointcut定义时，还可以使用&&、||、!运算符.
     * &&: 多个匹配都需要满足
     * ||: 多个匹配中只需要满足一个
     * !: 匹配不满足的情况下
     *
     * @Pointcut("bean(bean1)||bean(bean2)")//匹配bean1或bean2
     * @Pointcut("@target(Ann1) && @annotation(Ann2)")//匹配目标类上有Ann1注解并且目标方法上有Ann2注解
     * @Pointcut("@target(Ann1) && !@target(Ann2)")//匹配目标类上有Ann1注解但是没有Ann2注解
     */


    /**
     * 不同点：
     * @target要求对象的运行时类型与被注解的类型是同一个类型
     * @within要求对象的运行时类型是被注解的类型的子类
     *
     * 来源：
     * @within和@target的区别
     *https://blog.csdn.net/demon7552003/article/details/97601209
     */

}
