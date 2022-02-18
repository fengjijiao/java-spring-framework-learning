package us.fjj.spring.learning.recycledependence.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    public void m1() {
        System.out.println("service2的m1方法");
        this.service1.m1();
    }

    private Service1 service1;
    @Autowired
    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }

    public Service1 getService1() {
        return service1;
    }
}
