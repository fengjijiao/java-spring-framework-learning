package us.fjj.spring.learning.beanlifecycle.test21;

import org.springframework.stereotype.Component;

@Component
public class Service1 {
    public Service1() {
        System.out.println("创建Service1");
    }
}
