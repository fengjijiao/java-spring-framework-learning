package us.fjj.spring.learning.conditionalannotationusage.test3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig2 {
    @Conditional(OnMissingBeanCondition.class)
    @Bean
    public IService service2() {
        return new Service2();
    }
}
