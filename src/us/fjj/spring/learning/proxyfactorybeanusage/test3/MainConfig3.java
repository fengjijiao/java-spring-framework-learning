package us.fjj.spring.learning.proxyfactorybeanusage.test3;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class MainConfig3 {
    @Bean
    public ServiceB serviceB() {
        return new ServiceB();
    }
    @Bean
    public MethodBeforeAdvice beforeAdvice() {
        return new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println(String.format("将要执行%s方法", method.getName()));
            }
        };
    }
    @Bean
    public MethodInterceptor costTimeInterceptor() {
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
    public AfterReturningAdvice afterReturningAdvice() {
        return new AfterReturningAdvice() {
            @Override
            public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
                System.out.println(String.format("方法%s执行完成！", method.getName()));
            }
        };
    }
    @Bean
    public ProxyFactoryBean serviceBProxy() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTargetName("serviceB");
        proxyFactoryBean.setInterceptorNames("beforeAdvice", "costTimeInterceptor", "afterReturningAdvice");
        return proxyFactoryBean;
    }
}
