package us.fjj.spring.learning.commonproxyclass;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.*;

public class CommonProxyClassTest {
    /**
     * 通用代理类Test
     * 在普通代理类中，如果我们需要给系统中所有的接口都加上统计耗时的功能，那么我们需要给每个接口创建一个代理类，此时代码量和测试的工作量也是巨大的。
     * 解决如上问题的方法是写一个通用的代理类
     * 通用代理有两种实现方式: jdk动态代理、cglib代理
     *
     * @param args
     */
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /**
         * jdk为实现动态代理提供了支持，主要用到2个类：java.lang.reflect.Proxy、java.lang.reflect.InvocationHandler
         * jdk自带的代理只能为接口创建代理类，如果需要给具体的类创建代理类，需要使用cglib.
         *
         * 创建代理的两种方式
         * 方式一
         * 1.调用Proxy.getProxyClass方法获取代理类的Class对象
         * 2.使用InvocationHandler接口创建代理类的处理器
         * 3.通过代理类和InvocationHandler创建代理对象
         * 4.使用代理对象
         *
         */
        m1Test();
        /**
         * 我是InvocationHandler，被调用的方法是：m1
         * 我是InvocationHandler，被调用的方法是：m2
         * 我是InvocationHandler，被调用的方法是：m3
         */
        /**
         * 方式二
         * 1.使用InvocationHandler接口创建代理类的处理器
         * 2.使用Proxy类的静态方法newProxyInstance直接创建代理对象
         * 3.使用代理对象
         */
        m2Test();
        /**
         * 我是InvocationHandler，被调用的方法是：m1
         * 我是InvocationHandler，被调用的方法是：m2
         * 我是InvocationHandler，被调用的方法是：m3
         */

        //代理所有接口方法完成耗时计算代理处理类Test
        costTimeProxy();
        /**
         * 我是ServiceA的m1方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceA执行m1耗时73800ns
         * 我是ServiceA的m2方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceA执行m2耗时86700ns
         * 我是ServiceA的m3方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceA执行m3耗时50100ns
         * 我是ServiceB的m1方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceB执行m1耗时63200ns
         * 我是ServiceB的m2方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceB执行m2耗时35700ns
         * 我是ServiceB的m3方法！
         * us.fjj.spring.learning.commonproxyclass.ServiceB执行m3耗时33400ns
         */
        /**
         * 当我们创建一个新的接口的时候，不需要再去创建一个代理类了，只需要使用CostTimeInvocationHandler.createProxy创建一个新的代理对象就可以了，方便了很多。
         *
         * Proxy使用注意
         * 1.jdk中的Proxy只能为接口生成代理类，如果你想给某个类创建代理类，那么Proxy是无能为力的，此时需要我们用到下面要说的cglib了。
         * 2.Proxy类中提供了几个常见的静态方法大家需要掌握(创建代理类、直接创建代理类实例、是否是代理类、获取代理处理器类[InvocationHandler])
         * 3.通过Proxy创建代理对象，当调用代理对象任意方法的时候，会被InvocationHandler接口中的invoke方法进行处理，这个接口内容是关键。
         */

        //cglib常用方式一:拦截所有方法（MethodInterceptor）
        cglibMethodInterceptorTest();
        /**
         * enhancer.setSuperclass用来设置代理类的父类，即需要给哪个类创建代理类，此处是Service1
         * enhancer,setCallback传递的是MethodInterceptor接口类型的参数，MethodInterceptor接口有个intercept方法，这个方法会拦截代理对象所有的方法调用。
         * 还有一个重点是Object reuslt = methodProxy.invokeSuper(o, objects);可以调用被代理类，也就是Service1类中的具体方法，从方法名称的意思可以看出是调用了父类，实际对某个类创建代理，cglib底层通过修改字节码的方式为Service1类创建 了一个子类。
         */

        //cglib常用方式二：
    }

    //方式一
    public static void m1Test() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //1.调用Proxy.getProxyClass方法获取代理类的Class对象
        Class<IService> proxyClass = (Class<IService>) Proxy.getProxyClass(IService.class.getClassLoader(), IService.class);
        //2.使用InvocationHandler接口创建代理类的处理器
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我是InvocationHandler，被调用的方法是："+method.getName());
                return null;
            }
        };
        //3.通过代理类和InvocationHandler创建代理对象
        IService proxyService = proxyClass.getConstructor(InvocationHandler.class).newInstance(invocationHandler);
        //4.使用代理对象
        proxyService.m1();
        proxyService.m2();
        proxyService.m3();
    }

    //方式二
    public static void m2Test() {
        //1.使用InvocationHandler接口创建代理类的处理器
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我是InvocationHandler，被调用的方法是："+method.getName());
                return null;
            }
        };
        //2.使用Proxy类的静态方法newProxyInstance直接创建代理对象
        IService proxyService = (IService) Proxy.newProxyInstance(IService.class.getClassLoader(), new Class[]{IService.class}, invocationHandler);
        //3.使用代理对象
        proxyService.m1();
        proxyService.m2();
        proxyService.m3();
    }

    //通用代理类耗时Test
    public static void costTimeProxy() {
        IService serviceA = CostTimeInvocationHandler.createProxy(new ServiceA(), IService.class);
        IService serviceB = CostTimeInvocationHandler.createProxy(new ServiceB(), IService.class);
        serviceA.m1();
        serviceA.m2();
        serviceA.m3();

        serviceB.m1();
        serviceB.m2();
        serviceB.m3();
    }

    //cglib常用用法一: 拦截所有方法MethodInterceptor
    public static void cglibMethodInterceptorTest() {
        //使用Enhancer来给某个类创建代理类，步骤
        //1.创建EnHancer对象
        Enhancer enhancer = new Enhancer();
        //2.通过setSuperclass来设置父类型，即需要给哪个类创建代理类
        enhancer.setSuperclass(Service1.class);
        //3.设置回调，须实现org.springframework.cglib.proxy.Callback接口，此处我们使用的是org.springframework.cglib.proxy.MethodInterceptor，
        //也是一个接口，实现了Callback接口，
        //当调用代理对象的任何方法的时候，都会被MethodInterceptor接口的invoke方法处理
        enhancer.setCallback(new MethodInterceptor() {
            /**
             * 代理对象方法拦截
             * @param o     代理对象(target的子类)
             * @param method        被代理的类的方法，即Serivce1中的方法
             * @param objects       调用方法传递的参数
             * @param methodProxy       方法代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("调用方法："+method);
                //可以调用MethodProxy的invokeSuper调用被代理类的方法
                Object result = methodProxy.invokeSuper(o, objects);
                return result;
            }
        });
        //4.获取代理对象，调用enhancer.create方法获取代理对象，这个方法返回的是Object类型的，所以需要强转以下
        Service1 proxy = (Service1) enhancer.create();
        //5.调用代理对象的方法
        proxy.m1();
        proxy.m2();
        proxy.m3();
    }
}
