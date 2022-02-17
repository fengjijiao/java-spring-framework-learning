package us.fjj.spring.learning.fathersoncontainer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Map;

public class FatherSonContainerTest {
    /**
     * 案例1
     */
    @Test
    public static void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(
                us.fjj.spring.learning.fathersoncontainer.test1.module1.Module1Config.class,
                us.fjj.spring.learning.fathersoncontainer.test1.module2.Module2Config.class
        );
        context.refresh();

        context.getBean(us.fjj.spring.learning.fathersoncontainer.test1.module2.Service3.class).m1();
        context.getBean(us.fjj.spring.learning.fathersoncontainer.test1.module2.Service3.class).m2();
        /**
         * nested exception is org.springframework.context.annotation.ConflictingBeanDefinitionException: Annotation-specified bean name 'service1' for bean class [us.fjj.spring.learning.fathersoncontainer.test1.module2.Service1] conflicts with existing, non-compatible bean definition of same name and class [us.fjj.spring.learning.fathersoncontainer.test1.module1.Service1]
         */
        /**
         * 原因：两个模块都有Service1，被注册到spring容器的时候bean名称会冲突，导致注册失败。
         * 解决方法：
         * 1.重命名，防止两个bean名称一样
         * 2.使用父子容器
         */
    }

    /**
     * 案例2: 父子容器（BeanFactory方式）
     * 必须要有getter,setter
     */
    @Test
    public void test2() {
        DefaultListableBeanFactory parentFactory = new DefaultListableBeanFactory();
        parentFactory.registerBeanDefinition(
                "service1",
                BeanDefinitionBuilder
                        .genericBeanDefinition(us.fjj.spring.learning.fathersoncontainer.test2.module1.Service1.class)
                        .getBeanDefinition()
        );
        parentFactory.registerBeanDefinition(
                "service2",
                BeanDefinitionBuilder
                        .genericBeanDefinition(us.fjj.spring.learning.fathersoncontainer.test2.module1.Service2.class)
                        .addAutowiredProperty("service1")
                        .getBeanDefinition()
        );
        //parentFactory.getBean("service2", us.fjj.spring.learning.fathersoncontainer.test2.module1.Service2.class).m1();
        DefaultListableBeanFactory childFactory = new DefaultListableBeanFactory();
        childFactory.setParentBeanFactory(parentFactory);

        childFactory.registerBeanDefinition(
                "service1",
                BeanDefinitionBuilder
                        .genericBeanDefinition(us.fjj.spring.learning.fathersoncontainer.test2.module2.Service1.class)
                        .getBeanDefinition()
        );
        childFactory.registerBeanDefinition(
                "service3",
                BeanDefinitionBuilder
                        .genericBeanDefinition(us.fjj.spring.learning.fathersoncontainer.test2.module2.Service3.class)
                        .addAutowiredProperty("service1")
                        .addAutowiredProperty("service2")
                        .getBeanDefinition()
        );

        childFactory.getBean("service3", us.fjj.spring.learning.fathersoncontainer.test2.module2.Service3.class).m1();
        childFactory.getBean("service3", us.fjj.spring.learning.fathersoncontainer.test2.module2.Service3.class).m2();
        /**
         * 执行了Service3的m1方法(module2)
         * 执行了Service1的m1方法(module2)
         *
         * 执行了Service3的m2方法(module2)
         * 执行了Service2的m1方法(module1)
         * 执行了Service1的m1方法(module1)
         */
    }

    /**
     * 案例3:ApplicationContext方式
     */
    @Test
    public void test3() {
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.register(us.fjj.spring.learning.fathersoncontainer.test3.module1.Module1Config.class);
        parentContext.refresh();
        AnnotationConfigApplicationContext childContext = new AnnotationConfigApplicationContext();
        childContext.setParent(parentContext);
        childContext.register(us.fjj.spring.learning.fathersoncontainer.test3.module2.Module2Config.class);
        childContext.refresh();

        childContext.getBean("service3", us.fjj.spring.learning.fathersoncontainer.test3.module2.Service3.class).m1();
        childContext.getBean("service3", us.fjj.spring.learning.fathersoncontainer.test3.module2.Service3.class).m2();
        /**
         * 执行了Service3的m1方法(module2)
         * 执行了Service1的m1方法(module2)
         *
         * 执行了Service3的m2方法(module2)
         * 执行了Service2的m1方法(module1)
         * 执行了Service1的m1方法(module1)
         */
    }

    /**
     * 父子容器特点
     * 1.父容器和子容器是相互隔离的，他们内部可以存在名称相同的bean
     * 2.子容器可以访问父容器中的bean，而父容器不能访问子容器中的bean
     * 3.调用子容器的getBean方法获取bean的时候，会沿着当前容器开始向上面的容器进行查找，知道找到对应的bean为止
     * 4.子容器中可以通过任何注入方式注入父容器中的bean，而父容器中是无法注入子容器中的bean，原因是第二点
     */

    /**
     * 父子容器使用注意点
     * 使用容器的过程中，经常会使用到的一些方法，这些方法通常会在下面的两个接口中
     * {@link org.springframework.beans.factory.BeanFactory}
     * {@link org.springframework.beans.factory.ListableBeanFactory}
     * 使用父子容器时需要注意：
     * BeanFactory接口是spring容器的顶层接口，这个接口中的方法是支持容器嵌套结构查找的，比如经常使用的getBean方法，就是这个接口中定义的，调用getBean方法的时候，会从沿着当前容器向上查找，直到找到满足条件的bean为止。
     * 而ListableBeanFactory这个接口中的方法是不支持容器嵌套结构查找的，比如下面这个方法
     * String[] getBeanNamesForType(@Nullable Class<?>type)
     * 获取指定类型的所有bean名称，调用这个方法的时候只会返回当前容器中符合条件的bean,而不会去递归查找其父容器中的bean。
     *
     */
    @Test
    public void test4() {
        DefaultListableBeanFactory parentFactory = new DefaultListableBeanFactory();
        parentFactory.registerBeanDefinition(
                "username",
                BeanDefinitionBuilder
                        .genericBeanDefinition(String.class)
                        .addConstructorArgValue("yk")
                        .getBeanDefinition()
                );
        DefaultListableBeanFactory childFactory = new DefaultListableBeanFactory();
        childFactory.setParentBeanFactory(parentFactory);
        childFactory.registerBeanDefinition(
                "address",
                BeanDefinitionBuilder
                        .genericBeanDefinition(String.class)
                        .addConstructorArgValue("LuJiaZui, Shanghai")
                        .getBeanDefinition()
        );
        System.out.println(Arrays.asList(childFactory.getBeansOfType(String.class)));
        /**
         * [{address=LuJiaZui, Shanghai}]
         */
        /**
         * 可以看到只输出了address
         */
    }
    /**
     * 那么有没有方式解决ListableBeanFactory接口不支持层次查找的问题？
     * spring中有个工具类就是解决这个问题的
     * {@link org.springframework.beans.factory.BeanFactoryUtils}
     * 在这个类中提供了很多静态方法，有很多支持层次查找的方法，名称中包含有Ancestors的都是支持层次查找的.
     */
    @Test
    public void test5() {
        DefaultListableBeanFactory parentFactory = new DefaultListableBeanFactory();
        parentFactory.registerBeanDefinition(
                "username",
                BeanDefinitionBuilder
                        .genericBeanDefinition(String.class)
                        .addConstructorArgValue("yk")
                        .getBeanDefinition()
        );
        DefaultListableBeanFactory childFactory = new DefaultListableBeanFactory();
        childFactory.setParentBeanFactory(parentFactory);
        childFactory.registerBeanDefinition(
                "address",
                BeanDefinitionBuilder
                        .genericBeanDefinition(String.class)
                        .addConstructorArgValue("LuJiaZui, Shanghai")
                        .getBeanDefinition()
        );
        //层次查找所有符合类型的bean名称
        String[] beanNamesForTypeIncludingAncestors = BeanFactoryUtils
                .beanNamesForTypeIncludingAncestors(childFactory, String.class);
        System.out.println(Arrays.asList(beanNamesForTypeIncludingAncestors));

        Map<String, String> beansOfTypeIncludingAncestors = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(childFactory, String.class);
        System.out.println(Arrays.asList(beansOfTypeIncludingAncestors));

        /**
         * [address, username]
         * [{address=LuJiaZui, Shanghai, username=yk}]
         */
    }

    /**
     * springmvc父子容器的问题
     * 问题1：springmvc中只使用一个容器是否可以？
     * 只使用一个容器是可以正常运行的
     * 问题2：那么springmvc中为什么需要用到父子容器？
     * 通常我们使用springmvc的时候，采用3层结构，controller层、service层、dao层；父容器中会包含dao层和service层，而子容器中包含的只有controller层；这两个容器组成了父子容器的关系，
     * controller层通常会注入service层的bean。
     * 采用父子容器可以避免有些人在service层去注入controller层的bean,导致整个依赖层次比较混乱。
     * 父容器和子容器的需求也是不一样的，比如父容器中需要有事务支持，会注入一些支持事务的拓展组件，而子容器中controller完全用不到这些，对这些也不关心，子容器中需要注入一些springmvc相关的bean，而这些bean父容器中也同样不会用到，也是不关心一些东西，将这些互不相关的东西隔开，可以有效的避免一些不必要的错误，而父子容器加载的速度也会快一些。
     *
     */


    /**
     * 总结
     * 1.本文需要掌握父子容器的用法，了解父子容器的特点：子容器可以访问父容器中的bean，父容器无法访问子容器的bean。
     * 2.BeanFactory接口支持层次查找
     * 3.ListableBeanFactory接口不支持层次查找
     * 4.BeanFactoryUtils工具类中提供了一些非常实用的方法，比如支持bean层次查找的方法等等
     */
}
