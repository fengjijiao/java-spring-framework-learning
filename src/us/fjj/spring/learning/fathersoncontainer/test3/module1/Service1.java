package us.fjj.spring.learning.fathersoncontainer.test3.module1;

import org.springframework.stereotype.Component;

@Component
public class Service1 {
    public void m1() {
        System.out.println("执行了Service1的m1方法(module1)");
    }
}
