package us.fjj.spring.learning.dependsonannotationusage.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class MainConfig2 {
    @Bean
    public Service1 service1() {
        return new Service1();
    }
    @Bean
    public Service2 service2() {
        return new Service2();
    }
    @DependsOn({"service1", "service2"})
    @Bean
    public Service3 service3() {
        return new Service3();
    }

}
