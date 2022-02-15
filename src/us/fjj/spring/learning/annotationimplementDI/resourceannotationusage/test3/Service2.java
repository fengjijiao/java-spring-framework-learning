package us.fjj.spring.learning.annotationimplementDI.resourceannotationusage.test3;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class Service2 {
    @Resource
    private List<IService> services;
    @Resource
    private Map<String, IService> serviceMap;

    @Override
    public String toString() {
        return "Service2{" +
                "services=" + services +
                ", serviceMap=" + serviceMap +
                '}';
    }
}
