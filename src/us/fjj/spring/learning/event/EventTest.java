package us.fjj.spring.learning.event;

/**
 * 事件
 */

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.EventListenerMethodProcessor;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import us.fjj.spring.learning.event.test1.MainConfig1;
import us.fjj.spring.learning.event.test2.MainConfig2;
import us.fjj.spring.learning.event.test3.OrderCreateEvent;
import us.fjj.spring.learning.event.test3.SendEmailOnOrderCreateListener;
import us.fjj.spring.learning.event.test4.MainConfig4;
import us.fjj.spring.learning.event.test5.MainConfig5;
import us.fjj.spring.learning.event.test6.MainConfig6;
import us.fjj.spring.learning.event.test7.MainConfig7;

/**
 * 事件模式中的概念：
 * 事件源：事件的触发者，比如上面的注册器就是事件源。
 * 事件：描述了发生了什么事情的对象，比如上面的：xxx注册成功的事件
 * 事件监听器：监听到事件发生的时候，做一些处理，比如上面的：路人A（负责发送邮件）、路人B（负责发放优惠卷）
 */
public class EventTest {
    /**
     * 下面我们使用事件模式实现用户注册的业务
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        us.fjj.spring.learning.event.test1.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test1.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("ssw");
        /**
         * 注册用户ssw成功！
         */
    }
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        us.fjj.spring.learning.event.test2.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test2.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("ssw");
        /**
         * 注册用户ssw成功！
         * us.fjj.spring.learning.event.test2.UserRegisterService@47da3952
         * 发送欢迎邮件到用户ssw成功！
         */
    }
    /**
     * 小结
     * 上面将注册的主要逻辑（用户信息入库）和次要的业务逻辑（发送邮件）通过事件的方式解耦了。次要的业务做成可插拔的方式，比如不想发送邮件了，只需要将邮件监听器上的@Component注解注释即可，非常方便扩展。
     * 上面用到的和事件相关的几个类，都是我们自己实现的，其实这些功能在spring中已经帮我们实现好了，用起来更容易一些。
     */

    /**
     * Spring中实现事件模式
     * 事件相关的几个类
     * spring中事件类           我们自定义的事件类           作用
     * @see org.springframework.context.ApplicationEvent         AbstractEvent           表示事件对象的父类
     * @see org.springframework.context.ApplicationListener         EventListener           事件监听器接口
     * @see org.springframework.context.event.SimpleApplicationEventMulticaster         SimpleEventMulticaster          事件广播器的简单实现
     */

    /**
     * 硬编码的方式使用spring事件3步骤
     * 1.定义事件
     * 自定义事件，需要继承ApplicationEvent类
     * 2.定义监听器
     * 自定义事件监听器，需要实现ApplicationListener接口，这个接口有个方法onApplicationEvent需要实现，用来处理感兴趣的事件。
     * 3.创建事件广播器
     * 创建事件广播器ApplicationEventMulticaster，这是个接口，你可以自己实现这个接口，也可以直接使用系统给我们提供的SimpleApplicationEventMulticaster
     * ApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();
     * 4.向广播器中注册事件监听器
     * 如：
     * ApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();
     * applicationEventMulticaster.addApplicationListener(new SendEmailOnOrderCreateListener());
     * 5.通过广播器发布事件
     * 广播事件，调用ApplicationEventMulticaster#multicastEvent方法广播事件，此时广播器中对这个事件感兴趣的监听器会处理这个事件。
     * applicationEventMulticaster.multicastEvent(new OrderCreateEvent(applicationEventMulticaster, 1L));
     */

    /**
     * 案例：电商中订单创建成功后，给下单人发送一封邮件，发送邮件的功能放在监听器中实现。
     */
    @Test
    public void test3() {
        ApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        //注册事件监听器
        eventMulticaster.addApplicationListener(new SendEmailOnOrderCreateListener());
        //模拟订单创建
        eventMulticaster.multicastEvent(new OrderCreateEvent(eventMulticaster, 999L));
        /**
         * 发送邮件：订单(ID: 999)创建成功！
         */
    }

    /**
     * 上面的代码仅供演示原理。
     * 通常情况下，我们会使用以ApplicationContext结尾的类作为spring的容器来启动应用。
     * 1.AnnotationConfigApplicationContext和ClassPathXmlApplicationContext都继承了AbstractApplicationContext
     * 2.AbstractApplicationContext实现了ApplicationEventPublisher接口
     * 3.AbstractApplicationContext内部有个ApplicationEventMulticaster类型的字段
     * 即：AbstractApplicationContext内部已经集成了事件广播器ApplicationEventMulticaster，说明AbstractApplicationContext内部是具有事件相关功能的，这些功能是通过其内部的ApplicationEventMulticaster来实现的，也就是说事件的功能委托给了内部的ApplicationEventMulticaster来实现的。
     *
     * ApplicationEventPublisher接口(这个接口用于发布事件)
     * 这个接口的实现类中，比如AnnotationConfigApplicationContext内部将这2个方法委托给ApplicationEventMulticaster#multicastEvent进行处理了。
     * 所以调用AbstractApplicationContext中的publishEvent方法，也实现了广播事件的效果，不过使用AbstractApplicationContext也只能通过调用publishEvent方法来广播事件了。
     *
     *
     * 如果想在普通的bean中获取ApplicationEventPublisher对象，需要实现ApplicationEventPublisherAware接口。
     * 容器或自动通过内部的setApplicationEventPublisher方法将ApplicationEventPublisher注入进来，此时我们就可以通过这个来发布事件了。
     *
     *
     */

    /**
     * Spring为了简化事件的使用，提供了2种使用方式
     * 1.面向接口的方式
     * 2.面向@EventListener注解的方式
     */

    /**
     * 面向接口的方式
     * 案例：实现用户注册成功后发布事件，然后在监听器中发布邮件的功能
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        us.fjj.spring.learning.event.test4.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test4.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("yk");
        /**
         * 用户yk注册成功！
         * 发送邮件：用户yk注册成功！
         */
        /**
         * 原理: spring容器在创建bean的过程中，会判断bean是否为ApplicationListener类型，进而会将其作为监听器注册到AbstractApplicationContext#applicationEventMulticaster中。
         * @see org.springframework.context.support.ApplicationListenerDetector#postProcessAfterInitialization(Object, String) 
         */
    }

    /**
     * 面向@EventListener注解方式
     * 直接将这个注解标注在一个bean上，那么这个方法就可以用来处理感兴趣的事件，更简单
     * @see us.fjj.spring.learning.event.test5.UserRegisterListener
     * 案例
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        us.fjj.spring.learning.event.test5.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test5.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("yk");
        /**
         * 用户yk注册成功！
         * 给用户yk发送优惠卷成功!
         * 发送邮件：用户yk注册成功！
         */
        /**
         * 原理
         * @see EventListenerMethodProcessor#afterSingletonsInstantiated()
         */
    }

    /**
     * 监听器的排序功能
     * 默认情况下，监听器执行顺序是无序的
     * 可以通过以下方式实现排序
     * 1.实现org.springframework.core.Ordered接口
     * 2.实现org.springframework.core.PriorityOrdered接口
     * 3.类上使用@org.springframework.core.annotaiton.Order注解
     * 以上方式的排序规则
     * PriorityOrdered#getOrder ASC, Ordered或@Oreder ASC
     *
     */
    /**
     * 在test5的基础上在UserRegisterListener上添加@Order后，实现了先发送邮件后发送优惠卷。
     */
    @Test
    public void test6() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        us.fjj.spring.learning.event.test6.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test6.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("yk");
        /**
         * 【Thread[main,5,main]】用户yk注册成功！
         * 【Thread[main,5,main]】发送邮件：用户yk注册成功！
         * 【Thread[main,5,main]】给用户yk发送优惠卷成功!
         */
        /**
         * 从输出可以看出上面程序的执行都是在主线程中执行的，说明监听器中的逻辑和注册逻辑在一个线程中执行，此时如果监听器中的逻辑比较耗时或者失败，直接会导致注册失败，通常我们将一些非主要逻辑放在监听器中执行，至于这些非主要逻辑的成功或者失败，最好不要对主要逻辑产生影响，所以我们最好能够将监听器的运行和主业务隔离开，放在不同的线程中执行，主业务不关注监听器的结果，spring中支持这种功能。
         */
    }

    /**
     * 监听器异步模式
     * 我们只需要自定义一个类型为SimpleApplicationEventMulticaster名称为applicationEventMulticaster的bean就可以了，顺便给executor设置一个值，就可以实现监听器的异步执行了。
     */
    @Test
    public void test7() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig7.class);
        us.fjj.spring.learning.event.test7.UserRegisterService userRegisterService = context.getBean(us.fjj.spring.learning.event.test7.UserRegisterService.class);
        //模拟用户注册
        userRegisterService.register("yk");
        /**
         * 【Thread[main,5,main]】用户yk注册成功！
         * 【Thread[applicationEventMulticasterThreadPool-2,5,main]】给用户yk发送优惠卷成功!
         * 【Thread[applicationEventMulticasterThreadPool-1,5,main]】发送邮件：用户yk注册成功！
         */
    }
    /**
     * 关于事件的使用建议
     * 1.spring中事件是使用接口的方式还是使用注解的方式？具体使用哪种方式都可以，
     * 2.异步事件的模式，通常将一些非主要的业务放在监听器中执行，因为监听器中存在失败的风险，所以使用的过程需要注意。如果只是为了解耦，但是被解耦的次要业务也是必须要成功的，可以使用消息中间件的方式来解决这些问题。
     */
}
