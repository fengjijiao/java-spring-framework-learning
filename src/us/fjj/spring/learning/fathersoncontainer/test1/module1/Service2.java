package us.fjj.spring.learning.fathersoncontainer.test1.module1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    @Autowired
    private us.fjj.spring.learning.fathersoncontainer.test1.module1.Service1 service1;

    public void m1() {
        System.out.println("执行了Service2的m1方法(module1)");
        this.service1.m1();
    }
}
