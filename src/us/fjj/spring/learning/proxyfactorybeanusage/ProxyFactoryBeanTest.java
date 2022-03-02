package us.fjj.spring.learning.proxyfactorybeanusage;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.proxyfactorybeanusage.test1.MainConfig1;
import us.fjj.spring.learning.proxyfactorybeanusage.test2.MainConfig2;
import us.fjj.spring.learning.proxyfactorybeanusage.test3.MainConfig3;

/**
 * ProxyFactoryBean方式：Spring环境中给指定的bean创建代理的一种方式
 * 这个类实现了一个接口FactoryBean，
 * ProxyFactoryBean就是通过FactoryBean的方式来给指定bean创建一个代理对象。
 * 创建代理，有3个信息比较关键：
 * 1.需要增强的功能，这个放在通知（Advice）中实现。
 * 2.目标对象（target）: 表示你需要给哪个对象进行增强
 * 3.代理对象（proxy）： 将增强的功能和目标对象组合在一起，然后形成的一个代理对象，通过代理对象来访问目标对象，起到对目标对象增强的效果。
 * 使用ProxyFactoryBean也是围绕着这3个部分来的，ProxyFactoryBean的使用步骤：
 * 1.创建ProxyFactoryBean对象
 * 2.通过ProxyFactoryBean.setTargetName设置目标对象的bean名称，目标对象是spring容器中的一个bean
 * 3.通过ProxyFactoryBean.setInterceptorNames添加需要增强的通知
 * 4.将ProxyFactoryBean注册到sPRINg容器，假设名称为proxyBean
 * 5.从spring查找名称为proxyBean的bean, 这个bean就是生成好的代理对象。
 */
public class ProxyFactoryBeanTest {
    /**
     * 案例1，对上面的5步进行演示
     * 需求：在spring容器上注册类Service的bean,名称为service1,通过代理的方式来对这个bean进行增强，来2个通知
     * 一个前置通知：在调用service1中的任意方法之前，输出一条信息：准备调用xxx方法
     * 一个环绕通知：赋值统计所有方法的耗时
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        us.fjj.spring.learning.proxyfactorybeanusage.test1.Service service = context.getBean("service1Proxy", us.fjj.spring.learning.proxyfactorybeanusage.test1.Service.class);
        service.m1();
        service.m2();
        System.out.println(String.format("代理类的类型：%s", service.getClass()));
        System.out.println(String.format("代理类的父类：%s", service.getClass().getSuperclass()));
        System.out.println("代理类实现的接口：");
        for (Class<?> cls : service.getClass().getInterfaces()) {
            System.out.println(cls);
        }
        /**
         *准备执行：m1
         * m1方法执行ok
         * 执行耗时：10959900ns
         * 准备执行：m2
         * m2方法执行ok
         * 执行耗时：81800ns
         * 代理类的类型：class us.fjj.spring.learning.proxyfactorybeanusage.test1.Service$$EnhancerBySpringCGLIB$$f3a7d0a4
         * 代理类的父类：class us.fjj.spring.learning.proxyfactorybeanusage.test1.Service
         * 代理类实现的接口：
         * interface org.springframework.aop.SpringProxy
         * interface org.springframework.aop.framework.Advised
         * interface org.springframework.cglib.proxy.Factory
         *
         * Process finished with exit code 0
         */
    }
    /**
     * 批量注册增强器
     * 在proxyFactoryBean.setInterceptorNames("beforeAdvice", "costTimeInterceptor")需要匹配的bean名称后面加一个*，可以用来批量匹配。
     *这里需要注意，批量的方式注册的时候，如果增强其的类型不是（org.springframework.aop.Advisor\org.aopalliance.intercept.Interceptor）这两种类型的，比如下面3种类型的通知，我们需要将其包装为Advisor才可以，而MethodInterceptor是Interceptor类型的可以不用包装为Advisor类型的。
     * MethodBeforeAdvice(方法前置通知)
     * AfterReturningAdvice(方法后置通知)
     * ThrowsAdvice(异常通知)
     * 案例2
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println(context.getBean("serviceAProxy"));
        /**
         * 将要执行toString
         * 执行耗时：45100ns
         * us.fjj.spring.learning.proxyfactorybeanusage.test2.ServiceA@2b62442c
         */
    }
    /**
     * 非批量的方式
     * 非批量的方式，需要注册多个增强器，需要明确的指定多个增强器的bean名称，多个增强器按照参数中指定的顺序执行，如：
     * proxyFactoryBean.setInterceptorNames("advice1", "advice2");
     * advice1、advice2对应的bean类型必须为下面列表中指定的类型：
     * MethodBeforeAdvice(方法前置通知)
     * AfterReturningAdvice(方法后置通知)
     * ThrowsAdvice(异常通知)
     * org.aopalliance.intercept.MethodInterceptor(环绕通知)
     * org.springframework.aop.Advisor(顾问)
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        us.fjj.spring.learning.proxyfactorybeanusage.test3.ServiceB serviceB = context.getBean("serviceBProxy", us.fjj.spring.learning.proxyfactorybeanusage.test3.ServiceB.class);
        System.out.println(serviceB);
        /**
         * 将要执行toString方法
         * 方法toString执行完成！
         * 执行耗时：230700ns
         * us.fjj.spring.learning.proxyfactorybeanusage.test3.ServiceB@3af17be2
         */
    }
}
