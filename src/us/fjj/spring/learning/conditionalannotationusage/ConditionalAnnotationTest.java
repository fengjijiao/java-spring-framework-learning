package us.fjj.spring.learning.conditionalannotationusage;

/**
 * @Conditional主机
 * 可以用在任何类型或者方法上面，通过@Conditional注解可以配置一些条件判断，当所有条件都满足的时候，被@Conditional标注的目标才会被spring容器处理。
 */

/**
 * 关键问题：条件判断是在什么时候执行？
 *
 *
 * Spring对配置类的处理主要分为2个阶段：
 * 1.配置类解析阶段（会得到一批配置类的信息，和一些需要注册的bean）、
 * 2.bean注册阶段（将配置类解析阶段得到的配置类和需要注册的bean注册到spring容器中）
 * 配置类（类上有@Component、@Configuration、@ComponentScan、@Import、@ImportResource、@Bean注解的方法）
 * 判断一个类是不是配置类可以使用如下方法
 * org.springframework.context.annotation.ConfigurationClassUtils#isConfigurationCandidate
 *
 *
 * Spring对配置类处理过程
 * 1.通常会通过new AnnotationConfigApplicationContext()传入多个配置类来启动spring容器
 * 2.spring对传入的多个配置类进行解析
 * 3.配置类解析阶段：这个过程就是处理配置类上6种注解的过程，此过程中又会发现很多新的配置类，比如@Import导入的一批新的类刚好也符合配置类，而被@ComponentScan扫描到的一些类刚好也是配置类；此时会对这些新产生的配置类进行同样的过程解析
 * 4.bean注册阶段：配置类解析后，会得到一批配置类和一批需要注册的bean，此时spring容器会将这批配置类作为bean注册到spring容器,同样也会将这批需要注册的bean注册到spring容器
 * 5.经过上面3个阶段之后，spring容器中会注册很多新的bean，这些新的bean中可能又有很多新的配置类。
 * 6.Spring从容器中将所有bean拿出来，便利一下，会过滤得到一批未处理的新的配置类，继续交给第3步进行处理。
 * 7.step3到step6，这个过程会经历很多次，直到完成所有配置类的解析和bean的注册
 *
 * 从上面的过程中可以了解到：
 * 1.可以在配置类上面加上@Conditional注解，来控制是否需要解析这个配置类，配置类如果不被解析，那么这个配置上面的6中注解的解析都将被跳过
 * 2.可以在被注册的bean上面加上@Conditional注解，来控制这个bean是否需要注册到spring容器中
 * 3.如果配置类不会被注册到容器，那么这个配置类解析所产生的所有新的配置类及所产生的所有新的bean都不会被注册到容器
 *
 * 一个配置类被spring处理有2个阶段：配置类解析阶段、bean注册阶段（将配置类作为bean被注册到spring容器）
 * 如果将Condition接口的实现类作为配置类上的@Conditional，那么这个条件会对两个阶段都有效，此时通过Condition是无法精细的控制某个阶段的，如果想控制某个阶段，比如可以让他解析，但是不能让他注册，此时就需要用到另一个接口了：ConfigurationCondition
 *
 */


import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import us.fjj.spring.learning.conditionalannotationusage.test1.MainConfig1;
import us.fjj.spring.learning.conditionalannotationusage.test2.MainConfig2;
import us.fjj.spring.learning.conditionalannotationusage.test3.MainConfig3;
import us.fjj.spring.learning.conditionalannotationusage.test4.MainConfig4;
import us.fjj.spring.learning.conditionalannotationusage.test5.MainConfig5;
import us.fjj.spring.learning.conditionalannotationusage.test6.MainConfig6;
import us.fjj.spring.learning.conditionalannotationusage.test7.MainConfig7;
import us.fjj.spring.learning.conditionalannotationusage.test8.MainConfig8;
import us.fjj.spring.learning.conditionalannotationusage.test9.MainConfig9;

import java.util.Map;

/**
 * @Conditional使用的3个步骤
 * 1.自定义一个类，实现Condition或ConfigurationCondition接口，实现matches方法
 * 2.在目标对象上使用@Conditional注解，并指定value的值为自定义的Condition类型
 * 3.启动spring容器加载资源，此时@Conditional就会起作用了
 */
