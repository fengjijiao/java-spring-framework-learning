package us.fjj.spring.learning.replacemethod;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.MethodReplacer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;

public class ServiceBMethodReplacer implements MethodReplacer, ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
        return this.context.getBean(ServiceA.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
