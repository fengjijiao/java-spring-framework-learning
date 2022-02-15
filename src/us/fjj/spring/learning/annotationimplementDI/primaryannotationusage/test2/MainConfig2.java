package us.fjj.spring.learning.annotationimplementDI.primaryannotationusage.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MainConfig2 {
    @Bean
    public Service1 providerService1() {
        return new Service1();
    }
    @Bean
    @Primary//当缺少这个注解时，无法注入service3
    public Service2 providerService2() {
        return new Service2();
    }
    @Bean
    public Service3 providerService3(IService service) {
        return new Service3(service);
    }
}
