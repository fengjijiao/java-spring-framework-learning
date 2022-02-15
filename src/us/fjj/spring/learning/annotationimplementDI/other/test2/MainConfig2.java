package us.fjj.spring.learning.annotationimplementDI.other.test2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MainConfig2 {
    @Bean
    @Qualifier("tag1")//相当于在类上直接标注
    public Service0 service0() {
        return new Service0();
    }
    @Bean
    @Qualifier("tag2")
    public Service1 service1() {
        return new Service1();
    }

    @Bean
    @Qualifier("tag1")
    public Service2 service2() {
        return new Service2();
    }

    @Bean
    public Service3 service3(@Qualifier("tag1") Map<String, IService> serviceMap) {
        Service3 service3 = new Service3();
        service3.setServiceMap(serviceMap);
        return service3;
    }
}
