package us.fjj.spring.learning.proxyexpend.cglib.test1;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author jijiao
 */
public class TwoInterfaceCglibTest {

    @Test
    public void test() {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{IService1.class, IService2.class});
        //important
        enhancer.setSuperclass(TwoInterfaceCglibTest.class);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            System.out.println(String.format("执行了%s方法。", method.getName()));
            return null;
        });
        Object obj = enhancer.create();
        if (obj instanceof IService1) {
            ((IService1) obj).m1();
        }
        if (obj instanceof IService2) {
            ((IService2) obj).m2();
        }
        //看一下代理对象的类型
        System.out.println(obj.getClass());
        //看一下代理类实现的接口
        System.out.println("创建代理类实现的接口如下：");
        for (Class<?> cs : obj.getClass().getInterfaces()) {
            System.out.println(cs);
        }
        /**
         * 执行了m1方法。
         * 执行了m2方法。
         * class us.fjj.spring.learning.proxyexpend.cglib.test1.TwoInterfaceCglibTest$$EnhancerByCGLIB$$b8588599
         * 创建代理类实现的接口如下：
         * interface us.fjj.spring.learning.proxyexpend.cglib.test1.IService1
         * interface us.fjj.spring.learning.proxyexpend.cglib.test1.IService2
         * interface org.springframework.cglib.proxy.Factory
         */
    }
}
