package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    private Service1 service1;

    @Autowired
    public void setService1(Service1 service1) {
        System.out.println("执行了Service2的setService1方法！");
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
