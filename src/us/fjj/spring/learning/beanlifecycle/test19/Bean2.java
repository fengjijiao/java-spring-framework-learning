package us.fjj.spring.learning.beanlifecycle.test19;

import org.springframework.beans.factory.InitializingBean;

public class Bean2 implements InitializingBean {
    public void init() {
        System.out.println("调用init()方法");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("调用afterPropertiesSet");
    }
}
