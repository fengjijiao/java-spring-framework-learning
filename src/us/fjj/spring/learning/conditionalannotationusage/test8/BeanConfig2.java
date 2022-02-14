package us.fjj.spring.learning.conditionalannotationusage.test8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(MyCondition1.class)
public class BeanConfig2 {
    @Bean
    public String name() {
        return "LAOKAI";
    }
}
