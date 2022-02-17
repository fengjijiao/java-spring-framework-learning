package us.fjj.spring.learning.beanlifecycle.test25;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig25 {
    @Bean(destroyMethod = "customDestroyMethod")
    public ServiceA serviceA() {
        return new ServiceA();
    }
}