public class ConditionalAnnotationTest {
    /**
     * 案例1：阻止配置类的处理
     * 在配置类上面使用@Conditional，这个注解的value指定的Condition当中有一个为false的时候，spring就会跳过处理这个配置类。
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        Map<String, String> serviceMap = context.getBeansOfType(String.class);//从容器中获取指定类型的bean
        serviceMap.forEach((beanName, bean) -> {
            System.out.println(String.format("%s->%s", beanName, bean));
        });
        /**
         *
         */
    }
    /**
     * 案例2：阻止bean的注册
     *
     */
    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        Map<String, String> serviceMap = context.getBeansOfType(String.class);
        serviceMap.forEach((beanName, bean) -> {
            System.out.println(String.format("%s->%s", beanName, bean));
        });
        /**
         * address->LK SB
         */
    }
    /**
     * 案例3：bean不存在的时候才注册
     * 需求：
     * IService接口有两个实现类Service1和Service2，这两个类会放在2个配置类中通过@Bean的方式来注册到容器，此时我们想加个限制，只允许有一个IService类型的bean被注册到容器。
     * 可以在@Bean标记的2个方法上面加上条件限制，当容器中不存在IService类型的bean时，才将这个方法定义的bean注册到容器中。
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig3.class);
        for (String beanName :
                context.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, context.getBean(beanName)));
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor->org.springframework.context.annotation.ConfigurationClassPostProcessor@1c80e49b
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor->org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@458342d3
         * org.springframework.context.annotation.internalCommonAnnotationProcessor->org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@15c25153
         * org.springframework.context.event.internalEventListenerProcessor->org.springframework.context.event.EventListenerMethodProcessor@1252b961
         * org.springframework.context.event.internalEventListenerFactory->org.springframework.context.event.DefaultEventListenerFactory@9ed238c
         * mainConfig3->us.fjj.spring.learning.conditionalannotationusage.test3.MainConfig3$$EnhancerBySpringCGLIB$$f61329d2@56276db8
         * us.fjj.spring.learning.conditionalannotationusage.test3.BeanConfig1->us.fjj.spring.learning.conditionalannotationusage.test3.BeanConfig1@51e8e6e6
         * service1->us.fjj.spring.learning.conditionalannotationusage.test3.Service1@56f6d40b
         * us.fjj.spring.learning.conditionalannotationusage.test3.BeanConfig2->us.fjj.spring.learning.conditionalannotationusage.test3.BeanConfig2$$EnhancerBySpringCGLIB$$df1904fa@36676c1a
         */
        /**
         * 可以看到容器中只有一个IService类型的bean。
         */
    }

    /**
     * 案例4：根据环境选择配置类
     * 应用：在开发环境、测试环境、线上环境中，有些信息是不一样的，例如数据库的配置信息
     */
    @Test
    public void test4() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig4.class);
        System.out.println(context.getBean("name"));
        /**
         * 开发环境
         */
    }
    /**
     *多个Condition按顺序执行
     */
    @Test
    public void test5() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig5.class);
        /**
         * Condition1
         * Condition2
         * Condition3
         * Condition1
         * Condition2
         * Condition3
         * Condition1
         * Condition2
         * Condition3
         *
         * 上面有多行输出，是因为spring解析整个配置类的过程中，有好几个地方都会执行条件判断。
         * 我们只关注前3行，可以看出输出的顺序和@Conditional中value值的顺序是一样的。
         */
    }
    /**
     * 案例5:指定Condition的顺序
     */
    /**
     * 自定义的Condition可以实现PriorityOrdered接口或者继承Ordered接口，或者使用@Order注解，通过这些来指定这些Condition的优先级。
     * 排序规则：先按PriorityOrdered排序，然后按照order的值进行排序，也就是：PriorityOrdered asc, order值 asc
     * 下面这几个都可以指定order的值
     * 接口：org.springframework.core.Ordered，有个getOrder方法用来返回int类型的值
     * 接口：org.springframework.core.PriorityOrdered，继承了Ordered接口，所以也有getOrder方法
     * 注释：org.springframework.core.annotation.Order，有个int类型的value参数指定Order的大小
     */
    @Test
    public void test6() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig6.class);
        /**
         *Condition3
         * Condition2
         * Condition1
         * Condition3
         * Condition2
         * Condition1
         * Condition3
         * Condition2
         * Condition1
         */
    }

    @Test
    public void test7() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig7.class);
        context.getBeansOfType(String.class).forEach((beanName, bean) -> {
            System.out.println(String.format("%s->%s", beanName, bean));
        });
        /**
         *name->LAOKAI
         */
    }
    /**
     * 需求：当容器中有Service这种类型的bean的时候，BeanConfig2才生效。
     * - 加个Condition就行了
     */
    @Test
    public void test8() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig8.class);
        /**
         *
         */
    }
    /**
     * 为什么？
     * 配置类的处理会依次经过2个阶段：配置类解析阶段和bean注册阶段，Condition接口类型的条件会对这两个阶段都有效，解析阶段的时候
     * 容器中还没有Service这个bean的，配置类中通过@Bean注解定义的bean在bean注册阶段才会被注册到容器中，所以BeanConfig2在解析
     * 阶段去容器中是看不到Service这个bean的，所以就拒绝了。
     * 此时我们需要用到ConfigurationCondition了，让条件判断在bean注册阶段才起效。
     */
    /**
     * 案例6: ConfigurationCondition使用
     * (与上一步相比只需要修改MyCondition1的实现为ConfigurationCondition)
     */
    @Test
    public void test9() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig9.class);
        context.getBeansOfType(String.class).forEach((beanName, bean) -> {
            System.out.println(String.format("%s->%s", beanName, bean));
        });
        /**
         * name->LAOKAI
         */
        /**
         * 判断bean存不存在的问题，通常会使用ConfigurationCondition这个接口，阶段为：REGISTER_BEAN,这样可以确保条件判断是在bean注册阶段执行的。
         */
    }

    /**
     * 总结
     * 1.@Conditional注解可以标注在Spring需要处理的对象上（配置类、@Bean方法），相当于加了个条件判断，通过判断的结果，让spring觉得是否要继续处理被这个注解标注的对象
     * 2.spring处理配置类大致上有2个过程：解析配置类、注册bean，这两个过程中都可以使用@Conditional来进行控制spring是否需要处理这个过程。
     * 3.Condition默认会对两个过程都有效
     * 4.ConfigurationCondition控制得更加细致一些，可以控制到具体哪个阶段使用条件判断(以上两个阶段之一)
     */
}
