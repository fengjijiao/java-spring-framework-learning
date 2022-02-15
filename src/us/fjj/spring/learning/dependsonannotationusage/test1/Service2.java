package us.fjj.spring.learning.dependsonannotationusage.test1;

import org.springframework.stereotype.Component;

@Component
public class Service2 {
    public Service2() {
        System.out.println("创建了Service2");
    }
}
