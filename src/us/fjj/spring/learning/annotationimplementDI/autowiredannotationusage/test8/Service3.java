package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    @Autowired
    private IService service1;

    @Override
    public String toString() {
        return "Service3{" +
                "service1=" + service1 +
                '}';
    }
}
