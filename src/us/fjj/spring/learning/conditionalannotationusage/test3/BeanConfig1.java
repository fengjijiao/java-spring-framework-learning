package us.fjj.spring.learning.conditionalannotationusage.test3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig1 {
    @Conditional(OnMissingBeanCondition.class)
    @Bean
    public IService service1() {
        return new Service1();
    }
}
