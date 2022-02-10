package us.fjj.spring.learning.commonproxyclass;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

public class CostTimeProxy implements MethodInterceptor {
    //目标对象
    private Object target;

    public CostTimeProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //Object result = methodProxy.invokeSuper(o, objects);
        Object result = methodProxy.invoke(target, objects);
        //注意：上一行中第一个参数为target并非o，在@1中使用了target.getClass()得到的是一个Class类型的class实例（，JVM中一种类型仅有一个class实例），故这里
        //若依然是o，则调用的是enhancer new的一个与target同类型的实例（与target的相应数据不一致）
        //故这里是调用target的方法
        stopWatch.stop();
        System.out.println("执行" + method.getName() + "耗时" + stopWatch.getLastTaskTimeNanos() + "ns");
        return result;
    }

    public static <T> T createProxy(T target) {
        CostTimeProxy costTimeProxy = new CostTimeProxy(target);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());//@1
        enhancer.setCallback(costTimeProxy);
        return (T) enhancer.create();
    }
}
