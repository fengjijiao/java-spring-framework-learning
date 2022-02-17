package us.fjj.spring.learning.beanlifecycle.test21;

import org.springframework.stereotype.Component;

@Component
public class Service2 {
    public Service2() {
        System.out.println("创建Service2");
    }
}
