package us.fjj.spring.learning.commonproxyclass;

import org.springframework.cglib.proxy.*;
import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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

        //cglib常用方式二：拦截所有方法（MethodInterceptor）嵌套调用m1中调用m2
        cglibMethodInterceptorTest2();
        /**
         * m1方法被我劫持了！:)
         * 这里是Service2的m1方法！
         * m2方法被我劫持了！:)
         * 这里是Service2的m2方法！
         */
        /**
         * 从输出可以看到m1和m2方法都被拦截器处理了，但是m2方法是在Service2的m1中被调用的，也被拦截器处理了。
         * spring中的@Configuration注解就是采用这种方式实现的。
         * @Configuration
         * public class Config {
         * @Bean
         * public C1 c1(){
         * return new C1();
         * }
         * @Bean
         * public C2 c2(){
         * C1 c1 = this.c1(); //@1
         * return new C2(c1);
         * }
         * @Bean
         * public C3 c3(){
         * C1 c1 = this.c1(); //@2
         * return new C3(c1);
         * }
         * ...
         * }
         * 注意到c2和c3方法都调用了c1对象，如何确保两个方法内的c1都是同一个呢？
         * 这里采用cglib代理拦截@Bean注解的方式来实现的（拦截返回特定返回值即可，具体见下一个案例）。
         */

        //cglib常用方式三：拦截所有方法并返回固定值（FixedValue）
        /**
         * 当调用某个类的任何方法时都希望返回一个固定值的时候，可以用FixedValue接口。
         */
        cglibFixedValueTest();
        /**
         *这里是被替换后的返回值！
         * 这里是被替换后的返回值！
         * 这里是被替换后的返回值！
         */

        //cglib常用方式四：直接放行不做任何操作（NoOp.INSTANCE）
        /**
         * Callback接口下有一个子接口NoOp，将这个作为Callback的时候，被调用的方法会直接放行，像没有任何代理一样。
         */
        cglibNoOpTest();
        /**
         * 这里是Service1的m1方法！
         * 这里是Service1的m2方法！
         * 这里是Service1的m3方法！
         */

        //cglib常用方式五：不同的方法使用不同的拦截器（CallbackFilter）
        cglibCallbackFilterTest();
        /**
         * 这里是Service4的insert1方法！
         * 执行insert1耗时16616700ns
         * 这里是Service4的insert2方法！
         * 执行insert2耗时252400ns
         * 已被劫持！
         * 已被劫持！
         */

        //cglib常用方式六：对方式五进行优化（CallbackHelper）
        /**
         * cglib中有个CallbackHelper类，可以对方式五的代码进行优化，CallbackHelper类相当于对一些代码进行了封装，方便实现了案例5的需求。
         */
        cglibCallbackHelperTest();
        /**
         * 这里是Service4的insert1方法！
         * 执行insert1耗时15248800ns
         * 这里是Service4的insert2方法！
         * 执行insert2耗时286700ns
         * 已被劫持！
         * 已被劫持！
         */

        //cglib常用方式七：实现通用的统计任意类方法耗时代理类
        cglibCostTimeProxyTest();
        /**
         * 这里是Service4的insert1方法！
         * 执行insert1耗时4183300ns
         * 这里是Service4的insert2方法！
         * 执行insert2耗时99500ns
         * 执行get1耗时55800ns
         * get1 value
         * 执行get2耗时45600ns
         * get2 value
         */

        /**
         * CGLIB和Java动态代理的区别
         * 1.Java动态代理只能够对接口进行代理，不能对普通类进行代理（因为所有生成的代理类的父类为Proxy，Java类继承机制不允许多重继承）；CGLIB能够代理普通类。
         * 2.Java动态代理使用Java原生的反射API进行操作，在生成类上比较高效；CGLIB使用ASM框架直接对字节码进行操作，在类的执行过程中比较高效。
         *
         */
    }

    //方式一
    public static void m1Test() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //1.调用Proxy.getProxyClass方法获取代理类的Class对象
        Class<IService> proxyClass = (Class<IService>) Proxy.getProxyClass(IService.class.getClassLoader(), IService.class);
        //2.使用InvocationHandler接口创建代理类的处理器
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我是InvocationHandler，被调用的方法是：" + method.getName());
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
                System.out.println("我是InvocationHandler，被调用的方法是：" + method.getName());
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
                System.out.println("调用方法：" + method);
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

    public static void cglibMethodInterceptorTest2() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service2.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println(method.getName() + "方法被我劫持了！:)");
                Object result = methodProxy.invokeSuper(o, objects);//调用原类的方法
                return result;
            }
        });
        Service2 service2 = (Service2) enhancer.create();
        service2.m1();
    }

    public static void cglibFixedValueTest() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service3.class);
        enhancer.setCallback(new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                return "这里是被替换后的返回值！";
            }
        });
        Service3 service3 = (Service3) enhancer.create();
        System.out.println(service3.m1());
        System.out.println(service3.m2());
        System.out.println(service3.m3());
    }

    public static void cglibNoOpTest() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service1.class);
        enhancer.setCallback(NoOp.INSTANCE);
        Service1 service1 = (Service1) enhancer.create();
        service1.m1();
        service1.m2();
        service1.m3();
    }

    /**
     * 实现对所有insert开头的方法进行耗时统计，对get开头的方法返回固定字符串
     */
    public static void cglibCallbackFilterTest() {
        //创建两个Callback
        Callback[] callbacks = new Callback[]{
                //这个用来拦截所有insert开头的方法
                new MethodInterceptor() {
                    @Override
                    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                        StopWatch stopWatch = new StopWatch();
                        stopWatch.start();
                        Object result = methodProxy.invokeSuper(o, objects);
                        stopWatch.stop();
                        System.out.println("执行" + method.getName() + "耗时" + stopWatch.getLastTaskTimeNanos() + "ns");
                        return result;
                    }
                },
                //这个用来拦截所有get开头的方法，返回固定值
                new FixedValue() {
                    @Override
                    public Object loadObject() throws Exception {
                        return "已被劫持！";
                    }
                }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service4.class);
        //调用enhancer的setCallbacks传递Callback数组。
        enhancer.setCallbacks(callbacks);
        /**
         * 设置过滤器CallbackFilter
         * CallbackFilter用来判断调用方法的时候使用callbacks数组中的哪个callback来处理当前方法
         * 返回的是callbacks数组的下标
         */
        enhancer.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                //当前调用方法的方法名
                String methodName = method.getName();
                if (methodName.startsWith("insert")) return 0;
                else return 1;
            }
        });
        Service4 service4 = (Service4) enhancer.create();
        service4.insert1();
        service4.insert2();
        System.out.println(service4.get1());
        System.out.println(service4.get2());
    }

    public static void cglibCallbackHelperTest() {
        Enhancer enhancer = new Enhancer();
        //创建两个Callback
        Callback costTimeCallback = (MethodInterceptor) (Object o, Method method, Object[] objects, MethodProxy methodProxy) -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Object result = methodProxy.invokeSuper(o, objects);
            stopWatch.stop();
            System.out.println("执行" + method.getName() + "耗时" + stopWatch.getLastTaskTimeNanos() + "ns");
            return result;
        };
        Callback fixedValueCallback = (FixedValue) () -> "已被劫持！";
        CallbackHelper callbackHelper = new CallbackHelper(Service4.class, null) {
            @Override
            protected Object getCallback(Method method) {
                return method.getName().startsWith("insert")?costTimeCallback:fixedValueCallback;
            }
        };
        enhancer.setSuperclass(Service4.class);
        //调用enhancer的setCallbacks传递callback数组
        enhancer.setCallbacks(callbackHelper.getCallbacks());
        //设置CallbackFilter，用来判断某个方法具体走哪个callback
        enhancer.setCallbackFilter(callbackHelper);
        Service4 service4 = (Service4) enhancer.create();
        service4.insert1();
        service4.insert2();
        System.out.println(service4.get1());
        System.out.println(service4.get2());
    }

    public static void cglibCostTimeProxyTest() {
        Service4 service4 = CostTimeProxy.createProxy(new Service4());
        service4.insert1();
        service4.insert2();
        System.out.println(service4.get1());
        System.out.println(service4.get2());
    }
}
