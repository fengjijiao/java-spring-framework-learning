package us.fjj.spring.learning.recycledependence.test1;

import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class MethodBeforeInterceptor implements BeanPostProcessor {
    /**
     * 初始化之后调用
     * postProcessAfterInitialization方法内部会在service1初始化之后调用，内部会对service1这个bean进行处理，返回一个代理对象，通过代理来访问service1的方法，访问service1中的任何方法之前，会先输出: 你好，service1
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("service1".equals(beanName)) {
            //代理创建工厂，需传入被代理的目标对象
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            //添加一个方法前置通知，会在方法执行前调用通知中的before方法
            proxyFactory.addAdvice(new MethodBeforeAdvice() {
                @Override
                public void before(Method method, Object[] args, Object target) throws Throwable {
                    System.out.println("你好，service1");
                }
            });
            //返回代理对象
            return proxyFactory.getProxy();
        }
        return bean;
    }
}
