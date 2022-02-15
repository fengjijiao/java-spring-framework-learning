package us.fjj.spring.learning.dependsonannotationusage.test1;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@DependsOn({"service1", "service2"})
@Component
public class Service3 {
    public Service3() {
        System.out.println("创建了Service3");
    }
}
