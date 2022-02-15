package us.fjj.spring.learning.dependsonannotationusage.test1;

import org.springframework.stereotype.Component;

@Component
public class Service1 {
    public Service1() {
        System.out.println("创建了Service1");
    }
}
