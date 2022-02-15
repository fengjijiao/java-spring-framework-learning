package us.fjj.spring.learning.annotationimplementDI.other.test3.module1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.fjj.spring.learning.annotationimplementDI.other.test3.IService;

import java.util.Map;

@Component
public class Service5 {
    @Autowired
    private Map<String, IService> serviceMap;

    @Override
    public String toString() {
        return "Service5{" +
                "serviceMap=" + serviceMap +
                '}';
    }
}
