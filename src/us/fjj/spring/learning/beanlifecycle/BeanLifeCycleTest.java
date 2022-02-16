package us.fjj.spring.learning.beanlifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.*;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import us.fjj.spring.learning.beanlifecycle.test1.Car;
import us.fjj.spring.learning.beanlifecycle.test14.MySmartInstantiationAwareBeanPostProcessor;
import us.fjj.spring.learning.beanlifecycle.test14.Person;
import us.fjj.spring.learning.beanlifecycle.test15.UserModel;
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
    /**
     * BeanDefinitionRegistry唯一实现：DefaultListableBeanFactory
     * spring中BeanDefinitionRegistry接口有一个唯一的实现类：
     * org.springframework.beans.factory.support.DefaultListableBeanFactory
     * 大家可能看到了很多类也实现了BeanDefinitionRegistry接口，比如我们经常使用到的AnnotationConfigApplicationContext，但实际上
     * 其内部是转发给了DefaultListableBeanFactory进行处理的，所以真正实现这个接口的类是DefaultListableBeanFactory。
     */
    /**
     * 案例11: BeanDefinitionRegistry 案例
     */
    @Test
    public void test11() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        //定义一个bean
        GenericBeanDefinition nameBdf = new GenericBeanDefinition();
        nameBdf.setBeanClass(String.class);
        nameBdf.getConstructorArgumentValues().addIndexedArgumentValue(0, "JYX");

        //将bean注册到容器中
        factory.registerBeanDefinition("name", nameBdf);

        //通过名称获取BeanDefinition
        System.out.println(factory.getBeanDefinition("name"));
        //通过名称判断是否注册过BeanDefinition
        System.out.println(factory.containsBeanDefinition("name"));
        //获取所有注册的名称
        System.out.println(Arrays.asList(factory.getBeanDefinitionNames()));
        //判断指定的name是否使用过
        System.out.println(factory.isBeanNameInUse("name"));

        //别名相关方法
        //为name注册2个别名
        factory.registerAlias("name", "alias-name-1");
        factory.registerAlias("name", "alias-name-2");
        //判断alias-name-1是否已被作为别名使用
        System.out.println(factory.isAlias("alias-name-1"));
        //通过名称获取对应的所有别名
        System.out.println(Arrays.asList(factory.getAliases("name")));

        //最后我们再获取一下这个bean
        System.out.println(factory.getBean("name"));

        /**
         * Generic bean: class [java.lang.String]; scope=; abstract=false; lazyInit=null; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null
         * true
         * [name]
         * true
         * true
         * [alias-name-2, alias-name-1]
         * JYX
         */
    }
    /**
     * 阶段4到阶段14（从BeanDefinition合并阶段到Bean初始化完成阶段）都是在调用getBean从容器中获取bean对象的过程中发送的操作，需要仔细看。
     */
    /**
     * 阶段4：BeanDefinition合并阶段
     * <p>
     * 合并阶段：有时我们定义bean的时候有父子bean关系，此时子BeanDefinition中的信息是不完整的，比如设置属性的时候配置在父BeanDefinition中，此时子BeanDefinition中是没有这些信息的，需要将子bean的BeanDefinition和父bean的BeanDefinition进行合并,
     * 得到最终的一个RootBeanDefinition，合并之后得到的RootBeanDefinition包含bean定义的所有信息，包含了从父bean中继承过来的所有信息，后续bean的所有创建工作就是依靠合并之后BeanDefinition来进行的.
     * bean定义可能存在多级父子关系，合并的时候进行递归合并，最终得到一个包含完整信息的RootBeanDefinition。
     */
    @Test
    public void test12() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(factory);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:/us/fjj/spring/learning/beanlifecycle/test12/beans.xml");
        for (String beanName :
                factory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = factory.getBeanDefinition(beanName);
            BeanDefinition mergedBeanDefinition = factory.getMergedBeanDefinition(beanName);
            String beanDefinitionClassName = beanDefinition.getClass().getName();
            Object bean = factory.getBean(beanName);
            System.out.println(beanName + ":");
            System.out.println("    beanDefinitionClassName: " + beanDefinitionClassName);
            System.out.println("    beanDefinition: " + beanDefinition);
            System.out.println("    mergedBeanDefinition: " + mergedBeanDefinition);
            System.out.println("    bean: " + bean);
        }
