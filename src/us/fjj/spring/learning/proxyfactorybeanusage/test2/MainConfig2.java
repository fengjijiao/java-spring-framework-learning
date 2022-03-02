package us.fjj.spring.learning.proxyfactorybeanusage.test2;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class MainConfig2 {
    @Bean
    public ServiceA serviceA() {
        return new ServiceA();
    }
    @Bean
    public Advisor interceptor1() {
        MethodBeforeAdvice advice = new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println("将要执行"+method.getName());
            }
        };
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setAdvice(advice);
        return advisor;
    }
    @Bean
    public MethodInterceptor interceptor2() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                long startTime = System.nanoTime();
                Object result = invocation.proceed();
                long endTime = System.nanoTime();
                System.out.println("执行耗时："+(endTime - startTime)+"ns");
                return result;
            }
        };
    }
    @Bean
    public ProxyFactoryBean serviceAProxy() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetName("serviceA");
        proxyFactoryBean.setInterceptorNames("interceptor*");
        return proxyFactoryBean;
    }
}
