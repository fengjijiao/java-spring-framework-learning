package us.fjj.spring.learning.enableaspectjautoproxyusage.test4;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.stereotype.Component;

@Component
public class Advisor1 extends DefaultPointcutAdvisor {
    public Advisor1() {
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("Advisor1 start");
                Object result = invocation.proceed();
                System.out.println("Advisor1 end");
                return result;
            }
        };
        this.setAdvice(methodInterceptor);
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
