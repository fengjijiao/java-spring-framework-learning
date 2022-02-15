package us.fjj.spring.learning.annotationimplementDI.qualifierannotaitonusage.test1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class Service4 {
    @Resource
    @Qualifier("tag1")
    private List<IService> services;
    @Resource
    @Qualifier("tag2")
    private Map<String, IService> serviceMap;

    @Override
    public String toString() {
        return "Service4{" +
                "services=" + services +
                ", serviceMap=" + serviceMap +
                '}';
    }
}
