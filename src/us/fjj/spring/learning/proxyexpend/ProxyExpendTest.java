package us.fjj.spring.learning.proxyexpend;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.DefaultNamingPolicy;
import org.springframework.cglib.core.Predicate;
import org.springframework.cglib.proxy.*;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import us.fjj.spring.learning.proxyexpend.cglib.test1.TwoInterfaceCglibTest;
import us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel;
import us.fjj.spring.learning.proxyexpend.cglib.test4.BlogModel;
import us.fjj.spring.learning.proxyexpend.cglib.test6.DefaultMethodInfo;
import us.fjj.spring.learning.proxyexpend.cglib.test6.IMethodInfo;
import us.fjj.spring.learning.proxyexpend.jdkproxy.CostTimeInvocationHandler;
import us.fjj.spring.learning.proxyexpend.jdkproxy.IService1;
import us.fjj.spring.learning.proxyexpend.jdkproxy.IService2;
import us.fjj.spring.learning.proxyexpend.jdkproxy.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jijiao
 */
public class ProxyExpendTest {
    @Test
    public void test1() {
        Service service = new Service();
        Object obj = Proxy.newProxyInstance(
                service.getClass().getClassLoader(),
                new Class[]{
                        IService1.class,
                        IService2.class
                },
                new CostTimeInvocationHandler(service)
        );
        IService1 i1 = (IService1) obj;
        i1.m1();
        IService2 i2 = (IService2) obj;
        i2.m2();
        System.out.println(String.format("obj instanceof Service: %b", obj instanceof Service));
        System.out.println(String.format("obj instanceof IService1: %b", obj instanceof IService1));
        System.out.println(String.format("obj instanceof IService2: %b", obj instanceof IService2));
        System.out.println(String.format("proxy type: %s", obj));
        /**
         * m1 start
         * Service的m1方法
         * m1 end
         * m1 -> total cost time: 168100ns
         * m2 start
         * Service的m2方法
         * m2 end
         * m2 -> total cost time: 239200ns
         * obj instanceof Service: false
         * obj instanceof IService1: true
         * obj instanceof IService2: true
         * toString start
         * toString end
         * toString -> total cost time: 85500ns
         * proxy type: us.fjj.spring.learning.proxyexpend.jdkproxy.Service@b7f23d9
         */
        /**
         * 因jdkproxy生成的代理是Proxy的子类，故该对象不是Service的子类
         */
    }
    /**
     * cglib
     * final类型修饰的类不能被继承，final修饰的方法不能被重写，static修饰的方法也不能被重写，private修饰的方法也不能被子类重写，而其他类型的方法都可以被子类重写
     *
     * cglib整个过程如下:
     * 1.cglib根据父类，Callback, Filter及一些相关信息生成key
     * 2.然后根据key生成对应的子类的二进制表现形式
     * 3.使用ClassLoader装载对应的二进制，生成Class对象，并缓存
     * 4.最后实例化Class对象，并缓存
     */
    /**
     * 案例1：为多个接口创建代理
     *
     * @see TwoInterfaceCglibTest#test()
     */
    @Test
    public void test2() {
        //去除important下那一行后报错
    }