//        /**
//         * lesson1:
//         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
//         *     beanDefinition: Generic bean: class [us.fjj.spring.learning.beanlifecycle.test12.LessonModel]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     mergedBeanDefinition: Root bean: class [us.fjj.spring.learning.beanlifecycle.test12.LessonModel]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     bean: LessonModel{name='论如何锤爆想和', lessonCount=100, description='作者：老K'}
//         * lesson2:
//         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
//         *     beanDefinition: Generic bean with parent 'lesson1': class [null]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     mergedBeanDefinition: Root bean: class [us.fjj.spring.learning.beanlifecycle.test12.LessonModel]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     bean: LessonModel{name='打爆老凯', lessonCount=100, description='作者：老K'}
//         * lesson3:
//         *     beanDefinitionClassName: org.springframework.beans.factory.support.GenericBeanDefinition
//         *     beanDefinition: Generic bean with parent 'lesson2': class [null]; scope=; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     mergedBeanDefinition: Root bean: class [us.fjj.spring.learning.beanlifecycle.test12.LessonModel]; scope=singleton; abstract=false; lazyInit=false; autowireMode=0; dependencyCheck=0; autowireCandidate=true; primary=false; factoryBeanName=null; factoryMethodName=null; initMethodName=null; destroyMethodName=null; defined in class path resource [us/fjj/spring/learning/beanlifecycle/test12/beans.xml]
//         *     bean: LessonModel{name='打爆老凯', lessonCount=99, description='作者：老K'}
//         *
//         */
//        /**
//         * 从输出中可以看到，合并之前，BeanDefinition是不完整的，比如lesson2和lesson3中的class是null，属性信息也不完整，但是在合并之后这些信息就都完整了。
//         * 合并之前是GenericBeanDefinition类型，合并之后得到的是RootBeanDefinition类型的。
//         * 获取lesson3合并的BeanDefinition时，内部会递归进行合并，先将lesson1和lesson2合并，然后将lesson2在和lesson3合并，最后得到合并之后的BeanDefinition。
//         * 后面的阶段将使用合并产生的RootBeanDefinition。
//         * /
//
    }
