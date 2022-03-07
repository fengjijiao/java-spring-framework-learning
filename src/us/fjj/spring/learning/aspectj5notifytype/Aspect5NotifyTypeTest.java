package us.fjj.spring.learning.aspectj5notifytype;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import us.fjj.spring.learning.aspectj5notifytype.afterreturningtest.AfterReturningAspect;
import us.fjj.spring.learning.aspectj5notifytype.afterreturningtest.Service4;
import us.fjj.spring.learning.aspectj5notifytype.aftertest.AfterAspect;
import us.fjj.spring.learning.aspectj5notifytype.aftertest.Service3;
import us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.AfterThrowingAspect;
import us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.Service5;
import us.fjj.spring.learning.aspectj5notifytype.aroundtest.AroundAspect;
import us.fjj.spring.learning.aspectj5notifytype.aroundtest.Service2;
import us.fjj.spring.learning.aspectj5notifytype.beforetest.BeforeAspect;
import us.fjj.spring.learning.aspectj5notifytype.beforetest.Service1;

/**
 * @Aspect中有5种通知
 * 1.@Before: 前置通知，在方法执行之前执行
 * 2.@Around: 环绕通知，围绕着方法执行
 * 3.@After: 后置通知，在方法执行之后执行
 * 4.@AfterReturning: 返回通知，在方法返回结果之后执行
 * 5.@AfterThrowing: 异常通知，在方法抛出异常之后
 */
public class    Aspect5NotifyTypeTest {
    /**
     * @Before: 前置通知
     * 对应的通知将会被解析为这个通知类：org.springframework.aop.aspectj.AspectJMethodBeforeAdvice
     */
    @Test
    public void beforeTest() {
        Service1 target = new Service1();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAspect(BeforeAspect.class);
        Service1 proxy = proxyFactory.getProxy();
        System.out.println(proxy.say("lk"));
        System.out.println(proxy.work("lk"));
        /**
         * 我是前置通知
         * 你好：lk
         * 我是前置通知
         * 开始工作了：lk
         */
        System.out.println("Signature.class.isAssignableFrom(MethodSignature.class):"+Signature.class.isAssignableFrom(MethodSignature.class));
        System.out.println("MethodSignature.class.isAssignableFrom(Signature.class):"+MethodSignature.class.isAssignableFrom(Signature.class));
    }
    /**
     * 通知中获取被调方法信息
     * 通知中如果想获取被调用方法的信息，分2种情况
     * 1.非环绕通知，可以将org.aspectj.lang.JoinPoint作为通知方法的第一个参数，通过这个参数获取被调用方法的信息
     * {@link org.aspectj.lang.JoinPoint}
     * {@link JoinPoint#getSignature()}方法可以转化为{@link MethodSignature}（方法签名），通过MethodSignature获取被调用的目标方法(MethodSignature#getMethod()).
     * 2.如果是环绕通知，可以将org.aspectj.lang.ProceedingJoinPoint作为方法的第一个参数，通过这个参数获取被调用方法的信息
     * {@link org.aspectj.lang.ProceedingJoinPoint}
     */


    /**
     * @Around: 环绕通知
     * 环绕通知会包裹目标方法的执行，可以在通知内部调用ProceedingJoinPoint.process方法继续执行下一个拦截器。
     * 用起来和@Before类似，但是有2点不一样
     * 1.若需要获取目标方法的信息，需要将ProceedingJoinPoint作为第一个参数
     * 2.通常使用Object类型作为方法的返回值，返回值也可以是void.
     *
     * 特点：环绕通知比较特殊，其他4种类型的通知都可以用环绕通知来实现。
     *
     */
    @Test
    public void aroundTest() {
        Service2 target = new Service2();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAspect(AroundAspect.class);
        Service2 proxy = proxyFactory.getProxy();
        proxy.say("lk");
        proxy.work("lk");
        /**
         * lk说：你好！
         * public void us.fjj.spring.learning.aspectj5notifytype.aroundtest.Service2.say(java.lang.String)，耗时（纳秒）：13341700
         * lk开始工作了！
         * public void us.fjj.spring.learning.aspectj5notifytype.aroundtest.Service2.work(java.lang.String)，耗时（纳秒）：272600
         */
    }
    /**
     * @Around通知最后会被解析为下面这个通知类：
     * {@link org.springframework.aop.aspectj.AspectJAroundAdvice}
     */


