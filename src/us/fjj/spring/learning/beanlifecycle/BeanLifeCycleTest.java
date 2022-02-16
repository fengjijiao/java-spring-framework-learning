package us.fjj.spring.learning.beanlifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import us.fjj.spring.learning.beanlifecycle.test1.Car;
import us.fjj.spring.learning.beanlifecycle.test3.User;
import us.fjj.spring.learning.beanlifecycle.test5.CompositeObj;
import us.fjj.spring.learning.beanlifecycle.test7.Service1;
import us.fjj.spring.learning.beanlifecycle.test7.Service2;
import us.fjj.spring.learning.utils.CPXUtil;

import java.util.Arrays;

/**
 * Bean生命周期12环节
 * 1.阶段1：Bean元信息配置阶段
 * 2.阶段2：Bean元信息解析阶段
 * 3.阶段3：将Bean注册到容器中
 * 4.阶段4：BeanDefinition合并阶段
 * 5.阶段5：Bean Class加载阶段
 * 6.阶段6：Bean实例化阶段（2个小阶段）
 * Bean实例化前阶段
 * Bean实例化阶段
 * 7.阶段7：合并后的BeanDefinition处理
 * 8.阶段8：属性赋值阶段（3个小阶段）
 * Bean实例化后阶段
 * Bean属性赋值前阶段
 * Bean属性赋值阶段
 * 9.阶段9：Bean初始化阶段（5个小阶段）
 * Bean Aware接口回调阶段
 * Bean初始化前阶段
 * Bean初始化阶段
 * Bean初始化后阶段
 * 10.阶段10：所有单例bean初始化完成后阶段
 * 11.阶段11：Bean的使用阶段
 * 12.阶段12：Bean销毁前阶段
 * 13.阶段13：Bean销毁阶段
 */
public class BeanLifeCycleTest {
    /**
     * 阶段1：Bean元信息配置阶段
     * Bean信息定义的4种方式
     * 1.API的方式（其他的几种方式最终都会采用这种方式来定义bean配置信息）
     * 2.Xml文件方式
     * 3.properties文件的方式
     * 4.注解的方式
     *
     * Spring容器启动的过程种，会将Bean解析成Spring内部的BeanDefinition结构。
     * BeanDefinition内包含了bean定义的各种信息，如：bean对应的class、scope、lazy信息、dependsOn信息、autowiredCandidate(是否是候选对象)、primary(是否是主要的候选者)等信息。
     *
     */

    /**
     * BeanDefinition接口的实现类
     * 1.RootBeanDefinition类：表示根bean定义信息；通常bean中没有父bean的就使用这种表示
     * 2.ChildBeanDefinition类：表示子bean定义信息；如果需要指定父bean的，可以使用ChildBeanDefinition来定义子bean的配置信息，里面有个parentName属性，用来指定父bean的名称。
     * 3.GenericBeanDefinition类：通用的bean定义信息：既可以表示没有父bean的bean配置信息，也可以表示有父bean的子bean配置信息，这个类里面也有parentName属性，用来指定父bean的名称。
     * 4.ConfigurationClassBeanDefinition类：表示通过配置类中@Bean方法定义bean信息；可以通过配置类中使用@Bean来标注一些方法，通过这些方法来定义bean，这些方法配置的bean信息最后会转换为ConfigurationClassBeanDefinition类型的对象.
     * 5.AnnotatedBeanDefinition接口：表示通过注解的方式定义的bean信息，里面有个方法AnnotationMetadata getMetadata();用来获取定义这个bean的类上的所有注解信息.
     * 6.BeanDefinitionBuilder：构造BeanDefinition的工具；spring中为了方便操作BeanDefinition，提供了一个类：BeanDefinitionBuilder，内部提供了很多静态方法，通过这些方法可以非常方便的组装BeanDefinition对象。
     *
     */