    /**
     * 案例2：为类和接口同时创建代理
     */
    @Test
    public void test3() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(us.fjj.spring.learning.proxyexpend.cglib.test2.Service.class);
        enhancer.setInterfaces(new Class[]{us.fjj.spring.learning.proxyexpend.cglib.test2.IService1.class, us.fjj.spring.learning.proxyexpend.cglib.test2.IService2.class});
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("我来自MethodInterceptor!");
                Object result = methodProxy.invokeSuper(o, objects);
                return result;
            }
        });

        Object obj = enhancer.create();
        if (obj instanceof us.fjj.spring.learning.proxyexpend.cglib.test2.Service) {
            System.out.println("obj instanceof Service!");
        }
        if (obj instanceof us.fjj.spring.learning.proxyexpend.cglib.test2.IService1) {
            System.out.println("obj instanceof IService1!");
        }
        if (obj instanceof us.fjj.spring.learning.proxyexpend.cglib.test2.IService2) {
            System.out.println("obj instanceof IService2!");
        }
        System.out.println("类名：" + obj.getClass());
        System.out.println("实现了如下的接口：");
        Class<?>[] ints = obj.getClass().getInterfaces();
        for (Class<?> i :
                ints) {
            System.out.println(i);
        }
        /**
         * obj instanceof Service!
         * obj instanceof IService1!
         * obj instanceof IService2!
         * 类名：class us.fjj.spring.learning.proxyexpend.cglib.test2.Service$$EnhancerByCGLIB$$62df7a25
         * 实现了如下的接口：
         * interface us.fjj.spring.learning.proxyexpend.cglib.test2.IService1
         * interface us.fjj.spring.learning.proxyexpend.cglib.test2.IService2
         * interface org.springframework.cglib.proxy.Factory
         *
         * Process finished with exit code 0
         */
    }

    /**
     * 案例3：LazyLoader的使用
     * LazyLoader是cglib用于实现懒加载的callback。当被增强bean的方法初次被调用时，会触发回调，之后每次再进行方法调用都是对LazyLoader第一次返回的bean的调用，hibernate延迟加载有用到过这个。
     */
    @Test
    public void test4() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel.class);
        LazyLoader lazyLoader = new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("执行了lazyLoader.loadObject方法。");
                return new us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel("yk");
            }
        };
        enhancer.setCallback(lazyLoader);
        Object obj = enhancer.create();
        System.out.println("第一次调用say");
        ((us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel) obj).say();
        System.out.println("第一次调用say");
        ((us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel) obj).say();
        System.out.println(((UserModel) obj).toString());
        /**
         *第一次调用say
         * 执行了lazyLoader.loadObject方法。
         * yk say: hello!第一次调用say
         * yk say: hello!
         */
        /**
         * 当第一次调用say方法的时候，会被cglib拦截，进入lazyLoader的loadObject中，将这个方法的返回值作为say方法的调用者，loadObject中返回一个hello的UserModel，cglib内部会将loadObject方法的返回值和say方法关联起来，然后缓存起来，而第二次调用say方法的时候，通过方法名去缓存中找，会直接拿到第一次返回的UserModel，所以第二次不会再进入到loadObject方法中。
         */
    }

    /**
     * 案例4：LazyLoader实现延迟加载
     * 博客的内容一般比较多，西药用到内容的时候，我们再去加载，下面我们来模拟博客内容延迟加载的效果。
     */
    @Test
    public void test5() {
        //创建博客对象
        BlogModel blogModel = new BlogModel();
        System.out.println(blogModel.getTitle());
        System.out.println("博客内容：");
        System.out.println(blogModel.getBlogContentModel().getContent());
        System.out.println(blogModel.getBlogContentModel().getContent());
        /**
         * spring aop详解！
         * 博客内容：
         * 开始从数据库中获取博客内容...
         * lxh yyds!
         * lxh yyds!
         */
    }

    /**
     * 案例5: Dispatcher
     * Dispatcher和LazyLoader作用很相似，区别是用Dispatcher的话每次对增强bean进行方法调用都会触发回调。
     */
    @Test
    public void test6() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel.class);
        Dispatcher dispatcher = new Dispatcher() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("调用了Dispatcher.loadObject方法");
                return new us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel(System.currentTimeMillis() + "");
            }
        };
        enhancer.setCallback(dispatcher);
        Object obj = enhancer.create();
        System.out.println("第一次调用say方法");
        ((us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel) obj).say();
        System.out.println("第一次调用say方法");
        ((us.fjj.spring.learning.proxyexpend.cglib.test3.UserModel) obj).say();
        /**
         * 第一次调用say方法
         * 调用了Dispatcher.loadObject方法
         * 1645775909742 say: hello!
         * 第一次调用say方法
         * 调用了Dispatcher.loadObject方法
         * 1645775909742 say: hello!
         */
    }

    /**
     * 案例6：通过Dispatcher对类拓展一些接口
     * 下面有个UserService类，我们需要对这个类创建一个代理。
     * 代码中还定义了一个接口：IMethodInfo，用来统计被代理类的一些方法，有个实现类：DefaultMethodInfo。
     * 通过cglib创建一个代理类，父类为UserService，并且实现IMethodInfo接口，将接口所有的方法转发给DefaultMethodInfo处理，代理类中的其他方法，转发给其父类UserService处理。
     * <p>
     * 相当于对UserService这个类进行了增强，使其具有了IMethodInfo接口中的功能。
     */
    @Test
    public void test7() {
        Class<?> targetClass = us.fjj.spring.learning.proxyexpend.cglib.test6.UserService.class;
        Enhancer enhancer = new Enhancer();
        //设置代理的父类
        enhancer.setSuperclass(targetClass);
        //设置代理需要使用的接口列表
        enhancer.setInterfaces(new Class[]{IMethodInfo.class});
        //创建一个DefaultMethodInfo实例
        IMethodInfo methodInfo = new DefaultMethodInfo(targetClass);
        //创建调用器列表，此处定义了2个，第一个用来处理UserService中所有的方法，第二个用来处理IMethodInfo接口中的方法
        Callback[] callbacks = {
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        return methodProxy.invokeSuper(o, objects);
                    }
                },
                new Dispatcher() {
                    @Override
                    public Object loadObject() throws Exception {
                        /**
                         * 用来处理代理对象中IMethodInfo接口中的所有方法
                         * 所以此处返回的为IMethodInfo类型的对象
                         * 将由这个对象来处理代理对象中IMethodInfo接口中的所有方法
                         */
                        return methodInfo;
                    }
                }
        };
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                //.getDeclaringClass()获取声明类（接口或成员所在的类），否则返回null
                return method.getDeclaringClass() == IMethodInfo.class ? 1 : 0;
            }
        });
        Object obj = enhancer.create();

        //代理类的父类是UserService
        ((us.fjj.spring.learning.proxyexpend.cglib.test6.UserService) obj).add();
        ((us.fjj.spring.learning.proxyexpend.cglib.test6.UserService) obj).update();
        //代理类实现类IMethodInfo接口
        System.out.println(((IMethodInfo) obj).methodCount());
        System.out.println(((IMethodInfo) obj).methodNames());
        /**
         * 新增用户
         * 更新用户信息
         * 2
         * [add, update]
         */
    }

    /**
     * 案例7：cglib中的NamingPolicy接口
     * 接口NamingPolicy表示生成代理类的名字的策略，通过Enhancer.setNamingPolicy方法设置命名策略。
     * 默认的实现类：DefaultNamingPolicy，具体cglib动态生成类的命名控制。
     * DefaultNamingPolicy中有个getTag方法。
     * DefaultNamingPolicy生成的代理类的类名命名规则：
     * 被代理class name+"$$"+使用cglib处理的class name+"ByCGLIB" +"$$"+key的hashcode
     */
    @Test
    public void test8() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ProxyExpendTest.class);
        enhancer.setCallback(NoOp.INSTANCE);
        enhancer.setNamingPolicy(new DefaultNamingPolicy() {
            @Override
            protected String getTag() {
                return "-test-";
            }
        });
        Object obj = enhancer.create();
        System.out.println(obj.getClass());
        /**
         *class us.fjj.spring.learning.proxyexpend.ProxyExpendTest$$Enhancer-test$$df1a85a6
         */
    }
    /**
     * Objenesis: 实例化对象的一种方式
     * 如果不使用User中的有参构造函数，如何创建这个对象？
     * 通过反射：如果不适用有参构造函数，是无法创建对象的。
     * cglib中提供了一个接口：Objenesis，通过这个接口可以解决上面这种问题，它专门用来创建对象，即使你没有空的构造函数，都没有问题，它不使用构造方法创建java对象，即使有空的构造方法，也不会执行。
     * 用法如下：
     */
    @Test
    public void test9() {
        Objenesis objenesis = new ObjenesisStd();
        us.fjj.spring.learning.proxyexpend.cglib.test9.User user1 = objenesis.newInstance(us.fjj.spring.learning.proxyexpend.cglib.test9.User.class);
        System.out.println(user1);
        us.fjj.spring.learning.proxyexpend.cglib.test9.User user2 = objenesis.newInstance(us.fjj.spring.learning.proxyexpend.cglib.test9.User.class);
        System.out.println(user2);
        System.out.println(user1 == user2);
        /**
         * User{name='null'}
         * User{name='null'}
         * false
         */
    }

    /**
     * spring aop中用到了如上的所有用法。
     */
}
