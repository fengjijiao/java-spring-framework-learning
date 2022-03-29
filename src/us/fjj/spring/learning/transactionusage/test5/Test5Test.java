package us.fjj.spring.learning.transactionusage.test5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class Test5Test {
    private Tx1Service tx1Service;
    private Tx2Service tx2Service;
    private JdbcTemplate jdbcTemplate1;
    private JdbcTemplate jdbcTemplate2;

    @BeforeEach
    public void before() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        tx1Service = context.getBean(Tx1Service.class);
        tx2Service = context.getBean(Tx2Service.class);
        jdbcTemplate1 = context.getBean("jdbcTemplate1", JdbcTemplate.class);
        jdbcTemplate2 = context.getBean("jdbcTemplate2", JdbcTemplate.class);
        jdbcTemplate1.update("truncate table test1.user1");
        jdbcTemplate1.update("truncate table test1.user2");
        jdbcTemplate2.update("truncate table test2.user1");
        jdbcTemplate2.update("truncate table test2.user2");
    }

    @AfterEach
    public void after() {
        System.out.println("test1.user1表数据："+this.jdbcTemplate1.queryForList("select * from user1"));
        System.out.println("test1.user2表数据："+this.jdbcTemplate1.queryForList("select * from user2"));
        System.out.println("test2.user1表数据："+this.jdbcTemplate2.queryForList("select * from user1"));
        System.out.println("test2.user2表数据："+this.jdbcTemplate2.queryForList("select * from user2"));
    }

    @Test
    public void test1() {
        this.tx1Service.test1();
        /**
         * test1.user1表数据：[]
         * test1.user2表数据：[]
         * test2.user1表数据：[]
         * test2.user2表数据：[]
         *
         * java.lang.RuntimeException
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service.test1(Tx1Service.java:23)
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service$$FastClassBySpringCGLIB$$fd2b0a77.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:366)
         * 	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:99)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689)
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service$$EnhancerBySpringCGLIB$$26b6dcd8.test1(<generated>)
         * 	at us.fjj.spring.learning.transactionusage.test5.Test5Test.test1(Test5Test.java:41)
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
     * 结果分析
     * “张三”、“李四”均为插入                外围方法和内部方法使用同一个事务管理器transactionManager1，且事务管理器和jdbcTemplate的dataSource都是同一个，外围方法会开启事务，内部方法加入外围方法事务，外围方法他拿出异常导致事务回滚，内部方法也跟着回滚了。
     */

    @Test
    public void test2(){
        this.tx1Service.test2();
        /**
         * test1.user1表数据：[{id=1, name=张三}]
         * test1.user2表数据：[{id=1, name=李四}]
         * test2.user1表数据：[]
         * test2.user2表数据：[]
         *
         * java.lang.RuntimeException
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service.test2(Tx1Service.java:35)
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service$$FastClassBySpringCGLIB$$fd2b0a77.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:366)
         * 	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:99)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:689)
         * 	at us.fjj.spring.learning.transactionusage.test5.Tx1Service$$EnhancerBySpringCGLIB$$2d4fdd50.test2(<generated>)
         * 	at us.fjj.spring.learning.transactionusage.test5.Test5Test.test2(Test5Test.java:136)
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
     * 结论分析
     * “张三”、“李四”均插入                 外围方法test2和内部两个required用到的不是同一个事务管理器，内部的2个方法在各自的事务中执行，不受外部方法事务的控制。
     *
     */

    @Test
    public void test3() {
        this.tx1Service.test3();
    }
    /**
     * 结论分析
     * ”张三“、”李四“都未插入，”王五“、”赵六“插入成功。
     * 外围方法和内部的前2个required方法的事务管理器都是transactionManager1，所以他们3个在一个事务中执行；而内部的后2个required方法的事务管理器都是transactionManager2，他们分别在自己的事务中执行，不受外围方法事务的控制，外围方法感受到了异常，回滚事务，只会导致内部的前2个required方法回滚。
     */

    @Test
    public void test4() {
        this.tx1Service.test4();
        /**
         * test1.user1表数据：[]
         * test1.user2表数据：[]
         * test2.user1表数据：[{id=1, name=张三}, {id=2, name=王五}]
         * test2.user2表数据：[{id=1, name=李四}, {id=2, name=赵六}]
         *
         * java.lang.RuntimeException
         */
    }

    @Test
    public void test5() {
        this.tx1Service.test5();
        /**
         * test1.user1表数据：[]
         * test1.user2表数据：[]
         * test2.user1表数据：[]
         * test2.user2表数据：[]
         *
         * java.lang.RuntimeException
         */
    }
    /**
     * 结果分析
     * 4个表都未插入数据
     * 外围方法test5通过事务管理器transactionManager1开启了事务tm1，内部方法插入”张三“，”李四“加入了tm1事务，而test2通过事务管理器transactionManager2又开启一个事务tm2，test2内部方法插入”王五“、”赵六“加入了tm2事务，tm2内部抛出了异常，tm2和tm1都感觉到了这个异常，所以两个事务都进行了回滚操作。
     */




























}
