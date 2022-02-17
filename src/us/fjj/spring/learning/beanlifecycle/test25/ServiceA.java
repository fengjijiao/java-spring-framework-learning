package us.fjj.spring.learning.beanlifecycle.test25;

import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PreDestroy;

public class ServiceA implements DisposableBean {
    public ServiceA() {
        System.out.println("创建ServiceA实例");
    }

    @PreDestroy
    public void preDestroy1() {
        System.out.println("preDestroy1()");
    }

    @PreDestroy
    public void preDestroy2() {
        System.out.println("preDestroy2()");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean接口中的destroy()");
    }

    //自定义的销毁方法
    public void customDestroyMethod() {
        System.out.println("我是自定义的销毁方法：customDestroyMethod()");
    }
}
