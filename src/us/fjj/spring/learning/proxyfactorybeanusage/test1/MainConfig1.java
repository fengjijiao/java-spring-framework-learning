package us.fjj.spring.learning.proxyfactorybeanusage.test1;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
public class MainConfig1 {
    //注册目标对象
    @Bean
    public Service service1() {
        return new Service();
    }

    //注册前置通知
    @Bean
    public MethodBeforeAdvice beforeAdvice() {
        return new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, Object target) throws Throwable {
                System.out.println(String.format("准备执行：%s", method.getName()));
            }
        };
    }

    //注册环绕通知
    @Bean
    public MethodInterceptor costTimeInterceptor() {
        return new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                long startTime = System.nanoTime();
                Object result = invocation.proceed();
                long endTime = System.nanoTime();
                System.out.println(String.format("执行耗时：%dns", endTime - startTime));
                return result;
            }
        };
    }

    //注册ProxyFactoryBean
    @Bean
    public ProxyFactoryBean service1Proxy() {
        //1.创建ProxyFactoryBean
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        //2.设置目标对象的bean名称(注意：是setTargetName)
        proxyFactoryBean.setTargetName("service1");
        //3.设置拦截器的bean名称列表，此处有2个
        proxyFactoryBean.setInterceptorNames("beforeAdvice", "costTimeInterceptor");
        return proxyFactoryBean;
    }
}
