package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test9;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Service2 {
    @Autowired
    private List<IService> services;
    @Autowired
    private Map<String, IService> serviceMap;

    @Override
    public String toString() {
        return "Service2{" +
                "services=" + services +
                ", serviceMap=" + serviceMap +
                '}';
    }
}
