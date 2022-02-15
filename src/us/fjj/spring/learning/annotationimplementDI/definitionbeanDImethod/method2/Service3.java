package us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    private Service1 service1;
    private Service2 service2;

    @Override
    public String toString() {
        return "Service3{" +
                "service1=" + service1 +
                ", service2=" + service2 +
                '}';
    }

    public Service1 getService1() {
        return service1;
    }

    @Autowired
    public void setService1(Service1 service1) {
        this.service1 = service1;
    }

    public Service2 getService2() {
        return service2;
    }

    @Autowired
    public void setService2(Service2 service2) {
        this.service2 = service2;
    }
}
