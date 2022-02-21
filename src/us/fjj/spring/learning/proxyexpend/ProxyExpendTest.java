package us.fjj.spring.learning.proxyexpend;

import org.junit.jupiter.api.Test;
import us.fjj.spring.learning.proxyexpend.jdkproxy.CostTimeInvocationHandler;
import us.fjj.spring.learning.proxyexpend.jdkproxy.IService1;
import us.fjj.spring.learning.proxyexpend.jdkproxy.IService2;
import us.fjj.spring.learning.proxyexpend.jdkproxy.Service;

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
     */
    @Test
    public void test2() {
        //
    }

}
