package us.fjj.spring.learning.importannotationusage.test6;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * 耗时计算通用代理类
 */
public class CostTimeProxy implements MethodInterceptor {
    private Object target;

    public CostTimeProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = methodProxy.invoke(target, objects);//注意，这里为invoke，而非invokeSuper
        stopWatch.stop();
        System.out.println("执行"+method.getName()+"耗时"+stopWatch.getLastTaskTimeNanos()+"ns");
        return result;
    }

    public static <T> T createProxy(T target) {
        CostTimeProxy costTimeProxy = new CostTimeProxy(target);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(costTimeProxy);
        return (T) enhancer.create();
    }
}
