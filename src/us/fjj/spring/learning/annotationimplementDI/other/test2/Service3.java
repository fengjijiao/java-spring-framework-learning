package us.fjj.spring.learning.annotationimplementDI.other.test2;

import java.util.Map;

public class Service3 {
    private Map<String, IService> serviceMap;

    @Override
    public String toString() {
        return "Service3{" +
                "serviceMap=" + serviceMap +
                '}';
    }

    public Map<String, IService> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, IService> serviceMap) {
        this.serviceMap = serviceMap;
    }
}
