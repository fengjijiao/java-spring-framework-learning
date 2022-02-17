package us.fjj.spring.learning.beanlifecycle.test24;

import javax.annotation.PreDestroy;

public class ServiceB {
    public ServiceB() {
        System.out.println("ServiceB()构造方法");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("被@PreDestroy标注的方法destroy");
    }
}
