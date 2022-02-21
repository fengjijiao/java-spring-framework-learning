package us.fjj.spring.learning.proxyexpend.jdkproxy;

import org.springframework.util.StopWatch;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author jijiao
 */
public class CostTimeInvocationHandler implements InvocationHandler {
    private Object target;

    public CostTimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "CostTimeInvocationHandler{" +
                "target=" + target +
                '}';
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        System.out.println(String.format("%s start", method.getName()));
        stopWatch.start();
        Object result = method.invoke(target, objects);
        stopWatch.stop();
        System.out.println(String.format("%s end", method.getName()));
        System.out.println(String.format("%s -> total cost time: %sns", method.getName(), stopWatch.getLastTaskTimeNanos()));
        return result;
    }
}
