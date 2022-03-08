package us.fjj.spring.learning.enableasynandasynccusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.enableasynandasynccusage.test1.LogService;
import us.fjj.spring.learning.enableasynandasynccusage.test1.MainConfig1;
import us.fjj.spring.learning.enableasynandasynccusage.test2.GoodsService;
import us.fjj.spring.learning.enableasynandasynccusage.test2.MainConfig2;
import us.fjj.spring.learning.enableasynandasynccusage.test3.MainConfig3;
import us.fjj.spring.learning.enableasynandasynccusage.test4.LogService4;
import us.fjj.spring.learning.enableasynandasynccusage.test4.MainConfig4;
import us.fjj.spring.learning.enableasynandasynccusage.test5.LogService5;
import us.fjj.spring.learning.enableasynandasynccusage.test5.MainConfig5;
import us.fjj.spring.learning.enableasynandasynccusage.test6.CashOutService;
import us.fjj.spring.learning.enableasynandasynccusage.test6.MainConfig6;
import us.fjj.spring.learning.enableasynandasynccusage.test6.RechargeService;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @EnableAsync & @Async
 * 1.作用
 * 2.用法
 * 3.获取异步执行结果
 * 4.自定义异步执行的线程池
 * 5.自定义异常处理
 * 6.线程隔离
 * 7.源码&原理
 */
