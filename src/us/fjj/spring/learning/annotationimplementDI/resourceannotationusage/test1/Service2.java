package us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test1;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Service2 {
    @Resource
    private Service1 service1;

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
