package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service2 {
    private Service1 service1;

    @Autowired
    public void injectTest(Service1 service1, String name) {
        this.service1 = service1;
    }

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