    /**
     * 案例1：组装一个简单的bean
     */
    @Test
    public void test1() {
        //指定class
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Car.class.getName());
        /**
         * 等效于：
         * <bean class="...test1.Car" />
         */
        //获取BeanDefinition
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        System.out.println(beanDefinition);
        /**
         * Root bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         */
    }

    /**
     * 案例2：组装一个有属性的bean
     */
    @Test
    public void test2() {
        //指定class
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Car.class.getName());
        //设置普通类型属性
        beanDefinitionBuilder.addPropertyValue("name", "奥迪");//也可通过addPropertyReference添加属性参考引用其他bean
        //获取BeanDefinition
        BeanDefinition carBeanDefinition = beanDefinitionBuilder.getBeanDefinition();
        System.out.println(carBeanDefinition);

        //创建spring容器
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //调用registerBeanDefinition向容器中注册bean
        factory.registerBeanDefinition("car", carBeanDefinition);
        Car car = factory.getBean("car", Car.class);
        System.out.println(car);
        /**
         * Root bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         * Car{name='奥迪'}
         */
    }

    /**
     * 案例3：组装一个有依赖关系的bean
     */
    @Test
    public void test3() {
        BeanDefinitionBuilder carBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Car.class.getName());
        carBeanDefinitionBuilder.addPropertyValue("name", "yk");
        BeanDefinition carBeanDefinition = carBeanDefinitionBuilder.getBeanDefinition();

        BeanDefinitionBuilder userBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(User.class.getName());
        userBeanDefinitionBuilder.addPropertyValue("name", "jyx");
        userBeanDefinitionBuilder.addPropertyReference("car", "car");
        BeanDefinition userBeanDefinition = userBeanDefinitionBuilder.getBeanDefinition();

        //创建spring容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("car", carBeanDefinition);
        beanFactory.registerBeanDefinition("user", userBeanDefinition);
        System.out.println(beanFactory.getBean("car"));
        System.out.println(beanFactory.getBean("user"));

        /**
         * 上面的代码等效于：
         * <bean id="car" class="...test1.Car">
         * <property name="name" value="yk"/>
         * </bean>
         * <bean id="user" class="..test3.User">
         * <property name="name" value="jyx"/>
         * <property name="car" ref="car"/>
         * </bean>
         */
        /**
         * Car{name='yk'}
         * User{name='jyx', car=Car{name='yk'}}
         */
    }

    /**
     * 案例4：有父子关系的bean
     */
    @Test
    public void test4() {
        BeanDefinitionBuilder car1BeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Car.class.getName());
        car1BeanDefinitionBuilder.addPropertyValue("name", "奥迪");
        BeanDefinition car1BeanDefinition = car1BeanDefinitionBuilder.getBeanDefinition();

        BeanDefinitionBuilder car2BeanDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition(Car.class.getName());
        car2BeanDefinitionBuilder.setParentName("car1");
        BeanDefinition car2BeanDefinition = car2BeanDefinitionBuilder.getBeanDefinition();

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("car1", car1BeanDefinition);
        beanFactory.registerBeanDefinition("car2", car2BeanDefinition);

        System.out.println(beanFactory.getBean("car1"));
        System.out.println(beanFactory.getBean("car2"));
        /**
         * Car{name='奥迪'}
         * Car{name='奥迪'}
         */


        //等效于：


        BeanDefinitionBuilder car3BeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Car.class.getName());
        car3BeanDefinitionBuilder.addPropertyValue("name", "奥迪");
        BeanDefinition car3BeanDefinition = car3BeanDefinitionBuilder.getBeanDefinition();

        BeanDefinitionBuilder car4BeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
        car4BeanDefinitionBuilder.setParentName("car3");
        BeanDefinition car4BeanDefinition = car4BeanDefinitionBuilder.getBeanDefinition();

        DefaultListableBeanFactory beanFactory2 = new DefaultListableBeanFactory();
        beanFactory2.registerBeanDefinition("car3", car3BeanDefinition);
        beanFactory2.registerBeanDefinition("car4", car4BeanDefinition);

        System.out.println(beanFactory2.getBean("car3"));
        System.out.println(beanFactory2.getBean("car4"));
        /**
         * Car{name='奥迪'}
         * Car{name='奥迪'}
         */


        //等效于
        /**
         * <bean id="car1" class="...test1.Car">
         * <property name="name" value="奥迪"/>
         * </bean>
         * <bean id="car2" parent="car1" />
         */
    }

    /**
     * 案例5：通过xml文件设置属性
     */
    @Test
    public void test5() {
        //通过xml文件配置
        CPXUtil.printAllBean("us/fjj/spring/learning/beanlifecycle/test5/CompositeObjBeans.xml");
        /**
         * car1->Car{name='保时捷'}
         * car2->Car{name='保时捷'}
         * compositeObj->CompositeObj{name='coding', salary=7000, car1=Car{name='保时捷'}, stringList=[SSW1, SSW2, SSW3], carList=[Car{name='保时捷'}, Car{name='保时捷'}], stringSet=[YK1, YK2, YK3], carSet=[Car{name='保时捷'}, Car{name='保时捷'}], stringMap={JYX=YK, YK=JYX}, stringCarMap={YK=Car{name='保时捷'}, JYX=Car{name='保时捷'}}}
         */
    }

    /**
     * 案例6：通过api设置（Map、Set、List）属性
     */
    @Test
    public void test6() {
        //定义car1
        BeanDefinitionBuilder car1BeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(Car.class.getName());
        car1BeanDefinitionBuilder.addPropertyValue("name", "保斯捷");
        BeanDefinition car1 = car1BeanDefinitionBuilder.getBeanDefinition();

        //定义car2
        BeanDefinitionBuilder car2BeanDefinitionBuilder = BeanDefinitionBuilder.childBeanDefinition(Car.class.getName());
        car2BeanDefinitionBuilder.setParentName("car1");
        BeanDefinition car2 = car2BeanDefinitionBuilder.getBeanDefinition();

        ManagedList<String> stringList = new ManagedList<>();
        stringList.addAll(Arrays.asList("SSW1", "SSW2", "SSW3"));

        ManagedList<RuntimeBeanReference> carList = new ManagedList<>();
        carList.add(new RuntimeBeanReference("car1"));
        carList.add(new RuntimeBeanReference("car2"));

        ManagedSet<String> stringSet = new ManagedSet<>();
        stringSet.addAll(Arrays.asList("YK1", "YK2", "YK3"));

        ManagedSet<RuntimeBeanReference> carSet = new ManagedSet<>();
        carSet.add(new RuntimeBeanReference("car1"));
        carSet.add(new RuntimeBeanReference("car2"));

        ManagedMap<String, String> stringMap = new ManagedMap<>();
        stringMap.put("YK", "JYX");
        stringMap.put("JYX", "YK");

        ManagedMap<String, RuntimeBeanReference> stringCarMap = new ManagedMap<>();
        stringCarMap.put("YK", new RuntimeBeanReference("car1"));
        stringCarMap.put("JYX", new RuntimeBeanReference("car2"));


        //使用原生api来创建BeanDefinition
        GenericBeanDefinition compositeObj = new GenericBeanDefinition();
        compositeObj.setBeanClassName(CompositeObj.class.getName());
        compositeObj.getPropertyValues()
                .add("name", "coding")
                .add("salary", 8000)
                .add("carList", carList)
                .add("stringList", stringList)
                .add("car1", new RuntimeBeanReference("car1"))
                .add("carSet", carSet)
                .add("stringSet", stringSet)
                .add("stringCarMap", stringCarMap)
                .add("stringMap", stringMap);


        //将上面的bean注册到容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("car1", car1);
        beanFactory.registerBeanDefinition("car2", car2);
        beanFactory.registerBeanDefinition("compositeObj", compositeObj);

        //下面我们将容器中所有的bean输出
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, beanFactory.getBean(beanName)));
        }
        /**
         * car1->Car{name='保斯捷'}
         * car2->Car{name='保斯捷'}
         * compositeObj->CompositeObj{name='coding', salary=8000, car1=Car{name='保斯捷'}, stringList=[SSW1, SSW2, SSW3], carList=[Car{name='保斯捷'}, Car{name='保斯捷'}], stringSet=[YK1, YK2, YK3], carSet=[Car{name='保斯捷'}, Car{name='保斯捷'}], stringMap={YK=JYX, JYX=YK}, stringCarMap={YK=Car{name='保斯捷'}, JYX=Car{name='保斯捷'}}}
         */
        /**
         * 其中：
         * RuntimeBeanReference: 用来表示bean引用类型，类似于xml中的ref
         * ManagedList: 属性如果是List类型的，需要用到这个类进行操作，这个类继承了ArrayList
         * ManagedSet: 属性如果是Set类型的，需要用到这个类进行操作，这个类继承了LinkedHashSet
         * ManagedMap: 属性如果是Map类型的，需要用到这个类进行操作，这个类继承了LinkedHashMap
         */
    }

    /**
     * 案例7：properties文件方式
     * 将bran定义信息放在properties文件中，然后通过解析器将配置信息解析为BeanDefinition对象。
     */
    @Test
    public void test7() {
        //代码有问题，参考案例9
        /**
         * java.lang.IllegalStateException: org.springframework.context.support.GenericApplicationContext@4b5189ac has not been refreshed yet
         *
         * 	at org.springframework.context.support.AbstractApplicationContext.assertBeanFactoryActive(AbstractApplicationContext.java:1095)
         * 	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1107)
         * 	at us.fjj.spring.learning.beanlifecycle.BeanLifeCycleTest.test7(BeanLifeCycleTest.java:309)
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
        GenericApplicationContext gac = new GenericApplicationContext();
        Resource res = new ClassPathResource("us/fjj/spring/learning/beanlifecycle/test7/testObjBeans.properties");
        PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(gac);
        propReader.loadBeanDefinitions(res);
        Service1 service1 = (Service1) gac.getBean("service1");
        Service2 service2 = (Service2) gac.getBean("service2");
        System.out.println("service1->" + service1);
        System.out.println("service2->" + service2);
    }

    /**
     * 阶段2：Bean元信息解析阶段
     * Bean元信息的解析就是将各种方式定义的bean配置信息解析为BeanDefinition对象。
     *
     * Bean元信息的解析主要有3种方式
     * 1.xml文件定义bean的解析
     * 2.properties文件定义bean的解析
     * 3.注解方式定义bean的解析
     */

    /**
     * 案例8: XML方式解析（XmlBeanDefinitionReader）
     * spring中提供了一个类XmlBeanDefinitionReader，将xml中定义的bean解析为BeanDefinition对象。
     */
    @Test
    public void test8() {
        //定义一个spring容器，这个容器默认实现了BeanDefinitionRegistry，所以本身就是一个bean注册器
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //定义一个xml的BeanDefinition读取器，需要传递一个BeanDefinitionRegistry(bean注册器)对象
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);

        //指定bean xml配置文件的位置
        String location = "classpath:/us/fjj/spring/learning/beanlifecycle/test8/beans.xml";
        //通过XmlBeanDefinitionReader加载bean xml文件，然后将解析产生的BeanDefinition注册到容器中
        int countBean = xmlBeanDefinitionReader.loadBeanDefinitions(location);
        System.out.println(String.format("共注册了%s个bean", countBean));

        //打印出注册的bean的配置信息
        for (String beanName :
                factory.getBeanDefinitionNames()) {
            //通过名称从容器中获取对应的BeanDefinition信息
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            //获取BeanDefinition具体使用的是哪个类
            String beanDefinitionClassName = beanDefinition.getClass().getName();
            //通过名称获取bean对象
            Object bean = factory.getBean(beanName);
            //打印输出
            System.out.println(beanName + ":");
            System.out.println("    beanDefinitionClassName: " + beanDefinitionClassName);
            System.out.println("    beanDefinition: " + beanDefinition);
            System.out.println("    bean: " + bean);
        }
        /**
         * 共注册了4个bean
         * car:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test8/beans.xml]
         *     bean: Car{name='保时捷'}
         * car1:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test8/beans.xml]
         *     bean: Car{name='奥迪'}
         * car2:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean with parent 'car1': class [null]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test8/beans.xml]
         *     bean: Car{name='奥迪'}
         * user:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test3.User]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test8/beans.xml]
         *     bean: User{name='YK', car=Car{name='奥迪'}}
         *
         * Process finished with exit code 0
         */
    }

    /**
     * 案例9：将beans.properties文件解析为BeanDefinition对象
     */
    @Test
    public void test9() {
        //定义一个spring容器，这个容器默认是实现了BeanDefinitionRegistry，所以本身就行一个bean注册器
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //定义一个properties的BeanDefinition读取器，需要传递一个BeanDefinitionRegistry(bean注册器)对象
        PropertiesBeanDefinitionReader propertiesBeanDefinitionReader = new PropertiesBeanDefinitionReader(factory);

        //指定bean xml配置文件的位置
        String location = "classpath:/us/fjj/spring/learning/beanlifecycle/test9/beans.properties";
        //通过PropertiesBeanDefinitionReader加载bean properties文件，然后将解析产生的BeanDefinition注册到容器中
        int countBean = propertiesBeanDefinitionReader.loadBeanDefinitions(location);
        System.out.println(String.format("共注册了%s个bean", countBean));

        //打印出注册的bean的配置信息
        for (String beanName :
                factory.getBeanDefinitionNames()) {
            //通过名称从容器中获取对应的BeanDefinition信息
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            //获取BeanDefinition具体使用的类
            String beanDefinitionClassName = beanDefinition.getClass().getName();
            //通过名称获取bean对象
            Object bean = factory.getBean(beanName);
            //打印输出
            System.out.println(beanName + ":");
            System.out.println("    beanDefinitionClassName: " + beanDefinitionClassName);
            System.out.println("    beanDefinition: " + beanDefinition);
            System.out.println("    bean: " + bean);
        }
        /**
         * 共注册了4个bean
         * car:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: Car{name='???'}
         * user:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test3.User]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: User{name='YK', car=Car{name='??'}}
         * car1:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test1.Car]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: Car{name='??'}
         * car2:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
         *     beanDefinition: Generic bean with parent 'car1': class [null]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: Car{name='??'}
         */
    }

    /**
     * 案例10：注解方式：PropertiesBeanDefinitionReader
     * 注解的方式定义的bean,需要使用PropertiesBeanDefnitionReader这个类来进行解析，方式和上面2种方式类似
     */
    @Test
    public void test10() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(factory);
        annotatedBeanDefinitionReader.register(us.fjj.spring.learning.beanlifecycle.test10.Service1.class, us.fjj.spring.learning.beanlifecycle.test10.Service2.class);
        for (String beanName :
                factory.getBeanDefinitionNames()) {
            //通过名称从容器中获取对应的BeanDefinition信息
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            //获取BeanDefinition具体使用的类
            String beanDefinitionClassName = beanDefinition.getClass().getName();
            //通过名称获取bean对象
            Object bean = factory.getBean(beanName);
            //打印输出
            System.out.println(beanName + ":");
            System.out.println("    beanDefinitionClassName: " + beanDefinitionClassName);
            System.out.println("    beanDefinition: " + beanDefinition);
            System.out.println("    bean: " + bean);
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.annotation.ConfigurationClassPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.annotation.ConfigurationClassPostProcessor@535779e4
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@561868a0
         * org.springframework.context.annotation.internalCommonAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.annotation.CommonAnnotationBeanPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@5b1ebf56
         * org.springframework.context.event.internalEventListenerProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.event.EventListenerMethodProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.event.EventListenerMethodProcessor@512535ff
         * org.springframework.context.event.internalEventListenerFactory:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.event.DefaultEventListenerFactory]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.event.DefaultEventListenerFactory@3f270e0a
         * service1:
         *     beanDefinitionClassName: org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test10.Service1]; scope=prototype; abstract=false; lazyInit=true; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=true; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: us.fjj.spring.learning.beanlifecycle.test10.Service1@1a760689
         * service2:
         *     beanDefinitionClassName: org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test10.Service2]; scope=singleton; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: Service2{service1=null}
         */
        //可以看到service2中service1的值为null，添加@1后有值（具体原因在阅读完成之后就一目了然了）
        DefaultListableBeanFactory factory2 = new DefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader2 = new AnnotatedBeanDefinitionReader(factory2);
        annotatedBeanDefinitionReader2.register(us.fjj.spring.learning.beanlifecycle.test10.Service1.class, us.fjj.spring.learning.beanlifecycle.test10.Service2.class);
        factory2.getBeansOfType(BeanPostProcessor.class).values().forEach(factory2::addBeanPostProcessor);//@1
        for (String beanName :
                factory2.getBeanDefinitionNames()) {
            //通过名称从容器中获取对应的BeanDefinition信息
            BeanDefinition beanDefinition = factory2.getBeanDefinition(beanName);
            //获取BeanDefinition具体使用的类
            String beanDefinitionClassName = beanDefinition.getClass().getName();
            //通过名称获取bean对象
            Object bean = factory2.getBean(beanName);
            //打印输出
            System.out.println(beanName + ":");
            System.out.println("    beanDefinitionClassName: " + beanDefinitionClassName);
            System.out.println("    beanDefinition: " + beanDefinition);
            System.out.println("    bean: " + bean);
        }
        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.annotation.ConfigurationClassPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.annotation.ConfigurationClassPostProcessor@6986bbaf
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@4879dfad
         * org.springframework.context.annotation.internalCommonAnnotationProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.annotation.CommonAnnotationBeanPostProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@4758820d
         * org.springframework.context.event.internalEventListenerProcessor:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.event.EventListenerMethodProcessor]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.event.EventListenerMethodProcessor@62727399
         * org.springframework.context.event.internalEventListenerFactory:
         *     beanDefinitionClassName: org.springframework.beans.factory.support.RootBeanDefinition
         *     beanDefinition: Root bean: class [org.springframework.context.event.DefaultEventListenerFactory]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: org.springframework.context.event.DefaultEventListenerFactory@2654635
         * service1:
         *     beanDefinitionClassName: org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test10.Service1]; scope=prototype; abstract=false; lazyInit=true; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=true; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: us.fjj.spring.learning.beanlifecycle.test10.Service1@737a135b
         * service2:
         *     beanDefinitionClassName: org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition
         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test10.Service2]; scope=singleton; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         *     bean: Service2{service1=us.fjj.spring.learning.beanlifecycle.test10.Service1@1e0f9063}
         */
    }

    /**
     * 阶段3：Spring Bean注册阶段
     * bean注册阶段需要用到一个非常重要的接口：BeanDefinitionRegistry
     *
     */
}