//        /**
//         * 阶段5: Bean Class加载阶段
//         * 将bean的class名称转换为class类型的对象
//         * BeanDefinition中有个Object类型的字段：beanClass
//         *        private volatile Object beanClass;
//         * 用来表示bean的class对象，通常这个字段的值有2种类型，一种是bean对应的Class类型的对象，另一种是bean对应的Class的完整类名，第一种情况不需要解析，第二种情况：即这个字段是bean的类名的时候，就需要通过类加载器将其转换为一个Class对象。
//         * 此时会对阶段4合并产生的RootBeanDefinition中的beanClass进行解析，将bean的类名转换为Class对象，然后赋值给beanClass。
//         * 源码位于：org.springframework.beanas.factory.support.AbstractBeanFactory#resolveBeanClass
//         * 上面得到了Bean Class对象以及合并之后的BeanDefinition，下面就开始进入实例化这个对象的阶段了。
//         *
//         * Bean实例化分为3个阶段：前阶段、实例化阶段、后阶段
//         * /
    /**
     * 阶段6：Bean实例化阶段
     * 分为2个小阶段
     * 1.bean实例化前操作
     * 2.bean实例化操作
     */

    /**
     * Bean实例化前操作
     * DefaultListableBeanFactory有个非常重要的字段：
     * private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
     * 是一个BeanPostProcessor类型的集合
     * BeanPostProcessor是一个接口，还有很多子接口，这些接口中提供了很多方法，spring在bean生命周期的不同阶段，会调用上面这个列表中的BeanPostProcessor中的一些方法，来对生命周期进行拓展，
     * bean生命周期中的所有拓展点都是依靠这个集合中的BeanPostProcessor来实现的，所以如果大家想对bean的声明周期进行干预，这块需要掌握好
     *
     */
    /**
     * 案例13： 利用postProcessBeforeInstantiation进行替换bean（在这个方法中直接返回一个bean的一个实例）
     * 原理：bean初始化前阶段，会调用{@link org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessBeforeInitialization(Object, String)}
     */
    @Test
    public void test13() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //添加一个BeanPostProcessor: InstantiationAwareBeanPostProcessor
        factory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessor() {
            @Override
            public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
                System.out.println("调用postProcessBeforeInstantiation()方法");
                //发现类型是Car类型的时候，硬编码创建一个Car对象返回
                if (beanClass == Car.class) {
                    Car car = new Car();
                    car.setName("保时捷");
                    return car;
                }
                return null;//当返回null时胡继续执行下一阶段
            }
        });
        //定义一个car bean
        AbstractBeanDefinition carBeanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(Car.class)
                .addPropertyValue("name", "奥迪")
                .getBeanDefinition();
        factory.registerBeanDefinition("car", carBeanDefinition);
        //从容器中获取car这个bean的实例
        System.out.println(factory.getBean("car"));
        /**
         * 调用postProcessBeforeInstantiation()方法
         * Car{name='保时捷'}
         */
        /**
         * 定义的和输出的不一致的原因是因为：我们在创建InstatiationAwareBeanPostProcessor@postProcessBeforeInstantiation方法中手动创建了一个实例直接返回了，而不是依靠spring内部去创建这个实例。
         *
         *
         * 小结：
         * 实际上，在实例化前阶段对bean的创建进行干预的情况，用的非常少，所以大部分bean的创建还是会继续走下面的阶段。
         */
    }

    /**
     * Bean实例化操作
     * 这个过程会通过反射来调用bean的构造器来创建bean的实例。
     * 具体需要使用哪个构造器，spring为开发者提供了一个接口，允许开发者自己来判断用哪个构造器。
     *
     */
    /**
     * 案例14：自定义一个注解，当构造器被这个注解标注的时候，让spring自动选择使用这个构造器创建对象。
     * <p>
     * 通过{@link org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors(Class, String)}来确定使用哪个构造器来创建bean实例
     */
    @Test
    public void test14() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        //创建一个SmartInstantiationAwareBeanPostProcessor，将其添加到容器中
        factory.addBeanPostProcessor(new MySmartInstantiationAwareBeanPostProcessor());
        factory.registerBeanDefinition("name", BeanDefinitionBuilder
                .genericBeanDefinition(String.class)
                .addConstructorArgValue("JYX")
                .getBeanDefinition());
        factory.registerBeanDefinition("age", BeanDefinitionBuilder
                .genericBeanDefinition(Integer.class)
                .addConstructorArgValue(30)
                .getBeanDefinition());
        factory.registerBeanDefinition("person", BeanDefinitionBuilder
                .genericBeanDefinition(Person.class)
                .getBeanDefinition());

        Person person = factory.getBean("person", Person.class);
        System.out.println(person);
        /**
         * class us.fjj.spring.learning.beanlifecycle.test14.Person
         * 调用MySmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors方法
         * class java.lang.String
         * 调用MySmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors方法
         * 调用了Person(String name)
         * Person{name='JYX', age=null}
         */
        /*
        从输出中可以看出调用了Person中标注@MyAutowired标注的构造器。
         */
    }

    /**
     * 阶段7：合并后的BeanDefinition处理
     */

    /**
     * postProcessMergedBeanDefinition有2个实现类：
     * 1.org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
     * 在postProcessMergedBeanDefinition方法中对@Autowired、@Value标注的方法、字段进行缓存
     * 2.org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
     * 在postProcessMergedBeanDefinition方法中对@Resource标注的字段、@Resource标注的方法、@PostConstruct标注的字段、@PreDestroy标注的方法进行缓存
     *
     */

    /**
     * 阶段8：Bean属性设置阶段
     *
     * 属性设置阶段分为3个小的阶段
     * 1.实例化后阶段
     * 2.Bean属性赋值前处理
     * 3.Bean属性赋值
     */

    /**
     * 实例化后阶段
     * 会调用InstantiationAwareBeanPostProcessor接口的postProcessAfterInstantiation这个方法。
     * postProcessAfterInstantiation方法返回false的时候，后续的Bean属性赋值前处理、Bean属性赋值都会被跳过。
     */
    @Test
    public void test15() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();


        //设置跳过user1的属性赋值
        factory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessor() {
            @Override
            public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
                return !"user1".equals(beanName);
            }
        });

        factory.registerBeanDefinition("user1", BeanDefinitionBuilder
                .genericBeanDefinition(UserModel.class)
                .addPropertyValue("name", "YK")
                .addPropertyValue("age", 30)
                .getBeanDefinition());

        factory.registerBeanDefinition("user2", BeanDefinitionBuilder
                .genericBeanDefinition(UserModel.class)
                .addPropertyValue("name", "JYX")
                .addPropertyValue("age", 31)
                .getBeanDefinition());

        for (String beanName :
                factory.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, factory.getBean(beanName)));
        }
        /**
         * user1->UserModel{name='null', age=null}
         * user2->UserModel{name='JYX', age=31}
         */
    }

    /**
     * Bean属性赋值前阶段
     * 这个阶段会调用InstantiationAwareBeanPostProcessor接口的postProcessProperties方法。
     * 可以通过postProcessProperties参数中的PropertyValues保存了bean实例对象中所有属性值的设置，所以我们可以在这个方法中对PropertyValues值进行修改。
     *
     * 这个方法有两个比较重要的实现类
     * 1.AutowiredAnnotationBeanPostProcessor在这个方法中对@Autowired、@Value标注的字段、方法注入值。
     * 2.CommonAnnotationBeanPostProcessor在这个方法中对@Resource标注的字段和方法注入值。
     */

    /**
     * 案例16
     */
    @Test
    public void test16() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        factory.addBeanPostProcessor(new InstantiationAwareBeanPostProcessor() {
            /**
             * 对user1这个bean的属性值进行修改
             * @param pvs
             * @param bean
             * @param beanName
             * @return
             * @throws BeansException
             */
            @Override
            public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
                if ("user1".equals(beanName)) {
//                    if (pvs == null) {
//                        pvs = new MutablePropertyValues();
//                    }
                    if (pvs instanceof MutablePropertyValues) {
                        MutablePropertyValues mpvs = (MutablePropertyValues) pvs;
                        mpvs.add("name", "JYX");
                        mpvs.add("age", 99);
                    }
                }
                return null;
            }
        });

        //注意user1 没有给属性设置值
        factory.registerBeanDefinition("user1",
                BeanDefinitionBuilder
                        .genericBeanDefinition(UserModel.class)
                        .getBeanDefinition());
        factory.registerBeanDefinition("user2",
                BeanDefinitionBuilder
                        .genericBeanDefinition(UserModel.class)
                        .addPropertyValue("name", "YK")
                        .addPropertyValue("age", 1000)
                        .getBeanDefinition());

        for (String beanName :
                factory.getBeanDefinitionNames()) {
            System.out.println(String.format("%s->%s", beanName, factory.getBean(beanName)));
        }

        /**
         *user1->UserModel{name='JYX', age=99}
         * user2->UserModel{name='YK', age=1000}
         */
    }

    /**
     * Bean属性赋值阶段
     * 循环处理PropertyValues中的属性值信息，通过反射调用set方法将属性的值设置到bean实例中。
     * PropertyValues中的值是通过bean xml中的property元素配置的，或者调用MutablePropertyValues中add方法设置的值。
     */
}
