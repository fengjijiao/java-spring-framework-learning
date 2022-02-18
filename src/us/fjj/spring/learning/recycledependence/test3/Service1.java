package us.fjj.spring.learning.recycledependence.test3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service1 {
    public void m1() {
        System.out.println("service1的m1方法");
    }

    private Service2 service2;

    @Autowired
    public void setService2(Service2 service2) {
        this.service2 = service2;
    }

    @Override
    public String toString() {
        return "Service1{" +
                "service2=" + service2 +
                '}';
    }
}
