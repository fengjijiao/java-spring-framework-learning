package us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Service3 {
    @Autowired
    private IService service;

    @Override
    public String toString() {
        return "Service3{" +
                "service=" + service +
                '}';
    }
}
