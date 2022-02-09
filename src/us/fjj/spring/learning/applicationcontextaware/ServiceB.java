package us.fjj.spring.learning.applicationcontextaware;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceB implements ApplicationContextAware {
    public void say() {
        ServiceA serviceA = this.getServiceA();
        System.out.println(serviceA+";");
    }

    public ServiceA getServiceA() {
        return this.context.getBean(ServiceA.class);
    }

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
