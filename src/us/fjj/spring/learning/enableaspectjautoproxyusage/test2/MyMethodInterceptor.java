package us.fjj.spring.learning.enableaspectjautoproxyusage.test2;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("MethodInterceptor start");
        Object retVal = invocation.proceed();
        System.out.println("MethodInterceptor end");
        return retVal;
    }
}