    /**
     * @After：后置通知
     * 后置通知，在方法执行之后执行。
     * 特点：
     * 不管目标方法是否有异常，后置通知都会执行
     * 这种通知无法获取返回值
     * 可以使用JoinPoint作为方法的第一个参数，用来获取连接点的信息
     */
    @Test
    public void afterTest() {
        Service3 target = new Service3();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAspect(AfterAspect.class);
        Service3 proxy = proxyFactory.getProxy();
        System.out.println(proxy.say("lk"));
        System.out.println(proxy.work("lk"));
        /**
         * say执行完毕！
         * lk say:
         * work执行完毕！
         * lkworking
         */
    }
    /**
     * @After通知最后会被解析为下面这个通知类
     * org.springframework.aop.aspectj.AspectJAfterAdvice
     * 这个类中有invoke方法，这个方法内部会调用被通知的方法，其内部采用try..finally的方式实现的，所以不管目标方法是否有异常，通知一定会被执行。
     * @Override
     * public Object invoke(MethodInvocation mi) throws Throwable {
     *     try {
     *         //继续执行下一个拦截器
     *         retrun mi.proceed();
     *     } finally {
     *         //内部通过反射调用被@After标注的方法
     *         invokeAdviceMethod(getJoinPointMatch(), null, null);
     *     }
     * }
     */


    /**
     * @AfterReturning: 返回通知
     * 返回通知，在方法返回结果之后执行。
     * 特点：
     * 可以获取到方法的返回值
     * 当目标方法返回异常的时候，这个通知不会被调用，这点和@After通知是有区别的
     */
    @Test
    public void afterReturningTest() {
        Service4 target = new Service4();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAspect(AfterReturningAspect.class);
        Service4 proxy = proxyFactory.getProxy();
        proxy.say();
        /**
         * say执行完成，返回值：say: hello
         */
    }
    /**
     * 注意@AfterReturning注解，用到了2个参数
     * 1.value:用来指定切入点
     * 2.returning:用来指定返回值对应方法的参数名称，返回值对应方法的第二个参数，名称为retVal
     *
     *
     * @AfterReturning通知最后会被解析为下面这个通知类
     * org.springframework.aop.aspectj.AspectJAfterReturningAdvice
     */


    /**
     * @AfterThrowing: 异常通知
     * 在方法抛出异常之后会回调@AfterThrowing标注的方法
     * @AfterThrowing标注的方法可以指定异常的类型，当被调用的方法触发该异常及其子类型的异常后，会触发异常方法的回调。也可不指定异常类型，此时会匹配所有异常。
     * 未指定异常类型，可匹配所有异常类型，如下：
     * @AfterThrowing(value = "切入点“)
     * public void afterThrowing()
     * 指定异常类型：通过@AfterThrowing的throwing指定参数异常参数名称，我们用方法的第二个参数来接收异常，第二个参数名称为e，下面的代码，当目标方法发生IllegalArguemntException异常及其子类型的异常时，下面的方法会被回调。
     * @AfterThrowing(value="pc()", throwing = "e")
     * public void afterThrowing(JoinPoint joinPoint, IllegalArgumentException e)
     *
     * 特点：无论异常是否被异常通知捕获，异常还会继续向外抛出。
     *
     */
    @Test
    public void afterThrowing() {
        Service5 target = new Service5();
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAspect(AfterThrowingAspect.class);
        Service5 proxy = proxyFactory.getProxy();
        proxy.login("jyx", "sblk");
        /**
         * 当输入[jyx, sblk]时发生错误：username != lk
         *
         * java.lang.IllegalArgumentException: username != lk
         *
         * 	at us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.Service5.login(Service5.java:6)
         * 	at us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.Service5$$FastClassBySpringCGLIB$$61fb1065.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.aspectj.AspectJAfterThrowingAdvice.invoke(AspectJAfterThrowingAdvice.java:62)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:95)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689)
         * 	at us.fjj.spring.learning.aspectj5notifytype.afterthrowingtest.Service5$$EnhancerBySpringCGLIB$$63c185a1.login(<generated>)
         * 	at us.fjj.spring.learning.aspectj5notifytype.Aspect5NotifyTypeTest.afterThrowing(Aspect5NotifyTypeTest.java:186)
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
         *
         */
    }
    /**
     * @AfterThrowing通知最后会被解析为下面这个通知类
     * org.springframework.aop.aspectj.AspectJAfterThrowingAdvice
     * 这个类中的invoke方法：
     * @Override
     * public Object invoke(MethodInvocation mi) throws Throwable {
     *     try {
     *         //继续调用下一个拦截器链
     *         return mi.proceed();
     *     }catch(Throwable e) {
     *         //判断ex和需要匹配的异常类型是否一致
     *         if(shouldInvokeOnThrowing(e)) {
     *             //通过反射调用@AfterThrowing标注的方法
     *             invokeAdviceMethod(getJoinPointMatch(), null, e);
     *         }
     *         //继续向外抛出异常
     *         throw e;
     *     }
     * }
     */


    /**
     * 几种通知比对
     * 通知类型         执行时间点           可获取返回值          目标方法异常时是否会执行
     * @Before          方法执行之前          否              是
     * @Around          环绕方法执行          是           自己控制
     * @After           方法执行后           否           是
     * @AfterReturning      方法执行后           是           否
     * @AfterThrowing           方法发生异常后         否           是
     */


}
