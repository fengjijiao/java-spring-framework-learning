package us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test2;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Service2 {
    @Resource
    private IService service1;

    @Override
    public String toString() {
        return "Service2{" +
                "service1=" + service1 +
                '}';
    }
}
