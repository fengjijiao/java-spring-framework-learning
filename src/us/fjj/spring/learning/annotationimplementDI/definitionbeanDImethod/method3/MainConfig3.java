package us.fjj.spring.learning.annotationimplementDI.definitionbeanDImethod.method3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig3 {
    @Bean
    public Service1 providerService1() {
        return new Service1();
    }
    @Bean
    public Service2 providerService2() {
        return new Service2();
    }
    @Bean
    public Service3 providerService3(Service1 service1, Service2 service2) {
        Service3 service3 = new Service3();
        service3.setService1(service1);
        service3.setService2(service2);
        return service3;
    }
}