public class EnableAsyncAndAsyncTest {
    /**
     * 2.作用
     * spring容器中实现bean方法的异步调用
     * 比如有个logService的bean，logservice中有个log方法用来记录日志，当调用logService.log(msg)的时候，希望异步执行，那么可以通过@EnableAsync & @Async来实现。
     * 3.用法（2步）
     * 1.需要异步执行的方法上面使用@Async注解标注，若bean中所有的方法都需要异步执行，可以直接将@Async加载到类上。
     * 2.将@EnableAsync添加在spring配置类上，此时@Async注解才会起效。
     * 常见2种用法
     * 1.无返回值的
     * 2.可以获取返回值的
     * 4.无返回值的
     * 用法：方法返回值不是Future类型的，被执行时，会立即返回，并且无法获取方法返回值，如：
     *
     * @Async public void log(String msg) throws InterruptedException {
     * System.out.println("开始记录日志，" + System.currentTimeMillis());
     * //模拟耗时2s
     * TimeUnit.SECONDS.sleep(2);
     * System.out.println("日志记录完毕，" + System.currentTimeMillis());
     * }
     * <p>
     * 案例：实现日志异步记录的功能。
     */
    @Test
    public void test1() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
//        for (String beanName:
//             context.getBeanDefinitionNames()) {
//            System.out.println(beanName);
//        }
        LogService logService = context.getBean("logService", LogService.class);
        System.out.println(Thread.currentThread() + " start," + System.currentTimeMillis());
        logService.log("异步执行方法");
        System.out.println(Thread.currentThread() + " end," + System.currentTimeMillis());
        //防止退出
        TimeUnit.SECONDS.sleep(3);
        /**
         * Thread[main,5,main] start,1646719489494
         * Thread[main,5,main] end,1646719489531
         * Thread[SimpleAsyncTaskExecutor-1,5,main]开始记录日志，1646719489550
         * Thread[SimpleAsyncTaskExecutor-1,5,main]日志记录完毕，1646719491558
         */
        /**
         * 前2行输出，可以看出logService.log立即就返回了，后面2行来自于log方法，相差2s左右。
         * 前面2行在主线程中执行，后面2行在异步线程中执行。
         */
    }

    /**
     * 获取异步返回值
     * 用法：若需取异步执行结果，方法返回值必须为Future类型，使用spring提供的静态方法
     * {@link org.springframework.scheduling.annotation.AsyncResult#forValue(Object)}
     * 创建返回值，如：
     * public Future<String> getGoodsInfo(long goodsId) throws InterruptedException {
     * return AsyncResult.forValue(String.format(""商品$s基本信息!", goodsId));
     * }
     * <p>
     * 案例
     * 场景：电商中商品详情页通常会有很多信息：商品基本信息、商品描述信息、商品评论信息，通过3个方法或者这几个信息。
     * 这3个方法之间无关联，所以可以采用异步的方式并行获取，提升效率。
     * 下面是商品服务，内部3个方法都需要异步，所以直接在类上使用@Async标注，每个方法内部休眠500ms，模拟一下耗时操作。
     */
    @Test
    public void test2() throws InterruptedException, ExecutionException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MainConfig2.class);
        context.refresh();
        long startTime = System.nanoTime();
        System.out.println("开始获取关于商品的各种信息！");
        GoodsService goodsService = context.getBean("goodsService", GoodsService.class);
        Future<String> goodsInfo = goodsService.getGoodsInfo(1L);
        Future<String> goodsDesc = goodsService.getGoodsDesc(1L);
        Future<List<String>> goodsComments = goodsService.getGoodsComments(1L);
        System.out.printf("商品的基本信息：%s\n", goodsInfo.get());
        System.out.printf("商品的描述信息：%s\n", goodsDesc.get());
        System.out.printf("商品的评论信息：%s\n", goodsComments.get());
        long endTime = System.nanoTime();
        System.out.printf("执行总耗时%dns\n", endTime - startTime);
        /**
         * 开始获取关于商品的各种信息！
         * 商品的基本信息：商品1的基本信息。
         * 商品的描述信息：商品1的描述信息，
         * 商品的评论信息：[评论1, 评论2, 评论3]
         * 执行总耗时559877600ns
         */
        /**
         * 3个方法总计耗时500ms左右。
         * 如果不采用异步的方式，3个方法会同步执行，耗时差不多1.5s。
         * 可以借鉴这个案例，按照这个思路可以去优化一下你们的代码，方法之间无关联的可以采用异步的方式，并行获取，最终耗时为最长的那个方法，整体相对于同步的方式性能提升不少。
         *
         */
    }


    /**
     * 自定义异步执行的线程池
     * 默认情况下，@EnableAsync使用内置的线程池来异步调用方法，不过我们也可以自定义异步执行认为的线程池。
     * <p>
     * 有2种方式来自定义异步处理的线程池
     * 方式1：在spring容器中定义一个线程池类型的bean,bean名称必须是taskExecutor
     *
     * @Bean public Executor taskExecutor() {
     * ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
     * executor.setCorePoolSize(10);
     * executor.setMaxPoolSize(100);
     * executor.setThreadNamePrefix("my-thread-");
     * return executor;
     * }
     * <p>
     * 方式2
     * 定义一个bean,实现AsyncConfigurer接口中的getAsyncExecutor方法，这个方法需要返回自定义的线程池，
     * 案例
     */
    @Test
    public void test3() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        LogService logService = context.getBean("logService", LogService.class);
        System.out.println(Thread.currentThread() + "logService.log start");
        logService.log("异步执行方法");
        System.out.println(Thread.currentThread() + "logService.log end");
        //休眠一下，防止@Test退出
        TimeUnit.SECONDS.sleep(3);
        /**
         * Thread[main,5,main]logService.log start
         * Thread[main,5,main]logService.log end
         * Thread[log-thread-1,5,main]开始记录日志，1646723102520
         * Thread[log-thread-1,5,main]日志记录完毕，1646723104530
         */
    }

    /**
     * 自定义异常处理
     * 异步方法若发生了异常，我们如何获取异常信息？此时可以通过自定义异常处理来解决。
     * <p>
     * 异常情况分2种情况
     * 1.当返回值是Future的时候，方法内部有异常的时候，异常会向外抛出，可以对Future.get采用try..catch来捕获异常
     * 2.当返回值不是Future的时候，可以自定义一个bean，实现AsyncConfigurer接口中的getAsyncUncaughtExceptionHandler方法，返回自定义的异常处理器。
     * <p>
     * <p>
     * 情况1：返回值为Future类型
     * 用法：通过try..catch来捕获异常，如下：
     * try {
     * Future<String> future=logService.mockException();
     * System.out.println(future.get());
     * } catch(ExecutionException e) {
     * System.out.println("捕获ExecutionException异常");
     * //通过e.getCause获取实际的异常信息
     * e.getCause().printStackTrace();
     * } catch(InterruptedException e) {
     * e.printStackTrace();
     * }
     * <p>
     * 案例：
     */
    @Test
    public void test4() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        LogService4 logService4 = context.getBean("logService4", LogService4.class);
        try {
            Future<String> fu = logService4.mockException();
            System.out.println(fu.get());
        } catch (ExecutionException e) {
            System.out.println("捕获ExecutionException异常");
            //通过e.getCause()获取实际的异常信息
            e.getCause().printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //休眠一下，防止@Test退出
        TimeUnit.SECONDS.sleep(3);
        /**
         * 捕获ExecutionException异常
         * java.lang.IllegalArgumentException: 参数有误！
         * 	at us.fjj.spring.learning.enableasynandasynccusage.test4.LogService4.mockException(LogService4.java:12)
         * 	at us.fjj.spring.learning.enableasynandasynccusage.test4.LogService4$$FastClassBySpringCGLIB$$ccbc80dd.invoke(<generated>)
         * 	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:769)
         * 	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)
         * 	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.proceed(CglibAopProxy.java:747)
         * 	at org.springframework.aop.interceptor.AsyncExecutionInterceptor.lambda$invoke$0(AsyncExecutionInterceptor.java:115)
         * 	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
         * 	at java.base/java.lang.Thread.run(Thread.java:833)
         */
    }


    /**
     * 情况2：无返回值异常处理
     * 用法：当返回值不是Future的时候，可以自定义一个bean，实现AsyncConfigurer接口中的getAsyncUncaughtExceptionHandler方法，返回自定义的异常处理器，当目标方法执行过程中抛出异常的时候，此时会自动回调AsyncUncaughtExceptionHandler@handleUncaughtException这个方法，可以在这个方法中处理异常，如下：
     *
     * @Bean public AsyncConfigurer asyncConfigurer() {
     * return new AsyncConfigurer() {
     * @Nullable
     * @Override public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
     * return new AsyncUncaughtExceptionHandler() {
     * @Override public void handleUncaughtException(Throwable ex, Method method, Object... params) {
     * //当目标方法执行过程中抛出异常的时候，此时会自动回调这个方法，可以在这个方法中处理异常
     * }
     * };
     * }
     * };
     * }
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        LogService5 logService5 = context.getBean("logService5", LogService5.class);
        logService5.mockNoReturnException();
    }

    /**
     * 什么是线程池隔离？
     * 一个系统中可能有很多业务，比如充值业务、提现服务或者其他服务，这些服务中都有一些方法需要异步执行，默认情况下他们会使用同一线程池去执行，如果有一个业务量比较大，占用了线程池中的大量线程，此时会导致其他业务的方法无法执行，那么我们可以采用线程隔离的方式，对不同的业务使用不同的线程池，相互隔离，互不影响。
     * @Async注解有个value参数，用来指定线程池的bean名称，方法运行的时候，就会采用指定的线程池来执行目标方法。
     *
     * 使用步骤
     * 1.在spring容器中，自定义线程池相关的bean
     * 2.@Async("线程池bean名称")
     *
     * 案例
     */
    @Test
    public void test6() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        RechargeService rechargeService = context.getBean("rechargeService", RechargeService.class);
        CashOutService cashOutService = context.getBean("cashOutService", CashOutService.class);
        rechargeService.recharge();
        cashOutService.cashOut();
        TimeUnit.SECONDS.sleep(3);
        /**
         * Thread[cashOut-thread-1,5,main]模拟异步提现
         * Thread[recharge-thread-1,5,main]模拟异步充值
         */
    }

    /**
     * 源码&原理
     * 内部使用aop实现，@EnableAsync会引入一个ben后置处理器：
     * AsyncAnnotationBeanPostProcessor，将其注册到spring容器，这个bean后置处理器在所有bean创建过程中，判断bean的类上是否有@Async注解或者类中是否有@Async标注的方法，如果有，会通过aop给这个bean生成代理对象，会在代理对象中添加一个切面：
     * {@link org.springframework.scheduling.annotation.AsyncAnnotationAdvisor},这个切面中会引入一个拦截器：AnnotationAsyncExecutionInterceptor，方法异步调用的关键代码就是在这个拦截器的invoke方法中实现。
     */
}
