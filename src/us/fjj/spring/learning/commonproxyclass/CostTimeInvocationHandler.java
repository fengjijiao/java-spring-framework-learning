package us.fjj.spring.learning.commonproxyclass;

import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//通用耗时计算代理处理器
public class CostTimeInvocationHandler implements InvocationHandler {
    private Object target;//被代理对象

    public CostTimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = method.invoke(target, args);
        stopWatch.stop();
        System.out.println(this.target.getClass().getName()+"执行"+method.getName()+"耗时"+stopWatch.getLastTaskTimeNanos()+"ns");
        return result;
    }

    /**
     * 用来创建targetInterface接口的代理对象
     * @param target    需要被代理的对象
     * @param targetInterface   被代理的接口
     * @param <T>
     * @return
     */
    public static <T> T createProxy(Object target, Class<T> targetInterface) {
        if (!targetInterface.isInterface()) {
            throw new IllegalStateException("targetInterface必须是接口类型！");
        } else if (!targetInterface.isAssignableFrom(target.getClass())) {
            throw new IllegalStateException("target必须是targetInterface接口的实现类！");
        }
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new CostTimeInvocationHandler(target));
    }
}
