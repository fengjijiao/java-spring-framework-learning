package us.fjj.spring.learning.enableaspectjautoproxyusage;

import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.CarService;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.MainConfig1;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test1.UserService;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test2.MyAfterReturningAdvice;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test2.MyMethodBeforeAdvice;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test2.MyMethodInterceptor;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test2.UserService2;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test3.MainConfig3;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test3.UserService3;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test4.MainConfig4;
import us.fjj.spring.learning.enableaspectjautoproxyusage.test4.UserService4;

/**
 * @EnableAspectJAutoProxy自动为bean创建代理对象 需要结合@Aspect注解一起使用。
 */
public class EnableAspectJAutoProxyTest {
    /**
     * 使用案例
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        UserService userService = context.getBean("userService", UserService.class);
        CarService carService = context.getBean("carService", CarService.class);
        userService.say();
        carService.say();
        /**
         * 将要执行say
         * 我是UserService
         * 将要执行say
         * 我是CarService
         */
    }

    /**
     * 3.通知执行顺序
     *
     * @EnableAspectJAutoProxy允许spring容器中通过Advisor、@Asepct来定义通知，当spring容器中存在多个Advisor、@Aspect时，组成的拦截器调用链顺序是什么样的呢？ spring aop中的4种通知（Advice）
     * org.aopalliance.intercept.MethodInterceptor
     * org.springframework.aop.MethodBeforeAdvice
     * org.springframework.aop.AfterReturningAdvice
     * org.springframework.aop.ThrowsAdvice
     * 所有通知最终都需要转换为MethodInterceptor类型的通知，然后组成一个MethodInterceptor列表，我们称之为方法调用链或拦截器，上面列表中后面3种通过下面的转换器将其包装为MethodInterceptor类型的通知。
     * {@link org.springframework.aop.MethodBeforeAdvice} -> {@link org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor}
     * {@link org.springframework.aop.AfterReturningAdvice} -> {@link org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor}
     * {@link org.springframework.aop.ThrowsAdvice} -> {@link org.springframework.aop.framework.adapter.ThrowsAdviceInterceptor}
     * <p>
     * 4种通知的执行顺序：
     * MethodInterceptor start
     * MethodBeforeAdvice
     * AfterReturningAdvice
     * MethodInterceptor end
     * //返回值
     */
    @Test
    public void test2() {
        UserService2 target = new UserService2();
        ProxyFactory proxyFactory1 = new ProxyFactory();
        proxyFactory1.setTarget(target);
        proxyFactory1.addAdvice(new MyMethodInterceptor());
        proxyFactory1.addAdvice(new MyMethodBeforeAdvice());
        proxyFactory1.addAdvice(new MyAfterReturningAdvice());
        UserService2 proxy1 = (UserService2) proxyFactory1.getProxy();
        System.out.println(proxy1.say());
        /**
         * MethodInterceptor start
         * MethodBeforeAdvice
         * AfterReturningAdvice
         * MethodInterceptor end
         * 你好，lk
         */
        proxy1.say2();
        /**
         * MethodInterceptor start
         * MethodBeforeAdvice
         * 你好，lk
         * AfterReturningAdvice
         * MethodInterceptor end
         */
    }

    /**
     * 单个@Aspect中多个通知的执行顺序
     * 当单个@Aspect中定义了多种类型的通知时，@EnableAspectJAutoProxy内部会对其进行排序，排序顺序如下：
     *
     * @AfterThrowing
     * @AfterReturning
     * @After
     * @Around
     * @Before 案例：同时定义以上5个通知，会发现显示的与以上排序不一致。
     * 原因是：
     * try {
     * Object result = null;
     * try {
     * System.out.println("@Around通知start");
     * System.out.println("@Before通知!");
     * result = service4.say("路人");
     * System.out.println("@Around绕通知end");
     * return result;
     * } finally {
     * System.out.println("@After通知!");
     * }
     * System.out.println("@AfterReturning通知!");
     * return retVal;
     * } catch (Throwable ex) {
     * System.out.println("@AfterThrowing通知!");
     * //继续抛出异常
     * throw ex;
     * }
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        UserService3 userService3 = context.getBean("userService3", UserService3.class);
        System.out.println(userService3.say());
        System.out.println("----------------------");
        userService3.say2();
        /**
         *Around start
         * Before
         * Around end
         * After
         * AfterReturning: 你好，lk
         * 你好，lk
         * ----------------------
         * Around start
         * Before
         * 你好，lk
         * Around end
         * After
         * AfterReturning: null
         *
         */
    }

    /**
     *
     * @EnableAspectJAutoProxy为通知指定顺序
     * @EnableAspectJAutoProxy用在spring环境中，可以通过@Aspect、Advisor定义多个通知。
     * 当spring容器中有多个@Aspect、Advisor时，他们的顺序是怎样的？
     *
     * 先来看看如何为@Aspect、自定义Advisor指定顺序。
     * 1.用@Order注解
     * 需要在@Aspect标注的类上使用@org.springframework.core.annotation.Order注解，值越小，通知的优先级越高。
     * @Aspect
     * @Order(1)
     * public class AspectOrder1{}
     * 2.实现Ordered接口
     * 自定义的Advisor通过org.springframework.core.Ordered接口来指定顺序，这个接口有个public int getOrder()方法，用来返回通知的顺序。
     * spring中为我们提供了一个Advisor类型的抽象类{@link org.springframework.aop.support.AbstractPointcutAdvisor}，这个类实现了Ordered接口，
     * spring中大部分Advisor会是继承AbstactPointcutAdvisor，若需要自定义Advisor,也可以继承这个类，这个类的getOrder方法比较关键。
     *
     * spring为我们提供了一个默认的Advisor类：DefaultPointcutAdvisor，这个类就继承了AbstractPointcutAdvisor，通常我们可以直接使用DefaultPointcutAdvisor来自定义通知。
     *
     */


    /**
     * 多个@Aspect、Advisor排序规则
     * 1.在spring容器中获取@Aspect、Advisor类型的所有bean，得到一个列表list1
     * 2.对list1按照order的值升序排序，得到结果list2
     * 3.然后再对list2中@Aspect类型的bean内部的通知进行排序，规则
     * @AfterThrowing
     * @AfterReturning
     * @After
     * @Around
     * @Before
     * 4.最后运行的时候会得到上面排序生成的方法调用链列表去执行。
     *
     * 案例：下面我们创建2个Aspect、1个Advisor来验证一下执行顺序
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        UserService4 userService4 = context.getBean("userService4", UserService4.class);
        System.out.println(userService4.say());
        /**
         * Priority: Aspect1(1) > Advisor1(2) > Aspect2(3)
         *
         *
         * Aspect1 Around start
         * Aspect1 Before.
         * Advisor1 start
         * Aspect2 Around start
         * Aspect2 Before.
         * Aspect2 Around end
         * Aspect2 After.
         * Aspect2 AfterReturning.
         * Advisor1 end
         * Aspect1 Around end
         * Aspect1 After.
         * Aspect1 AfterReturning.
         * 你好, lk
         */

        /**
         * try {
         * Object result = null;
         * try {
         * System.out.println("MyAspect1 @Around通知start");
         * System.out.println("MyAspect1 @Before通知!");
         * System.out.println("Advisor1 start");
         * try {
         * try {
         * System.out.println("MyAspect2 @Around通知start");
         * System.out.println("MyAspect2 @Before通知!");
         * result = "你好：路人";
         * System.out.println("MyAspect2 @Around绕通知end");
         * return result;
         * } finally {
         * System.out.println("MyAspect2 @After通知!");
         * }
         * System.out.println("MyAspect2 @AfterReturning通知!");
         * } catch (Throwable ex) {
         * System.out.println("MyAspect2 @AfterThrowing通知!");
         * //继续抛出异常
         * throw ex;
         * }
         * System.out.println("Advisor1 end");
         * System.out.println("MyAspect1 @Around绕通知end");
         * return result;
         * } finally {
         * System.out.println("MyAspect1 @After通知!");
         * }
         * 再来和输出结果对比一下，是完全一致的。
         * 9、@EnableAspectJAutoProxy另外2个功能
         * 这个注解还有2个参数，大家看一下下面的注释，比较简单，就不用案例演示了。
         * 10、@EnableAspectJAutoProxy原理
         * @EnableAspectJAutoProxy 会在spring容器中注册一个bean
         * System.out.println("MyAspect1 @AfterReturning通知!");
         * return retVal;
         * } catch (Throwable ex) {
         * System.out.println("MyAspect1 @AfterThrowing通知!");
         * //继续抛出异常
         * throw ex;
         * }
         */
    }
    /**
     * @EnableAspectJAutoProxy原理
     * @EnableAspectJAutoProxy会在spring容器中注册一个bean
     * {@link org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator}
     * AnnotationAwareAspectJAutoProxyCreator是BeanPostProcessor类型的，BeanPostProcessor是bean后置处理器，可以在bean生命周期中对bean进行操作，比如对bean生成代理等；而AnnotationAwareAspectJAutoProxyCreator就是对符合条件的bean，自动生成代理对象，源码位于posttProcessAfterInitialization方.
     */


}
