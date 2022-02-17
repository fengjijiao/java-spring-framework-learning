package us.fjj.spring.learning.fathersoncontainer.test3.module2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.fjj.spring.learning.fathersoncontainer.test3.module1.Service2;

@Component
public class Service3 {
    @Autowired
    private Service1 service1;

    @Autowired
    private Service2 service2;

    public void m1() {
        System.out.println("执行了Service3的m1方法(module2)");
        service1.m1();
    }

    public void m2() {
        System.out.println("执行了Service3的m2方法(module2)");
        service2.m1();
    }
}
