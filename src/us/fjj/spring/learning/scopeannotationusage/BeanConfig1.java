package us.fjj.spring.learning.scopeannotationusage;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfig1 {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Service1 service1() {
        return new Service1();
    }
    @Bean
    public Service2 service2() {
        return new Service2();
    }
}
